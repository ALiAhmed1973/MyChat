package com.projects.mychat.privatechat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.projects.mychat.MainActivity;
import com.projects.mychat.R;

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

    private static final String NOTIFICATION_CHANEL_ID ="com.projects.mychat.privatechat.main_channel" ;
    private static final int MAIN_NOTIFICATION = 123;
    private static final int PENDINGINTENT_REQUEST_CODE = 128;
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
        pushNotificationHandling();
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
            pushNotify(message.getText());
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

    public void pushNotify(String message)
    {
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_COMPONENT_NAME,message);
        ChatSDK.push().getBroadcastHandler().onReceive(context,intent);
    }

    private void pushNotificationHandling() {
        ChatSDK.push().setBroadcastHandler((context, intent) -> {
            // Handle push notifications here
            String messageText = intent.getStringExtra(Intent.EXTRA_COMPONENT_NAME);
            startNotification(messageText);
        });
    }
    public MutableLiveData<List<Message>> getMessagesMutableLiveData()
    {

        return messagesMutableLiveData;
    }
    private void startNotification(String messageText)
    {
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANEL_ID,"Main_Notification"
                    ,NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.shape_incoming_icon )
                .setContentTitle("ChatApp")
                .setContentText(messageText)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN&&Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(MAIN_NOTIFICATION,builder.build());

    }
    private static PendingIntent contentIntent(Context context)
    {
        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,PENDINGINTENT_REQUEST_CODE,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
