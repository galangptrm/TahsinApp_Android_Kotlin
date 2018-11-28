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

class PilihAyatAdapter (val ayatList: AyatList, val namaSurat : String): RecyclerView.Adapter<PilihAyatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PilihAyatViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_ayat_layout, parent, false)
        return PilihAyatViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return ayatList.data.ayahs.count()
    }

    override fun onBindViewHolder(holder: PilihAyatViewHolder, position: Int) {

        val ayat = ayatList.data.ayahs.get(position)

        holder?.view?.txtView_nomorAyat?.text = ayat.numberInSurah.toString()
        holder?.view?.txtView_teksAyat?.text = ayat.text

        holder?.view.floatActBtn_toBacaAyat.setOnClickListener {
            val toBacaAyat_intent = Intent(holder.view.context, TestingActivity::class.java)
            toBacaAyat_intent.putExtra("nomorAyat_extra", ayat.numberInSurah)
            toBacaAyat_intent.putExtra("namaSurat_extra", namaSurat)
            toBacaAyat_intent.putExtra("textAyat_extra", ayat.text)

            holder.view.context.startActivity(toBacaAyat_intent)
        }

    }
}

class PilihAyatViewHolder(val view: View) : RecyclerView.ViewHolder(view)