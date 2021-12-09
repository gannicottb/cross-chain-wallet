// import pkg from "@ethereumjs/tx";
//
// const {Transaction} = pkg;
//
// const signedTx = "0xf86b8084861c468b82520894668d0d974314cbdb3b898a4241d439f8834fd4638802c68af0bb140000802aa0bc1066ab8b0f40c66ea1721229d41890318b18423e846b8b9b7516ed3eb08934a07222f7c9bfdcb1a9504fc82af6df7fd8c1cc248aa9c6217db5517f1a30e87dac"
//
// const tx = Transaction.fromSerializedTx(Buffer.from(signedTx))
// const address = Buffer.from(tx.getSenderAddress())
// const publicKey = Buffer.from(tx.getSenderPublicKey())
// console.log(address)
// console.log(publicKey)

const web3 = require("web3")

web3.eth.personal.ecRecover("0xf8efa9c9d18ad82feee5c662583027e45d2e1fbfa40fe48ba49dfa983ac8008b", "").then(console.log)