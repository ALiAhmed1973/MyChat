package com.projects.mychat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.mychat.databinding.FragmentSignUpBinding;


public class SignUpFragment extends Fragment {

FragmentSignUpBinding binding;
    public SignUpFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentSignUpBinding.inflate(inflater,container,false);
       binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Navigation.findNavController(view).navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment());
           }
       });
       return binding.getRoot();
    }
}