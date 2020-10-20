package com.projects.mychat.singleChat;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import sdk.chat.core.dao.User;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.types.ConnectionType;
import sdk.guru.common.RX;

public class SingleChatViewModel extends ViewModel {
    private MutableLiveData<List<User>> ListOfContacts = new MutableLiveData<>();
    private Context context;

    public SingleChatViewModel(Context context)
    {
        this.context = context;
        getContacts();
    }





    public void getContacts()
    {
        ListOfContacts.setValue(ChatSDK.contact().contacts());
    }

    @SuppressLint("CheckResult")
    public void searchForAddUser(String searchEmail)
    {


    }


    public MutableLiveData<List<User>> getAllOfContacts()
    {
        return ListOfContacts;
    }
//    @SuppressLint("CheckResult")
//    private boolean addUser(User user)
//    {
//
//        ChatSDK.contact().addContact(user, ConnectionType.Contact).observeOn(RX.main()).subscribe(() -> {
//
//        }, T-> {
//
//            Log.d("Added notworking", T.toString());
//        });
//
//
//
//    }
}
