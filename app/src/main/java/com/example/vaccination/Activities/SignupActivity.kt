package com.example.vaccination.Activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccination.Firebase.User
import com.example.vaccination.Firebase.UserDao
import com.example.vaccination.R
import com.example.vaccination.Utils.validateEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.utilities.Utilities
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*


class SignupActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var surname: EditText
    private lateinit var email: EditText
    private lateinit var dateBirth: EditText
    private lateinit var regionSpinner: Spinner
    private lateinit var selectedRegion: String
    private var selectedRegionPos: Int = 0
    private lateinit var pwd: EditText
    private lateinit var repeatPwd: EditText

    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseDb: DatabaseReference

    val datePattern: String = "dd/MM/yyyy"

    private lateinit var registerBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        name = findViewById<EditText>(R.id.inputNameSignup)
        surname = findViewById<EditText>(R.id.inputSurnameSignup)
        email = findViewById<EditText>(R.id.inputEmailSignup)
        dateBirth = findViewById<EditText>(R.id.inputDateBirthSignup)
        regionSpinner = findViewById<Spinner>(R.id.spinnerRegionSignup)
        pwd = findViewById<EditText>(R.id.inputPwdSignup)
        repeatPwd = findViewById<EditText>(R.id.inputRepeatPwdSignup)

        registerBtn = findViewById<Button>(R.id.btnRegister)

        // Initialize Firebase Auth
        auth = Firebase.auth
        // Initialize Firebase Database
        firebaseDb = Firebase.database.reference

        // Show Date Picker both on focus (when the input is clicked for the first time) and
        // on click (when the input is clicked with the focus already on the input itself)
        dateBirth.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus)
                showDatePicker()
        }
        dateBirth.setOnClickListener(){
            showDatePicker()
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.regions_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            regionSpinner.adapter = adapter
        }
        regionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedRegion = parent?.getItemAtPosition(position).toString()
                selectedRegionPos = position
//                Toast.makeText(applicationContext, "Region:" + position + selectedRegion, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // TODO("Not yet implemented")
            }
        }

        // Login
        registerBtn?.setOnClickListener() {
            register()
        }

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI(currentUser)
        }
    }

    private fun register(): Boolean {
        val nameText    = name.text.toString()
        val surnameText = surname.text.toString()
        val emailText   = email.text.toString()
        val dateBirthText = dateBirth.text.toString()
        val regionText  = selectedRegion
        val pwdText     = pwd.text.toString()

        if (!validateSignupInput()) {
            return false
        }

        registerBtn.isEnabled = false

        auth.createUserWithEmailAndPassword(emailText, pwdText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val uid = user.uid
                    // Write on the database
                    var newUser = User(nameText, surnameText, emailText, dateBirthText, regionText, uid)
                    UserDao.insert(firebaseDb, newUser)
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    var message: String? = ""
                    if (task.exception != null)
                        message = ": " + task.exception!!.message
                    Toast.makeText(baseContext, "Authentication failed" + message,
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
                registerBtn.isEnabled = true
            }
        
        return true
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showDatePicker() {
        // Create and show Calendar
        var day: Int = 0
        var month: Int = 0
        var year: Int = 0
        val calendar: Calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        val datePickerDialog = DatePickerDialog(this@SignupActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            // Display selected date in _root_ide_package_.android.widget.EditText
            val _month: Int = month + 1
            val _zeroDay: String = if(dayOfMonth < 10) "0" else ""
            val _zeroMonth: String = if (_month < 10) "0" else ""
            dateBirth.setText(_zeroDay + dayOfMonth + "/" + _zeroMonth + _month + "/" + year)
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun validateSignupInput(): Boolean {
        val nameText = name.text.toString()
        val surnameText = surname.text.toString()
        val dateBirthText = dateBirth.text.toString()
        val emailText = email.text.toString()
        val region = selectedRegionPos
        val pwdText = pwd.text.toString()
        val repeatPwdText = repeatPwd.text.toString()


        // Check if the inputs are correct
        if (nameText.isEmpty()) {
            Toast.makeText(applicationContext, "Enter name", Toast.LENGTH_SHORT).show()
            return false
        }
        if (surnameText.isEmpty()) {
            Toast.makeText(applicationContext, "Enter surname", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check if the email input is filled and correct
        val errorMsg = validateEmail(emailText)
        if (errorMsg.isNotEmpty()) {
            Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_SHORT).show()
            return false
        }

        // Check birth date
        if (dateBirthText.isEmpty()) {
            Toast.makeText(applicationContext, "Enter date of birth", Toast.LENGTH_SHORT).show()
            return false
        }

        val formatter = DateTimeFormatter.ofPattern(datePattern)
        try {
            val formattedDate = LocalDate.parse(dateBirthText, formatter)
            if (formattedDate.year >= Calendar.getInstance().get(Calendar.YEAR)) {
                Toast.makeText(applicationContext, "Invalid year", Toast.LENGTH_SHORT).show()
                return false
            }
        } catch (exc: DateTimeParseException) {
            Toast.makeText(applicationContext, "Invalid date", Toast.LENGTH_SHORT).show()
            return false
        } catch (exc: Exception) {
            Toast.makeText(applicationContext, "Invalid date", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check Region
        if (region == 0) {
            Toast.makeText(applicationContext, "Enter region of residence", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check if the password input is filled
        if (pwdText.isEmpty()) {
            Toast.makeText(applicationContext, "Enter password", Toast.LENGTH_SHORT).show()
            return false
        }
        if (repeatPwdText.isEmpty()) {
            Toast.makeText(applicationContext, "Enter password", Toast.LENGTH_SHORT).show()
            return false
        }

        // TODO check some pwd criteria

        if (!pwdText.equals(repeatPwdText)) {
            Toast.makeText(applicationContext, "Password must be the same", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    companion object {
        private const val TAG = "SignupActivity"
    }
}