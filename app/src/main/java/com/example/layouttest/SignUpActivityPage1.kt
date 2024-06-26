package com.example.layouttest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignUpActivityPage1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_page1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ///////////////////////////// < 가입 1/3 > ////////////////////////////////



        // [layout] 버튼 불러오기
        val btnYes: Button = findViewById<Button>(R.id.btn_yes)
        val btnNo: Button = findViewById<Button>(R.id.btn_no)
        val btnCancel: ImageView = findViewById<ImageView>(R.id.cancel)

        // [클릭리스너]
        btnYes.setOnClickListener{
            val intent = Intent(this, SignUpActivityPage2::class.java)
            startActivity(intent)
        }
        btnNo.setOnClickListener{
            Toast.makeText(this,"Android 4기만 가입할 수 있습니다", Toast.LENGTH_SHORT).show()
            finish()
        }
        btnCancel.setOnClickListener{
            finish()



        }
    }
}