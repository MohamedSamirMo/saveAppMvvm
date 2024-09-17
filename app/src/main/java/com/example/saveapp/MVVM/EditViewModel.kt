package com.example.saveapp.MVVM

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.saveapp.ContactModel
import com.example.saveapp.MyDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class EditViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    var LiveDataComplete = MutableLiveData<Boolean>()

    fun updateContact(contact: ContactModel) {
        MyDatabase.myDatabase.getDao().updateContact(contact)
            .subscribeOn(Schedulers.io()) // تنفيذ العملية في الخلفية
            .observeOn(AndroidSchedulers.mainThread()) // تحديث الـ UI بعد العملية
            .subscribe({
                LiveDataComplete.value = true
            }, { error ->
                error.printStackTrace()
                LiveDataComplete.value = false
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}