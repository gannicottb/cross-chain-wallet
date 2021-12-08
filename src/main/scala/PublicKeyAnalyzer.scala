package com.ciphertrace

import scodec.bits.ByteVector
import org.bouncycastle.crypto.Digest
import org.bouncycastle.crypto.digests.{RIPEMD160Digest, SHA1Digest, SHA256Digest, SHA512Digest}
import fr.acinq.bitcoin.ByteVector32
import zio.Task
import zio.console._

object PublicKeyAnalyzer {

  def toBech32(pubKey: String) = for {
    _ <- putStrLn(s"Input: $pubKey")
    sha256 <- Task(sha256(ByteVector(pubKey.getBytes()))).tap(o => putStrLn(s"SHA256: $o"))
    ripemd <- Task(ripemd160(sha256)).tap(o => putStrLn(s"RIPEMD-160: $o"))
    result <- Task(Bech32.encodeWitnessAddress("bc", 0.toByte, ripemd)).tap(o => putStrLn(s"Bech32: $o"))
  } yield result

  def hash(digest: Digest)(input: ByteVector): ByteVector = {
    digest.update(input.toArray, 0, input.length.toInt)
    val out = new Array[Byte](digest.getDigestSize)
    digest.doFinal(out, 0)
    ByteVector.view(out)
  }

  def sha256 = (x: ByteVector) => ByteVector32(hash(new SHA256Digest)(x))

  def ripemd160 = hash(new RIPEMD160Digest) _

}
