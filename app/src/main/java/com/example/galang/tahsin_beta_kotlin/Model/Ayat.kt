package com.example.galang.tahsin_beta_kotlin.Model

class AyatList (val data : AyatData)

class AyatData (val number : Int,
                val numberOfAyahs : Int,
                val ayahs : List<Ayat>)

class Ayat (val number: Int,
            val text : String,
            val numberInSurah : Int)