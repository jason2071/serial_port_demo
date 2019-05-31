package com.example.mydemo.demo

import android.os.RemoteException
import com.example.mydemo.ArkeSdkDemoApplication
import com.usdk.apiservice.aidl.led.Light
import com.usdk.apiservice.aidl.led.ULed

object Led {

    private var redOn = false

    @Throws(RemoteException::class)
    private fun led(): ULed {
        return ArkeSdkDemoApplication.deviceService.getLed()
    }

    @Throws(RemoteException::class)
    fun redLight() {
        if (!redOn) {
            led().turnOn(Light.RED)
            redOn = true
        } else {
            led().turnOff(Light.RED)
            redOn = false
        }
    }
}