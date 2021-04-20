package com.example.vaccination.HttpRequest

import com.example.vaccination.Model.VaccinePrenotationModel
import com.example.vaccination.Utils.BASE_URL_API
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VaccinePrenotationClient {
    private var vaccinePrenotationAPI: VaccinePrenotationAPI

    private val client = OkHttpClient.Builder().build()

    init {
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL_API)
                .client(client)
                .build()

        vaccinePrenotationAPI = retrofit.create(VaccinePrenotationAPI::class.java)
    }

    fun getVaccinePrenotationByUid(uid: String): Call<List<VaccinePrenotationModel>> {
        return vaccinePrenotationAPI.getVaccinePrenotation(uid)
    }

    fun insertVaccinePrenotation(vaccinePrenotation: VaccinePrenotationModel) : Call<String> {
        return vaccinePrenotationAPI.insertVaccinePrenotation(vaccinePrenotation)
    }

    companion object {
        const val URL = "/vaccinePrenotations"
    }
}