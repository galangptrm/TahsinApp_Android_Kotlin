package com.example.galang.tahsin_beta_kotlin.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.galang.tahsin_beta_kotlin.R
import com.example.galang.tahsin_beta_kotlin.view.tahsinmenu.TestingActivity
import com.example.galang.tahsin_beta_kotlin.view.tahsinmenu.TestingActivity.Companion.NAMASURAT_EXTRA
import com.example.galang.tahsin_beta_kotlin.view.tahsinmenu.TestingActivity.Companion.NOMORAYATAWAL_EXTRA
import com.example.galang.tahsin_beta_kotlin.view.tahsinmenu.TestingActivity.Companion.TEXTAYAT_EXTRA
import com.example.galang.tahsin_beta_kotlin.view.tahsinmenu.TestingActivity.Companion.TYPE_INTENT
import com.example.galang.tahsin_beta_kotlin.view.tahsinmenu.TestingActivity.Companion.URUTANAYAT_EXTRA
import kotlinx.android.synthetic.main.list_ayat_layout.view.*

class PilihAyatAdapter (val ayatList: List<String>, val namaSurat : String, val urutanAyatAwal : Int):
        RecyclerView.Adapter<PilihAyatViewHolder>() {
    companion object {
        val fromAdapter = "fromAdapter"
    }
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

            toBacaAyat_intent.putExtra(NOMORAYATAWAL_EXTRA, position+1)                        //URUTAN AYAT DARI 1 SURAT
            toBacaAyat_intent.putExtra(NAMASURAT_EXTRA, namaSurat)                               //NAMA SURAT
            toBacaAyat_intent.putExtra(URUTANAYAT_EXTRA, urutanAyatAwal+position)          //URUTAN AYAT DARI KESELURUHAN ALQURAN

            //TEXT AYATNYA
            if (namaSurat != "Al Fatihah" && position==0){
                toBacaAyat_intent.putExtra(TEXTAYAT_EXTRA, ayatList.get(position).substring(38, ayatList.get(position).length))
            }else {
                toBacaAyat_intent.putExtra(TEXTAYAT_EXTRA, ayatList.get(position))
            }
            toBacaAyat_intent.putExtra(TYPE_INTENT, fromAdapter)

            holder.view.context.startActivity(toBacaAyat_intent)
        }
    }
}

class PilihAyatViewHolder(val view: View) : RecyclerView.ViewHolder(view)