package com.example.layouttest

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.regex.Pattern

private fun Intent.putExtra(s: String, userPw: EditText) {

}


class SignUpActivityPage3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_page3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ///////////////////////////// < 가입 3/3 > ////////////////////////////////



        // [EditText] 레이아웃 받아와서 -> 인풋필터 적용
        userId = findViewById<EditText>(R.id.et_id_act3)
        userPw = findViewById<EditText>(R.id.et_pw_act3)
        userId.filters = arrayOf(filterId)
        userPw.filters = arrayOf(filterPw)

        // [클릭리스너] 취소
        val btnCancel: ImageView = findViewById<ImageView>(R.id.cancel)
        btnCancel.setOnClickListener {
            finish()
        }
        // [클릭리스너] 가입 완료
        btnFinish = findViewById<Button>(R.id.btn_finish)
        colorBtnFinish()
        btnFinish.setOnClickListener {
            if (userId.text.toString() == "" || userPw.text.toString() == "") {
                Toast.makeText(this, "정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //for문으로 아이디 중복 점검
            for (account in AccountManager.accounts) {
                if (account.id == userId.text.toString()) {
                    Toast.makeText(this, "이미 존재하는 계정입니다", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // 인스턴스에 모든 data를 수합한 다음, accounts 리스트에 추가
            var signupAccount: Account? = intent.getParcelableExtra<Account>("signupAccount")
            signupAccount?.id = userId.text.toString()
            signupAccount?.pw = userPw.text.toString()
            AccountManager.add(signupAccount!!)

            Toast.makeText(this, "가입이 완료되었습니다", Toast.LENGTH_SHORT).show()
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("signupId", userId.text.toString())
            intent.putExtra("signupPw", userPw.text.toString())
            startActivity(intent)
        }


    }

    // [input 필터]
    var filterId = InputFilter { source, start, end, dest, dstart, dend ->
        val kor = Pattern.compile("^[a-z0-9]+$")
        if (!kor.matcher(source).matches()) {
            ""
        } else source
    }
    var filterPw = InputFilter { source, start, end, dest, dstart, dend ->
        val kor = Pattern.compile("^[a-zA-Z0-9]+$")
        if (!kor.matcher(source).matches()) {
            ""
        } else source
    }



    fun colorBtnFinish(){
        // [layout] 로그인 버튼에 .setTint로 기본 컬러를 부여합니다
        colorDisabled = getColor(R.color.disabled_black)
        colorMain = getColor(R.color.mainColor) // onCreate 밖에서 변수 생성
        btnFinish.background.setTint(colorDisabled)

        progressBar(50)

        // [TextWatcher] 텍스트와쳐를 만듭니다
        val idPwTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((userId.text.isNotEmpty())&&(userPw.text.isNotEmpty())) {
                    btnFinish.background.setTint(colorMain)
                    progressBar(100)
                } else if ((userId.text.isNotEmpty())||(userPw.text.isNotEmpty())) {
                    btnFinish.background.setTint(colorDisabled)
                    progressBar(70)
                } else {
                    btnFinish.background.setTint(colorDisabled)
                    progressBar(50)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        // [TextChangedListener] 텍스트변경리스너를 추가합니다
        userId.addTextChangedListener(idPwTextWatcher)
        userPw.addTextChangedListener(idPwTextWatcher)
    }
    lateinit var btnFinish: Button
    lateinit var userId: EditText
    lateinit var userPw: EditText
    var colorDisabled:Int = 0
    var colorMain:Int = 0 // 사전 선언
    // [로그인 버튼 컬러]

    // [캐릭터 진행바 채우기 메서드]
    fun progressBar(_num:Int){
        var progressBar: LinearLayout = findViewById<LinearLayout>(R.id.progress_act3)
        var num: Int = _num
        progressBar.layoutParams.height = ( num * resources.displayMetrics.density ).toInt()
        progressBar.requestLayout()
    }
}
