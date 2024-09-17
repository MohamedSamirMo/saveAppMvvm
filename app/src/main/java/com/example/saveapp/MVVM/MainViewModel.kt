package com.example.saveapp.MVVM

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.saveapp.ContactModel
import com.example.saveapp.MyDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    var LiveDataComplete= MutableLiveData<Boolean>()
    fun insertContact(name: String, phone: String, data: String){
        val contact = ContactModel(name, phone, data)

        MyDatabase
            .myDatabase.getDao()
            .insertContact(ContactModel(data, name, phone))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    LiveDataComplete.value=true

                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()

                }

            })
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


}