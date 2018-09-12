package com.api

import cats.Monad
import cats.data.EitherT
import cats.effect.{ IO, Timer }
import com.domain.{ Credential, DomainError, User, UserToken }
import org.joda.time.{ DateTime, DateTimeZone }

import scala.concurrent.duration._
import scala.language.higherKinds

trait AsyncTokenService[F[_]] {
  protected def authenticate(credentials: Credential): F[Either[DomainError, User]]

  protected def issueToken(user: User): F[Either[DomainError, UserToken]]

  def requestToken(credentials: Credential)(implicit F: Monad[F]): F[Either[DomainError, UserToken]] =
    (
      for {
        user  <- EitherT(authenticate(credentials))
        token <- EitherT(issueToken(user))
      } yield token
    ).value
}

class IOAsyncTokenService(checkUsersCredential: Credential => Either[DomainError, User],
                          generateToken: (User, DateTime) => Either[DomainError, UserToken])(implicit t: Timer[IO])
    extends AsyncTokenService[IO] {

  private val intGenerator = scala.util.Random

  override protected def authenticate(credentials: Credential): IO[Either[DomainError, User]] =
    IO.sleep(intGenerator.nextInt(5000).millisecond).flatMap { _ =>
      IO.pure(checkUsersCredential(credentials))
    }

  override protected def issueToken(user: User): IO[Either[DomainError, UserToken]] =
    IO.sleep(intGenerator.nextInt(5000).millisecond).flatMap { _ =>
      IO.pure(generateToken(user, new DateTime(DateTimeZone.UTC)))
    }
}
