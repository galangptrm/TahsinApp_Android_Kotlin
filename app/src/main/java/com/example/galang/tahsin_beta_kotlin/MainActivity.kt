package com.example.galang.tahsin_beta_kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.galang.tahsin_beta_kotlin.view.pengingatmenu.activity.PengingatActivity
import com.example.galang.tahsin_beta_kotlin.view.tahsinmenu.PilihSuratActivity
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun toTahsinMenu(view: View) {
        startActivity<PilihSuratActivity>()
    }

    fun toPengingatMenu(view: View) {
        startActivity<PengingatActivity>()
    }

}
