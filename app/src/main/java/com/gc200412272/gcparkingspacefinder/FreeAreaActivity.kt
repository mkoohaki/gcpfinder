package com.gc200412272.gcparkingspacefinder

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_free_area.*

class FreeAreaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_area)

        btn_a.setOnClickListener {
            val i = Intent(applicationContext, Activity_A::class.java)

            startActivity(i)
        }

        btn_b.setOnClickListener {
            val i = Intent(applicationContext, Activity_B::class.java)

            startActivity(i)
        }

        btn_bb.setOnClickListener {
            val i = Intent(applicationContext, Activity_BB::class.java)

            startActivity(i)
        }

        btn_bk.setOnClickListener {
            val i = Intent(applicationContext, Activity_BK::class.java)

            startActivity(i)
        }

        btn_d.setOnClickListener {
            val i = Intent(applicationContext, Activity_D::class.java)

            startActivity(i)
        }

        btn_eh.setOnClickListener {
            val i = Intent(applicationContext, Activity_EH::class.java)

            startActivity(i)
        }

        btn_f.setOnClickListener {
            val i = Intent(applicationContext, Activity_F::class.java)

            startActivity(i)
        }

        btn_g.setOnClickListener {
            val i = Intent(applicationContext, Activity_G::class.java)

            startActivity(i)
        }

        btn_k.setOnClickListener {
            val i = Intent(applicationContext, Activity_K::class.java)

            startActivity(i)
        }

        btn_m.setOnClickListener {
            val i = Intent(applicationContext, Activity_M::class.java)

            startActivity(i)
        }

        btn_n.setOnClickListener {
            val i = Intent(applicationContext, Activity_N::class.java)

            startActivity(i)
        }

        btn_google_maps.setOnClickListener {

            val google = "https://www.google.ca/maps"

            val i = Intent(Intent.ACTION_VIEW)

            i.data = Uri.parse(google)

            startActivity(i)
        }

        btn_logout.setOnClickListener {
            // navigate to Main Activity to add a Restaurant
            FirebaseAuth.getInstance().signOut()
            finish()

            //reload the login
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
        back.setOnClickListener {
            finish()
        }
    }
}