package com.example.galang.tahsin_beta_kotlin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.galang.tahsin_beta_kotlin.Model.Kesalahan
import com.example.galang.tahsin_beta_kotlin.R
import kotlinx.android.synthetic.main.list_kesalahan_layout.view.*

class ListKesalahanAdapter (var kesalahanList: ArrayList<Kesalahan>): RecyclerView.Adapter<ListKesalahanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListKesalahanViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.list_kesalahan_layout, parent, false)
        return ListKesalahanViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return kesalahanList.size
    }

    override fun onBindViewHolder(holder: ListKesalahanViewHolder, position: Int) {

        val kesalahan = kesalahanList.get(position)

        holder?.view?.txtView_jenisKesalahan?.text = kesalahan.jenis
        holder?.view?.txtView_lokasiKesalahan?.text = "Kata"+" '"+ kesalahan.teks +"' tidak terdapat pada ayat"
        if (kesalahan.jenis == "DELETE"){
            holder?.view?.txtView_lokasiKesalahan?.text = "Kata"+" '"+ kesalahan.teks +"' tidak terdapat pada ayat"
        } else if(kesalahan.jenis == "INSERT"){
            holder?.view?.txtView_lokasiKesalahan?.text = "Anda lupa membaca"+" '"+ kesalahan.teks +"' pada ayat"
        }

    }
}

class ListKesalahanViewHolder(val view: View) : RecyclerView.ViewHolder(view)