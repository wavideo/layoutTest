package com.example.layouttest

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.regex.Pattern

private fun Intent.putExtra(s: String, signupAccount: Account) {
}



class SignUpActivityPage2 : AppCompatActivity() {

    // [onCreate] 시작
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup_page2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ///////////////////////////// < 가입 2/3 > ////////////////////////////////



        // [EditText]
        etName = findViewById<EditText>(R.id.et_name)
        etName.filters = arrayOf(filterKor) // input 필터 : 한국어

        // [textPicker] 넘버피커를 텍스트피커로 마개조
        val npTeam: NumberPicker = findViewById<NumberPicker>(R.id.np_team01)
        val teamValue = arrayOf("개발", "창업")
        npTeam.displayedValues = teamValue // 디스플레이 값에 텍스트 배열 할당하기
        npTeam.minValue = 0
        npTeam.maxValue = teamValue.size - 1 // 피커 선택지 갯수
        npTeam.wrapSelectorWheel = true

        val npTeamNumber: NumberPicker = findViewById<NumberPicker>(R.id.np_team02)
        var teamNumber = arrayOf("1조", "2조", "3조", "4조", "5조", "6조")
        npTeamNumber.displayedValues = teamNumber
        npTeamNumber.minValue = 0
        npTeamNumber.maxValue = teamNumber.size - 1
        npTeamNumber.wrapSelectorWheel = true

        // [값변 리스너] A 선택지에 따라 B 선택지 다르게 노출
        npTeam.setOnValueChangedListener { picker, oldVal, newVal ->
            if (newVal == 1) {
                teamNumber = arrayOf("1조", "2조")
                npTeamNumber.minValue = 0
                npTeamNumber.maxValue = teamNumber.size - 1
            }
            if (newVal == 0) {
                teamNumber = arrayOf("1조", "2조", "3조", "4조", "5조", "6조")
                npTeamNumber.minValue = 0
                npTeamNumber.maxValue = teamNumber.size - 1
            }
        }



        // [클릭 리스너] 취소
        val btnCancel: ImageView = findViewById<ImageView>(R.id.cancel)
        btnCancel.setOnClickListener {
            finish()
        }
        // [클릭 리스너] 다음
        btnNext = findViewById<Button>(R.id.btn_next)
        colorBtnNext()
        btnNext.setOnClickListener {

            // 비어있나 체크
            if (etName.text.toString() == "") {
                Toast.makeText(this, "정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 가입용 인스턴스에 필요한 data를 담습니다
            var signupAccount : Account = Account("","")
            signupAccount.name = etName.text.toString()
            signupAccount.team = npTeam.value
            signupAccount.teamNum = npTeamNumber.value

            // 인스턴스 하나만 intent에 담아 넘깁니다
            val intent = Intent(this, SignUpActivityPage3::class.java)
            intent.putExtra("signupAccount", signupAccount)
            startActivity(intent)
        }
        // [클릭 리스너] 다음






    } // [onCreate] 끝



    //////////////////////// < 메서드 > /////////////////////////

    // [inputFilter] 한국어 키보드
    var filterKor = InputFilter { source, start, end, dest, dstart, dend ->
        val kor = Pattern.compile("^[ㄱ-ㅣ가-힣]+$")
        if (!kor.matcher(source).matches()) {
            ""
        } else source
    }



    // [로그인 버튼 컬러]
    fun colorBtnNext(){
        // [layout] 로그인 버튼에 .setTint로 기본 컬러를 부여합니다
        colorDisabled = getColor(R.color.disabled_black)
        colorMain = getColor(R.color.mainColor) // onCreate 밖에서 변수 생성
        btnNext.background.setTint(colorDisabled)

        progressBar(10) // 진행바

        // [TextWatcher] 텍스트와쳐를 만듭니다
        val nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (etName.text.isNotEmpty()) {
                    btnNext.background.setTint(colorMain)
                    progressBar(30)
                } else {
                    btnNext.background.setTint(colorDisabled)
                    progressBar(10)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        // [TextChangedListener] 텍스트변경리스너를 추가합니다
        etName.addTextChangedListener(nameTextWatcher)
    }
    lateinit var btnNext: Button
    lateinit var etName: EditText
    var colorDisabled:Int = 0
    var colorMain:Int = 0 // 사전 선언
    // [로그인 버튼 컬러]

    // [캐릭터 진행바 채우기 메서드]
    fun progressBar(_num:Int){
        var progressBar: LinearLayout = findViewById<LinearLayout>(R.id.progress_act2)
        var num: Int = _num
        progressBar.layoutParams.height = ( num * resources.displayMetrics.density ).toInt()
        progressBar.requestLayout()
    }

}