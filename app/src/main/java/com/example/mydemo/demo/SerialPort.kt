package com.example.mydemo.demo

import android.os.RemoteException
import android.os.SystemClock
import android.util.Log
import com.example.mydemo.ArkeSdkDemoApplication
import com.usdk.apiservice.aidl.serialport.SerialPortError
import com.usdk.apiservice.aidl.serialport.USerialPort
import com.example.mydemo.utils.BytesUtil


object SerialPort {

    private var serialPort: USerialPort? = null

    @Throws(RemoteException::class)
    fun open(deviceName: String): Boolean {
        serialPort = ArkeSdkDemoApplication.deviceService.getSerialPort(deviceName)
        val ret = serialPort?.open()
        return ret == SerialPortError.SUCCESS
    }

    @Throws(RemoteException::class)
    fun init(baudRate: Int, parityBit: Int, dataBit: Int): Boolean {
        val ret = serialPort?.init(baudRate, parityBit, dataBit)
        return ret == SerialPortError.SUCCESS
    }

    @Throws(RemoteException::class)
    fun write(data: ByteArray, timeout: Int): Int? {
        val writeLength = serialPort?.write(data, timeout)
        return writeLength
        //return BytesUtil.subBytes(data, 0, writeLength!!)
    }

    @Throws(RemoteException::class)
    fun read(data: ByteArray, timeout: Int): ByteArray {
        val readLength = serialPort!!.read(data, timeout)
        return BytesUtil.subBytes(data, 0, readLength)
    }

    @Throws(RemoteException::class)
    fun read2(data: ByteArray, timeout: Int): ByteArray {

        var readLength = 0
        var i = 0
        do {

            Log.d("MainActivityA", "i: $i")

            i++

            readLength = serialPort!!.read(data, timeout)

            if (readLength == 0) {
                continue
            }

            if (readLength < 0) {
                //throw SDKException("read data fail!");
            }

            Log.d("MainActivityA", "len: $readLength")

            break

        } while (i < 10)

        return BytesUtil.subBytes(data, 0, readLength)
    }

    @Throws(RemoteException::class)
    fun clearInputBuffer(): Boolean {
        val ret = serialPort?.clearInputBuffer()
        return ret == SerialPortError.SUCCESS
    }

    @Throws(RemoteException::class)
    fun close(): Boolean {
        val ret = serialPort?.close()
        return ret == SerialPortError.SUCCESS
    }
}