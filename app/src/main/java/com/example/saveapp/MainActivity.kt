package com.example.saveapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.saveapp.MVVM.MainViewModel
import com.example.saveapp.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    val ViewModel:MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
   private val CompositeDisposable:CompositeDisposable=CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ViewModel.LiveDataComplete.observe(this){
            if (it){
                Toast.makeText(this, "Contact inserted", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnSave.setOnClickListener {
            val data = binding.inputData.text.toString()
            val name = binding.inputName.text.toString()
            val phone = binding.inputPhone.text.toString()

            ViewModel.insertContact(name,phone,data)
        }
        binding.btnDisplay.setOnClickListener {
            val intent = Intent(this, DisplayActivity::class.java)
            startActivity(intent)
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        CompositeDisposable.clear()
    }
}