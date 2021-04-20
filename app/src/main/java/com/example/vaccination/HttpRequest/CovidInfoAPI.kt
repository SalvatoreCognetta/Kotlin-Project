package com.example.vaccination.HttpRequest

import com.example.vaccination.Model.CovidInfoModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CovidInfoAPI {

    @GET(CovidInfoClient.URL+"/{region}")
    fun getCovidInfo(@Path("region") region: String) : Call<List<CovidInfoModel>>

    @GET(CovidInfoClient.URL)
    fun getCovidInfo() : Call<List<CovidInfoModel>>

    @POST(CovidInfoClient.URL)
    fun insertCovidInfo(vaccinationInfo: CovidInfoModel) : Call<String>
}