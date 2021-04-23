package fi.oamk.groupfinderapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG: String = RegisterActivity::class.java.name
    private lateinit var uname: TextView
    private lateinit var ucity: TextView
    private lateinit var uage: TextView
    private lateinit var email: TextView
    private lateinit var password: TextView
    private lateinit var add_photo_btn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        add_photo_btn = findViewById(R.id.add_photo_btn)
        uname = findViewById(R.id.reg_name)
        ucity = findViewById(R.id.reg_city)
        uage = findViewById(R.id.reg_age)

        add_photo_btn.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 0) {
            selectedPhotoUri = data?.data
            add_photo_btn.setImageURI(selectedPhotoUri)
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun register(view: View) {
        email = findViewById(R.id.reg_email)
        password = findViewById(R.id.reg_password)

        if(!isValidEmail(email.text.toString())) {
            Toast.makeText(baseContext, "Wrong email format",
                    Toast.LENGTH_SHORT).show()
        }

        if(password.text.length < 6) {
            Toast.makeText(baseContext, "Password must be at least 6 symbols long",
                    Toast.LENGTH_SHORT).show()
        }

        auth.createUserWithEmailAndPassword(email.text.toString().trim(), password.text.toString().trim())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(baseContext, "You have successfully been registered",
                                Toast.LENGTH_SHORT).show()
                        uploadImageToFirebaseStorage()
                    } else {
                        Toast.makeText(baseContext, "Registration failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/avatars/$filename")
        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Successfully uploaded image")
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("RegisterActivity", "File Location: $it")
                        saveUserToFirebaseDatabase(it.toString())
                    }
                }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, uname.text.toString(), ucity.text.toString(), uage.text.toString(), profileImageUrl)

        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Saved user to Firebase Database")
                }
    }

    fun moveToLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}

class User(val uid: String, val uname:String, val ucity: String, val uage: String, val profileImageUrl: String) {
    constructor() : this("","","","","")
}