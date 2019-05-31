package com.example.mydemo.utils


object CRC16 {

    fun balanceByte(): ByteArray {
        return byteArrayOf(
            0x10.toByte(), 0x02.toByte(), 0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x3c.toByte(), 0x00.toByte(), 0x01.toByte(), 0x00.toByte(), 0x01.toByte(), 0x00.toByte(), 0x13.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x01.toByte(), 0x00.toByte(), 0x08.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0xb3.toByte(), 0xce.toByte(), 0x13.toByte(),
            0x00.toByte(), 0xbf.toByte(), 0x4e.toByte(), 0x10.toByte(), 0x03.toByte()
        )
    }

    fun balanceInt(): IntArray {
        return intArrayOf(
            0x10, 0x02, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x3c, 0x00, 0x01, 0x00, 0x01, 0x00, 0x13,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x08,
            0x00, 0x00, 0x00, 0x00, 0x00, 0xb3, 0xce, 0x13,
            0x00, 0xbf, 0x4e, 0x10, 0x03
        )
    }

    fun calculate(bytes: ByteArray): Int {
        var i: Int
        var value = 0
        for (aByte in bytes) {
            i = 0x80
            while (i != 0) {
                value = if (value and 0x8000 != 0) {
                    value shl 1 xor 0x8005
                } else {
                    value shl 1
                }
                if ((aByte.toInt()) and i != 0) {
                    value = value xor 0x8005
                }
                i = i shr 1
            }
        }
        return value
    }
}