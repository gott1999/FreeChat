package edu.xww.urchat.data.struct.security

import java.security.Key
import java.security.PrivateKey
import java.security.PublicKey

// 会话密钥串
data class Keychain(
    val publicKey: PublicKey,
    val privateKey: PrivateKey,
    val sessionKey: Key,
    val challenge: String
)
