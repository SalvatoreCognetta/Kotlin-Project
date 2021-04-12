package com.example.vaccination.Activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.vaccination.R
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
    private lateinit var pwd: EditText
    private lateinit var repeatPwd: EditText

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
        regionSpinner.onItemSelectedListener


        // Login
        registerBtn?.setOnClickListener() {
            register()
        }

    }

    private fun register(): Boolean {

        if (!validateSignupInput()) {
            return false
        }
        
        return true
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
        val region = region.text.toString()
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
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (emailText.isEmpty()) {
            Toast.makeText(applicationContext, "Enter email address", Toast.LENGTH_SHORT).show()
            return false
        } else if (!emailText.trim { it <= ' ' }.matches(emailPattern.toRegex())) {
            Toast.makeText(applicationContext, "Invalid email address", Toast.LENGTH_SHORT).show()
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
        } catch (exc: DateTimeParseException) {
            Toast.makeText(applicationContext, "Invalid date", Toast.LENGTH_SHORT).show()
            return false
        } catch (exc: Exception) {
            Toast.makeText(applicationContext, "Invalid date", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check Region


        // Check if the password input is filled
        if (pwdText.isEmpty()) {
            Toast.makeText(applicationContext, "Enter password", Toast.LENGTH_SHORT).show()
            return false
        }
        if (repeatPwdText.isEmpty()) {
            Toast.makeText(applicationContext, "Enter password", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!pwdText.equals(repeatPwdText)) {
            Toast.makeText(applicationContext, "Password must be the same", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}