package com.example.covidmessergerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val toolbar: Toolbar =findViewById(R.id.toolbar_login)
        setSupportActionBar(toolbar)
        supportActionBar!!.title="Login"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener{
            var intent= Intent(this@LoginActivity,WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        mAuth=FirebaseAuth.getInstance()
        login_btn.setOnClickListener{
            loginUser()
        }
    }
    private fun loginUser(){
        val email:String=email_login.text.toString()
        val password=password_login.text.toString()
        if(email=="")
        {
            Toast.makeText(this@LoginActivity,"please fill emaild", Toast.LENGTH_LONG).show()
        }else if(password==""){
            Toast.makeText(this@LoginActivity,"please fill password", Toast.LENGTH_LONG).show()
        }else{
             mAuth.signInWithEmailAndPassword(email,password)
                 .addOnCompleteListener {
                     task->
                     if(task.isSuccessful){
                         val intent=Intent(this@LoginActivity,MainActivity::class.java)
                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                         startActivity(intent)
                         finish()
                     }else{
                         Toast.makeText(this@LoginActivity,"error message: "+task.exception!!.message,Toast.LENGTH_LONG).show()}
                 }

        }
    }
}
