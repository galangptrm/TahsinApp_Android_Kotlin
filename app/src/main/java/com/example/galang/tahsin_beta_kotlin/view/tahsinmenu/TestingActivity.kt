package com.example.galang.tahsin_beta_kotlin.view.tahsinmenu

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.widget.Toast
import com.example.galang.tahsin_beta_kotlin.adapter.ListKesalahanAdapter
import com.example.galang.tahsin_beta_kotlin.algorithm.diff_match_patch
import com.example.galang.tahsin_beta_kotlin.model.AyatGundulList
import com.example.galang.tahsin_beta_kotlin.model.Kesalahan
import kotlinx.android.synthetic.main.activity_testing.*
import kotlin.collections.ArrayList
import com.github.zagum.speechrecognitionview.RecognitionProgressView
import android.Manifest
import android.util.Log
import android.view.View
import com.example.galang.tahsin_beta_kotlin.adapter.PilihAyatAdapter.Companion.fromAdapter
import com.example.galang.tahsin_beta_kotlin.algorithm.LevensteinDistance
import com.example.galang.tahsin_beta_kotlin.R
import com.example.galang.tahsin_beta_kotlin.view.tahsinmenu.PilihAyatActivity.Companion.fromActivity


class TestingActivity : AppCompatActivity(), RecognitionListener {
    companion object {
        var NAMASURAT_EXTRA = "nama_surat"
        var NOMORAYATAWAL_EXTRA = "nomor_ayatawal"
        var NOMORAYATAKHIR_EXTRA = "nomor_ayatakhir"
        var TEXTAYAT_EXTRA = "text_ayat"
        var URUTANAYAT_EXTRA = "urutan_ayat"
        var TYPE_INTENT = "type_intent"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)

        _permissionRequest()

        _objectInitiation()

