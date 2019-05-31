package com.example.mydemo.utils;

import android.util.Log;

public class Crc16Utils {

    public static int calculate_crc(byte[] bytes) {
        int i;
        int crc_value = 0;
        for (byte aByte : bytes) {
            for (i = 0x80; i != 0; i >>= 1) {
                if ((crc_value & 0x8000) != 0) {
                    crc_value = (crc_value << 1) ^ 0x8005;
                } else {
                    crc_value = crc_value << 1;
                }
                if ((aByte & i) != 0) {
                    crc_value ^= 0x8005;
                }
            }
        }
        return crc_value;
    }
}
