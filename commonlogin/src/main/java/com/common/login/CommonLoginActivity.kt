package com.common.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.common.login.databinding.ActivityCommonLoginBinding

class CommonLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommonLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommonLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applyConfig()
        setupListeners()
    }

    private fun applyConfig() {
        val logoResId = intent.getIntExtra(EXTRA_LOGO_RES_ID, 0)
        val title = intent.getStringExtra(EXTRA_TITLE_TEXT)
        val subtitle = intent.getStringExtra(EXTRA_SUBTITLE_TEXT)
        val loginButtonText = intent.getStringExtra(EXTRA_LOGIN_BUTTON_TEXT)

        val showRememberMe =
            intent.getBooleanExtra(EXTRA_SHOW_REMEMBER_ME, true)
        val showForgotPassword =
            intent.getBooleanExtra(EXTRA_SHOW_FORGOT_PASSWORD, true)

        val enableEmail =
            intent.getBooleanExtra(EXTRA_ENABLE_EMAIL, true)
        val enablePhone =
            intent.getBooleanExtra(EXTRA_ENABLE_PHONE, true)

        if (logoResId != 0) {
            binding.imageLogo.setImageResource(logoResId)
        }

        binding.textTitle.text = title ?: getString(R.string.common_login_title)
        binding.textSubtitle.text =
            subtitle ?: getString(R.string.common_login_subtitle)
        binding.btnLogin.text =
            loginButtonText ?: getString(R.string.common_login_button_text)

        binding.cbRememberMe.isVisible = showRememberMe
        binding.tvForgotPassword.isVisible = showForgotPassword

        binding.tilEmail.isVisible = enableEmail
        binding.tilPhone.isVisible = enablePhone
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            if (validateInputs()) {
                val email = binding.etEmail.text?.toString()?.trim().orEmpty()
                val phone = binding.etPhone.text?.toString()?.trim().orEmpty()
                val password = binding.etPassword.text?.toString().orEmpty()
                val rememberMe = binding.cbRememberMe.isChecked

                val data = Intent().apply {
                    putExtra(RESULT_EMAIL, email)
                    putExtra(RESULT_PHONE, phone)
                    putExtra(RESULT_PASSWORD, password)
                    putExtra(RESULT_REMEMBER_ME, rememberMe)
                }
                setResult(Activity.RESULT_OK, data)
                finish()
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            handleForgotPassword()
        }
    }

    private fun validateInputs(): Boolean {
        var valid = true

        val emailEnabled = binding.tilEmail.isVisible
        val phoneEnabled = binding.tilPhone.isVisible

        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val phone = binding.etPhone.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString().orEmpty()

        // Clear old errors
        binding.tilEmail.error = null
        binding.tilPhone.error = null
        binding.tilPassword.error = null

        // At least one of email/phone must be entered (if visible)
        if (emailEnabled && phoneEnabled) {
            if (email.isEmpty() && phone.isEmpty()) {
                val msg = getString(R.string.common_login_error_contact_required)
                binding.tilEmail.error = msg
                binding.tilPhone.error = msg
                valid = false
            }
        } else if (emailEnabled) {
            if (email.isEmpty()) {
                binding.tilEmail.error =
                    getString(R.string.common_login_error_contact_required)
                valid = false
            }
        } else if (phoneEnabled) {
            if (phone.isEmpty()) {
                binding.tilPhone.error =
                    getString(R.string.common_login_error_contact_required)
                valid = false
            }
        }

        // Email format check (if not empty)
        if (emailEnabled && email.isNotEmpty()) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.error =
                    getString(R.string.common_login_error_email_invalid)
                valid = false
            }
        }

        // Phone format check (simple)
        if (phoneEnabled && phone.isNotEmpty()) {
            if (!Patterns.PHONE.matcher(phone).matches() || phone.length < 7) {
                binding.tilPhone.error =
                    getString(R.string.common_login_error_phone_invalid)
                valid = false
            }
        }

        val minPasswordLength = intent.getIntExtra(
            EXTRA_MIN_PASSWORD_LENGTH,
            DEFAULT_MIN_PASSWORD_LENGTH
        )

        if (password.isEmpty()) {
            binding.tilPassword.error =
                getString(R.string.common_login_error_password_required)
            valid = false
        } else if (password.length < minPasswordLength) {
            binding.tilPassword.error = getString(
                R.string.common_login_error_password_too_short,
                minPasswordLength
            )
            valid = false
        }

        return valid
    }

    private fun handleForgotPassword() {
        val emailEnabled = binding.tilEmail.isVisible
        val phoneEnabled = binding.tilPhone.isVisible

        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val phone = binding.etPhone.text?.toString()?.trim().orEmpty()

        binding.tilEmail.error = null
        binding.tilPhone.error = null

        var contactValue: String? = null
        var contactType: String? = null
        var valid = true

        if (emailEnabled && email.isNotEmpty()) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.error =
                    getString(R.string.common_login_error_email_invalid)
                valid = false
            } else {
                contactValue = email
                contactType = CONTACT_TYPE_EMAIL
            }
        } else if (phoneEnabled && phone.isNotEmpty()) {
            if (!Patterns.PHONE.matcher(phone).matches() || phone.length < 7) {
                binding.tilPhone.error =
                    getString(R.string.common_login_error_phone_invalid)
                valid = false
            } else {
                contactValue = phone
                contactType = CONTACT_TYPE_PHONE
            }
        } else {
            // nothing entered
            val msg = getString(R.string.common_login_error_contact_required)
            if (emailEnabled) binding.tilEmail.error = msg
            if (phoneEnabled) binding.tilPhone.error = msg
            valid = false
        }

        if (!valid || contactValue == null || contactType == null) return

        val data = Intent().apply {
            putExtra(RESULT_CONTACT_VALUE, contactValue)
            putExtra(RESULT_CONTACT_TYPE, contactType)
        }
        setResult(RESULT_FORGOT_PASSWORD, data)
        finish()
    }

    companion object {
        private const val DEFAULT_MIN_PASSWORD_LENGTH = 6

        // Config extras
        const val EXTRA_LOGO_RES_ID = "extra_logo_res_id"
        const val EXTRA_TITLE_TEXT = "extra_title_text"
        const val EXTRA_SUBTITLE_TEXT = "extra_subtitle_text"
        const val EXTRA_LOGIN_BUTTON_TEXT = "extra_login_button_text"
        const val EXTRA_SHOW_REMEMBER_ME = "extra_show_remember_me"
        const val EXTRA_SHOW_FORGOT_PASSWORD = "extra_show_forgot_password"
        const val EXTRA_MIN_PASSWORD_LENGTH = "extra_min_password_length"
        const val EXTRA_ENABLE_EMAIL = "extra_enable_email"
        const val EXTRA_ENABLE_PHONE = "extra_enable_phone"

        // Result extras
        const val RESULT_EMAIL = "result_email"
        const val RESULT_PHONE = "result_phone"
        const val RESULT_PASSWORD = "result_password"
        const val RESULT_REMEMBER_ME = "result_remember_me"

        // Forgot password result
        const val RESULT_CONTACT_VALUE = "result_contact_value"
        const val RESULT_CONTACT_TYPE = "result_contact_type"
        const val CONTACT_TYPE_EMAIL = "email"
        const val CONTACT_TYPE_PHONE = "phone"

        const val RESULT_FORGOT_PASSWORD: Int = Activity.RESULT_FIRST_USER + 100

        /**
         * Helper to create configured Intent for any app.
         */
        fun createIntent(
            context: Context,
            logoResId: Int? = null,
            titleText: String? = null,
            subtitleText: String? = null,
            loginButtonText: String? = null,
            showRememberMe: Boolean = true,
            showForgotPassword: Boolean = true,
            minPasswordLength: Int = DEFAULT_MIN_PASSWORD_LENGTH,
            enableEmail: Boolean = true,
            enablePhone: Boolean = true
        ): Intent {
            return Intent(context, CommonLoginActivity::class.java).apply {
                logoResId?.let { putExtra(EXTRA_LOGO_RES_ID, it) }
                titleText?.let { putExtra(EXTRA_TITLE_TEXT, it) }
                subtitleText?.let { putExtra(EXTRA_SUBTITLE_TEXT, it) }
                loginButtonText?.let { putExtra(EXTRA_LOGIN_BUTTON_TEXT, it) }
                putExtra(EXTRA_SHOW_REMEMBER_ME, showRememberMe)
                putExtra(EXTRA_SHOW_FORGOT_PASSWORD, showForgotPassword)
                putExtra(EXTRA_MIN_PASSWORD_LENGTH, minPasswordLength)
                putExtra(EXTRA_ENABLE_EMAIL, enableEmail)
                putExtra(EXTRA_ENABLE_PHONE, enablePhone)
            }
        }
    }
}
