package fi.oamk.groupfinderapp

import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CreatePostActivity: AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var edTitle: EditText
    private lateinit var edDate: EditText
    private lateinit var edTime: EditText
    private lateinit var edContact: EditText
    private lateinit var edDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        edTitle = findViewById(R.id.title)
        edDate = findViewById(R.id.date)
        edTime = findViewById(R.id.time)
        edContact = findViewById(R.id.contact)
        edDescription = findViewById(R.id.description)

        database = Firebase.database.reference
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.submit -> {
            val title = edTitle.text.toString()
            val date = edDate.text.toString()
            val time = edTime.text.toString()
            val contact = edContact.text.toString()
            val description = edDescription.text.toString()


            //class Item(key: String, title: String, date: String, time: String, contact: String, description: String)

            val key = database.child( "posts").push().key.toString()
            val item = Item(key, title, date, time, contact, description)
            database.child( "posts").child(key).setValue(item)
            onBackPressed()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }


}