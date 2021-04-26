package fi.oamk.groupfinderapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_post.*
import java.util.*

class PostActivity : AppCompatActivity() {
    lateinit var data: Post
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        data = (intent.extras?.getSerializable("POST_KEY") as? Post)!!

        Log.d("TestPost", data.num_participants.toString())

        dateView.text = data.date.toString()
        titleView.text = data.title.toString()
        placeView.text = data.place.toString()
        timeView.text = data.time.toString()
        contactInfoView.text = data.contact.toString()
        generalInfoView.text = data.description.toString()
        placesView.text = data.num_participants.toString()
        Log.d("SinglePost", data.title.toString())

        if(data.num_participants.toInt() < 1) {
            submit_participation.text = "Out of places"
            submit_participation.setOnClickListener{
                Toast.makeText(baseContext, "There are no more places for this event :(",
                    Toast.LENGTH_LONG).show()
            }
        } else {
            submit_participation.setOnClickListener{
                submitParticipation()
            }
        }
    }

    fun submitParticipation() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/events/${data.key.toString()}")
        val post = Post(data.contact.toString(), data.place.toString(), data.date.toString(), data.time.toString(), data.description.toString(), data.title.toString(), data.key.toString(), data.num_participants.toString(), data.premium.toString())
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(baseContext, "You are already registered for this event",
                        Toast.LENGTH_SHORT).show()
                    return
                } else {
                    ref.setValue(post)
                        .addOnSuccessListener {
                            Toast.makeText(baseContext, "Success",
                                Toast.LENGTH_SHORT).show()
                        }
                    if (data.premium.toString() == "0") {
                        val refPost = FirebaseDatabase.getInstance().getReference("/posts/${data.key.toString()}").child("num_participants")
                        refPost.setValue(
                            (data.num_participants.toInt() - 1).toString()
                        )
                    } else {
                        val refPost = FirebaseDatabase.getInstance().getReference("/pposts/${data.key.toString()}").child("num_participants")
                        refPost.setValue(
                            (data.num_participants.toInt() - 1).toString()
                        )
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }
}