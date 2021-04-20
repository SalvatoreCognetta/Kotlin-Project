package com.example.vaccination.HttpRequest

import com.example.vaccination.Model.CovidCenterModel
import com.example.vaccination.Utils.BASE_URL_API
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CovidCenterClient {
    private val covidCenterAPI: CovidCenterAPI

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL_API)
            .build()

        covidCenterAPI = retrofit.create(CovidCenterAPI::class.java)
    }

    fun getCovidCenterByRegion(region: String): Call<List<CovidCenterModel>> {
        return covidCenterAPI.getCovidCenter(region)
    }

    fun getAllCovidCenter(): Call<List<CovidCenterModel>> {
        return covidCenterAPI.getCovidCenter()
    }

    fun insertCovidCenter(covidCenter: CovidCenterModel) : Call<String> {
        return covidCenterAPI.insertCovidCenter(covidCenter)
    }

    companion object {
        const val URL = "/covidCenters"
    }
}