package com.example.morningfirebasedbapp

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UsersActivity : AppCompatActivity() {
    lateinit var listUsers:ListView
    lateinit var adapter:CustomAdapter
    lateinit var progressDialogue:ProgressDialog
    lateinit var users:ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        listUsers = findViewById(R.id.mListUsers)
        progressDialogue = ProgressDialog(this)
        progressDialogue.setTitle("Loading")
        progressDialogue.setMessage("Please wait....")
        users = ArrayList()
        adapter = CustomAdapter(this,users)
        var ref = FirebaseDatabase.getInstance().getReference().child("Users")
        // start loading the users from the ref
        progressDialogue.show()
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                for (data in snapshot.children){
                    var user = data.getValue(User::class.java)
                    users.add(user!!)
                }
                adapter.notifyDataSetChanged()
                progressDialogue.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UsersActivity,"Sorry, DATABASE is inaccessible",Toast.LENGTH_LONG).show()
            }
        })
        // Tell the listview to use the adapter
        listUsers.adapter = adapter
    }
}