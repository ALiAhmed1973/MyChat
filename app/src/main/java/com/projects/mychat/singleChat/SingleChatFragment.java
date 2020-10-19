package com.projects.mychat.singleChat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.mychat.databinding.FragmentSingleChatBinding;

import java.util.ArrayList;
import java.util.List;

import sdk.chat.core.dao.User;
import sdk.chat.core.session.ChatSDK;


public class SingleChatFragment extends Fragment implements SingleChatAdapter.OnItemClickListener {

    FragmentSingleChatBinding binding;
    SingleChatAdapter singleChatAdapter;
    List<User> users ;
    AddUserPopUp addUserPopUp ;
    SingleChatViewModel viewModel;
    public SingleChatFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding= FragmentSingleChatBinding.inflate(inflater,container,false);
       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
       binding.recyclerViewContacts.setLayoutManager(linearLayoutManager);
       singleChatAdapter = new SingleChatAdapter(getContext(),this);
       users = ChatSDK.contact().contacts();
       singleChatAdapter.setUserListItems(users);
       binding.recyclerViewContacts.setAdapter(singleChatAdapter);
        addUserPopUp = new AddUserPopUp(getContext());

        viewModel = new ViewModelProvider(this).get(SingleChatViewModel.class);
        viewModel.getAllOfContacts().observe(getViewLifecycleOwner(),
                users -> singleChatAdapter.setUserListItems(users));



       binding.fabAddUsers.setOnClickListener(v -> {
           addUserPopUp.showPopupWindow(v);
       });
        return binding.getRoot();
    }

    @Override
    public void onItemClick(int position) {

    }
}