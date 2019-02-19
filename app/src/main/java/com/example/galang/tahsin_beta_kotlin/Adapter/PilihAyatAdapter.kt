package com.example.galang.tahsin_beta_kotlin.Adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.galang.tahsin_beta_kotlin.Model.AyatList
import com.example.galang.tahsin_beta_kotlin.R
import com.example.galang.tahsin_beta_kotlin.TestingActivity
import kotlinx.android.synthetic.main.list_ayat_layout.view.*

class PilihAyatAdapter (val ayatList: List<String>, val namaSurat : String, val urutanAyatAwal : Int):
        RecyclerView.Adapter<PilihAyatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PilihAyatViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_ayat_layout, parent, false)
        return PilihAyatViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return ayatList.count()
    }

    override fun onBindViewHolder(holder: PilihAyatViewHolder, position: Int) {

        holder?.view?.txtView_nomorAyat?.text = (position+1).toString()

        if (namaSurat != "Al Fatihah" && position==0){
            holder?.view?.txtView_teksAyat?.text = ayatList.get(position).substring(38, ayatList.get(position).length)
        }else {
            holder?.view?.txtView_teksAyat?.text = ayatList.get(position)
        }

        holder?.view.setOnClickListener {
            val toBacaAyat_intent = Intent(holder.view.context, TestingActivity::class.java)

            toBacaAyat_intent.putExtra("nomorAyat_extra", position+1)                        //URUTAN AYAT DARI 1 SURAT
            toBacaAyat_intent.putExtra("namaSurat_extra", namaSurat)                               //NAMA SURAT
            toBacaAyat_intent.putExtra("urutanAyat_extra", urutanAyatAwal+position)          //URUTAN AYAT DARI KESELURUHAN ALQURAN

            //TEXT AYATNYA
            if (namaSurat != "Al Fatihah" && position==0){
                toBacaAyat_intent.putExtra("textAyat_extra", ayatList.get(position).substring(38, ayatList.get(position).length))
            }else {
                toBacaAyat_intent.putExtra("textAyat_extra", ayatList.get(position))
            }

            holder.view.context.startActivity(toBacaAyat_intent)
        }
    }
}

class PilihAyatViewHolder(val view: View) : RecyclerView.ViewHolder(view)