package com.projects.mychat.singleChat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import sdk.chat.core.dao.User;
import sdk.chat.core.session.ChatSDK;

public class SingleChatViewModel extends ViewModel {
    private MutableLiveData<List<User>> ListOfContacts = new MutableLiveData<>();
    private Context context;
    private static final String TAG=SingleChatViewModel.class.getSimpleName();

    public SingleChatViewModel(Context context)
    {
        this.context = context;
        getContacts();
    }





    public void getContacts()
    {

        List<User> users = ChatSDK.contact().contacts();
        ListOfContacts.setValue(users);
        Log.d(TAG, "getContacts: "+users.toString());
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
