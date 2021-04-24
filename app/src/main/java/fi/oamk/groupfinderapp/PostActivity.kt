package fi.oamk.groupfinderapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {
    lateinit var data: Post
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        data = (intent.extras?.getSerializable("POST_KEY") as? Post)!!

        Log.d("TestPost", data.num_participants.toString())

        dateView.text = "Date: ${data.date.toString()}"
        timeView.text = "Time: ${data.time.toString()}"
        contactInfoView.text = "Contact at ${data.contact.toString()}"
        generalInfoView.text = data.description.toString()
        placesView.text = data.num_participants.toString()
        Log.d("SinglePost", data.title.toString())

        submit_participation.setOnClickListener{
            submitParticipation()
        }
    }

    fun submitParticipation() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/events/${data.key.toString()}")
        val post = Post(data.contact.toString(),data.date.toString(), data.time.toString(), data.description.toString(), data.title.toString(), data.key.toString(), data.num_participants.toString())
        ref.setValue(post)
            .addOnSuccessListener {
                Toast.makeText(baseContext, "Success",
                        Toast.LENGTH_SHORT).show()
            }
        val refPost = FirebaseDatabase.getInstance().getReference("/posts/${data.key.toString()}").child("num_participants")
        refPost.setValue(
                (data.num_participants.toInt() - 1).toString()
        )
    }
}