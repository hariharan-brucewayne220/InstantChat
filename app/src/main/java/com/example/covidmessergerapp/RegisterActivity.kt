package com.example.covidmessergerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers:DatabaseReference
    private var firebaseUserID:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar: Toolbar =findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.title="Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener{
            val intent=Intent(this@RegisterActivity,WelcomeActivity::class.java)
            finish()
        }

        mAuth=FirebaseAuth.getInstance()

        register_btn.setOnClickListener{
            registerUser()
        }
    }
    private fun registerUser(){
        val username:String=username_register.text.toString()
        val email:String=email_register.text.toString()
        val password:String=password_register.text.toString()
        if(username == ""){
            Toast.makeText(this@RegisterActivity,"please fill username",Toast.LENGTH_LONG).show()
        }
        else if(email=="")
        {
            Toast.makeText(this@RegisterActivity,"please fill username",Toast.LENGTH_LONG).show()
        }else if(password==""){
            Toast.makeText(this@RegisterActivity,"please fill password",Toast.LENGTH_LONG).show()
        }else{
           mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task->
               if(task.isSuccessful){
                 firebaseUserID=mAuth.currentUser!!.uid
                   refUsers=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
                val userHashMap=HashMap<String,Any>()
                   userHashMap["uid"]=firebaseUserID
                   userHashMap["username"]=username
                   userHashMap["profile"]="https://firebasestorage.googleapis.com/v0/b/covidmessengerapp.appspot.com/o/corona.png?alt=media&token=dbc883af-7d7b-49d4-85b3-da9a95ad3cb0"
                   userHashMap["cover"]="https://firebasestorage.googleapis.com/v0/b/covidmessengerapp.appspot.com/o/cover.jpg?alt=media&token=904cb5cc-c601-4d63-b827-3a70f83a25ec"
                   userHashMap["status"]="offline"
                   userHashMap["search"]=username.toLowerCase()
                   userHashMap["facebook"]="https://m.facebook.com"
                   userHashMap["instagram"]="https://m.instagram.com"
                   userHashMap["website"]="https://m.google.com"


                   refUsers.updateChildren(userHashMap)
                       .addOnCompleteListener{task->
                   if(task.isSuccessful){
                       val intent=Intent(this@RegisterActivity,MainActivity::class.java)
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                       startActivity(intent)
                       finish()
                   }
                       }


               }else{
                   Toast.makeText(this@RegisterActivity,"error message: "+task.exception!!.message,Toast.LENGTH_LONG).show()
               }
           }
        }
    }
}
