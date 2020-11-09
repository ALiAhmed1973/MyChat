package com.projects.mychat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.projects.mychat.databinding.FragmentMyProfileBinding;

import sdk.chat.core.dao.User;
import sdk.chat.core.session.ChatSDK;
import sdk.guru.common.RX;


public class MyProfileFragment extends Fragment {
    private static final String TAG = "MyProfileFragment";
FragmentMyProfileBinding binding;
private User currentUser;
private String name;
private String avatarUrl;
    public MyProfileFragment() {
        // Required empty public constructor
    }




    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater,container,false);
        currentUser= ChatSDK.core().currentUser();

        binding.editTextPersonName.setText(currentUser.getName());
        binding.textViewPersonEmail.setText(currentUser.getEmail());
        Glide.with(this).load(currentUser.getAvatarURL()).placeholder(R.drawable.icn_200_image_message_error)
                .error(R.drawable.icn_200_image_message_error).into(binding.imageViewProfilePic);

        binding.buttonSave.setOnClickListener(view -> {
            name = binding.editTextPersonName.getText().toString();
            currentUser.setName(name);
            ChatSDK.core().pushUser()
                    .observeOn(RX.main())
                    .subscribe(()->{
                Toast.makeText(view.getContext(),"Success",Toast.LENGTH_SHORT).show();
            },t->{
                    Toast.makeText(view.getContext(),"Failed: "+t.toString(),Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onCreateView: "+t.toString());}
                    );
           // Navigation.findNavController(view).navigate(MyProfileFragmentDirections.actionMyProfileFragmentToSingleChatFragment());
        });
        return binding.getRoot();
    }
}