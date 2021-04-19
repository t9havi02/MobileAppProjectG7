package fi.oamk.groupfinderapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * A fragment representing a list of Items.
 */
class PostFragment : Fragment() {

    private lateinit var posts: ArrayList<String>
    private lateinit var rcList: RecyclerView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Firebase.database.reference

        posts = arrayListOf()

        database.child("posts").get().addOnSuccessListener {
            if (it.value != null) {
                val postsFromDB = it.value as HashMap<String, Any>
                posts.clear()
                postsFromDB.map { (key, value) ->
                    val postFromDB = value as HashMap<String, Any>

                    val title = postFromDB.get("title").toString()
                    posts.add(title)
                }
                rcList.adapter?.notifyDataSetChanged()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        rcList = view.findViewById(R.id.list)
        rcList.layoutManager = LinearLayoutManager(context)
        rcList.adapter = MyPostRecyclerViewAdapter(posts)

        return view
    }
}