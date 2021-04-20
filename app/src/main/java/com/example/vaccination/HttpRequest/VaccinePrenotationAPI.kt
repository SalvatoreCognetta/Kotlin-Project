package com.example.vaccination.HttpRequest

import com.example.vaccination.Model.VaccinePrenotationModel
import retrofit2.Call
import retrofit2.http.*

interface VaccinePrenotationAPI {

    @GET(VaccinePrenotationClient.URL+"/{uid}")
    fun getVaccinePrenotation(@Path("uid") uid: String) : Call<List<VaccinePrenotationModel>>

    @Headers("Content-Type: application/json")
    @POST(VaccinePrenotationClient.URL)
    fun insertVaccinePrenotation(@Body vaccinationPrenotation: VaccinePrenotationModel) : Call<String>

}