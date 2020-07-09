package com.example.covidmessergerapp.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covidmessergerapp.AdapterClasses.UserAdapter
import com.example.covidmessergerapp.ModelClasses.ChatList
import com.example.covidmessergerapp.ModelClasses.Users
import com.example.covidmessergerapp.Notifications.Token

import com.example.covidmessergerapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.fragment_chats.*

/**
 * A simple [Fragment] subclass.
 */
class ChatsFragment : Fragment() {
    private var userAdapter:UserAdapter?=null
    private var mUsers:List<Users>?=null
    private var usersChatList :List<ChatList>?=null
    private var firebaseUser: FirebaseUser?=null

    lateinit var recyler_view_chatlist:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view:View= inflater.inflate(R.layout.fragment_chats, container, false)

        recyler_view_chatlist=view.findViewById(R.id.recycler_view_chatlist)
        recyler_view_chatlist.setHasFixedSize(true)
        recyler_view_chatlist.layoutManager=LinearLayoutManager(context)
        firebaseUser=FirebaseAuth.getInstance().currentUser
        usersChatList=ArrayList()
        mUsers=ArrayList()
        val ref=FirebaseDatabase.getInstance().reference.child("ChatList").child(firebaseUser!!.uid)
        ref!!.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                (usersChatList as ArrayList).clear()
                for(dataSnapshot in p0.children){
                    val chatList=dataSnapshot.getValue(ChatList::class.java)
                    (usersChatList as ArrayList).add(chatList!!)
                }
                retrieveChatList()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
        updateToken(FirebaseInstanceId.getInstance().token)
        return view
    }

    private fun updateToken(token: String?) {
       val ref=FirebaseDatabase.getInstance().reference.child("Tokens")
        val token1= Token(token!!)
        ref.child(firebaseUser!!.uid).setValue(token1)
    }


    private fun retrieveChatList()
    {

        val ref= FirebaseDatabase.getInstance().reference.child("Users")
        ref!!.addValueEventListener( object : ValueEventListener
        {
            override fun onDataChange(p0: DataSnapshot) {
                (mUsers as ArrayList<Users>).clear()

                for (dataSnapshot in p0.children) {
                    val user = dataSnapshot.getValue(Users::class.java)
                    for (eachChatList in usersChatList!!) {
                        if (!(user!!.getUID()).equals(eachChatList.getId())) {
                            (mUsers as ArrayList<Users>).add(user!!)
                        }
                    }
                    userAdapter = UserAdapter(context!!, mUsers!!, false)
                    recycler_view_chatlist!!.adapter = userAdapter
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })



}
}
