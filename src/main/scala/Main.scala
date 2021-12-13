package com.ciphertrace

import io.circe.syntax._
import scodec.bits.ByteVector
import zio._
import zio.console._
import zhttp.http._
import zhttp.service.Server

object Main extends App {
  val port = 8090

  val helloRoutes = Http.collect[Request] {
    case Method.GET -> Root / "text" => Response.text("Hello, World!")
    case Method.GET -> Root / "json" => Response.jsonString(Seq("Hello", "World").asJson.spaces2)
  }
  val app = Http.collectM[Request] {
    case Method.GET -> Root / "bech32" / pubKey =>
      val ethPubKey = "028fa5bcf187b4f488c739261012a45956a77a7b98590e06d938c6497aaca36133"
      val keyOpt = if (pubKey.trim.isEmpty) ethPubKey else pubKey
      for {
       result <- PublicKeyAnalyzer.toBech32(keyOpt)
       doesMatch = result == "tb1q4ead8pr0qq5p78t4yvw3vedamacrzsreshdsvm"
    } yield Response.text(result + s"\nDoes match? $doesMatch")
  }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    (putStrLn(s"Server booting on $port!") <* Server.start(port, app +++ helloRoutes)).exitCode
}
