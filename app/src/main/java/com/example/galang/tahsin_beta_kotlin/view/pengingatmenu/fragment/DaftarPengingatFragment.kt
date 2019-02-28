package com.example.galang.tahsin_beta_kotlin.view.pengingatmenu.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.galang.tahsin_beta_kotlin.R

class DaftarPengingatFragment : Fragment() {

    companion object {
        fun daftarInstance(): DaftarPengingatFragment = DaftarPengingatFragment()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daftar_pengingat, container, false)
    }


}
