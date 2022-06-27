package auth0.invite

import java.security.SecureRandom
import kotlin.random.Random

fun generatePassword(
    length: Int = 16,
    dictionary: String = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
) = dictionary
    .asSequence()
    .shuffled(KotlinSecureRandom())
    .take(length)
    .joinToString("")

class KotlinSecureRandom(private val secureRandom: SecureRandom = SecureRandom.getInstanceStrong()) : Random() {
    override fun nextBits(bitCount: Int) =
        secureRandom.nextInt().takeUpperBits(bitCount)
}

internal fun Int.takeUpperBits(bitCount: Int): Int =
    this.ushr(32 - bitCount) and (-bitCount).shr(31)