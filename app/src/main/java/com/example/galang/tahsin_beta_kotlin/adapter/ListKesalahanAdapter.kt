package com.example.galang.tahsin_beta_kotlin.adapter

import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.galang.tahsin_beta_kotlin.model.Kesalahan
import com.example.galang.tahsin_beta_kotlin.R
import kotlinx.android.synthetic.main.list_kesalahan_layout.view.*

class ListKesalahanAdapter (val kesalahanList: ArrayList<Kesalahan>): RecyclerView.Adapter<ListKesalahanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListKesalahanViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_kesalahan_layout, parent, false)
        return ListKesalahanViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return kesalahanList.count()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBindViewHolder(holder: ListKesalahanViewHolder, position: Int) {

        val kesalahan = kesalahanList.get(position)

        if (kesalahan.jenis.equals("DELETE")){
            holder?.view?.txtView_jenisKesalahan?.text = " Kelebihan huruf "
            holder?.view?.txtView_lokasiKesalahan?.text = "Kata"+" '"+ kesalahan.teks +"' tidak terdapat pada ayat"
            holder?.view?.txtView_jenisKesalahan.setBackgroundColor(Color.RED)
            holder?.view?.txtView_jenisKesalahan.setTextColor(Color.WHITE)

        } else if(kesalahan.jenis.equals("INSERT")){
            holder?.view?.txtView_jenisKesalahan?.text = " Kekurangan huruf "
            holder?.view?.txtView_lokasiKesalahan?.text = "Anda lupa membaca"+" '"+ kesalahan.teks +"' pada ayat"
            holder?.view?.txtView_jenisKesalahan.setBackgroundColor(Color.YELLOW )
            holder?.view?.txtView_jenisKesalahan.setTextColor(Color.BLACK)

        } else {
            holder?.view?.setVisibility(View.GONE);
            holder?.view?.setLayoutParams(RecyclerView.LayoutParams(0, 0))
        }

    }
}

class ListKesalahanViewHolder(val view: View) : RecyclerView.ViewHolder(view)