package com.example.vaccination.Fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.vaccination.Activities.LoginActivity
import com.example.vaccination.Activities.MainActivity
import com.example.vaccination.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_account_page.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var nameText: TextView

    private lateinit var inputNameText: TextView
    private lateinit var inputSurnameText: TextView
    private lateinit var inputEmailText: TextView
    private lateinit var inputDateBirthText: TextView

    private lateinit var inputOldPwdText: TextView

    private lateinit var saveAccountBtn: Button
    private lateinit var logoutBtn: Button

    private lateinit var firebaseDb: DatabaseReference

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

        // Initialize Firebase Database
        firebaseDb = Firebase.database.reference

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameText = view.findViewById(R.id.nameText)
        inputNameText = view.findViewById(R.id.inputNameAccount)
        inputSurnameText = view.findViewById(R.id.inputSurnameAccount)
        inputEmailText = view.findViewById(R.id.inputEmailAccount)
        inputDateBirthText = view.findViewById(R.id.inputDateBirthAccount)

        saveAccountBtn = view.findViewById(R.id.btnSaveAccount)
        logoutBtn = view.findViewById(R.id.btnLogout)

        var account = (activity as MainActivity).accountLocal
        if (account != null) {
            if (account.name != null && account.name!!.isNotEmpty()) {
                nameText.text = account.name + " " + account.surname

                inputNameText.text = account.name
                inputSurnameText.text = account.surname

                Log.i(TAG, "Name: ${account.name}")
            } else {
                nameText.text = account.email
                Log.i(TAG, "Email: ${account.email}")
            }

            inputEmailText.text = account.email
            inputDateBirthText.text = account.birthday
        }

        val watcher: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                saveAccountBtn.isVisible = true
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //Do something or nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //Do something or nothing
            }
        }

        inputNameText.addTextChangedListener(watcher)
        inputSurnameText.addTextChangedListener(watcher)
        inputEmailText.addTextChangedListener(watcher)
        inputDateBirthText.addTextChangedListener(watcher)

        saveAccountBtn.setOnClickListener() {
            if (account != null) {
                account.name = inputNameText.text.toString()
                account.surname = inputSurnameText.text.toString()
                account.email = inputEmailText.text.toString()
                account.birthday = inputDateBirthText.text.toString()

                val accountValues = account.toMap()

                val childUpdates = hashMapOf<String, Any>("/users/${account.uid}" to accountValues)

                firebaseDb.updateChildren(childUpdates)

                Toast.makeText(context, "Information updated successfully", Toast.LENGTH_SHORT).show()

                saveAccountBtn.isVisible = false

                Log.i(TAG, "is visible: ${saveAccountBtn.isVisible}")

                // Refresh fragment
                val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
                ft.detach(this@AccountPageFragment).attach(this@AccountPageFragment).commit()

            }
        }

        logoutBtn.setOnClickListener() {
            Firebase.auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountPageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        private val TAG = "AccountPageFragment"
    }
}