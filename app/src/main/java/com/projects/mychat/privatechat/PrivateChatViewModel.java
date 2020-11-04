package com.projects.mychat.privatechat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.projects.mychat.singleChat.SingleChatViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import sdk.chat.core.dao.Message;
import sdk.chat.core.dao.Thread;
import sdk.chat.core.dao.User;
import sdk.chat.core.hook.Hook;
import sdk.chat.core.hook.HookEvent;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.types.MessageType;
import sdk.guru.common.RX;

public class PrivateChatViewModel extends ViewModel {

    private Context context;
    private static final String TAG= PrivateChatViewModel.class.getSimpleName();
    private MutableLiveData<List<Message>> messagesMutableLiveData = new MutableLiveData<>();
    private Thread thread;
    public MutableLiveData<Boolean> clearEditText =  new MutableLiveData<>();
    private User user;
    public PrivateChatViewModel(Context context,String  userEntityId){
        this.context=context;
         user=  ChatSDK.core().getUserNowForEntityID(userEntityId);
        createThread("thread name:" + user.getEmail(),user);
        listeningMessageReceived();
        getMessageReceived();
    }

    @SuppressLint("CheckResult")
    private void createThread(String name, User user) {
        ChatSDK.thread().createThread(name, user, ChatSDK.currentUser())
                .observeOn(RX.main())
                .doFinally(() -> {
                    // Runs when process completed from error or success
                })
                .subscribe(thread -> {
                    // When the thread type created
                    this.thread = thread;
                    messagesMutableLiveData.setValue(thread.getMessages());
                    Log.d(TAG, "createThread: " + thread.getEntityID());
                    Log.d(TAG, "createThread: " + thread.getMessages());
                }, throwable -> {
                    // If there type an error
                    Log.d(TAG, "createThread: " + throwable.getMessage());
                });
    }

    @SuppressLint("CheckResult")
    public void sendTextMessage(String message) {
        ChatSDK.thread().sendMessageWithText(message, thread).subscribe(() -> {
            Log.d(TAG, "sendTextMessage: " + "success");
            clearEditText.setValue(true);
            Log.d(TAG, "sendTextMessage: " + thread.getMessages().toString());
            messagesMutableLiveData.setValue(thread.getMessages());
        }, throwable -> {
            Log.d(TAG, "sendTextMessage: " + "failed " + throwable.getMessage());
            clearEditText.setValue(false);
        });
    }

    private void getMessageReceived () {
        ChatSDK.hook().addHook(Hook.sync(data -> {

            // Get the body from the notification
            if (data.get(HookEvent.Message) instanceof Message) {

                // Cast it as a text
                Message message = (Message) data.get(HookEvent.Message);

                // Check the text getTypingStateType
                if (message.getMessageType().is(MessageType.Image)) {

                }else if(message.getMessageType().is(MessageType.Text))
                {
                    Log.d(TAG, "getMessageReceived: "+message.getText());
                }
            }
        }), HookEvent.MessageReceived);
    }
    private void listeningMessageReceived() {
        ChatSDK.hook().addHook(Hook.sync(data -> {
            Message message = (Message) data.get(HookEvent.Message);
            Log.d(TAG, "messageReceived: "+message.getText());
            messagesMutableLiveData.postValue(thread.getMessages());
        }), HookEvent.MessageReceived);

        // Asynchronous code
        ChatSDK.hook().addHook(Hook.async(data -> Completable.create(emitter -> {
            // ... Async code here
            emitter.onComplete();
            Log.d(TAG, "Asynchronous: "+data.toString());
            Log.d(TAG, "Asynchronous: " + thread.getMessages().toString());
        })), HookEvent.MessageReceived);
    }
    public MutableLiveData<List<Message>> getMessagesMutableLiveData()
    {
        return messagesMutableLiveData;
    }
}
