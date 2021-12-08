package com.ciphertrace

import io.circe.syntax._

import zio._
import zio.console._
import zhttp.http._
import zhttp.service.Server

object Main extends App {
  val port = 8090
  val app = Http.collect[Request] {
    case Method.GET -> Root / "text" => Response.text("Hello, World")
    case Method.GET -> Root / "json" => Response.jsonString(Seq("Hello", "World").asJson.spaces4)
  }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    (putStrLn(s"Server booting on $port!") <* Server.start(port, app)).exitCode
}
