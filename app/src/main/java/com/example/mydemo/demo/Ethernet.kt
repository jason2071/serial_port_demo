package com.example.mydemo.demo

import android.os.RemoteException
import com.example.mydemo.ArkeSdkDemoApplication
import com.usdk.apiservice.aidl.ethernet.UEthernet
import com.usdk.apiservice.aidl.ethernet.EthernetData
import android.os.Bundle


object Ethernet {

    @Throws(RemoteException::class)
    private fun ethernet(): UEthernet? {
        return ArkeSdkDemoApplication.deviceService.getEthernet()
    }

    @Throws(RemoteException::class)
    fun getConfigInfo(): String {
        var string = ""
        val bundle = ethernet()?.configInfo
        if (bundle != null) {
            val localIp = bundle.getString(EthernetData.LOCAL_IP)
            val gateway = bundle.getString(EthernetData.GATEWAY)
            val mask = bundle.getString(EthernetData.MASK)
            val dns1 = bundle.getString(EthernetData.DNS1)
            val dns2 = bundle.getString(EthernetData.DNS2)
            val isDhcp = bundle.getBoolean(EthernetData.IS_DHCP)

            string = "localIp: $localIp \n" +
                    "gateway: $gateway \n" +
                    "mask: $mask \n" +
                    "dns1: $dns1 \n" +
                    "dns2: $dns2 \n" +
                    "isDhcp: $isDhcp \n"

        }
        return string
    }
}