package fi.oamk.groupfinderapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_profile.*

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

        activate_premium.setOnClickListener{
            activatePremium()
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        fetchUser()
        fetchEvents()
    }

    private fun fetchUser() {
        val ref = FirebaseDatabase.getInstance().getReference("users").child(userId)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)
                profile_city.text = user?.ucity
                profile_age.text = "${user?.uage} years"
                profile_name.text = (user?.uname)
                Picasso.get().load(user?.profileImageUrl).into(profile_avatar);
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun fetchEvents() {
        val ref = FirebaseDatabase.getInstance().getReference("/users/$userId/events")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach() {
                    Log.d("PostsActivity", it.toString())
                    val post = it.getValue(Post::class.java)
                    if(post != null) {
                        adapter.add(PostItem(post))
                    }
                }
                recyclerview_user_events.adapter = adapter

                adapter.setOnItemClickListener { item, view ->
                    val postItem = item as PostItem
                    val intent = Intent(view.context, PostActivity::class.java)
                    intent.putExtra(MainActivity.POST_KEY, postItem.post)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun activatePremium() {
        val ref = FirebaseDatabase.getInstance().getReference("/users/$userId").child("premium")
        ref.setValue(1.toString())
                .addOnSuccessListener {
                    Toast.makeText(baseContext, "Premium account activated",
                            Toast.LENGTH_SHORT).show()
                }
    }

}


