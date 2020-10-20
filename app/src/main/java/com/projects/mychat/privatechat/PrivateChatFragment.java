package com.projects.mychat.privatechat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.mychat.ChatUser;
import com.projects.mychat.databinding.FragmentPrivateChatBinding;

import java.util.zip.Inflater;

import sdk.chat.core.dao.Thread;
import sdk.chat.core.dao.User;
import sdk.chat.core.session.ChatSDK;
import sdk.guru.common.RX;


public class PrivateChatFragment extends Fragment {
private User user;
private Thread thread;
    FragmentPrivateChatBinding binding;
    public PrivateChatFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPrivateChatBinding.inflate(inflater,container,false);
        if(getArguments()!=null) {
            PrivateChatFragmentArgs args = PrivateChatFragmentArgs.fromBundle(getArguments());
            ChatUser chatUser = args.getCurrentUser();
            user=chatUser.getUser();
            Log.d("PrivateChatFragment", "onCreateView: "+chatUser.getUser().getEmail());
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        binding.recyclerViewChat.setLayoutManager(linearLayoutManager);
        createThread("thread name:"+user.getName(),user);


        binding.imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextMessage(binding.editTextTextMultiLine.getText().toString(),thread);
            }
        });
        return binding.getRoot();
    }

    private void createThread (String name, User user) {
                ChatSDK.thread().createThread(name, user, ChatSDK.currentUser())
                .observeOn(RX.main())
                .doFinally(() -> {
                    // Runs when process completed from error or success
                })
                .subscribe(thread -> {
                    // When the thread type created
                    this.thread= thread;
                    thread.getMessages();
                    Log.d("PrivateChatFragment", "createThread: "+thread.getEntityID());
                    Log.d("PrivateChatFragment", "createThread: "+thread.getMessages());
                }, throwable -> {
                    // If there type an error
                    Log.d("PrivateChatFragment", "createThread: "+throwable.getMessage());
                });

    }

    private void sendTextMessage (String message, Thread thread) {
         ChatSDK.thread().sendMessageWithText(message, thread).subscribe(() -> {
             Log.d("PrivateChatFragment", "sendTextMessage: "+"success");
        }, throwable -> {
             Log.d("PrivateChatFragment", "sendTextMessage: "+"failed "+throwable.getMessage());
        });
    }
}