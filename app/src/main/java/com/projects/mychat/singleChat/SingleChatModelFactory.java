package com.projects.mychat.singleChat;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SingleChatModelFactory extends ViewModelProvider.NewInstanceFactory {
    Context context;

    public SingleChatModelFactory(Context context) {
       this.context=context;

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SingleChatViewModel(context);
    }
}
