package com.projects.mychat.privatechat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
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
    private FragmentPrivateChatBinding binding;
    private PrivateChatAdapter adapter;

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
        PrivateChatModelFactory modelFactory = new PrivateChatModelFactory(getContext(),user);
        PrivateChatViewModel viewModel= new ViewModelProvider(this,modelFactory).get(PrivateChatViewModel.class);

        adapter = new PrivateChatAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        binding.recyclerViewChat.setLayoutManager(linearLayoutManager);
        binding.recyclerViewChat.setAdapter(adapter);

        viewModel.getMessagesMutableLiveData().observe(getViewLifecycleOwner(), messages -> {
            adapter.setMessageList(messages);
            binding.recyclerViewChat.smoothScrollToPosition(adapter.getItemCount());
        });

        viewModel.clearEditText.observe(getViewLifecycleOwner(), isSuccess -> {
            if(isSuccess)
            {
                binding.editTextTextMultiLine.setText("");
                viewModel.clearEditText.setValue(false);
            }
        });


        binding.imageButtonSend.setOnClickListener(v ->
                viewModel.sendTextMessage(binding.editTextTextMultiLine.getText().toString()));
        return binding.getRoot();
    }






}