package com.example.vaccination.HttpRequest

import com.example.vaccination.Model.CovidCenterModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CovidCenterAPI {

    @GET(CovidCenterClient.URL+"/{region}")
    fun getCovidCenter(@Path("region") region: String) : Call<List<CovidCenterModel>>

    @GET(CovidCenterClient.URL)
    fun getCovidCenter() : Call<List<CovidCenterModel>>

    @POST(CovidCenterClient.URL)
    fun insertCovidCenter(vaccinationCenter: CovidCenterModel) : Call<String>
}