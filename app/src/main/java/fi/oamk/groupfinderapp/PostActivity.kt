package fi.oamk.groupfinderapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        if (data != null) {
            dateView.text = "Date: ${data.date.toString()}"
            timeView.text = "Time: ${data.time.toString()}"
            contactInfoView.text = "Contact at ${data.contact.toString()}"
            generalInfoView.text = data.description.toString()
            Log.d("SinglePost", data.title.toString())
        }

        submit_participation.setOnClickListener{
            submitParticipation()
        }
    }

    fun submitParticipation() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid/events/${data.title.toString()}")
        val post = Post(data.contact.toString(),data.date.toString(), data.time.toString(), data.description.toString(), data.title.toString(), data.user.toString())
        ref.setValue(post)
            .addOnSuccessListener {
                Log.d("Participate", "Add post to Firebase")
            }
    }
}