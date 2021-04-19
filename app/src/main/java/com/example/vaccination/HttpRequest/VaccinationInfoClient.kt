package com.example.vaccination.HttpRequest

import com.example.vaccination.Model.VaccinationInfoModel
import com.example.vaccination.Utils.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VaccinationInfoClient {
    private val vaccinationInfoAPI: VaccinationInfoAPI

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL_API)
            .build()

        vaccinationInfoAPI = retrofit.create(VaccinationInfoAPI::class.java)
    }

    fun getVaccinationInfo(): Call<List<VaccinationInfoModel>> {
        return vaccinationInfoAPI.getVaccinationInfo()
    }

    fun insertVaccinationInfo(vaccinationInfo: VaccinationInfoModel) : Call<String> {
        return vaccinationInfoAPI.insertVaccinationInfo(vaccinationInfo)
    }

    companion object {
        const val URL = "/vaccinationInfos"
    }
}