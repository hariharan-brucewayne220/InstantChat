package com.example.covidmessergerapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.covidmessergerapp.ModelClasses.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_visit.*

class UserVisitActivity : AppCompatActivity() {

    var user:Users?=null
    private var userVisitId:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_visit)

        userVisitId=intent.getStringExtra("visit_id")
        val ref=FirebaseDatabase.getInstance().reference.child("Users").child(userVisitId)
        ref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
              if(p0.exists()){
                 user=p0.getValue(Users::class.java)
                  username_display.text=user!!.getUserName()
                  Picasso.get().load(user!!.getProfile()).into(profile_display)
                  Picasso.get().load(user!!.getCover()).into(cover_display)
              }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        facebook_display.setOnClickListener{
            val uri= Uri.parse(user!!.getFacebook())
            val intent=Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }
        instagram_display.setOnClickListener{
            val uri= Uri.parse(user!!.getInstagram())
            val intent=Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }
        website_display.setOnClickListener{
            val uri= Uri.parse(user!!.getWebsite())
            val intent=Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }
        send_message_btn.setOnClickListener{
            val intent=Intent(this,MessageChatActivity::class.java)
        }
    }
}
