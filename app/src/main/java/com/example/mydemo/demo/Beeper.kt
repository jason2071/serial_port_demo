package com.example.mydemo.demo

import android.os.RemoteException
import com.example.mydemo.ArkeSdkDemoApplication
import com.usdk.apiservice.aidl.beeper.UBeeper

object Beeper {

    @Throws(RemoteException::class)
    private fun beeper(): UBeeper {
        return ArkeSdkDemoApplication.deviceService.getBeeper()
    }

    @Throws(RemoteException::class)
    fun normal() {
        beeper().startBeep(500)
    }
}