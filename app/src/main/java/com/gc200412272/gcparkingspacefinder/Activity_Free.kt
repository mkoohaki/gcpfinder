package com.gc200412272.gcparkingspacefinder

import android.annotation.SuppressLint
import android.content.Intent
import android.database.DataSetObserver
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_free.*
import kotlinx.android.synthetic.main.item_place.view.*
import java.text.SimpleDateFormat
import java.util.*

class Activity_Free : AppCompatActivity() {

    // connect to Firestore
    val db = FirebaseFirestore.getInstance()
    val area = "b"
    private var adapter: PlaceAdapter? = null

    val successMessage = "You Update Successfully!"
    val unsuccessMessage = "Information did not updated!"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free)

        back.setOnClickListener {
            finish()
        }

        val area = intent.getStringExtra("area")
        if (area != null) {
            txt_area.text = area.toUpperCase(Locale.ROOT)

            // set our recyclerview to use LinearLayour
            placesRecyclerView.layoutManager = LinearLayoutManager(this)

            val query = db.collection("spaces").orderBy("id").startAt(area+"A1").endBefore(area+"Z")

            // pass query results to RecyclerAdapter for display in RecyclerView
            val options =
                FirestoreRecyclerOptions.Builder<Space>().setQuery(query , Space::class.java)
                    .build()
            adapter = PlaceAdapter(options)
            placesRecyclerView.adapter = adapter as PlaceAdapter
        }
    }

    // tell adapter to start watching data for changes
    override fun onStart() {
        super.onStart()
        adapter!!.startListening()

        // check user is signed in
        val user = Firebase.auth.currentUser
        if (user == null) {

            //reload signin so providers reinitialized
            val intent = Intent(applicationContext , LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        if (adapter != null) {
            adapter!!.stopListening()
        }
    }

    // create inner classes needed to bind the data to the recyclerview
    private inner class PlaceViewHolder internal constructor(private val view: View) :
        RecyclerView.ViewHolder(view) {}

    private inner class PlaceAdapter internal constructor(options: FirestoreRecyclerOptions<Space>) :
        FirestoreRecyclerAdapter<Space , PlaceViewHolder>(options) , Adapter {
        override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): PlaceViewHolder {

            // inflate the item_restaurant.xml layout template to populate the recyclerview
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_place , parent , false)
            return PlaceViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(
            holder: PlaceViewHolder ,
            position: Int ,
            model: Space
        ) {
            if (model.parked == true) {
                holder.itemView.placeTextView2.text = model.id?.toUpperCase(Locale.ROOT)
                holder.itemView.reservedTextView.text = "Reserved"
                holder.itemView.reservedTextView.setTextColor(Color.parseColor("#FF0000"))
            } else {
                holder.itemView.placeTextView1.text = model.id?.toUpperCase(Locale.ROOT)
                holder.itemView.freeTextView.text = "Free"
                holder.itemView.freeTextView.setTextColor(Color.parseColor("#00fa00"))
            }

            holder.itemView.setOnClickListener {
                if(model.parked)
                    model.id?.let { it1 -> updateLocation(it1 , false) }
                else
                    model.id?.let { it1 -> updateLocation(it1 , true) }
            }
        }

        override fun registerDataSetObserver(observer: DataSetObserver?) {
            TODO("Not yet implemented")
        }

        override fun unregisterDataSetObserver(observer: DataSetObserver?) {
            TODO("Not yet implemented")
        }

        override fun getCount(): Int {
            TODO("Not yet implemented")
        }

        override fun getView(position: Int , convertView: View? , parent: ViewGroup?): View {
            TODO("Not yet implemented")
        }

        override fun getViewTypeCount(): Int {
            TODO("Not yet implemented")
        }

        override fun isEmpty(): Boolean {
            TODO("Not yet implemented")
        }
    }

    private fun updateLocation(location: String , parked: Boolean) {
        db.collection("spaces").document(location)
            .update(
                mapOf(
                    "parked" to parked ,
                    "time" to getTime()
                )
            )
            .addOnSuccessListener {
                Toast.makeText(applicationContext , successMessage , Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext , unsuccessMessage , Toast.LENGTH_SHORT).show()
            }
    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(): String {
        val sdf = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
        val currentDateandTime: String = sdf.format(Date())

        return currentDateandTime
    }
}