package com.symbolscope.weartotp.totp

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object TotpGenerator {
    private const val TIME_STEP = 30L
    private const val DIGITS = 6

    fun generateCode(secret: String, timeMs: Long = System.currentTimeMillis()): String {
        val key = Base32.decode(secret)
        val counter = timeMs / 1000 / TIME_STEP
        val counterBytes = ByteArray(8)
        var c = counter
        for (i in 7 downTo 0) {
            counterBytes[i] = (c and 0xFF).toByte()
            c = c shr 8
        }

        val mac = Mac.getInstance("HmacSHA1")
        mac.init(SecretKeySpec(key, "HmacSHA1"))
        val hash = mac.doFinal(counterBytes)

        val offset = (hash.last().toInt() and 0xF)
        val value = ((hash[offset].toInt() and 0x7F) shl 24) or
                ((hash[offset + 1].toInt() and 0xFF) shl 16) or
                ((hash[offset + 2].toInt() and 0xFF) shl 8) or
                (hash[offset + 3].toInt() and 0xFF)

        val code = value % 1_000_000
        return code.toString().padStart(DIGITS, '0')
    }

    fun secondsUntilNext(timeMs: Long = System.currentTimeMillis()): Int {
        return (TIME_STEP - (timeMs / 1000) % TIME_STEP).toInt()
    }
}
