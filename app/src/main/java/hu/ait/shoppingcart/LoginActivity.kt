package hu.ait.shoppingcart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import hu.ait.shoppingcart.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun loginClick(v: View) {
        if (!isFormValid()){
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            binding.etEmail.text.toString(), binding.etPassword.text.toString()
        ).addOnSuccessListener {
            Toast.makeText(this@LoginActivity,
                "Login OK",
                Toast.LENGTH_LONG).show()

            // navigate to other Activity
            startActivity(Intent(this, GroupsActivity::class.java))
            finish()

        }.addOnFailureListener{
            Toast.makeText(this@LoginActivity,
                "Error: ${it.message}",
                Toast.LENGTH_LONG).show()
        }

    }

    fun registerClick(v: View){
        if (!isFormValid()){
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            binding.etEmail.text.toString(), binding.etPassword.text.toString()
        ).addOnSuccessListener {
            Toast.makeText(this@LoginActivity,
                "Registration OK",
                Toast.LENGTH_LONG).show()
        }.addOnFailureListener{
            Toast.makeText(this@LoginActivity,
                "Error: ${it.message}",
                Toast.LENGTH_LONG).show()
        }
    }

    fun isFormValid(): Boolean {
        return when {
            binding.etEmail.text.isEmpty() -> {
                binding.etEmail.error = "This field can not be empty"
                false
            }
            binding.etPassword.text.isEmpty() -> {
                binding.etPassword.error = "The password can not be empty"
                false
            }
            else -> true
        }
    }
}