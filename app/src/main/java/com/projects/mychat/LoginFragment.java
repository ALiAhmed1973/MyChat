package com.projects.mychat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.projects.mychat.databinding.FragmentLoginBinding;

import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.types.AccountDetails;
import sdk.guru.common.RX;


public class LoginFragment extends Fragment {

private FragmentLoginBinding binding;
private String email;
private String password;
    public LoginFragment() {
        // Required empty public constructor
    }

    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater,container,false);

        binding.textViewSignup.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment()));

        binding.buttonLogin.setOnClickListener(view -> {

             email = binding.editTextEmailAddress.getText().toString();
             password = binding.editTextPassword.getText().toString();
            if(!checkingFields())
            {
                return ;
            }
            AccountDetails details = AccountDetails.username(email, password);
            binding.loginProgressbar.setVisibility(View.VISIBLE);
            ChatSDK.auth().authenticate(details)
                    .observeOn(RX.main())
                    .subscribe(()-> {
                        Toast.makeText(view.getContext(),"Login",Toast.LENGTH_SHORT).show();
                        binding.loginProgressbar.setVisibility(View.INVISIBLE);
                          Navigation.findNavController(view).navigate(LoginFragmentDirections.actionLoginFragmentToSingleChatFragment());
                    }, throwable -> {
                        binding.loginProgressbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(view.getContext(),throwable.toString(),Toast.LENGTH_LONG).show();
                    });
        });



        if (ChatSDK.auth().isAuthenticated()) {
            Navigation.findNavController(this.getActivity(),R.id.myNavHostFragment).navigate(LoginFragmentDirections.actionLoginFragmentToSingleChatFragment());
            Log.d("isAuthenticated","yes Authenticated");
        } else {
            Log.d("isAuthenticated","No Authenticated");
        }
        if(ChatSDK.auth().isAuthenticatedThisSession())
        {

            Log.d("isAuthenticatedSession","yes AuthenticatedThisSession");
        }else
        {
            Log.d("isAuthenticatedSession","No AuthenticatedThisSession");
        }
        return binding.getRoot();
    }

    private boolean checkingFields()
    {
        if(email.isEmpty()||password.isEmpty())
        {
            Toast.makeText(getContext(),"One of your fields is empty",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}