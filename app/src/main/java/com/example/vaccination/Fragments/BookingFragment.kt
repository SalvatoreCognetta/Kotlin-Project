package com.example.vaccination.Fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.vaccination.HttpRequest.CovidCenterClient
import com.example.vaccination.Model.CovidCenterModel
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
 * Use the [BookingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var locationManager: LocationManager
    private lateinit var covidCenterSpinner: Spinner
    private lateinit var selectedCenter: String
    private var selectedCenterPos: Int = 0

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
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        covidCenterSpinner = view.findViewById(R.id.covidCenterSpinner)

        // Get all the covid center by calling the rest api
        val covideCenterRestApi = CovidCenterClient()
        val covidCenterCall = covideCenterRestApi.getAllCovidCenter()

        var covidCenters : List<CovidCenterModel>

        covidCenterCall?.enqueue(object : Callback<List<CovidCenterModel>> {
            override fun onFailure(call: Call<List<CovidCenterModel>>, t: Throwable) {
                Log.v(TAG, t.message!!)
            }

            override fun onResponse(
                call: Call<List<CovidCenterModel>>,
                response: Response<List<CovidCenterModel>>
            ) {
                val body = response.body()
                if (body != null && body.isNotEmpty()) {
                    covidCenters = body
                    Log.v(TAG, covidCenters.toString())
//                    vacciantionInfoText.text = vaccinationInfo.textInfo

                    // Populate the spinner
                    var covidCentersArr = arrayOfNulls<String>(covidCenters.size+2)
                    covidCentersArr[0] = "Choose center"
                    covidCentersArr[1] = "Select nearest center"
                    for (i in 0 until covidCenters.size) {
                        covidCentersArr[i+2] = covidCenters[i].location
                    }

                    // Create an ArrayAdapter using the string array and a default spinner layout
                    val spinnerArrayAdapter: ArrayAdapter<String> =
                        ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, covidCentersArr)
                    // Specify the layout to use when the list of choices appears
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    // Apply the adapter to the spinner
                    covidCenterSpinner.adapter = spinnerArrayAdapter
                }

            }
        })

        covidCenterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCenter = parent?.getItemAtPosition(position).toString()
                selectedCenterPos = position
                if (position == 1) {
                    Log.i(TAG, "position")
                    getLocation()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // TODO("Not yet implemented")
            }
        }
    }


    private fun requestPermissionGps() {
        if(ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    private fun getLocation() {
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                var latitute = location!!.latitude
                var longitute = location!!.longitude

                Log.i(TAG, "Latitute: $latitute ; Longitute: $longitute")
                locationManager.removeUpdates(this)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String) {
            }

            override fun onProviderDisabled(provider: String) {
            }

        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
            return
        }
        locationManager!!.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0L,
            0f,
            locationListener
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i("onRequestPermissionsResult", "Latitute: ")
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> getLocation()
                //PackageManager.PERMISSION_DENIED -> //Tell to user the need of grant permission
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        private const val TAG = "BookingFragment"
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }
}