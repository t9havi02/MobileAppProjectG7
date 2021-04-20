package fi.oamk.groupfinderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.post.view.*
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerview_posts: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerview_posts = findViewById(R.id.recyclerview_posts)
        fetchPosts()
    }

    companion object {
        val POST_KEY = "POST_KEY"
    }

    private fun fetchPosts() {
        val ref = FirebaseDatabase.getInstance().getReference("/posts")
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

                adapter.setOnItemClickListener { item, view ->

                    val postItem = item as PostItem

                    val intent = Intent(view.context, PostActivity::class.java)
                    intent.putExtra(POST_KEY, postItem.post)
                    startActivity(intent)
                }

                recyclerview_posts.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_profile -> startActivity(Intent(this, ProfileActivity::class.java))
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

class PostItem (val post: Post): Item<ViewHolder>(), Serializable {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.postTitle.text = post.title
    }

    override fun getLayout(): Int {
        return R.layout.post
    }
}

class Post(val contact: String, val date:String, val description: String, val time: String, val title: String, val user: String) : Serializable {
    constructor() : this("","","","","", "")
}