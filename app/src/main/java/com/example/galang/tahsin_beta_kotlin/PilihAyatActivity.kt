package com.example.galang.tahsin_beta_kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.galang.tahsin_beta_kotlin.Adapter.PilihAyatAdapter
import com.example.galang.tahsin_beta_kotlin.Model.AyatDiakritikList
import kotlinx.android.synthetic.main.activity_pilih_ayat.*

class PilihAyatActivity : AppCompatActivity() {
    //URL http://api.alquran.cloud/surah/<surahNumber>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_ayat)
        recView_pilihAyat.layoutManager = LinearLayoutManager(this)

        //Parsing the Extra Intent
        val namaSurat       = intent.getStringExtra("namaSurat_extra")                  // NAMA SURAT
        val nomorSurat      = intent.getIntExtra("nomorSurat_extra", 1)     //NOMOR SURAT
        val urutanAyatAwal  = intent.getIntExtra("ayatAwal_extra", 1)       //URUTAN AYAT PERTAMA DARI KESELURUHAN ALQURAN
        val jumlahAyat      = intent.getIntExtra("jumlahAyat_extra", 1)     //JUMLAH AYAT

        supportActionBar?.title = nomorSurat.toString()+" "+namaSurat
        fetchJson(namaSurat, urutanAyatAwal, jumlahAyat)
    }

    fun fetchJson(namaSurat : String, urutanAyatAwal : Int, jumlahAyat : Int){
            var listAyat = AyatDiakritikList().getSomeAyats(urutanAyatAwal, urutanAyatAwal+jumlahAyat)
            recView_pilihAyat.adapter = PilihAyatAdapter(listAyat, namaSurat, urutanAyatAwal)
    }

}