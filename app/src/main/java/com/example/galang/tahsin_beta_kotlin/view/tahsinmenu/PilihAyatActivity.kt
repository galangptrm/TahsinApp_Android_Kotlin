package com.example.galang.tahsin_beta_kotlin.view.tahsinmenu

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.galang.tahsin_beta_kotlin.adapter.PilihAyatAdapter
import com.example.galang.tahsin_beta_kotlin.model.AyatDiakritikList
import com.example.galang.tahsin_beta_kotlin.R
import com.example.galang.tahsin_beta_kotlin.view.tahsinmenu.TestingActivity.Companion.NAMASURAT_EXTRA
import com.example.galang.tahsin_beta_kotlin.view.tahsinmenu.TestingActivity.Companion.NOMORAYATAKHIR_EXTRA
import com.example.galang.tahsin_beta_kotlin.view.tahsinmenu.TestingActivity.Companion.NOMORAYATAWAL_EXTRA
import com.example.galang.tahsin_beta_kotlin.view.tahsinmenu.TestingActivity.Companion.TEXTAYAT_EXTRA
import com.example.galang.tahsin_beta_kotlin.view.tahsinmenu.TestingActivity.Companion.TYPE_INTENT
import com.example.galang.tahsin_beta_kotlin.view.tahsinmenu.TestingActivity.Companion.URUTANAYAT_EXTRA
import kotlinx.android.synthetic.main.activity_pilih_ayat.*

class PilihAyatActivity : AppCompatActivity() {

    companion object {
        val fromActivity = "fromActivity"
    }

    val TAG = "tag_surat";
    //URL http://api.alquran.cloud/surah/<surahNumber>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_ayat)
        recView_pilihAyat.layoutManager = LinearLayoutManager(this)

        //Parsing the Extra Intent
        val namaSurat = intent.getStringExtra("namaSurat_extra")                  // NAMA SURAT
        val nomorSurat = intent.getIntExtra("nomorSurat_extra", 1)     //NOMOR SURAT
        val urutanAyatAwal = intent.getIntExtra("ayatAwal_extra", 1)       //URUTAN AYAT PERTAMA DARI KESELURUHAN ALQURAN
        val jumlahAyat = intent.getIntExtra("jumlahAyat_extra", 1)     //JUMLAH AYAT

        val ayatAwal = edtAwalAyat.text
        val ayatAkhir = edtAkhirAyat.text
        btnTest.setOnClickListener {
            getRangeAyatList(namaSurat, urutanAyatAwal, Integer.parseInt(ayatAwal.toString()),
                    Integer.parseInt(ayatAkhir.toString()))
        }

        Log.d(TAG, Integer.toString(urutanAyatAwal));
        Log.d(TAG, Integer.toString(jumlahAyat));

        supportActionBar?.title = nomorSurat.toString() + " " + namaSurat
        fetchJson(namaSurat, urutanAyatAwal, jumlahAyat)
    }

    fun fetchJson(namaSurat: String, urutanAyatAwal: Int, jumlahAyat: Int) {
        var listAyat = AyatDiakritikList().getSomeAyats(urutanAyatAwal, urutanAyatAwal + jumlahAyat)
        recView_pilihAyat.adapter = PilihAyatAdapter(listAyat, namaSurat, urutanAyatAwal)
    }

    fun getRangeAyatList(namaSurat: String, urutanAyatAwal: Int, ayatAwal: Int, ayatAkhir: Int) {
        var listAyat = AyatDiakritikList().getSomeAyats(urutanAyatAwal + (ayatAwal - 1), urutanAyatAwal + ayatAkhir)

        val intent = Intent(this, TestingActivity::class.java)
        val rangeAyat = listAyat.joinToString(separator = "\n")
        Log.d(TAG, rangeAyat)

        intent.putExtra(NAMASURAT_EXTRA, namaSurat)
        intent.putExtra(NOMORAYATAWAL_EXTRA, ayatAwal)
        intent.putExtra(NOMORAYATAKHIR_EXTRA, ayatAkhir)
        intent.putExtra(TEXTAYAT_EXTRA, rangeAyat)
        intent.putExtra(URUTANAYAT_EXTRA, urutanAyatAwal + ayatAwal)
        intent.putExtra(TYPE_INTENT, fromActivity)
        startActivity(intent)
//        recView_pilihAyat.adapter = PilihAyatAdapter(listAyat, namaSurat, urutanAyatAwal + ayatAwal)
    }

}