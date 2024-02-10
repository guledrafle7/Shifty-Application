package com.example.shifty.activities.generatedqr

import android.R.string
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils.split
import androidx.appcompat.app.AppCompatActivity
import com.example.shifty.databinding.ActivityGeneratedQrcodesBinding
import com.example.shifty.utils.Constant.Companion.QR_GENERATED_DATE_KEY
import com.example.shifty.utils.Constant.Companion.QR_GENERATED_VALUE_KEY
import com.example.shifty.utils.Constant.Companion.currentDate
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.util.*


class GeneratedQRCodesActivity : AppCompatActivity() {

    private var intentShift: String? =null

    private lateinit var binding: ActivityGeneratedQrcodesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGeneratedQrcodesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        intentShift = intent.getStringExtra(QR_GENERATED_VALUE_KEY)

        binding.apply {
            tvDate.text = "Date: ${intent.getStringExtra(QR_GENERATED_DATE_KEY)}"

            ivCheckInQr.setImageBitmap(generateQRCodeBitmap(qrCodeText = "$intentShift,1,0", qrCodeSize = 512))
            ivCheckOutQr.setImageBitmap(generateQRCodeBitmap(qrCodeText = "$intentShift,1,1", qrCodeSize = 512))

            appbar.ivBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun generateQRCodeBitmap(qrCodeText: String, qrCodeSize: Int): Bitmap {
        val hints = Hashtable<EncodeHintType, Any>()
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        val bitMatrix = QRCodeWriter().encode(qrCodeText, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hints)
        val bitmap = Bitmap.createBitmap(qrCodeSize, qrCodeSize, Bitmap.Config.ARGB_8888)
        for (x in 0 until qrCodeSize) {
            for (y in 0 until qrCodeSize) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }
}