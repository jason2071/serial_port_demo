package com.example.mydemo

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import com.example.mydemo.api.DeviceService
import com.example.mydemo.manager.Contextor
import com.usdk.apiservice.aidl.UDeviceService


class ArkeSdkDemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        bindSdkDeviceService()
        Contextor.getInstance().init(baseContext)
    }

    override fun onTerminate() {
        super.onTerminate()
        deviceService.unRegister()
    }

    private fun bindSdkDeviceService() {
        val intent = Intent()
        intent.action = ACTION_API_SERVICE
        intent.`package` = PACKAGE_API_SERVICE

        log("binding sdk device service...")

        val flag = bindService(intent, connection, Context.BIND_AUTO_CREATE)
        if (!flag) {
            log("SDK service binding failed.")
            return
        }
        log("SDK service binding successfully.")
    }

    private val connection: ServiceConnection
        get() = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                log("SDK service disconnected.")
                mDeviceService = null
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                log("SDK service connected.")
                try {

                    mDeviceService = DeviceService(UDeviceService.Stub.asInterface(service))
                    if (mDeviceService != null) {
                        mDeviceService!!.register()
                        log("SDK deviceService initiated version:" + mDeviceService!!.version() + ".")
                    }

                } catch (e: RemoteException) {
                    throw RuntimeException("SDK deviceService initiating failed.", e)
                }
            }
        }

    companion object {
        private const val TAG = "ArkeSdkDemoApplication"
        private const val ACTION_API_SERVICE = "com.usdk.apiservice"
        private const val PACKAGE_API_SERVICE = "com.usdk.apiservice"
        private var mDeviceService: DeviceService? = null

        val deviceService: DeviceService
            get() = if (mDeviceService != null)
                mDeviceService!!
            else
                throw RuntimeException("SDK service is still not connected.")
    }

    private fun log(s: String) {
        Log.d(TAG, s)
    }
}