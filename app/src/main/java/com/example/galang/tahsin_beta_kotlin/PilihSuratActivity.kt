package com.example.galang.tahsin_beta_kotlin

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.galang.tahsin_beta_kotlin.Adapter.PilihSuratAdapter
import com.example.galang.tahsin_beta_kotlin.Model.SuratList
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_pilih_surat.*
import okhttp3.*
import java.io.IOException

@Suppress("DEPRECATION")
class PilihSuratActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_surat)
        recView_pilihSurat.layoutManager = LinearLayoutManager(this)
        imgBtn_refresh.visibility = View.INVISIBLE
        getQuestions().execute()
    }

    internal inner class getQuestions : AsyncTask<Void, Void, String>() {

        lateinit var progressDialog: ProgressDialog
        var hasInternet = false

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(this@PilihSuratActivity)
            progressDialog.setMessage("Menampilkan Daftar Surat...")
            progressDialog.setCancelable(false)
            progressDialog.show()
        }

        override fun doInBackground(vararg p0: Void?): String? {

            if (isNetworkAvailable()) {
                hasInternet = true
                val URLSURAT = "https://api.banghasan.com/quran/format/json/surat"
                val request = Request.Builder().url(URLSURAT).build()
                val httpClient = OkHttpClient()
                httpClient.newCall(request).enqueue(object : Callback{
                    override fun onFailure(call: Call?, e: IOException?) {
                        print(e?.message)
                        progressDialog.dismiss()
                        imgBtn_refresh.visibility = View.VISIBLE
                        imgBtn_refresh.setOnClickListener {
                            recreate()
                        }
                    }
                    override fun onResponse(call: Call?, response: Response?) {
                        val responseBody = response?.body()?.string()
                        println(responseBody)
                        val gsonSurat = GsonBuilder().create()
                        val suratList = gsonSurat.fromJson(responseBody, SuratList::class.java)

                        runOnUiThread(){
                            recView_pilihSurat.adapter = PilihSuratAdapter(suratList)
                            progressDialog.dismiss()

                            Toast.makeText(this@PilihSuratActivity, "Daftar surat ditampilkan", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                return ""
            } else {
                progressDialog.dismiss()
                imgBtn_refresh.visibility = View.VISIBLE
                imgBtn_refresh.setOnClickListener {
                    recreate()
                }
                return ""
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
//
        }

    }



    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}
