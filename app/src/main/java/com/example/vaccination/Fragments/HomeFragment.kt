package com.example.vaccination.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import com.example.vaccination.HttpRequest.CovidInfoClient
import com.example.vaccination.HttpRequest.VaccinationInfoClient
import com.example.vaccination.Model.CovidInfoModel
import com.example.vaccination.Model.VaccinationInfoModel
import com.example.vaccination.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var navigateBtn: Button
    private lateinit var vacciantionInfoText: TextView
    private lateinit var covidInfoAbruzzoText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vacciantionInfoText = view.findViewById(R.id.vaccinationInfoText)

        val vaccinationRestApi = VaccinationInfoClient()
        val vaccinationCall = vaccinationRestApi.getVaccinationInfo()

        vaccinationCall?.enqueue(object : Callback<List<VaccinationInfoModel>> {
            override fun onFailure(call: Call<List<VaccinationInfoModel>>, t: Throwable) {
                Log.v(TAG, t.message!!)
            }

            override fun onResponse(call: Call<List<VaccinationInfoModel>>, response: Response<List<VaccinationInfoModel>>) {
                val body = response.body()
                var vaccinationInfo: VaccinationInfoModel? = null
                if (body != null && body.isNotEmpty()) {
                    vaccinationInfo = body[0]
                    Log.v(TAG, vaccinationInfo.toString())
                    vacciantionInfoText.text = vaccinationInfo.textInfo
                }

            }
        })


        covidInfoAbruzzoText = view.findViewById(R.id.covidInfoAbruzzoText)

        val covidRestApi = CovidInfoClient()
        val call = covidRestApi.getCovidInfo("Abruzzo")

        call?.enqueue(object : Callback<List<CovidInfoModel>> {
            override fun onFailure(call: Call<List<CovidInfoModel>>, t: Throwable) {
                Log.v(TAG, t.message!!)
                covidInfoAbruzzoText.text = "No data available"
            }

            override fun onResponse(call: Call<List<CovidInfoModel>>, response: Response<List<CovidInfoModel>>) {
                val body = response.body()
                var covidInfo: CovidInfoModel? = null
                if (body != null && body.isNotEmpty()) {
                    covidInfo = body[0]
                    Log.v(TAG, covidInfo.toString())
                    covidInfoAbruzzoText.text = covidInfo.toString()
                } else {
                    Log.v(TAG, "null body")
                    covidInfoAbruzzoText.text = "No data available"
                }

            }
        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        private val TAG = "HomeFragment"
    }
}