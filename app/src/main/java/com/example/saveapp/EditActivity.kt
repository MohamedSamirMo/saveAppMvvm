package com.example.saveapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.saveapp.MVVM.EditViewModel
import com.example.saveapp.databinding.ActivityEditBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class EditActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityEditBinding.inflate(layoutInflater)
    }
    private var contactModel: ContactModel? = null
    private val CompositeDisposable:CompositeDisposable=CompositeDisposable()
    val ViewModel: EditViewModel by lazy {
        ViewModelProvider(this)[EditViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ViewModel.LiveDataComplete.observe(this){
            if (it){
                Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show()
            }
        }
        // استلام بيانات جهة الاتصال من الـIntent
        contactModel = intent.getParcelableExtra("contact")

        // عرض البيانات في الـEditText
        contactModel?.let {
            binding.etContactName.setText(it.name)
            binding.etContactPhone.setText(it.phone)
            binding.etContactData.setText(it.data)
        }

        // عند الضغط على زر الحفظ
        binding.btnSave.setOnClickListener {
            val name = binding.etContactName.text.toString()
            val phone = binding.etContactPhone.text.toString()
            val data = binding.etContactData.text.toString()

            if (name.isEmpty() || phone.isEmpty() || data.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // تحديث جهة الاتصال باستخدام RxJava
            contactModel?.let {
                val updatedContact = it.copy(name = name, phone = phone, data = data)
                ViewModel.updateContact(updatedContact)

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CompositeDisposable.clear()
    }
}
