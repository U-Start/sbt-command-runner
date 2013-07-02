package com.eligotech.sbt.plugins

import scala.util.control.Exception._

trait Validations {
  owner =>
  protected type Validation[A] = Either[String, A]

  protected def map[A, B](v: Validation[A])(f: A => B): Validation[B] = v match {
    case Right(a)  => Right(f(a))
    case Left(msg) => Left(msg)
  }

  protected def flatMap[A, B](v: Validation[A])(f: A => Validation[B]): Validation[B] = v match {
    case Right(a)  => f(a)
    case Left(msg) => Left(msg)
  }

  implicit def toRichValidation[A](v: Validation[A]) = new {
    def map[B](f: A => B): Validation[B] = owner.map(v)(f)

    def flatMap[B](f: A => Validation[B]): Validation[B] = owner.flatMap(v)(f)
  }

  protected def valid[A](a: A): Validation[A] = Right(a)

  protected def failure[A](msg: String): Validation[A] = Left(msg)

  protected def safely[A, B](process: A => B): A => Validation[B] = a => safe(process(a))

  protected def safe[A](process: => A): Validation[A] =
    catching(classOf[Throwable]).either(process) match {
      case Right(result)   => valid(result)
      case Left(exception) => failure(exception.toString)
    }
}
