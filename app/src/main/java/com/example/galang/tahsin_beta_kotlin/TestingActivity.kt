package com.example.galang.tahsin_beta_kotlin

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import com.example.galang.tahsin_beta_kotlin.Adapter.ListKesalahanAdapter
import com.example.galang.tahsin_beta_kotlin.Algorithm.diff_match_patch
import com.example.galang.tahsin_beta_kotlin.Model.Kesalahan
import kotlinx.android.synthetic.main.activity_testing.*
import kotlin.collections.ArrayList

class TestingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)

        _objectInitiation()

        _speechInitialization()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                var _result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                txtView_SpeechResult.setText(_result.get(0));

                kesalahanList.clear()

                _diff_match("الحمد لله رب العالمين", txtView_SpeechResult.text.toString())

                fetchKesalahan(this.kesalahanList)
            }
        }
    }

    var kesalahanList : ArrayList<Kesalahan> = arrayListOf()
    private fun fetchKesalahan(kesalahanList : ArrayList<Kesalahan>){
        recView_kesalahan.layoutManager = LinearLayoutManager(this)
        recView_kesalahan.adapter = ListKesalahanAdapter(kesalahanList)
    }

    private fun _diff_match(strSpeech: String, strAyat: String) {

        val dmp = diff_match_patch()
        val diff = dmp.diff_main(strAyat, strSpeech, true)
        dmp.Diff_Timeout = 5.0F
        dmp.diff_cleanupSemantic(diff)

        val levDistance = dmp.diff_levenshtein(diff)
        val percentDifference = levDistance.toFloat()/strSpeech.length.toFloat()
        txtView_distanceResult.text  = ""

        if (percentDifference >= 0.92 ){
            txtView_distanceResult.text = "Perbedaan bacaan terlalu besar, " +
                    "kemungkinan anda salah membaca ayat (" + percentDifference +")"
        }else{
            txtView_distanceResult.text = "Distance = " + percentDifference
            var tempString = ""

            for (indx in diff.indices) {
                tempString += diff[indx].text
            }

            var ss = SpannableString(tempString)
            var tempIndx = 0

            if (diff!=null){
                for (indx in diff.indices) {
                    if (diff[indx] == null) {
                        break
                    }
                    if (diff[indx].operation.name == "EQUAL") {
                        kesalahanList.add(indx, Kesalahan(diff[indx].operation.name, diff[indx].text,
                                tempIndx,tempIndx + diff[indx].text.length))
                        tempIndx += diff[indx].text.length
                    }
                    else if (diff[indx].operation.name == "DELETE") {
                        var fcsRed = BackgroundColorSpan(Color.RED)
                        ss.setSpan(fcsRed, tempIndx, tempIndx + diff[indx].text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        kesalahanList.add(indx, Kesalahan(diff[indx].operation.name, diff[indx].text,
                                tempIndx,tempIndx + diff[indx].text.length))
                        tempIndx += diff[indx].text.length
                    }
                    else if (diff[indx].operation.name == "INSERT") {
                        var fcsGrn = BackgroundColorSpan(Color.GREEN)
                        ss.setSpan(fcsGrn, tempIndx, tempIndx + diff[indx].text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                        kesalahanList.add(indx, Kesalahan(diff[indx].operation.name, diff[indx].text,
                                tempIndx,tempIndx + diff[indx].text.length))
                        tempIndx += diff[indx].text.length
                    }
                }
            }
            txtView_SpeechResult.text = ss
        }

    }

    private fun _objectInitiation() {
        val namaSurat = intent.getStringExtra("namaSurat_extra")
        val nomorAyat = intent.getIntExtra("nomorAyat_extra", 0)
        val textAyat = intent.getStringExtra("textAyat_extra")

        txtView_AyatPreview.setText(textAyat)
        supportActionBar?.title = namaSurat + " : " + nomorAyat.toString()
    }

    private fun _speechInitialization() {

        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-SA")

        floatActBtn_speech.setOnClickListener {
            Toast.makeText(this, "Clicked 2", Toast.LENGTH_SHORT).show()
            if (speechIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(speechIntent, 10)
            } else {
                Toast.makeText(this, "Your Device not Support", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun testing() {
        var numbers = "0123456789"
        var ssb = SpannableString(numbers)

        ssb.setSpan(ForegroundColorSpan(Color.RED), 1, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        txtView_distanceResult.text = ssb

    }
}

private operator fun CharSequence.plusAssign(ss: SpannableStringBuilder) {

}


