package com.projects.mychat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.mychat.databinding.FragmentMyProfileBinding;


public class MyProfileFragment extends Fragment {


FragmentMyProfileBinding binding;
    public MyProfileFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater,container,false);
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(MyProfileFragmentDirections.actionMyProfileFragmentToSingleChatFragment());
            }
        });
        return binding.getRoot();
    }
}