package com.api

import cats.Monad
import cats.data.EitherT
import cats.effect.{ IO, Timer }
import com.domain.{ Credential, ServiceError, User, UserToken }
import org.joda.time.{ DateTime, DateTimeZone }

import scala.concurrent.duration._
import scala.language.higherKinds

trait AsyncTokenService[F[_]] {
  protected def authenticate(credentials: Credential): F[Either[ServiceError, User]]

  protected def issueToken(user: User): F[Either[ServiceError, UserToken]]

  def requestToken(credentials: Credential)(implicit F: Monad[F]): F[Either[ServiceError, UserToken]] =
    (
      for {
        user  <- EitherT(authenticate(credentials))
        token <- EitherT(issueToken(user))
      } yield token
    ).value
}

/**
 * 2. Service Implementation
 * Provide an implementation for the following API, which is different from the one designed in the previous section:
 */
class IOAsyncTokenService(checkUsersCredential: Credential => Either[ServiceError, User],
                          generateToken: (User, DateTime) => Either[ServiceError, UserToken])(implicit t: Timer[IO])
    extends AsyncTokenService[IO] {

  private val intGenerator = scala.util.Random

  override protected def authenticate(credentials: Credential): IO[Either[ServiceError, User]] =
    IO.sleep(intGenerator.nextInt(5000).millisecond).flatMap { _ =>
      IO.pure(checkUsersCredential(credentials))
    }

  override protected def issueToken(user: User): IO[Either[ServiceError, UserToken]] =
    IO.sleep(intGenerator.nextInt(5000).millisecond).flatMap { _ =>
      IO.pure(generateToken(user, new DateTime(DateTimeZone.UTC)))
    }
}
