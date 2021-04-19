package com.example.vaccination.HttpRequest

import com.example.vaccination.Model.CovidInfoModel
import com.example.vaccination.Utils.BASE_URL_API
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CovidInfoClient {
    private val covidInfoAPI: CovidInfoAPI

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL_API)
            .build()

        covidInfoAPI = retrofit.create(CovidInfoAPI::class.java)
    }

    fun getCovidInfo(): Call<List<CovidInfoModel>> {
        return covidInfoAPI.getCovidInfo()
    }

    fun insertCovidInfo(covidInfo: CovidInfoModel) : Call<String> {
        return covidInfoAPI.insertCovidInfo(covidInfo)
    }

    companion object {
        const val URL = "/covidInfos"
    }
}