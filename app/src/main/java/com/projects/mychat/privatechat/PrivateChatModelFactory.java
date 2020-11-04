package com.projects.mychat.privatechat;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.projects.mychat.singleChat.SingleChatViewModel;

import sdk.chat.core.dao.User;

public class PrivateChatModelFactory extends ViewModelProvider.NewInstanceFactory {
    Context context;
    String userEntityId;

    public PrivateChatModelFactory(Context context, String userEntityId) {
        this.context=context;
        this.userEntityId=userEntityId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PrivateChatViewModel(context,userEntityId);
    }
}
