package com.projects.mychat.singleChat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projects.mychat.ChatUser;
import com.projects.mychat.R;
import com.projects.mychat.databinding.FragmentSingleChatBinding;

import java.util.ArrayList;
import java.util.List;

import sdk.chat.core.dao.User;
import sdk.chat.core.session.ChatSDK;


public class SingleChatFragment extends Fragment implements SingleChatAdapter.OnItemClickListener {
    private static final String TAG = SingleChatFragment.class.getSimpleName();
    FragmentSingleChatBinding binding;
    SingleChatAdapter singleChatAdapter;
    List<User> users = new ArrayList<>();
    AddUserPopUp addUserPopUp ;
    SingleChatViewModel viewModel;
    SingleChatModelFactory singleChatModelFactory;
    public SingleChatFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding= FragmentSingleChatBinding.inflate(inflater,container,false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        binding.recyclerViewContacts.setLayoutManager(linearLayoutManager);
        singleChatAdapter = new SingleChatAdapter(getContext(),users,this);
        binding.recyclerViewContacts.setAdapter(singleChatAdapter);


        singleChatModelFactory = new SingleChatModelFactory(getContext());
        viewModel = new ViewModelProvider(this,singleChatModelFactory).get(SingleChatViewModel.class);
        viewModel.getAllOfContacts().observe(getViewLifecycleOwner(),
                users ->
                        singleChatAdapter.setUserListItems(users)
        );

        addUserPopUp = new AddUserPopUp(getContext(),viewModel);

       binding.fabAddUsers.setOnClickListener(v -> {
           addUserPopUp.showPopupWindow(v);
       });


        return binding.getRoot();
    }

    @Override
    public void onItemClick(User currentUser) {
        ChatUser chatUser = new ChatUser(currentUser);
        if(chatUser!=null) {
            Navigation.findNavController(this.getActivity(), R.id.myNavHostFragment).
                    navigate(SingleChatFragmentDirections.actionSingleChatFragmentToPrivateChatFragment(chatUser));
            Log.d("SingleChatFragment", "onItemClick: " + currentUser.getEmail());
        }
    }
}