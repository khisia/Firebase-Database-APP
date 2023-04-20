package com.example.morningfirebasedbapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class UserUpdateActivity : AppCompatActivity() {
    lateinit var edtName: EditText
    lateinit var edtEmail: EditText
    lateinit var edtIdNumber: EditText
    lateinit var progressDialog: ProgressDialog
    lateinit var btnUpdate:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_update)
        edtName = findViewById(R.id.mEdtName)
        edtEmail = findViewById(R.id.mEdtEmail)
        edtIdNumber = findViewById(R.id.mEdtIdNumber)
        btnUpdate = findViewById(R.id.mBtnUserUpdate)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Updating")
        progressDialog.setMessage("Please wait...")
        var receivedName = intent.getStringExtra("name")
        var receivedEmail = intent.getStringExtra("email")
        var receivedIdNumber = intent.getStringExtra("idNumber")
        var receivedId = intent.getStringExtra("id")
        // Set the received data to the EditText
        edtName.setText(receivedName)
        edtEmail.setText(receivedEmail)
        edtIdNumber.setText(receivedIdNumber)
        //  Set onclickListener on the button update
        btnUpdate.setOnClickListener {
            var name = edtName.text.toString().trim()
            var email = edtEmail.text.toString().trim()
            var idNumber = edtIdNumber.text.toString().trim()
            var id = receivedId
            if (name.isEmpty()){
                edtName.setError("Please fill this field")
                edtEmail.requestFocus()
            }else if (idNumber.isEmpty()){
                edtIdNumber.setError("Please fill this field")
                edtIdNumber.requestFocus()
            }else{
                // proceed to save
                var user = User(name, email, idNumber, id!!)
                // Create a reference in the firebase database
                var ref = FirebaseDatabase.getInstance().
                getReference().child("Users/"+id)
                // Show the progress to start saving
                progressDialog.dismiss()
                ref.setValue(user).addOnCompleteListener{
                    // Dismiss the progress and check if the task was successfully
                        task ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Users updated successfully", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@UserUpdateActivity,UsersActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this,"User saving failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}