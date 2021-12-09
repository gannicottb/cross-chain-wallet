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
      /*
      Tx Hash: 0xf8efa9c9d18ad82feee5c662583027e45d2e1fbfa40fe48ba49dfa983ac8008b
      Sender Address: 0x58b92725e690d3a76bb5048564ddf195c5543c6e
      Signature:  0xbc1066ab8b0f40c66ea1721229d41890318b18423e846b8b9b7516ed3eb089347222f7c9bfdcb1a9504fc82af6df7fd8c1cc248aa9c6217db5517f1a30e87dac01
      PublicKey:  0x8fa5bcf187b4f488c739261012a45956a77a7b98590e06d938c6497aaca36133433d7f523d35ed80fb9744e9f11bef606a8b2086b60d3e16aee182d0f159a14e
      028fa5bcf187b4f488c739261012a45956a77a7b98590e06d938c6497aaca36133
      ETH Derived Address:  0x58b92725e690d3a76bb5048564ddf195c5543c6e
      Does the Public Address Derived from Key and Sender Address Equal:  True
      BTC P2PK Derived Address:  1951v3dixBuvsf7qp6KCiPXwKVe1VGsRt9
       */
      for {
       result <- PublicKeyAnalyzer.toBech32(keyOpt)
       doesMatch = result == "tb1q4ead8pr0qq5p78t4yvw3vedamacrzsreshdsvm"
    } yield Response.text(result + s"\nDoes match? $doesMatch")
  }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    (putStrLn(s"Server booting on $port!") <* Server.start(port, app +++ helloRoutes)).exitCode
}
