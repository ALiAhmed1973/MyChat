package com.projects.mychat.singleChat;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import sdk.chat.core.dao.User;

public class SingleChatViewModel extends ViewModel {
    Context  context;
    private MutableLiveData<List<User>> listMutableLiveData = new MutableLiveData<>();
    public SingleChatViewModel(Context context){
        this.context=context;
    }

    private void getContacts()
    {

    }
}
