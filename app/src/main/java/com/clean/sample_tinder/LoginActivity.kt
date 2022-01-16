package com.clean.sample_tinder

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.clean.sample_tinder.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        auth = Firebase.auth
        initButton()
        initEditText()

    }
    private fun initButton() {
        binding.loginButton.setOnClickListener {
            var info = getInfo()

            auth.signInWithEmailAndPassword(info[0],info[1])
                .addOnCompleteListener(this) {
                    if(it.isSuccessful) {
                        finish()
                    } else {
                        Toast.makeText(this,"로그인 오류!!",Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.signUpButton.setOnClickListener {
            var info = getInfo()
            auth.createUserWithEmailAndPassword(info[0],info[1])
                .addOnCompleteListener(this) {
                    if(it.isSuccessful) {
                        Toast.makeText(this,"회원가입 성공",Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("login",it.exception.toString())
                        Toast.makeText(this,"회원가입 실패",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun initEditText() {
        binding.emailEditText.addTextChangedListener {
            val enable = binding.emailEditText.text.isNotEmpty() && binding.password1EditText.text.isNotEmpty()
            binding.loginButton.isEnabled = enable
            binding.signUpButton.isEnabled = enable
        }
        binding.password1EditText.addTextChangedListener {
            val enable = binding.emailEditText.text.isNotEmpty() && binding.password1EditText.text.isNotEmpty()
            binding.loginButton.isEnabled = enable
            binding.signUpButton.isEnabled = enable
        }
    }

    private fun getInfo(): Array<String> {
        val email = binding.emailEditText.text.toString()
        val password = binding.password1EditText.text.toString()
        return arrayOf(email,password)
    }
}