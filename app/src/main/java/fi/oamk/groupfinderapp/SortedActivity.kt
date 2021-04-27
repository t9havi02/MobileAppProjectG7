package fi.oamk.groupfinderapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.post.view.*
import java.io.Serializable


class SortedActivity : AppCompatActivity() {

    private lateinit var sortedPosts: RecyclerView
    private lateinit var sortedPremiumPosts: RecyclerView
    private lateinit var city: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sorted)
        sortedPosts = findViewById(R.id.rcsortedPosts)
        sortedPremiumPosts = findViewById(R.id.rcsortedPremiumPosts)
        city = findViewById(R.id.places)
        city.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                fetchPosts()
                fetchPremiumPosts()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })
        fetchPosts()
        fetchPremiumPosts()
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
                        if(post.place.toString() == city.selectedItem.toString()){
                            adapter.add(SortedPostItem(post))
                        }
                    }
                }

                adapter.setOnItemClickListener { item, view ->
                    val postItem = item as SortedPostItem
                    val intent = Intent(view.context, PostActivity::class.java)
                    intent.putExtra(POST_KEY, postItem.post)
                    startActivity(intent)
                }

                sortedPosts.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun fetchPremiumPosts() {
        val ref = FirebaseDatabase.getInstance().getReference("/pposts")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach() {
                    Log.d("PremiumPostsActivity", it.toString())
                    val post = it.getValue(Post::class.java)
                    if(post != null) {
                        if (post.place.toString() == city.selectedItem.toString()) {
                            adapter.add(SortedPremiumPostItem(post))
                        }
                    }
                }

                adapter.setOnItemClickListener { item, view ->
                    val postItem = item as SortedPremiumPostItem
                    val intent = Intent(view.context, PostActivity::class.java)
                    intent.putExtra(POST_KEY, postItem.post)
                    startActivity(intent)
                }

                sortedPremiumPosts.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}
class SortedPostItem (val post: Post): Item<ViewHolder>(), Serializable {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.postTitle.text = post.title
    }
    override fun getLayout(): Int {
        return R.layout.post
    }
}
class SortedPremiumPostItem (val post: Post): Item<ViewHolder>(), Serializable {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.postTitle.text = post.title
    }
    override fun getLayout(): Int {
        return R.layout.premium_post
    }
}