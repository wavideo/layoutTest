package com.example.layouttest

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.regex.Pattern
import kotlin.random.Random

class HomeActivity : AppCompatActivity() {
    private fun Intent(actionView: String, parse: Uri?, i: Int) {
    }

    // [onCreate] 시작
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ///////////////////////////// < 가입 1/3 > ////////////////////////////////



        //getExtra로 아이디를 받아옵니다. 아이디를 find로 대조해 myAccount를 선언합니다.
        val myAccount: Account? = intent.getParcelableExtra("myAccount")
        val myId : String = myAccount?.id.toString()
        var tvId : TextView = findViewById<TextView>(R.id.home_id)
        tvId.setText(myId.toString())

        // [이름]
        val myName: String = myAccount?.name.toString() // myAccount에서 이름을 얻어내고
        var etName: TextView = findViewById<TextView>(R.id.home_name)
        etName.setText(myName.toString()) //layout의 EditText에 대입합니다

        // [MBTI]
        var myMbti: String = myAccount?.mbti.toString()
        var etMbti: EditText = findViewById<EditText>(R.id.home_mbti)
        if (myMbti != "") {
            etMbti.setText(myMbti.toString())
        } // 기존 MBTI값이 있으면 화면에 노출합니다
        etMbti.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS // 대문자 키보드
        etMbti.filters = arrayOf(filterEngCap, InputFilter.LengthFilter(4)) // 대문자+4글자 필터

        // [팀] 데이터를 문자화합니다
        val myTeam = when (myAccount?.team) {
            0 -> "개발"
            else -> "창업"
        }
        val myTeamNum: Int = (myAccount?.teamNum?.toInt())!! + 1
        val myTeamString: String = "${myTeam} ${myTeamNum}조"

        var etTeam: TextView = findViewById<TextView>(R.id.home_team)
        etTeam.setText(myTeamString.toString())

        ///////////////////////////////////////////////////////////////



        // [클릭 리스너] 취소
        val btnCancel: ImageView = findViewById<ImageView>(R.id.cancel)
        btnCancel.setOnClickListener {
            finish()
        }

        // [클릭 리스너] 닫기
        val btnClose: Button = findViewById<Button>(R.id.btn_close_home)
        btnClose.setOnClickListener {
            if (etMbti.text.toString() != myMbti) {
                if (etMbti.length() == 4) {
                        (AccountManager.find(myAccount.id))!!.mbti =
                        etMbti.text.toString() // 새로운 MBTI 값이 있으면 업데이트합니다
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent) // Main 액티비티로 전환
                } else {
                    Toast.makeText(this,"정확한 MBTI를 입력해주세요",Toast.LENGTH_SHORT).show()
                }
            } else {
                finish()
            }
        }

        // [텍스트 와쳐]
        var colorDisable:Int = getColor(R.color.disabled_black)
        var colorMain:Int = getColor(R.color.mainColor)

        var mbtiWatcher = object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (etMbti.text.toString() != myMbti) {
                    btnClose.setText("저장하기")
                    btnClose.background.setTint(colorMain)
                } else {
                    btnClose.setText("로그아웃")
                    btnClose.background.setTint(colorDisable)
                }
            }
        }

        etMbti.addTextChangedListener(mbtiWatcher)






        // 노션 링크 버튼의 이름
        val btnLinkText: TextView = findViewById<TextView>(R.id.btn_link_text)
        btnLinkText.setText("${myTeamString} Notion")
        // [클릭 리스너 : 노션링크] 시작
        val btnLink: LinearLayout = findViewById<LinearLayout>(R.id.btn_link)
        btnLink.setOnClickListener {

            var notionLink:String = ""
            if (myTeam == "개발") {
                notionLink = when (myTeamNum) {
                    1 -> "https://teamsparta.notion.site/1-cb8551f16716420693b1e37d817b2695?pvs=25"
                    2 -> "https://teamsparta.notion.site/2-21b6a808efc74ae28729af18bd198127?pvs=25"
                    3 -> "https://teamsparta.notion.site/3-8d485fbe4a70473a8be9d258319af4d3?pvs=25"
                    4 -> "https://teamsparta.notion.site/e15558e4663e41bba1eb3da24beb3f8b?pvs=25"
                    5 -> "https://teamsparta.notion.site/5-6c423b16c6db43638344e182c4d00893?pvs=25"
                    6 -> "https://teamsparta.notion.site/3b5bb02fbee9471790cb8601b3f51998?pvs=25"
                    else -> ""
                }
            } else if (myTeam == "창업") {
                notionLink = when (myTeamNum) {
                    1 -> "https://teamsparta.notion.site/1-6f9264e7e60a4c6bb04b140140bd51d6?pvs=25"
                    2 -> "https://teamsparta.notion.site/2-b38058c2b6654b7094a5f3eeb2f2f388?pvs=25"
                    else -> ""
                }
            }
            // if로 결정된 노션링크로, 웹브라우저를 intent합니다
            var intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(notionLink)
            )
            startActivity(intent)
        }
        // [클릭 리스너 : 노션링크] 끝



        // [ 캐릭터 랜덤 기믹 ]
        val ivCharacter: ImageView = findViewById(R.id.iv_character)
        fun randomImage() {
            var num = Random.nextInt(6)
            ivCharacter.setImageResource(
                when (num) {
                    0 -> R.drawable.my_character
                    1 -> R.drawable.my_character_01
                    2 -> R.drawable.my_character_02
                    3 -> R.drawable.my_character_03
                    4 -> R.drawable.my_character_04
                    else -> R.drawable.my_character_05
                }
            )
        }
        //접속하면 랜덤
        randomImage()
        //클릭하면 랜덤
        ivCharacter.setOnClickListener{
            randomImage()
        }



    }
    // [onCreate] 끝

    ///////////////////////////////////////////////////////////



    // [인풋 필터] 대문자만
    var filterEngCap = InputFilter { source, start, end, dest, dstart, dend ->
        val kor = Pattern.compile("^[A-Z]+$")
        if (!kor.matcher(source).matches()) {
            ""
        } else source
    }
}