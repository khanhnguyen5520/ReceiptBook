package com.example.receiptbook.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.receiptbook.R
import com.example.receiptbook.databinding.FragmentSettingBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    private val signInRequestCode = 1001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.lbShare.setOnClickListener {
            shareApp()
        }

        binding.lbFeedback.setOnClickListener {
            showFeedbackDialog()
        }
        binding.lbAccount.setOnClickListener {
            signIn()
        }
        return binding.root
    }

    private fun signIn() {
        val signInDialog = Dialog(requireContext())
        signInDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        signInDialog.setContentView(R.layout.bottom_sign_in)
        signInDialog.show()
        signInDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        signInDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        signInDialog.window!!.setGravity(Gravity.BOTTOM)

        signInDialog.findViewById<Button>(R.id.btnSignInByGoogle).setOnClickListener {
            signInWithGoogle()
        }

        signInDialog.findViewById<TextView>(R.id.btnCancelSignIn).setOnClickListener {
            signInDialog.dismiss()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, signInRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == signInRequestCode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            Toast.makeText(requireContext(), "Signed in as: ${account.email}", Toast.LENGTH_SHORT).show()
        } catch (e: ApiException) {
            Toast.makeText(requireContext(), "Sign in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareApp() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Tôi đã tìm thấy một ứng dụng thu chi đầy màu sắc đơn giản tuyệt vời:${requireContext().getAppName()}. nó cho phép tạo ghi chú văn bản và ảnh, Hãy thử ngay bây giờ:"
            )
            type = "text/plain"
        }

        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun showFeedbackDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_feedback, null)
        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val stars = listOf(
            dialogView.findViewById(R.id.start1),
            dialogView.findViewById(R.id.start2),
            dialogView.findViewById(R.id.start3),
            dialogView.findViewById(R.id.start4),
            dialogView.findViewById<ImageView>(R.id.start5)
        )
        val editText = dialogView.findViewById<EditText>(R.id.edtFeedBack)
        val line = dialogView.findViewById<View>(R.id.lineFb)
        val btnSubmit = dialogView.findViewById<TextView>(R.id.btnSubmitFeedBack)

        stars.forEachIndexed { index, star ->
            star.setOnClickListener {
                stars.forEach { it.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_unselected)) }

                for (i in 0..index) {
                    stars[i].setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_selected))
                }

                if (index == 4) {
                    val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${requireContext().packageName}"))
                    startActivity(playStoreIntent)
                    dialog.dismiss()
                } else {
                    line.visibility = View.VISIBLE
                    editText.visibility = View.VISIBLE
                    btnSubmit.visibility = View.VISIBLE
                }
            }
        }

        dialogView.findViewById<TextView>(R.id.btnCancelFeedBack).setOnClickListener {
            dialog.dismiss()
        }

        btnSubmit.setOnClickListener {
            val feedback = editText.text.toString()
            if (feedback.isNotEmpty()) {
                Toast.makeText(requireContext(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please write your feedback before submitting.", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun Context.getAppName(): String {
        val applicationInfo = applicationInfo
        val packageManager = packageManager
        return applicationInfo.loadLabel(packageManager).toString()
    }
}