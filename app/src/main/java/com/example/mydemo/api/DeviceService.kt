package com.example.mydemo.api

import android.os.Binder
import android.os.Bundle
import android.os.RemoteException
import com.usdk.apiservice.aidl.UDeviceService
import com.usdk.apiservice.aidl.beeper.UBeeper
import com.usdk.apiservice.aidl.constants.RFDeviceName
import com.usdk.apiservice.aidl.device.UDeviceManager
import com.usdk.apiservice.aidl.ethernet.UEthernet
import com.usdk.apiservice.aidl.led.ULed
import com.usdk.apiservice.aidl.serialport.USerialPort

class DeviceService(private val deviceService: UDeviceService?) {

    fun register() {
        try {
            deviceService?.register(null, Binder())
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    fun unRegister() {
        try {
            deviceService?.unregister(null)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    fun version(): String {
        return deviceService?.version!!
    }

    fun getBeeper(): UBeeper {
        try {
            return UBeeper.Stub.asInterface(deviceService?.beeper)
        } catch (e: RemoteException) {
            throw IllegalStateException("Fail to get beeper device service.", e)
        }

    }

    fun getLed(): ULed {
        try {
            val param = Bundle()
            param.putString("rfDeviceName", RFDeviceName.INNER)
            return ULed.Stub.asInterface(deviceService?.getLed(Bundle()))
        } catch (e: RemoteException) {
            throw IllegalStateException("Fail to get led device service.", e)
        }
    }

    fun getDeviceManager(): UDeviceManager? {
        try {
            return UDeviceManager.Stub.asInterface(deviceService?.deviceManager)
        } catch (e: RemoteException) {
            throw IllegalStateException("Fail to get led device service.", e)
        }
    }

    fun getEthernet(): UEthernet? {
        try {
            return UEthernet.Stub.asInterface(deviceService?.ethernet)
        } catch (e: RemoteException) {
            throw IllegalStateException("Fail to get led device service.", e)
        }
    }

    fun getSerialPort(deviceName: String): USerialPort? {
        try {
            return USerialPort.Stub.asInterface(deviceService?.getSerialPort(deviceName))
        } catch (e: RemoteException) {
            throw IllegalStateException("Fail to get serial port device service.", e)
        }

    }
}