package com.example.galang.tahsin_beta_kotlin.view.pengingatmenu.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import com.example.galang.tahsin_beta_kotlin.R
import com.example.galang.tahsin_beta_kotlin.adapter.TabPagerAdapter
import com.example.galang.tahsin_beta_kotlin.view.pengingatmenu.fragment.DaftarPengingatFragment
import com.example.galang.tahsin_beta_kotlin.view.pengingatmenu.fragment.InputPengingatFragment
import kotlinx.android.synthetic.main.activity_pengingat.*

class PengingatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengingat)

        setupViewPager(view_pager_pengingat)
        tab_pengingat.setupWithViewPager(view_pager_pengingat)
    }

    private fun setupViewPager(pager: ViewPager) {
        val adapter = fragmentManager?.let { TabPagerAdapter(supportFragmentManager) }

        val input = InputPengingatFragment.inputInstance()
        adapter?.addFragment(input, "INPUT PENGINGAT")

        val daftar = DaftarPengingatFragment.daftarInstance()
        adapter?.addFragment(daftar, "DAFTAR PENGINGAT")

        pager?.adapter = adapter
    }
}
