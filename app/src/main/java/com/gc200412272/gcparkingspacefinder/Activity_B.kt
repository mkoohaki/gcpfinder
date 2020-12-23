package com.gc200412272.gcparkingspacefinder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_b.*
import kotlinx.android.synthetic.main.activity_b.back
import kotlinx.android.synthetic.main.activity_b.btn_logout
import kotlinx.android.synthetic.main.activity_b.submit
import kotlinx.android.synthetic.main.activity_b.txt_number
import java.text.SimpleDateFormat
import java.util.*


class Activity_B : AppCompatActivity() {

    // connect to Firestore
    val db = FirebaseFirestore.getInstance()

    var parked = true
    var checked = false

    val successMessage = "You Submitted Successfully!"
    val unsuccessMessage = "Information did not updated!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)

        val area = "b"
        val query = db.collection("spaces")

        back.setOnClickListener {
            finish()
        }

        btn_logout.setOnClickListener {
            // navigate to Main Activity to add a Restaurant
            FirebaseAuth.getInstance().signOut()
            finish()

            //reload the login
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

        val spinner: Spinner = findViewById(R.id.spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.area_b,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        submit.setOnClickListener {

            var no: Int = 0
            var zone: String = spinner.selectedItem as String

            if (txt_number.text.toString().isNotEmpty()) {
                no = Integer.parseInt(txt_number.text.toString())
            }

            val numberErrorMessage = "This space number does not exist!"
            val reservedErrorMessage = "The location already is reserved!"
            val freeErrorMessage = "The location already is free!"

            if (zone.isNotEmpty() && no > 0 && checked) {

                if ((zone == "A" && no < 30) ||
                    (zone == "B" && no < 28) ||
                    (zone == "C" && no < 28) ||
                    (zone == "D" && no < 28) ||
                    (zone == "E" && no < 28) ||
                    (zone == "F" && no < 30) ||
                    (zone == "G" && no < 15)) {

                    val location = area.toString()+zone.toString()+no.toString()

                    query.document(location).get()
                        .addOnSuccessListener { space ->
                            if (space.exists()) {
                                if (parked) {
                                    query.whereEqualTo("parked", false).get()
                                        .addOnSuccessListener { documents ->
                                            var reserved = false

                                            for (document in documents) {
                                                if (document.id == location) {
                                                    updateLocation(location, true)
                                                    reserved = true
                                                }
                                            }
                                            if(!reserved) {
                                                Toast.makeText(
                                                    applicationContext,
                                                    reservedErrorMessage,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                } else {
                                    query.whereEqualTo("parked", true).get()
                                        .addOnSuccessListener { docs ->
                                            var reserved = true

                                            for (doc in docs) {
                                                if (doc.id == location) {
                                                    updateLocation(location, false)
                                                    reserved = false
                                                }
                                            }
                                            if(reserved) {
                                                Toast.makeText(
                                                    applicationContext,
                                                    freeErrorMessage,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                }
                            } else {
                                if(parked) {
                                    saveLocation(location)
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        freeErrorMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                applicationContext,
                                "Error: " + exception,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(this, numberErrorMessage, Toast.LENGTH_SHORT).show()
                }
                txt_number.setText("")
                spinner.setSelection(0)
            }
        }

        button.setOnClickListener {

            val intent = Intent(applicationContext, Activity_Free::class.java)
            intent.putExtra("area", area)
            startActivity(intent)
        }
    }

    // Code from https://developer.android.com/guide/topics/ui/controls/radiobutton
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radioButton_leaving ->
                    if (checked) {
                        parked = false
                    }
                R.id.radioButton_parking ->
                    if (checked) {
                        parked = true
                    }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(): String {
        val sdf = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
        val currentDateandTime: String = sdf.format(Date())

        return currentDateandTime
    }

    private fun saveLocation(location: String) {

        val space = Space()
        space.id = location
        space.parked = parked
        space.time = getTime()

        // connect & save to Firebase. collection  will be created if it doesn't exist already
        val db = FirebaseFirestore.getInstance().collection("spaces")
        val id = location
        db.document(id).set(space)

        Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()
    }

    private fun updateLocation(location: String, parked: Boolean) {
        db.collection("spaces").document(location)
            .update(
                mapOf(
                    "parked" to parked,
                    "time" to getTime()
                )
            )
            .addOnSuccessListener {
                Toast.makeText(applicationContext, successMessage, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, unsuccessMessage, Toast.LENGTH_SHORT).show()
            }
    }

    override fun onRestart() {
        super.onRestart()

        val intent = Intent(applicationContext, Activity_B::class.java)
        startActivity(intent)
        this.finish()
    }
}

