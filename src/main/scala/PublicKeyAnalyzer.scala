package com.ciphertrace

import scodec.bits.ByteVector
import org.bouncycastle.crypto.Digest
import org.bouncycastle.crypto.digests.{RIPEMD160Digest, SHA1Digest, SHA256Digest, SHA512Digest}
import fr.acinq.bitcoin.ByteVector32
import fr.acinq.bitcoin.ByteVector32.byteVector32toByteVector
import zio.Task
import zio.console._

object PublicKeyAnalyzer {

  def toBech32(pubKey: String) = for {
    _ <- putStrLn(s"Input: $pubKey")
    sha256hash <- Task(sha256(ByteVector(pubKey.getBytes()))).tap(o => putStrLn(s"SHA256: $o"))
    _ <- Task(sha256(byteVector32toByteVector(sha256hash))).tap(o => putStrLn(s"SHA -> SHA: $o"))
    ripemd <- Task(ripemd160(sha256hash)).tap(o => putStrLn(s"RIPEMD-160: $o"))
    result <- Task(Bech32.encodeWitnessAddress("tb", 0.toByte, ripemd)).tap(o => putStrLn(s"Bech32: $o"))
  } yield result

  // copied from https://github.com/gingerballsinc/bitcoin-lib/blob/master/src/main/scala/fr/acinq/bitcoin/Crypto.scala
  def hash(digest: Digest)(input: ByteVector): ByteVector = {
    digest.update(input.toArray, 0, input.length.toInt)
    val out = new Array[Byte](digest.getDigestSize)
    digest.doFinal(out, 0)
    ByteVector.view(out)
  }

  def sha256 = (x: ByteVector) => ByteVector32(hash(new SHA256Digest)(x))

  def ripemd160 = hash(new RIPEMD160Digest) _

}
