package com.example.mydemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.RemoteException
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mydemo.demo.SerialPort
import com.example.mydemo.utils.BytesUtil
import com.example.mydemo.utils.CRC16
import com.example.mydemo.utils.Crc16Utils
import com.example.mydemo.utils.DemoByte
import com.google.gson.Gson
import com.usdk.apiservice.aidl.serialport.BaudRate
import com.usdk.apiservice.aidl.serialport.DataBit
import com.usdk.apiservice.aidl.serialport.DeviceName
import com.usdk.apiservice.aidl.serialport.ParityBit
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


private const val TAG = "MainActivityA"

enum class Post {
    OPEN, CLOSE
}

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var onClick: View.OnClickListener = this
    private var isOpen = Post.CLOSE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setButtonClick()

        val byteBalance = DemoByte.checkBalanceByte().drop(2).dropLast(4).toByteArray()
        val byteStr1 = ByteArray(4)
        val demo1 = Crc16Utils.calculate_crc(byteBalance)
        byteStr1[0] = (demo1 and 0x000000ff).toByte()
        byteStr1[1] = (demo1 and 0x0000ff00).ushr(8).toByte()

        log("Balance: " + Integer.toHexString(demo1))
        log("Balance: " + gson(byteStr1))
        log("Balance: " + BytesUtil.byteArray2HexString(byteStr1))


        val byteCancel = DemoByte.writeCancel().drop(2).dropLast(4).toByteArray()
        val byteStr3 = ByteArray(4)
        val demo3 = Crc16Utils.calculate_crc(byteCancel)
        byteStr3[0] = (demo3 and 0x000000ff).toByte()
        byteStr3[1] = (demo3 and 0x0000ff00).ushr(8).toByte()

        log("Cancel: " + Integer.toHexString(demo3))
        log("Cancel: " + gson(byteStr3))
        log("Cancel: " + BytesUtil.byteArray2HexString(byteStr3))


        val deviceInfo = DemoByte.deviceInfo().drop(2).dropLast(4).toByteArray()
        val byteStr4 = ByteArray(4)
        val demo4 = Crc16Utils.calculate_crc(deviceInfo)
        byteStr4[0] = (demo4 and 0x000000ff).toByte()
        byteStr4[1] = (demo4 and 0x0000ff00).ushr(8).toByte()

        log("deviceInfo: " + Integer.toHexString(demo4))
        log("deviceInfo: " + gson(byteStr4))
        log("deviceInfo: " + BytesUtil.byteArray2HexString(byteStr4))

    }

    private fun setButtonClick() {
        btnOpen.setOnClickListener(onClick)
        btnClose.setOnClickListener(onClick)
        btnWrite.setOnClickListener(onClick)
        btnRead.setOnClickListener(onClick)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnOpen -> {
                tvDisplayWriteResult.text = ""
                tvDisplayReadResult.text = ""
                getDeviceName()
            }
            R.id.btnClose -> closeSerialPort()
            R.id.btnWrite -> writeData()
            R.id.btnRead -> readData()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getDeviceName() {
        val devices = DemoByte.deviceName()
        if (devices.isNotEmpty()) {
            val deviceName = getDeviceName("ttyUSB", "ttyACM") ?: throw RemoteException("no device")
            openSerialPort(deviceName)
        } else {
            openSerialPort(DeviceName.USBD)
        }
    }

    private fun getDeviceName(vararg prefixes: String): String? {
        val dev = File("/dev")
        for (file in dev.listFiles()!!) {
            for (prefix in prefixes) {
                if (file.absolutePath.startsWith("/dev/$prefix")) {
                    return file.toString().substring(5)
                }
            }
        }
        return null
    }

    @SuppressLint("SetTextI18n")
    private fun openSerialPort(deviceName: String) {
        val openState = SerialPort.open(deviceName)
        if (!openState) {
            tvDisplayResult.text = "$deviceName: open fail"
        } else {
            isOpen = Post.OPEN
            tvDisplayResult.text = "$deviceName: open success"
            initSerialPort()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initSerialPort() {
        val initState = SerialPort.init(BaudRate.BPS_115200, ParityBit.NOPAR, DataBit.DBS_8)
        if (!initState) {
            tvDisplayInit.text = "init fail"
        } else {
            tvDisplayInit.text = "init success"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun readData() {
        if (isOpen == Post.CLOSE) {
            tvDisplayResult.text = "port is close"
        } else {
            val readData = ByteArray(50)
            val result = SerialPort.read2(readData, 1000)
            tvDisplayReadResult.text = Gson().toJson(result)
            //closeSerialPort()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun writeData() {
        if (isOpen == Post.CLOSE) {
            tvDisplayResult.text = "port is close"
        } else {
            val len = SerialPort.write(DemoByte.writeCancel(), 0)
            if (len != null) {
                if (len > 0) {
                    //tvDisplayWriteResult.text = len.toString()
                    tvDisplayWriteResult.text = BytesUtil.byteArray2HexString(DemoByte.writeCancel())
                    //clearBuffer()
                } else {
                    tvDisplayWriteResult.text = "write fail"
                }
            } else {
                tvDisplayWriteResult.text = "write length is null"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun closeSerialPort() {
        tvDisplayInit.text = ""
        val status = SerialPort.close()
        if (!status) {
            tvDisplayResult.text = "close fail"
        } else {
            tvDisplayResult.text = "close success"
            isOpen = Post.CLOSE
        }
    }

    private fun clearBuffer() {
        try {
            SerialPort.clearInputBuffer()
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    private fun gson(any: Any): String {
        return Gson().toJson(any)
    }

    private fun log(s: String) {
        Log.d(TAG, s)
    }

    private fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}
