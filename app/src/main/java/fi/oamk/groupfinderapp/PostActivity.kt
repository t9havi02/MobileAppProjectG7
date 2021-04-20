package fi.oamk.groupfinderapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val data = intent.extras?.getSerializable("POST_KEY") as? Post

        if (data != null) {
            dateView.text = "Date: ${data.date.toString()}"
            timeView.text = "Time: ${data.time.toString()}"
            contactInfoView.text = "Contact at ${data.contact.toString()}"
            generalInfoView.text = data.description.toString()
            Log.d("SinglePost", data.title.toString())
        }
    }
}