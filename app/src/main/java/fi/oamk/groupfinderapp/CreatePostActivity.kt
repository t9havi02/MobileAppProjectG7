package fi.oamk.groupfinderapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_post.*

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

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        edTitle = findViewById(R.id.title)
        edDate = findViewById(R.id.date)
        edTime = findViewById(R.id.time)
        edContact = findViewById(R.id.contact)
        edDescription = findViewById(R.id.description)

        database = Firebase.database.reference
    }

    fun post(view: View) {
        val title = edTitle.text.toString()
        val date = edDate.text.toString()
        val time = edTime.text.toString()
        val contact = edContact.text.toString()
        val description = edDescription.text.toString()

        val key = database.child( "posts").push().key.toString()
        val item = Item(key, title, date, time, contact, description)
        if(check_premium.isChecked) {
            database.child( "pposts").child(key).setValue(item)
                    .addOnSuccessListener {
                        Toast.makeText(baseContext, "Premium post was created",
                                Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
        } else {
            database.child( "posts").child(key).setValue(item)
                    .addOnSuccessListener {
                        Toast.makeText(baseContext, "Post created",
                                Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
        }
    }




}