package com.example.vaccination.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.vaccination.R
import com.example.vaccination.Utils.fromFragmentToActivity
import com.example.vaccination.Utils.validateEmail
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecoverPwdFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecoverPwdFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var email: EditText
    private lateinit var recoveryPwdBtn: Button
    private lateinit var goToLoginBtn: Button

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
        var rootView = inflater.inflate(R.layout.recover_pwd_fragment, container, false)

        email = rootView.findViewById(R.id.emailRecoverInput)
        recoveryPwdBtn = rootView.findViewById(R.id.recoverPwdBtn)
        goToLoginBtn = rootView.findViewById(R.id.goToLoginBtn)

        recoveryPwdBtn?.setOnClickListener() {
            // Check email
            recoverPwd()

//            // Go back to previous activity
//            if (activity?.supportFragmentManager != null)
//                fromFragmentToActivity(activity?.supportFragmentManager!!, this)
        }

        goToLoginBtn?.setOnClickListener() {
            //activity?.supportFragmentManager?.popBackStack()
            //activity?.onBackPressed() //close the app

            // Go back to previous activity
            if (activity?.supportFragmentManager != null)
                fromFragmentToActivity(activity?.supportFragmentManager!!, this)
        }

        return rootView
    }

    private fun recoverPwd() {
        var emailAddress = email.text.toString()

        val errorMsg = validateEmail(emailAddress)
        if (errorMsg.isNotEmpty()) {
            Toast.makeText(activity, errorMsg, Toast.LENGTH_SHORT).show()
        } else {
            recoveryPwdBtn.isEnabled = false
            Firebase.auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                        Toast.makeText(activity, "Email sent", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d(TAG, "Email not sent, something went wrong.")
                        var message: String? = "Email not sent, something went wrong."
                        if (task.exception != null)
                            message = task.exception!!.message
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                    recoveryPwdBtn.isEnabled = true
                }
        }
    }


    companion object {
        private const val TAG = "RecoverPwdFragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RecoverPwdFragment.
         */
        @JvmStatic
        fun newInstance() = RecoverPwdFragment()
    }
}