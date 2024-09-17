package com.example.saveapp.MVVM

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.saveapp.ContactModel
import com.example.saveapp.MyDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class DiplayViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val contactsLiveData = MutableLiveData<List<ContactModel>>()
    val errorLiveData = MutableLiveData<String>()
    val loadingLiveData = MutableLiveData<Boolean>()
    private val contactDao = MyDatabase.myDatabase.getDao()


    fun loadContacts() {
        contactDao.getAllContacts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<ContactModel>> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    errorLiveData.value = e.message
                }

                override fun onSuccess(t: List<ContactModel>) {
                    contactsLiveData.value = t
                }

            })
    }

    fun deleteContact(contact: ContactModel) {
        contactDao.deleteContact(contact)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadContacts()
            }, { error ->
                error.printStackTrace()
            }).let { compositeDisposable.add(it) }
    }

    fun editContact(contact: ContactModel) {
        contactDao.updateContact(contact)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadContacts()
            }, { error ->
                error.printStackTrace()
            }).let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


}