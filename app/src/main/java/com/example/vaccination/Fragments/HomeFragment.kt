package com.example.vaccination.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.vaccination.HttpRequest.CovidInfoClient
import com.example.vaccination.HttpRequest.VaccinationInfoClient
import com.example.vaccination.Model.CovidInfoModel
import com.example.vaccination.Model.VaccinationInfoModel
import com.example.vaccination.R
import com.example.vaccination.Utils.OnBackPressedInterface
import com.google.android.material.card.MaterialCardView
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
class HomeFragment : Fragment(), OnBackPressedInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var navigateBtn: Button
    private lateinit var mainRelativeContainer: RelativeLayout

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

        // Get the main container of the fragment
        mainRelativeContainer = view.findViewById(R.id.relativeContainer)

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


        // Create an ArrayAdapter using the string array for the regions
        val regionArray: Array<String> = resources.getStringArray(R.array.regions_array)
        var belowId = R.id.vaccinationInfoCard

        regionArray.forEach {
            Log.i(TAG, it)
            if (it != "Region") {
                val region = it

                val titleRegionId = CreateTextView(belowId, region)
                val arrayId = CreateMaterialCardView(titleRegionId, " ")
                belowId = arrayId[0]

                val covidRestApi = CovidInfoClient()
                val call = covidRestApi.getCovidInfo(region)

                call?.enqueue(object : Callback<List<CovidInfoModel>> {
                    override fun onFailure(call: Call<List<CovidInfoModel>>, t: Throwable) {
                        Log.v(TAG, t.message!!)
                        val textCovid = view.findViewById<TextView>(arrayId[1])
                        textCovid.text = "No data available"
                    }

                    override fun onResponse(call: Call<List<CovidInfoModel>>, response: Response<List<CovidInfoModel>>) {
                        val body = response.body()
                        var covidInfo: CovidInfoModel? = null
                        if (body != null && body.isNotEmpty()) {
                            covidInfo = body[0]
                            Log.v(TAG, body.toString())
                            val textCovid = view.findViewById<TextView>(arrayId[1])
                            textCovid.text = covidInfo.toString()
                        } else {
                            Log.v(TAG, "null body")
                            val textCovid = view.findViewById<TextView>(arrayId[1])
                            textCovid.text = "No data available"
                        }

                    }
                })
            }

        }
    }

    fun CreateTextView(belowViewId: Int, title: String) : Int {
        // TextView
        var titleRegion = TextView(context)
        val titleRegionId = View.generateViewId()
        titleRegion.id = titleRegionId
        var titleRegionLayoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        )
        titleRegionLayoutParams.topMargin = 20
        titleRegionLayoutParams.addRule(RelativeLayout.BELOW, belowViewId)

        titleRegion.layoutParams = titleRegionLayoutParams

        titleRegion.text = title
        titleRegion.setTextColor(requireContext().getColor(R.color.black))
        titleRegion.textSize = 18f

        var typeface = ResourcesCompat.getFont(requireContext(), R.font.abeezee)
        titleRegion.typeface = typeface

        mainRelativeContainer.addView(titleRegion)

        return titleRegionId
    }

    fun CreateMaterialCardView(belowViewId: Int, textInfo: String) : IntArray {
        // Main Card
        var card = MaterialCardView(context)
        val cardId = View.generateViewId()
        card.id = cardId
        var layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.bottomMargin = 20
        layoutParams.addRule(RelativeLayout.BELOW, belowViewId)

        card.layoutParams = layoutParams
        card.radius = resources.getDimension(R.dimen.card_corner_radius)
        card.elevation = resources.getDimension(R.dimen.card_elevation)

        // Relative layout
        var relativeLayout = RelativeLayout(context)
        val relativeLayoutId = View.generateViewId()
        relativeLayout.id = relativeLayoutId
        var relativeLayoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        var margin = 10
        relativeLayoutParams.setMargins(margin, margin, margin, margin)
        relativeLayout.layoutParams = relativeLayoutParams

        // TextView
        var textCovid = TextView(context)
        val textCovidId = View.generateViewId()
        textCovid.id = textCovidId
        var textCovidLayoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        )
        textCovid.layoutParams = textCovidLayoutParams
        textCovid.setPadding(10, 10, 10, 10)

        textCovid.text = textInfo
        textCovid.setTextColor(requireContext().getColor(R.color.black))

        var typeface = ResourcesCompat.getFont(requireContext(), R.font.abeezee)
        textCovid.typeface = typeface


        relativeLayout.addView(textCovid)
        card.addView(relativeLayout)

        mainRelativeContainer.addView(card)

        return intArrayOf(cardId, textCovidId)
    }

    override fun onBackPressed(): Boolean {
        return false
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