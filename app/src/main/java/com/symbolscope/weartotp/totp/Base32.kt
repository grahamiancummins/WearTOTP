package com.symbolscope.weartotp.totp

object Base32 {
    private const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"

    fun decode(input: String): ByteArray {
        val sanitized = input.uppercase().trimEnd('=').replace(" ", "")
        val byteCount = sanitized.length * 5 / 8
        val result = ByteArray(byteCount)

        var buffer = 0L
        var bitsLeft = 0
        var byteIndex = 0

        for (char in sanitized) {
            val value = ALPHABET.indexOf(char)
            if (value < 0) continue
            buffer = (buffer shl 5) or value.toLong()
            bitsLeft += 5
            if (bitsLeft >= 8) {
                bitsLeft -= 8
                result[byteIndex++] = ((buffer shr bitsLeft) and 0xFF).toByte()
            }
        }

        return result
    }
}
