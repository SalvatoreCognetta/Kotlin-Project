package com.example.vaccination.HttpRequest

import com.example.vaccination.Model.VaccinationInfoModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface VaccinationInfoAPI {
    @GET(VaccinationInfoClient.URL)
    fun getVaccinationInfo() : Call<List<VaccinationInfoModel>>

    @POST(VaccinationInfoClient.URL)
    fun insertVaccinationInfo(vaccinationInfo: VaccinationInfoModel) : Call<String>

}