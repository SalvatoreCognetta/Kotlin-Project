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
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.vaccination.HttpRequest.CovidCenterClient
import com.example.vaccination.HttpRequest.VaccinePrenotationClient
import com.example.vaccination.Model.CovidCenterModel
import com.example.vaccination.Model.VaccinePrenotationModel
import com.example.vaccination.R
import com.example.vaccination.Utils.*
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

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
    private lateinit var locationCenterBtn: Button
    private lateinit var bookBtn: Button
    private lateinit var covidCenterSpinner: Spinner
    private lateinit var selectedCenter: String
    private var selectedCenterPos: Int = 0
    private lateinit var latitude: Number
    private lateinit var longitude: Number
    private var covidCenterLocations: MutableList<Location> = mutableListOf()
    private lateinit var auth: FirebaseAuth
    private var covidCentersList : MutableList<CovidCenterModel> = mutableListOf()
    private lateinit var bookingProgressLayout: RelativeLayout

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
        // Initialize Firebase Auth
        auth = Firebase.auth
        getVaccinePrenotation()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        locationCenterBtn = view.findViewById(R.id.locationCenterBtn)
        covidCenterSpinner = view.findViewById(R.id.covidCenterSpinner)
        bookBtn = view.findViewById(R.id.bookBtn)
        bookingProgressLayout = view.findViewById(R.id.bookingProgressLayout)
        bookingProgressLayout.isVisible = false

        //if no prenotation
        // Get all the covid centers by rest call
        getCovidCenters()

        // Set a listener on the spinner
        covidCenterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCenter = parent?.getItemAtPosition(position).toString()
                selectedCenterPos = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // TODO("Not yet implemented")
            }
        }

        // When clicked on location btn set the nearest covid center
        locationCenterBtn.setOnClickListener() {
            locationCenterBtn.isEnabled = false
            getLocation()
        }

        bookBtn.setOnClickListener() {
            if (selectedCenterPos == 0) {
                Toast.makeText(activity, "Select a vaccine center", Toast.LENGTH_SHORT).show()
            } else {
                bookBtn.isEnabled = false
                sendVaccinePrenotation()
            }

        }
    }

    private fun getCovidCenters() {
        // Get all the covid center by calling the rest api
        val covidCenterRestApi = CovidCenterClient()
        val covidCenterCall = covidCenterRestApi.getAllCovidCenter()

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
                Log.v(TAG, body.toString())
                if (body != null && body.isNotEmpty()) {
                    covidCentersList = body.toMutableList()
                    Log.v(TAG, covidCentersList.toString())

                    for (center in covidCentersList) {
                        covidCenterLocations.add(center.getLocation())
                    }

                    // Populate the spinner
                    var covidCentersArr = arrayOfNulls<String>(covidCentersList.size + 1)
                    covidCentersArr[0] = "Choose center"
                    for (i in 0 until covidCentersList.size) {
                        covidCentersArr[i + 1] = covidCentersList[i].location
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
    }

    private fun sendVaccinePrenotation() {
        var vaccinePrenotation = VaccinePrenotationModel(auth.currentUser.uid, selectedCenter, Date(), null, null)
        // Get all the covid center by calling the rest api
        val vaccinePrenotationRestApi = VaccinePrenotationClient()
        Log.i(TAG, vaccinePrenotation.toString())
        val vaccinePrenotationCall = vaccinePrenotationRestApi.insertVaccinePrenotation(vaccinePrenotation)

        vaccinePrenotationCall?.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.v(TAG, t.message!!)
                Toast.makeText(activity, "Failed to send the request", Toast.LENGTH_SHORT).show()
                bookBtn.isEnabled = true
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                val body = response.body()
                if (body != null) {
                    val vaccineId = body
                    Log.v(TAG, vaccineId.toString())
                    Toast.makeText(activity, "Booking request sent", Toast.LENGTH_SHORT).show()
                    val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
                    ft.detach(this@BookingFragment).attach(this@BookingFragment).commit()
                }
                bookBtn.isEnabled = true
            }
        })
    }

    private fun getVaccinePrenotation() {
        // Get all the covid center by calling the rest api
        val vaccinePrenotationRestApi = VaccinePrenotationClient()
        val vaccinePrenotationCall = vaccinePrenotationRestApi.getVaccinePrenotationByUid(auth.currentUser.uid)

        vaccinePrenotationCall?.enqueue(object : Callback<List<VaccinePrenotationModel>> {
            override fun onFailure(call: Call<List<VaccinePrenotationModel>>, t: Throwable) {
                Log.v(TAG, t.message!!)
                Toast.makeText(activity, "Failed to send the request", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<VaccinePrenotationModel>>, response: Response<List<VaccinePrenotationModel>>) {
                val body = response.body()
                if (body != null && body.isNotEmpty()) {
                    // There are prenotations
                    val booking = body[0]
                    Log.v(TAG, booking.toString())
                    disableBooking()
                    showProgressLayout(booking)
                }
            }
        })
    }

    private fun disableBooking() {
        locationCenterBtn.isEnabled = false
        covidCenterSpinner.isEnabled = false
        bookBtn.isEnabled = false
    }

    private fun showProgressLayout(booking: VaccinePrenotationModel) {
        val formatter = SimpleDateFormat("HH:mm:ss dd/MM/yyyy")
        bookingProgressLayout.isVisible = true

        if (booking.dateFirstDose != null) {
            requireView().findViewById<TextView>(R.id.secondStepIcon).background = ResourcesCompat.getDrawable(resources, R.drawable.btn_bg, null)
            val formattedDate = formatter.format(booking.dateFirstDose)
            requireView().findViewById<TextView>(R.id.secondStepText).text = "Your first vaccination dose will be administered on $formattedDate at ${booking.covidCenter}"
            requireView().findViewById<MaterialCardView>(R.id.secondStepCard).isVisible = true
        }

        if (booking.dateSecondDose != null) {
            requireView().findViewById<TextView>(R.id.thirdStepIcon).background = ResourcesCompat.getDrawable(resources, R.drawable.btn_bg, null)
            val formattedDate = formatter.format(booking.dateSecondDose)
            requireView().findViewById<TextView>(R.id.thirdStepText).text = "Your second vaccination dose will be administered on $formattedDate at ${booking.covidCenter}"
            requireView().findViewById<MaterialCardView>(R.id.thirdStepCard).isVisible = true
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

                // Compute nearest
                var nearestLoc = getClosestLocation(location, covidCenterLocations)

                Log.i(TAG, "Closest Latitude: ${nearestLoc.first.longitude} ; Longitude: ${nearestLoc.first.latitude}")

                covidCenterSpinner.setSelection(nearestLoc.second + 1)

                locationCenterBtn.isEnabled = true

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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