        _speechInitialization()

    }

    var ayatGundul = ""
    var kesalahanList: ArrayList<Kesalahan> = arrayListOf()

    private fun _permissionRequest() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.RECORD_AUDIO)) {
            } else {
                var MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.RECORD_AUDIO), MY_PERMISSIONS_REQUEST_RECORD_AUDIO)
                Toast.makeText(this, "Record Persmission Granted", Toast.LENGTH_SHORT).show()

            }
        } else {
            // Permission has already been granted
        }
    }

    val TAG = "tag_ayat"
    private fun _objectInitiation() {

        val namaSurat = intent.getStringExtra(NAMASURAT_EXTRA)
        val nomorAyat = intent.getIntExtra(NOMORAYATAWAL_EXTRA, 1)
        val nomorAkhirAyat = intent.getIntExtra(NOMORAYATAKHIR_EXTRA, 1)
        val textAyat = intent.getStringExtra(TEXTAYAT_EXTRA)
        val urutanAyat = intent.getIntExtra(URUTANAYAT_EXTRA, 1)
        val typeIntent = intent.getStringExtra(TYPE_INTENT)

        Log.d(TAG, textAyat)
        Log.d(TAG, Integer.toString(urutanAyat))

        if (typeIntent == fromAdapter) {
            if (namaSurat != "Al Fatihah" && nomorAyat == 1) {
                this.ayatGundul = AyatGundulList().getAyat(urutanAyat).substring(23, AyatGundulList().getAyat(urutanAyat).length)
            } else {
                this.ayatGundul = AyatGundulList().getAyat(urutanAyat)
            }
        } else if (typeIntent == fromActivity) {
            this.ayatGundul = AyatGundulList().getSomeAyats(urutanAyat + (nomorAyat - 2), urutanAyat + (nomorAkhirAyat - 1))
        }

        Log.d(TAG, ayatGundul)
        txtView_distanceResult.text = "Tekan tombol rekam dan mulai membaca ayat"

        txtView_AyatPreview.text = textAyat
        supportActionBar?.title = namaSurat + " : " + nomorAyat.toString()
    }

    private fun _fetchKesalahan(kesalahanList: ArrayList<Kesalahan>) {
        recView_kesalahan.layoutManager = LinearLayoutManager(this)
        recView_kesalahan.adapter = ListKesalahanAdapter(kesalahanList)
    }

    private fun _diff_match(strSpeech: String, strAyat: String) {

        val dmp = diff_match_patch()
        val diff = dmp.diff_main(strAyat, strSpeech, true)
        dmp.Diff_Timeout = 5.0F
        dmp.diff_cleanupSemantic(diff)

        val levensteinDistance = LevensteinDistance()
        val resultDistance = levensteinDistance.dynamicEditDistance(strAyat.toCharArray(), strSpeech.toCharArray())

        val percentDifference = resultDistance.toFloat() / strSpeech.length.toFloat()

        txtView_distanceResult.text = ""

        if (percentDifference >= 0.92) {
            txtView_distanceResult.text = "Perbedaan bacaan terlalu besar, " +
                    "kemungkinan anda salah membaca ayat (jumlah kesalahan : " + "%.2f".format(percentDifference * 100) + "%)"
        } else {
            txtView_distanceResult.text = "Jumlah Kesalahan = " + "%.2f".format(percentDifference * 100) + "%"

            var tempString = ""

            for (indx in diff.indices) {
                tempString += diff[indx].text
            }

            var ss = SpannableString(tempString)

            var tempIndx = 0

            if (diff != null) {
                for (indx in diff.indices) {
                    if (diff[indx] == null) {
                        break
                    }
                    if (diff[indx].operation.name == "EQUAL") {
                        kesalahanList.add(indx, Kesalahan(diff[indx].operation.name,
                                diff[indx].text,
                                tempIndx,
                                tempIndx + diff[indx].text.length))
                        tempIndx += diff[indx].text.length
                    } else if (diff[indx].operation.name == "DELETE") {
                        var fcsRed = BackgroundColorSpan(Color.RED)
                        ss.setSpan(fcsRed, tempIndx, tempIndx + diff[indx].text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        kesalahanList.add(indx,
                                Kesalahan(diff[indx].operation.name,
                                        diff[indx].text,
                                        tempIndx,
                                        tempIndx + diff[indx].text.length))
                        tempIndx += diff[indx].text.length
                    } else if (diff[indx].operation.name == "INSERT") {
                        var fcsGrn = BackgroundColorSpan(Color.YELLOW)
                        ss.setSpan(fcsGrn, tempIndx, tempIndx + diff[indx].text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        kesalahanList.add(indx,
                                Kesalahan(diff[indx].operation.name,
                                        diff[indx].text,
                                        tempIndx,
                                        tempIndx + diff[indx].text.length))
                        tempIndx += diff[indx].text.length
                    }
                }
            }
            txtView_SpeechResult.text = ss
        }

    }

    private fun _speechInitialization() {

        val speech = SpeechRecognizer.createSpeechRecognizer(this)
        speech.setRecognitionListener(this)

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-SA")

        val recognitionProgressView = this.findViewById<RecognitionProgressView>(R.id.recognition_animation)
        this.findViewById<RecognitionProgressView>(R.id.recognition_animation).visibility = View.INVISIBLE
        recognitionProgressView.setSpeechRecognizer(speech)
        recognitionProgressView.setRecognitionListener(this)
        recognitionProgressView.play()

        recognitionProgressView.setCircleRadiusInDp(2)
        recognitionProgressView.setSpacingInDp(2)
        recognitionProgressView.setIdleStateAmplitudeInDp(2)
        recognitionProgressView.setRotationRadiusInDp(10)
        val heights = intArrayOf(17, 15, 22, 15, 12)
        val colors = intArrayOf(
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorAccent)
        )
        recognitionProgressView.setColors(colors)
        recognitionProgressView.setBarMaxHeightsInDp(heights)

        floatActBtn_speech.setOnClickListener {

            if (intent.resolveActivity(packageManager) != null) {
                speech.startListening(intent)
            } else {
                Toast.makeText(this, "Your Device not Support Speech Recognizer", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onReadyForSpeech(params: Bundle?) {
        this.findViewById<RecognitionProgressView>(R.id.recognition_animation).visibility = View.VISIBLE
        txtView_distanceResult.text = "Silahkan mulai baca ayat..."
    }

    override fun onRmsChanged(rmsdB: Float) {

    }

    override fun onBufferReceived(buffer: ByteArray?) {

    }

    override fun onPartialResults(partialResults: Bundle?) {
        txtView_SpeechResult.text = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).toString()
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
    }

    override fun onBeginningOfSpeech() {
        this.findViewById<RecognitionProgressView>(R.id.recognition_animation).visibility = View.VISIBLE
        txtView_distanceResult.text = "Mendengarkan..."

    }

    override fun onEndOfSpeech() {
        txtView_distanceResult.text = ""
        this.findViewById<RecognitionProgressView>(R.id.recognition_animation).visibility = View.INVISIBLE
    }

    override fun onError(error: Int) {
        when (error) {
            1 -> Toast.makeText(this, "Error 1 - jaringan internet terlalu lambat, silahkan coba lagi ", Toast.LENGTH_SHORT).show()
            2 -> Toast.makeText(this, "Error 2 - kesalahan pada jaringan internet, silahkan coba lagi ", Toast.LENGTH_SHORT).show()
            3 -> Toast.makeText(this, "Error 3 - perekaman suara gagal, silahkan coba lagi ", Toast.LENGTH_SHORT).show()
            4 -> Toast.makeText(this, "Error 4 - kesalahan pada server, silahkan coba lagi", Toast.LENGTH_SHORT).show()
            5 -> Toast.makeText(this, "Error 5 - aplikasi mengalami kesalahan, silahkan coba lagi ", Toast.LENGTH_SHORT).show()
            6 -> Toast.makeText(this, "Error 6 - suara anda tidak terdengar, silahkan coba lagi ", Toast.LENGTH_SHORT).show()
            7 -> Toast.makeText(this, "Error 7 - suara anda tidak terdengar, silahkan coba lagi ", Toast.LENGTH_SHORT).show()
            8 -> Toast.makeText(this, "Error 8 - aplikasi sedang sibuk menerjemahkan, silahkan coba lagi ", Toast.LENGTH_SHORT).show()
            9 -> Toast.makeText(this, "Error 9 - aplikasi tidak mendapat izin merekam suara", Toast.LENGTH_SHORT).show()
        }
        txtView_distanceResult.text = "Tekan tombol rekam dan mulai membaca ayat"
    }

    override fun onResults(results: Bundle?) {
        val data = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        txtView_SpeechResult.text = data?.get(0).toString()
        txtView_distanceResult.text = ""

        kesalahanList.clear()
        _diff_match(ayatGundul, txtView_SpeechResult.text.toString())
        _fetchKesalahan(this.kesalahanList)
    }

}


