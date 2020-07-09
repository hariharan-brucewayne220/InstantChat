package com.example.covidmessergerapp.AdapterClasses

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.covidmessergerapp.MainActivity
import com.example.covidmessergerapp.MessageChatActivity
import com.example.covidmessergerapp.ModelClasses.Chat
import com.example.covidmessergerapp.ModelClasses.Users
import com.example.covidmessergerapp.R
import com.example.covidmessergerapp.UserVisitActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_main.view.profile_image
import kotlinx.android.synthetic.main.activity_main.view.user_name

class UserAdapter(mContext: Context,
                 mUsers:List<Users>,
                 isChatCheck:Boolean
      ):RecyclerView.Adapter<UserAdapter.ViewHolder?>()
{   private val mContext:Context
    private val mUsers:List<Users>
    private var isChatCheck: Boolean
    var lastMsg:String=""
    init {
        this.mUsers=mUsers
        this.mContext=mContext
        this.isChatCheck=isChatCheck
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view:View=LayoutInflater.from(mContext).inflate(R.layout.user_search_item_layout,viewGroup,false)
        return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val user: Users=mUsers[i]
        holder.userNameTxt.text=user.getUserName()
        Picasso.get().load(user.getProfile()).placeholder(R.drawable.corona).into(holder.profileImageView)
        if(isChatCheck){
            retrieveLastMessage(user.getUID(),holder.lastMessageTxt)
        }
        else{
            holder.lastMessageTxt.visibility=View.GONE
        }
        if(isChatCheck){
            if(user.getStatus()=="online"){
                holder.onlineImageView.visibility=View.VISIBLE
                holder.offlineImageView.visibility=View.GONE
            }
            else{
                holder.onlineImageView.visibility=View.GONE
                holder.offlineImageView.visibility=View.VISIBLE
            }
        }
        else{
            holder.onlineImageView.visibility=View.GONE
            holder.offlineImageView.visibility=View.GONE
        }
        holder.itemView.setOnClickListener{
            val options= arrayOf<CharSequence>(
                "Send Message",
                "Visit Profile"
            )
            val builder:AlertDialog.Builder=AlertDialog.Builder(mContext)
            builder.setTitle("What do you want ?")
            builder.setItems(options,DialogInterface.OnClickListener{
                        _, pos ->
                    if(pos==0){
                        val intent= Intent(mContext, MessageChatActivity::class.java)
                        intent.putExtra("visit_id",user.getUID())
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        mContext.startActivity(intent)

                    }
                    if(pos==1){
                        val intent= Intent(mContext, UserVisitActivity::class.java)
                        intent.putExtra("visit_id",user.getUID())
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        mContext.startActivity(intent)

                    }

            })
            builder.show()
        }
    }



    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
     var userNameTxt: TextView
     var profileImageView:CircleImageView
     var onlineImageView:CircleImageView
     var offlineImageView:CircleImageView
     var lastMessageTxt:TextView
     init{
          userNameTxt=itemView.findViewById(R.id.username)
         profileImageView=itemView.findViewById(R.id.profile_image)
         onlineImageView=itemView.findViewById(R.id.image_online)
         offlineImageView=itemView.findViewById(R.id.image_offline)
         lastMessageTxt=itemView.findViewById(R.id.message_last)
     }
 }
    private fun retrieveLastMessage(chatUserId: String?, lastMessageTxt: TextView) {
       lastMsg="defaultMsg"

        val firebaseUsers= FirebaseAuth.getInstance().currentUser
        val reference=FirebaseDatabase.getInstance().reference.child("Chats")

        reference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                for(dataSnapShot in p0.children){
                    val chat: Chat?=dataSnapShot.getValue(Chat::class.java)

                    if(firebaseUsers!=null &&chat!=null){
                        if(chat.getReceiver()==firebaseUsers.uid &&
                            chat.getSender()==chatUserId ||
                                chat.getReceiver()==chatUserId &&
                            chat.getSender()==firebaseUsers.uid){
                            lastMsg=chat.getMessage()!!
                        }
                    }
                }
                when(lastMsg){
                    "defaultMsg"->lastMessageTxt.text="No Message"
                    "sent you an image."->lastMessageTxt.text="image sent."
                    else->lastMessageTxt.setText(lastMsg)
                }
                lastMsg="defaultMsg"
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}