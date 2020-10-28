package com.projects.mychat.privatechat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.projects.mychat.ChatUser;
import com.projects.mychat.databinding.FragmentPrivateChatBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import sdk.chat.core.dao.Message;
import sdk.chat.core.dao.Thread;
import sdk.chat.core.dao.User;
import sdk.chat.core.hook.Hook;
import sdk.chat.core.hook.HookEvent;
import sdk.chat.core.session.ChatSDK;
import sdk.guru.common.RX;


public class PrivateChatFragment extends Fragment {
    private static final String TAG = PrivateChatFragment.class.getSimpleName();
    private User user;
    private Thread thread;
    private FragmentPrivateChatBinding binding;
    private PrivateChatAdapter adapter;
    private List<Message> messageList = new ArrayList<>();

    public PrivateChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPrivateChatBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            PrivateChatFragmentArgs args = PrivateChatFragmentArgs.fromBundle(getArguments());
            ChatUser chatUser = args.getCurrentUser();
            user = chatUser.getUser();
            Log.d("PrivateChatFragment", "onCreateView: " + chatUser.getUser().getEmail());
        }
        adapter = new PrivateChatAdapter(getContext(), messageList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.recyclerViewChat.setLayoutManager(linearLayoutManager);
        binding.recyclerViewChat.setAdapter(adapter);
        createThread("thread name:" + user.getName(), user);


        binding.imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextMessage(binding.editTextTextMultiLine.getText().toString(), thread);
            }
        });
        messageReceived();
        return binding.getRoot();
    }

    private void createThread(String name, User user) {
        ChatSDK.thread().createThread(name, user, ChatSDK.currentUser())
                .observeOn(RX.main())
                .doFinally(() -> {
                    // Runs when process completed from error or success
                })
                .subscribe(thread -> {
                    // When the thread type created
                    this.thread = thread;
                    messageList = thread.getMessages();
                    adapter.setMessageList(messageList);
                    Log.d("PrivateChatFragment", "createThread: " + thread.getEntityID());
                    Log.d("PrivateChatFragment", "createThread: " + thread.getMessages());
                }, throwable -> {
                    // If there type an error
                    Log.d("PrivateChatFragment", "createThread: " + throwable.getMessage());
                });

    }

    private void sendTextMessage(String message, Thread thread) {
        ChatSDK.thread().sendMessageWithText(message, thread).subscribe(() -> {
            Log.d("PrivateChatFragment", "sendTextMessage: " + "success");
            binding.editTextTextMultiLine.setText("");
            messageList = thread.getMessages();
            adapter.setMessageList(messageList);
        }, throwable -> {
            Log.d("PrivateChatFragment", "sendTextMessage: " + "failed " + throwable.getMessage());
        });
    }

    private void messageReceived() {
        ChatSDK.hook().addHook(Hook.sync(data -> {
            Message message = (Message) data.get(HookEvent.Message);
            Log.d(TAG, "messageReceived: "+message.getText());
            messageList = thread.getMessages();
            adapter.setMessageList(messageList);
        }), HookEvent.MessageReceived);

        // Asynchronous code
        ChatSDK.hook().addHook(Hook.async(data -> Completable.create(emitter -> {
            // ... Async code here
            emitter.onComplete();
            Log.d(TAG, "messageReceived: "+data.toString());
        })), HookEvent.MessageReceived);
    }
}