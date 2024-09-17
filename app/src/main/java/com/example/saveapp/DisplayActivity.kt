package com.example.saveapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.saveapp.MVVM.DiplayViewModel
import com.example.saveapp.databinding.ActivityDisplayBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable

class DisplayActivity : AppCompatActivity() {

    private val CompositeDisposable:CompositeDisposable=CompositeDisposable()
    val ViewModel: DiplayViewModel by lazy {
        ViewModelProvider(this)[DiplayViewModel::class.java]
    }
    private val contactAdapter: ContactAdapter by lazy {
        ContactAdapter(
            onDeleteClick = { ViewModel.deleteContact(it) },
            onEditClick = { ViewModel.editContact(it) }
        )
    }

    private val binding: ActivityDisplayBinding by lazy {
        ActivityDisplayBinding.inflate(layoutInflater)
    }

    private lateinit var contactDao: MyDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ViewModel.contactsLiveData.observe(this) { contacts ->
            contactAdapter.list = ArrayList(contacts)
            binding.recDisplay.adapter = contactAdapter
        }

        contactDao = MyDatabase.myDatabase.getDao()

        // Load contacts using RxJava
        ViewModel.loadContacts()
    }


    override fun onDestroy() {
        super.onDestroy()
        CompositeDisposable.clear()
    }
}
