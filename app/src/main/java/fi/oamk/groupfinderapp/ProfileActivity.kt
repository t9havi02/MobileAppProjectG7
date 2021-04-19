package fi.oamk.groupfinderapp

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ProfileActivity : AppCompatActivity() {
    lateinit var userId: String
    lateinit var profile_name: TextView
    lateinit var profile_age: TextView
    lateinit var profile_city: TextView
    lateinit var profile_avatar: ImageView
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        profile_name = findViewById(R.id.p_name)
        profile_age = findViewById(R.id.p_age)
        profile_city = findViewById(R.id.p_city)
        profile_avatar = findViewById(R.id.profile_avatar)

        userId = FirebaseAuth.getInstance().currentUser!!.uid

        fetchUser()
    }

    private fun fetchUser() {
        val ref = FirebaseDatabase.getInstance().getReference("users").child(userId)
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)
                profile_city.text = user?.ucity
                profile_age.text = "${user?.uage} years"
                profile_name.text = (user?.uname)
                Picasso.get().load(user?.profileImageUrl).into(profile_avatar);
                Log.d("ProfileView", "${user?.uname} ")
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}