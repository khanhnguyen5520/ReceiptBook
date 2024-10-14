package com.example.receiptbook

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.receiptbook.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.fab
        binding.bottomNavigationView
//        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar)
         if (savedInstanceState == null){
             supportFragmentManager.beginTransaction().replace(R.id.fragment_layout, BlankFragment1()).commit()
         }

        replaceFragment(BlankFragment1())

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(BlankFragment1())
                R.id.shorts -> replaceFragment(BlankFragment2())
                R.id.subscriptions -> replaceFragment(BlankFragment3())
                R.id.library -> replaceFragment(BlankFragment4())
            }
            true
        }

        binding.fab.setOnClickListener { showBottomDialog() }
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun showBottomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheetlayout)

        val videoLayout = dialog.findViewById<LinearLayout>(R.id.layoutVideo)
        val shortsLayout = dialog.findViewById<LinearLayout>(R.id.layoutShorts)
        val liveLayout = dialog.findViewById<LinearLayout>(R.id.layoutLive)
        val cancelButton = dialog.findViewById<ImageView>(R.id.cancelButton)

        videoLayout.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(
                this@MainActivity,
                "Upload a Video is clicked",
                Toast.LENGTH_SHORT
            ).show()
        }

        shortsLayout.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(
                this@MainActivity,
                "Create a short is Clicked",
                Toast.LENGTH_SHORT
            ).show()
        }

        liveLayout.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(
                this@MainActivity,
                "Go live is Clicked",
                Toast.LENGTH_SHORT
            ).show()
        }

        cancelButton.setOnClickListener { dialog.dismiss() }

        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }
}