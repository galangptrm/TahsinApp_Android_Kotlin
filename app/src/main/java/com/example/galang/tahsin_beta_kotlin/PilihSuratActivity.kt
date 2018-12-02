package com.example.galang.tahsin_beta_kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.galang.tahsin_beta_kotlin.Adapter.PilihSuratAdapter
import com.example.galang.tahsin_beta_kotlin.Model.SuratList
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_pilih_surat.*
import okhttp3.*
import java.io.IOException

class PilihSuratActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_surat)
        recView_pilihSurat.layoutManager = LinearLayoutManager(this)
        fetchJson()
    }

    fun fetchJson(){
        val URLSURAT = "https://api.banghasan.com/quran/format/json/surat"
        val request = Request.Builder().url(URLSURAT).build()
        val httpClient = OkHttpClient()
        httpClient.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                print(e?.message)
            }
            override fun onResponse(call: Call?, response: Response?) {
                val responseBody = response?.body()?.string()
                println(responseBody)
                val gsonSurat = GsonBuilder().create()
                val suratList = gsonSurat.fromJson(responseBody, SuratList::class.java)
                runOnUiThread(){
                    recView_pilihSurat.adapter = PilihSuratAdapter(suratList)
                }
            }
        })
    }

}
