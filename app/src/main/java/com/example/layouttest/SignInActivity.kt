package com.example.layouttest

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    // [init] 테스트용 계정을 불러옵니다
    init {
        AccountManager.getTest()
    }

    /* [class 단위의 변수 선언]
    레이아웃이 생기기 전이지만, onStop과 onCreate 양쪽에서 사용되는 변수라서
    lateinit으로 먼저 선언해두었습니다! */
    private val accounts = AccountManager.accounts
    private lateinit var etId: EditText
    private lateinit var etPw: EditText
    private lateinit var btnSignIn: Button
    private lateinit var btnSignUp: Button

    var colorMain = 0
    var colorRed = 0
    var colorBlack = 0
    var colorDisabled = 0



    // [onCreate] 시작
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        ///////////////////////////// < MAIN > ////////////////////////////////
        // [layout] 레이아웃의 위젯을 불러옵니다
        etId = findViewById<EditText>(R.id.et_id)
        etPw = findViewById<EditText>(R.id.et_pw)
        btnSignIn = findViewById<Button>(R.id.btn_signin)
        btnSignUp = findViewById<Button>(R.id.btn_signup)
        // .filters로 EditText의 inputType을 제한했습니다
        etId.filters = arrayOf(filterId)
        etPw.filters = arrayOf(filterPw)
        // [getColor] ContextCompat.getColor로 R.color를 불러옵니다
        colorMain = getColor(R.color.mainColor)
        colorRed = getColor(R.color.red)
        colorBlack = getColor(R.color.black)
        colorDisabled = getColor(R.color.disabled_black)

        // 로그인 버튼 컬러, 이스터 에그 메서드
        colorBtnSignIn()
        easterEggLogo()

        // SignUpActivity에서 만든 Id,Pw를 받아옵니다. 받아온 값이 있다면, 빈칸에 입력합니다
        val signupId:String = intent.getStringExtra("signupId").toString()
        val signupPw:String = intent.getStringExtra("signupPw").toString()
        if (signupId!="null") {
            etId.setText(signupId)
            etPw.setText(signupPw)
        }



        // [가입 클릭리스너]
        btnSignUp.setOnClickListener {
            var intent = Intent(this, SignUpActivityPage1::class.java)
            startActivity(intent)
        }

        // [로그인 클릭리스너]
        btnSignIn.setOnClickListener {
            // 유저가 레이아웃 위젯에 입력한 값을 가져와서 -> 로그인 확인용 인스턴스 생성
            val inputId = etId.text.toString()
            val inputPw = etPw.text.toString()
            var myAccount = AccountManager.find(inputId)

            // 비어있으면 토스트 발생
            if (inputId == "") {
                Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (inputPw == "") {
                Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 로그인용 인스턴스가 계정목록에 있으면, myId와 함께 액티비티 전환
            if (myAccount?.pw == inputPw) {
                Toast.makeText(this, "로그인에 성공했습니다", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)

                intent.putExtra("myAccount", myAccount)
                startActivity(intent)
            } else {
                Toast.makeText(this, "아이디와 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            }
        }
        // [로그인 클릭리스너]



    } // [onCreate] 끝



    //////////////////////////////////////////////////////////////////////////////////
    /*                                  메서드 블록                                    */
    //////////////////////////////////////////////////////////////////////////////////

    // [로그인 버튼 컬러]
    fun colorBtnSignIn(){
        // [layout] 로그인 버튼에 .setTint로 기본 컬러를 부여합니다
        btnSignIn.background.setTint(colorDisabled)

        // [TextWatcher] 텍스트와쳐를 만듭니다
        val idPwTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((etId.text.isNotEmpty()) && (etPw.text.isNotEmpty())) {
                    btnSignIn.background.setTint(colorMain)
                } else {
                    btnSignIn.background.setTint(colorDisabled)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        // [TextChangedListener] 텍스트변경리스너를 추가합니다
        etId.addTextChangedListener(idPwTextWatcher)
        etPw.addTextChangedListener(idPwTextWatcher)
    }
    // [로그인 버튼 컬러]



    // [로고 클릭 이스터에그]
    fun easterEggLogo() {
        var applogoCounter: Int = 0
        val applogo: ImageView = findViewById<ImageView>(R.id.iv_title) // 앱로고
        val apptitle: TextView = findViewById<TextView>(R.id.tv_title)
        val background: ConstraintLayout = findViewById(R.id.main) // 배경

        // 클릭 리스너 - 시작
        applogo.setOnClickListener {
            ++applogoCounter
            if (applogoCounter > 10) {
                Toast.makeText(this, "이스터에그가 발동합니다", Toast.LENGTH_SHORT).show()

                colorMain = getColor(R.color.red)
                // 메인컬러 일일이 집어넣기
                applogo.imageTintList = ColorStateList.valueOf(colorRed)
                apptitle.setTextColor(colorRed)
                btnSignUp.setTextColor(colorRed)
                background.setBackgroundColor(colorBlack)

                if ((etId.text.isNotEmpty()) && (etPw.text.isNotEmpty())) {
                    btnSignIn.background.setTint(colorRed)
                } else {
                    btnSignIn.background.setTint(colorDisabled)
                } // 로그인 버튼 컬러 if로 확인

            }
        } // 클릭 리스너 - 끝

    }
    // [로고 클릭 이스터 에그]



    // [inputType 필터] EditText에 쓸 InputFilter입니다
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



    // [onStop] 다음 액티비티로 넘어가면 -> 입력해 둔 IdPw 값과 check용 변수를 처음 상태로 돌립니다
    override fun onStop() {
        super.onStop()
        etId.setText("")
        etPw.setText("")
    }

}