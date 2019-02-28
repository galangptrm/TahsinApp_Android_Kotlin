package com.example.galang.tahsin_beta_kotlin.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.galang.tahsin_beta_kotlin.model.SuratList
import com.example.galang.tahsin_beta_kotlin.view.tahsinmenu.PilihAyatActivity
import com.example.galang.tahsin_beta_kotlin.R
import kotlinx.android.synthetic.main.list_surat_layout.view.*

class PilihSuratAdapter (val suratList: SuratList): RecyclerView.Adapter<PilihSuratViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PilihSuratViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_surat_layout, parent, false)
        return PilihSuratViewHolder(cellForRow)
    }
    override fun getItemCount(): Int {
        return suratList.hasil.count()
    }
    override fun onBindViewHolder(holder: PilihSuratViewHolder, position: Int) {
        val surat = suratList.hasil.get(position)
        holder?.view?.txtView_NomorSurat?.text = surat.nomor.toString()
        holder?.view?.txtView_NamaSurat?.text = surat.nama
        holder?.view?.txtView_TurunSurat?.text = surat.type
        holder?.view?.txtView_JumlahAyat?.text = "- "+surat.ayat+" Ayat"
        holder?.view?.txtView_AsmaSurat?.text = surat.asma

        holder.view.setOnClickListener{

            val toAyatPage_intent = Intent(holder.view.context, PilihAyatActivity::class.java)
            toAyatPage_intent.putExtra("namaSurat_extra", surat.nama) //NAMA SURAT
            toAyatPage_intent.putExtra("nomorSurat_extra", surat.nomor) // NOMOR URUT SURAT
            toAyatPage_intent.putExtra("ayatAwal_extra", surat.start) //NOMOR URUT AYAT PERTAMA DARI KESELURUHAN SURAT ALQURAN
            toAyatPage_intent.putExtra("jumlahAyat_extra", surat.ayat.toInt()) //JUMLAH AYAT



            holder.view.context.startActivity(toAyatPage_intent)
        }
    }
}

class PilihSuratViewHolder(val view: View) : RecyclerView.ViewHolder(view)