package com.sun.unsplash_01.ui.main

import android.Manifest
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.sun.unsplash_01.databinding.ActivityMainBinding
import com.sun.unsplash_01.utils.imagedownload.CheckPermission

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!CheckPermission(this).isPermissionGranted()) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        currentFocus?.let {
            if (it is SearchView.SearchAutoComplete) {
                val viewRect = Rect()
                it.getGlobalVisibleRect(viewRect)
                if (!viewRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    it.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    companion object {
        const val REQUEST_CODE = 0
    }
}
