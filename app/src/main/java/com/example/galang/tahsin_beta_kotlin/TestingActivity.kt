package com.example.galang.tahsin_beta_kotlin

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils.isEmpty
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Toast
import com.example.galang.tahsin_beta_kotlin.Algorithm.diff_match_patch
import kotlinx.android.synthetic.main.activity_testing.*

class TestingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)

        _objectInitiation()

        _speechInitialization()

//        testing()
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                var _result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                txtView_SpeechResult.setText(_result.get(0));

//                txtView_AyatPreview.setText("");

                _diff_match("الحمد لله رب العالمين", txtView_SpeechResult.text.toString())
//                _levensteinDistance(txtView_AyatPreview.text.toString(), txtView_SpeechResult.text.toString())

            }
        }
    }

    private fun testing() {
        var numbers = "0123456789"
        var ssb = SpannableString(numbers)

        ssb.setSpan(ForegroundColorSpan(Color.RED), 1, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        txtView_distanceResult.text = ssb
        txtView_resultText.text = numbers.length.toString()

    }

    private fun _diff_match(strSpeech: String, strAyat: String) {

        val dmp = diff_match_patch()
        val diff = dmp.diff_main(strAyat, strSpeech, true)
        dmp.Diff_Timeout = 5.0F
        // Result: [(-1, "Hell"), (1, "G"), (0, "o"), (1, "odbye"), (0, " World.")]
        dmp.diff_cleanupSemantic(diff)
        // Result: [(-1, "Hello"), (1, "Goodbye"), (0, " World.")]

        var levDistance = dmp.diff_levenshtein(diff)

        var tempString = ""
        var tempString2 = ""

        for (indx in diff.indices) {
            tempString += diff[indx].text
            tempString2 += diff[indx].toString() + "\n"
        }

        var ss = SpannableString(tempString)

        var tempIndx = 0
        var tempIndx2 = 0

        for (indx in diff.indices) {

            if (diff[indx] == null) {
                break
            }
            if (diff[indx].operation.name == "EQUAL") {
//                var fcsBlu = BackgroundColorSpan(Color.BLUE)
//                ss.setSpan(fcsBlu, tempIndx, tempIndx + diff[indx].text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                tempIndx += diff[indx].text.length
            }
            else if (diff[indx].operation.name == "DELETE") {
                var fcsRed = BackgroundColorSpan(Color.RED)
                ss.setSpan(fcsRed, tempIndx, tempIndx + diff[indx].text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                tempIndx += diff[indx].text.length
            }
            else if (diff[indx].operation.name == "INSERT") {
                var fcsGrn = BackgroundColorSpan(Color.GREEN)
                ss.setSpan(fcsGrn, tempIndx, tempIndx + diff[indx].text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                tempIndx += diff[indx].text.length
            }

        }
        txtView_distanceResult.text = ss
        txtView_resultText.text = "Distance = "+levDistance.toString() + "\n" + tempString.length.toString() + "\n" + tempString2
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
            Toast.makeText(this, "Clicked 2", Toast.LENGTH_SHORT)
            if (speechIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(speechIntent, 10)
            } else {
                Toast.makeText(this, "Your Device not Support", Toast.LENGTH_SHORT)
            }
        }

    }

    private fun _levensteinDistance(stringPrimer: String, stringSekunder: String) {

        txtView_distanceResult.text = "Jumlah kesalahan : " + execute(stringPrimer, stringSekunder).toString()

        var tempString = "_"

        val stringBtemp = txtView_SpeechResult.text.toString()

        var ss = SpannableStringBuilder(stringBtemp)

        val fcsRed = ForegroundColorSpan(Color.RED)
        val fcsBlu = ForegroundColorSpan(Color.BLUE)
        val fcsGrn = ForegroundColorSpan(Color.GREEN)

        var i = stringBtemp.length
        var h = 0

        while (h < indexKesalahan!!.size && indexKesalahan!!.size != 0) {
            tempString += indexKesalahan!![h].noString.toString() + " - " + indexKesalahan!![h].jenis + " - " + indexKesalahan!![h].hurufAwal + " - " + indexKesalahan!![h].hurufAkhir + "\n"
            h++
        }

/*        while (i > 0 && indexKesalahan!!.size != 0){
            tempString += i.toString() +"|"+ indexKesalahan!![h].noString.toString() + " - " + indexKesalahan!![h].jenis + " - " + indexKesalahan!![h].hurufAwal + " - " + indexKesalahan!![h].hurufAkhir + "\n"
            val fcsRed = ForegroundColorSpan(Color.RED)
            val fcsBlu = ForegroundColorSpan(Color.BLUE)
            val fcsGrn = ForegroundColorSpan(Color.GREEN)

                if (i == indexKesalahan!![h].noString){
                    if (indexKesalahan!![h].jenis == "delete") {
                        ss.setSpan(fcsRed, indexKesalahan!![h].noString-1, indexKesalahan!![h].noString, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    else if (indexKesalahan!![h].jenis == "add") {
                        ss.setSpan(fcsGrn, indexKesalahan!![h].noString-1, indexKesalahan!![h].noString, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    else if (indexKesalahan!![h].jenis == "edit") {
                        ss.setSpan(fcsBlu, indexKesalahan!![h].noString-1, indexKesalahan!![h].noString, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    h++
                }
            i--
        }
*/

        txtView_SpeechResult.text = ss

//        tempString = ""

//        for (indx in indexKesalahan!!.indices){
//            tempString += indx.toString() +"|"+ indexKesalahan!![indx].noString.toString() + " - " + indexKesalahan!![indx].jenis + " - " + indexKesalahan!![indx].hurufAwal + " - " + indexKesalahan!![indx].hurufAkhir + "\n"
//        }

        txtView_resultText.text = tempString

        indexKesalahan!!.clear()

    }

    /**
     * Uses recursion to find minimum edits
     */
    fun recEditDistance(str1: CharArray, str2: CharArray, len1: Int, len2: Int): Int {

        if (len1 == str1.size) {
            return str2.size - len2
        }
        return if (len2 == str2.size) {
            str1.size - len1
        } else min(if (recEditDistance(str1, str2, len1 + 1, len2 + 1) + str1[len1].toInt() == str2[len2].toInt()) 0 else 1, recEditDistance(str1, str2, len1, len2 + 1) + 1, recEditDistance(str1, str2, len1 + 1, len2) + 1)
    }

    /**
     * Uses bottom up DP to find the edit distance
     */
    fun dynamicEditDistance(str1: CharArray, str2: CharArray): Int {
        val temp = Array(str1.size + 1) { IntArray(str2.size + 1) }

        for (i in 0 until temp[0].size) {
            temp[0][i] = i
        }

        for (i in temp.indices) {
            temp[i][0] = i
        }

        for (i in 1..str1.size) {
            for (j in 1..str2.size) {
                if (str1[i - 1] == str2[j - 1]) {
                    temp[i][j] = temp[i - 1][j - 1]
                } else {
                    temp[i][j] = 1 + min(temp[i - 1][j - 1], temp[i - 1][j], temp[i][j - 1])
                }
            }
        }
        printActualEdits(temp, str1, str2)
        return temp[str1.size][str2.size]
    }

    var texts: MutableList<String> = ArrayList()

    var indexKesalahan: MutableList<ObjKesalahan>? = ArrayList()

    /**
     * Prints the actual edits which needs to be done.
     */
    fun printActualEdits(T: Array<IntArray>, str1: CharArray, str2: CharArray) {

        var i = T.size - 1
        var j = T[0].size - 1

        var stringTemp = ""

        while (true) {

            if (i == 0 || j == 0) {
                break
            }
            if (str1[i - 1] == str2[j - 1]) {
                //move diagonally to upper left
                i = i - 1
                j = j - 1
            } else if (T[i][j] == T[i - 1][j - 1] + 1) {
                //stringTemp = "Edit " + str2[j - 1] + "(" + j + ") in string2 to " + str1[i - 1]
                //println("Edit " + str2[j - 1] + "(" + j + ") in string2 to " + str1[i - 1])
                indexKesalahan!!.add(ObjKesalahan("edit", i, str2[j - 1].toString(), str1[i - 1].toString()))
//                texts.add("Edit " + str2[j - 1] + "(" + j + ") in string2 to " + str1[i - 1])
                i = i - 1
                j = j - 1
            } else if (T[i][j] == T[i - 1][j] + 1) {
                //stringTemp = "Add in string2 " + str1[i - 1]
                //println("Add in string2 " + str1[i - 1])
                indexKesalahan!!.add(ObjKesalahan("add", i, "", str1[i - 1].toString()))
//                texts.add("Add in string2 " + str1[i - 1])
                i = i - 1
            } else if (T[i][j] == T[i][j - 1] + 1) {
                //stringTemp = "Delete in string2 " + str2[j - 1]
                //println("Delete in string2 " + str2[j - 1])
                indexKesalahan!!.add(ObjKesalahan("delete", j, str2[j - 1].toString(), ""))
//                texts.add("Delete in string2 " + str2[j - 1])
                j = j - 1
            } else {
                throw IllegalArgumentException("Some wrong with given data")
            }
        }

        while (true) {
            if (j > 0) {
                //stringTemp = "Delete in string2 " + str2[j - 1]
                //println("Delete in string2 " + str2[j - 1])
                //texts.add("Delete in string2 " + str2[j - 1])
//                indexKesalahan!!.add(ObjKesalahan("delete", j, str2[j-1].toString(),""))
                j = j - 1
            } else if (i > 0) {
                //stringTemp = "Add in string2 " + str1[i - 1]
                //println("Add in string2 " + str1[i - 1])
                //texts.add("Add in string2 " + str1[i - 1])
//                indexKesalahan!!.add(ObjKesalahan("delete", i, str2[i-1].toString(),""))
                i = i - 1
            } else {
                break
            }
        }

    }

    private fun min(a: Int, b: Int, c: Int): Int {
        val l = Math.min(a, b)
        return Math.min(l, c)
    }

    fun execute(stringPrimer: String, stringSekunder: String): Int {

        return dynamicEditDistance(stringPrimer.toCharArray(), stringSekunder.toCharArray())
    }

}

private operator fun CharSequence.plusAssign(ss: SpannableStringBuilder) {

}

class ObjKesalahan
(val jenis: String,
 val noString: Int,
 val hurufAwal: String,
 val hurufAkhir: String)


