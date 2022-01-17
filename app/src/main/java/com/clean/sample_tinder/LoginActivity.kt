package com.clean.sample_tinder

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.clean.sample_tinder.databinding.ActivityLoginBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var callback: CallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        auth = Firebase.auth
        callback = CallbackManager.Factory.create()

        initButton()
        initEditText()


    }
    private fun initButton() {
        binding.loginButton.setOnClickListener {
            var info = getInfo()

            auth.signInWithEmailAndPassword(info[0],info[1])
                .addOnCompleteListener(this) {
                    if(it.isSuccessful) {
                        handleSuccessLogin()
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
        binding.facebookLoginButton.setPermissions("email","public_profile")
        binding.facebookLoginButton.registerCallback(callback,object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                // 로그인 성공적
                var credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this@LoginActivity) {
                        if(it.isSuccessful) {
                            handleSuccessLogin()
                        } else {
                            Toast.makeText(this@LoginActivity,"패이스북로긴실패!!",Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            override fun onCancel() {
                // 로그인하다가 취소
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(this@LoginActivity,"패이스북로긴실패!!",Toast.LENGTH_SHORT).show()
            }

        })
        binding.facebookLoginButton.setOnClickListener {

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callback.onActivityResult(requestCode,resultCode, data)
    }

    private fun handleSuccessLogin() {
        if(auth.currentUser == null) {
            Toast.makeText(this,"로그인 실패",Toast.LENGTH_SHORT).show()
            return
        }

        val userId = auth.currentUser?.uid.orEmpty()
        val currentUserDB = Firebase.database.reference.child("Users").child(userId)
        val user = mutableMapOf<String, Any>()

        user["userId"] = userId
        currentUserDB.updateChildren(user)
        finish()
    }
}