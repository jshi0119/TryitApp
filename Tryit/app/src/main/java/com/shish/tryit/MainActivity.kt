package com.shish.tryit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var auth: FirebaseAuth

    val database = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val query = database.collection("users")
        val recyclerOptions = FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User::class.java)
                .setLifecycleOwner(this).build()

        val recyclerAdapter =
            object: FirestoreRecyclerAdapter<User, UserViewHolder>(recyclerOptions) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
                val view = LayoutInflater.from(this@MainActivity).inflate(
                    android.R.layout.simple_list_item_2, parent, false)
                return UserViewHolder(view)
            }

            override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
                val textViewName: TextView = holder.itemView.findViewById(android.R.id.text1)
                val textViewMovie: TextView = holder.itemView.findViewById(android.R.id.text2)
                textViewName.text = model.displayName
                textViewMovie.text = model.movie
            }
        }
        val layoutManager = LinearLayoutManager(this@MainActivity)

        val usersViewUsers = findViewById<RecyclerView>(R.id.users_recycler_view)
        usersViewUsers.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                layoutManager.orientation
            )
        )
        usersViewUsers.adapter = recyclerAdapter
        usersViewUsers.layoutManager = layoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout_menu_item) {
            Log.i(TAG, "Logout")
            auth.signOut()
            val logoutIntent = Intent(this, LoginActivity::class.java)
            logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            updateUI(logoutIntent)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun updateUI(intent: Intent?) {
        if (intent == null) {
            Log.w(TAG, "Invalid navigation")
            return
        }

        startActivity(intent)
        finish()
    }
}