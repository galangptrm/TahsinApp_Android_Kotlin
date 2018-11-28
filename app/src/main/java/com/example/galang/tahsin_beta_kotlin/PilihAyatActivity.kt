package com.example.galang.tahsin_beta_kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.galang.tahsin_beta_kotlin.Adapter.PilihAyatAdapter
import com.example.galang.tahsin_beta_kotlin.Model.AyatList
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_pilih_ayat.*
import okhttp3.*
import java.io.IOException

class PilihAyatActivity : AppCompatActivity() {

    //URL http://api.alquran.cloud/surah/<surahNumber>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_ayat)

        recView_pilihAyat.layoutManager = LinearLayoutManager(this)

        //Parsing the Extra Intent
        val namaSurat = intent.getStringExtra("namaSurat_extra")
        val nomorSurat = intent.getIntExtra("nomorSurat_extra", 0)

        supportActionBar?.title = nomorSurat.toString()+" "+namaSurat

        fetchJson(nomorSurat.toString(), namaSurat)
    }

    fun fetchJson(nomorSurat : String, namaSurat : String){

        val URL_AYAT = "http://api.alquran.cloud/surah/"+nomorSurat

        val request = Request.Builder().url(URL_AYAT).build()

        val httpClient = OkHttpClient()
        httpClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call?, e: IOException?) {
                //Toast.makeText(this@PilihAyatActivity, "Request URL gagal", Toast.LENGTH_SHORT)
            }

            override fun onResponse(call: Call?, response: Response?) {

                val responseBody = response?.body()?.string()
                println(responseBody)

                val gsonAyat = GsonBuilder().create()

                val ayatList = gsonAyat.fromJson(responseBody, AyatList::class.java)

                runOnUiThread(){
                    recView_pilihAyat.adapter = PilihAyatAdapter(ayatList, namaSurat)
                }
            }

        })
    }
}
