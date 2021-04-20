package fi.oamk.groupfinderapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val data = intent.extras?.getSerializable("POST_KEY") as? Post

        if (data != null) {
            Log.d("SinglePost", data.title.toString())
        }
    }
}