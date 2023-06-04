package com.dongbin.popple.ui.register

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.dongbin.popple.R
import com.dongbin.popple.data.api.provideUserApi
import com.dongbin.popple.data.model.register.RequestRegisterDto
import com.dongbin.popple.databinding.ActivityRegisterBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    private var isDuplicatedId: Boolean = true  // 아이디 중복 시
    private var isDuplicatedNickname: Boolean = true    // 닉네임 중복 시
    private var isIncorrectPasswordPattern: Boolean = true  // 비밀번호 양식 불만족 시
    private var isIncorrectPwWithPwCheck: Boolean = true    // 비밀번호, 비밀번호 확인 불일치 시

    private var isSsoRegister: Boolean = false  // SSO를 통한 가입일 시

    // 아이디 & 비밀번호 유효성 검사 정규식
    private val regexForPassword =
        "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{9,16}$"   // 비밀번호 정규식
    private val patternForEmail: Pattern = Pattern.compile(regexForPassword)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel =
            ViewModelProvider(
                this,
                RegisterViewModelFactory(provideUserApi())
            )[RegisterViewModel::class.java]

        initView()

        registerViewModel.accountResponse.observe(this) {// 아이디 중복 확인
            // Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            isDuplicatedId = if (it == "available") {
                Toast.makeText(this, "가입된 아이디입니다.", Toast.LENGTH_SHORT).show()
                true
            } else {
                Toast.makeText(this, "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
                binding.btRegisterIdCheck.apply {
                    setBackgroundColor(getColor(R.color.blue))
                    text = "사용 가능"
                }
                false
            }
        }

        registerViewModel.nicknameResponse.observe(this) {// 닉네임 중복 확인
            // Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            isDuplicatedNickname =
                if (it == "available") {
                    Toast.makeText(this, "사용 중인 닉네임입니다.", Toast.LENGTH_SHORT).show()
                    true
                } else {
                    Toast.makeText(this, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show()
                    binding.btRegisterNicknameCheck.apply {
                        setBackgroundColor(getColor(R.color.blue))
                        text = "사용 가능"
                    }
                    false
                }
        }

        registerViewModel.registerResponse.observe(this) {
            if (it == binding.etRegisterId.text.toString()) {
                Toast.makeText(this, "$it 계정으로 가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() = with(binding) {
        // Toolbar
        setSupportActionBar(tbRegister)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }

        // 아이디 중복 확인 클릭 이벤트
        btRegisterIdCheck.setOnClickListener {
            registerViewModel.checkAccount(etRegisterId.text.toString())
        }

        // 닉네임 중복 확인 클릭 이벤트
        btRegisterNicknameCheck.setOnClickListener {
            registerViewModel.checkNickname(etRegisterNickname.text.toString())
        }

        // 생년월일 선택 클릭 이벤트
        etRegisterBirth.apply {
            isFocusable = false
            val today = GregorianCalendar()
            var tyear = today.get(Calendar.YEAR)
            var tmonth = today.get(Calendar.MONTH)
            var tdate = today.get(Calendar.DATE)

            setOnClickListener {
                DatePickerDialog(context, { _, year, month, dayOfMonth ->
                    setText("$year-${month + 1}-$dayOfMonth")
                    tyear = year
                    tmonth = month
                    tdate = dayOfMonth
                }, tyear, tmonth, tdate).show()
            }
        }

        // 가입 버튼 클릭 이벤트
        btRegisterNext.setOnClickListener {// 모든 내용 기입 시 회원가입 시도
            if (!isDuplicatedId && !isDuplicatedNickname && !isIncorrectPasswordPattern &&
                !isIncorrectPwWithPwCheck && etRegisterName.text.isNotBlank() &&
                etRegisterName.text.isNotBlank()
            ) {
                registerViewModel.register(
                    RequestRegisterDto(
                        account = etRegisterId.text.toString(),
                        password = etRegisterPw.text.toString(),
                        name = etRegisterName.text.toString(),
                        nickname = etRegisterNickname.text.toString(),
                        birth = etRegisterBirth.text.toString(),
                        login_type = "normal",
                        created_at = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS"))
                    )
                )
            } else {
                Toast.makeText(baseContext, "모든 정보를 기입해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        // ID Regex check
        etRegisterId.doOnTextChanged { t1, _, _, _ ->
            binding.btRegisterIdCheck.apply {
                setBackgroundColor(getColor(R.color.main))
                text = "중복 확인"
            }
            isDuplicatedId = true
            if (t1 != null) {
                val matcherForEmail: Matcher = Patterns.EMAIL_ADDRESS.matcher(t1)
                if (matcherForEmail.find()) {
                    binding.etRegisterId.setTextColor(getColor(R.color.blue))
                    binding.btRegisterIdCheck.isClickable = true
                } else {
                    binding.etRegisterId.setTextColor(getColor(R.color.red))
                    binding.btRegisterIdCheck.isClickable = false
                }
            }
        }

        // 비밀번호 정규식 표현 확인
        etRegisterPw.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                val matcherForPassword: Matcher = patternForEmail.matcher(text)
                isIncorrectPasswordPattern = if (matcherForPassword.find()) {
                    binding.etRegisterPw.setTextColor(getColor(R.color.blue))
                    false
                } else {
                    binding.etRegisterPw.setTextColor(getColor(R.color.red))
                    true
                }
            }
        }

        // 비밀번호, 비밀번호 확인 일치 확인
        etRegisterPwCheck.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                isIncorrectPwWithPwCheck =
                    if (text.toString() == binding.etRegisterPw.text.toString()) {
                        binding.etRegisterPwCheck.setTextColor(getColor(R.color.blue))
                        false
                    } else {
                        binding.etRegisterPwCheck.setTextColor((getColor(R.color.red)))
                        true
                    }
            }
        }

        // Nickname check
        etRegisterNickname.doOnTextChanged { _, _, _, _ ->
            binding.btRegisterNicknameCheck.apply {
                setBackgroundColor(getColor(R.color.main))
                text = "중복 확인"
            }
            isDuplicatedNickname = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}