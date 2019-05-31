package com.example.mydemo.demo

import android.os.RemoteException
import com.example.mydemo.ArkeSdkDemoApplication
import com.usdk.apiservice.aidl.device.DeviceInfo
import com.usdk.apiservice.aidl.device.UDeviceManager

object Device {

    @Throws(RemoteException::class)
    private fun deviceManager(): UDeviceManager? {
        return ArkeSdkDemoApplication.deviceService.getDeviceManager()
    }

    @Throws(RemoteException::class)
    fun getInfo(): String {
        val device = deviceManager()?.deviceInfo
        return "Serial no: ${device?.serialNo} \n" +
                "Hardware serial no: ${device?.hardWareSn} \n" +
                "Terminal model: ${device?.model} \n" +
                "Manufacturer: ${device?.manufacture}"
    }

    @Throws(RemoteException::class)
    fun getModuleVersion(): String {
        val moduleNames = arrayListOf<String>()
        moduleNames.add("rfcard")
        moduleNames.add("iccard")
        moduleNames.add("printer")
        moduleNames.add("masterControl")
        moduleNames.add("EMVKernel")
        moduleNames.add("libEMV")
        moduleNames.add("pinpad")
        moduleNames.add("s-module")
        val versions = deviceManager()?.getSystemModulesVersion(moduleNames)
        var string = ""
        for (name in moduleNames) {
            string += name + ": " + versions?.getString(name) + "\n"
        }
        return string
    }
}