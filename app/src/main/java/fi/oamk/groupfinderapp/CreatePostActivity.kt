package fi.oamk.groupfinderapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

class CreatePostActivity: AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var edTitle: EditText
    private lateinit var edDate: EditText
    private lateinit var edTime: EditText
    private lateinit var edContact: EditText
    private lateinit var edDescription: EditText
    private lateinit var edNumParticipants: EditText
    private lateinit var userId: String
    private lateinit var p_value: String
    private lateinit var premium: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        edTitle = findViewById(R.id.title)
        edDate = findViewById(R.id.date)
        edTime = findViewById(R.id.time)
        edContact = findViewById(R.id.contact)
        edDescription = findViewById(R.id.description)
        edNumParticipants = findViewById(R.id.num_participants)
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        database = Firebase.database.reference

        val ref = FirebaseDatabase.getInstance().getReference("users").child(userId).child("premium")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                p_value = snapshot.getValue() as String
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    fun post(view: View) {
        val title = edTitle.text.toString()
        val date = edDate.text.toString()
        val time = edTime.text.toString()
        val contact = edContact.text.toString()
        val description = edDescription.text.toString()
        val num_participants = edNumParticipants.text.toString()
        if (check_premium.isChecked) {
            premium = "1"
        } else {
            premium = "0"
        }

        val key = database.child( "posts").push().key.toString()
        val item = Item(key, title, date, time, contact, description, num_participants, premium)
        if(check_premium.isChecked) {
            if(p_value.toInt() == 1) {
                database.child( "pposts").child(key).setValue(item)
                        .addOnSuccessListener {
                            Toast.makeText(baseContext, "Premium post was created",
                                    Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
            } else {
                Toast.makeText(baseContext, "You do not have permission to create premium posts",
                        Toast.LENGTH_SHORT).show()
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