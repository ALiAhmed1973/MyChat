package com.projects.mychat;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.projects.mychat.databinding.FragmentSignUpBinding;

import java.util.HashMap;
import java.util.Objects;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import sdk.chat.core.dao.User;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.types.AccountDetails;
import sdk.guru.common.RX;


public class SignUpFragment extends Fragment {

    private static String urlImage = "";
    private FragmentSignUpBinding binding;
    private String email;
    private String name;
    private String password;
    private String passwordConfirm;
    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View view) {

                 email = binding.editTextPersonEmail.getText().toString();
                 name = binding.editTextTextPersonName.getText().toString();
                 password = binding.editTextPersonPassword.getText().toString();
                 passwordConfirm = binding.editTextPersonPasswordConfirmation.getText().toString();

                 if(!checkingFields())
                 {
                  return ;
                 }
                AccountDetails details = new AccountDetails();
                details.type = AccountDetails.Type.Register;
                details.username=email;
                details.password=password;

                ChatSDK.auth().authenticate(details).observeOn(RX.main()).subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        pushUser(name);
                    }
                }
            , throwable -> {
                    Log.d("authenticateNot working", throwable.toString());
                    Toast.makeText(view.getContext(),throwable.toString(),Toast.LENGTH_LONG).show();
                });



            }
        });
        return binding.getRoot();

    }

    @SuppressLint("CheckResult")
    private void pushUser(String name){
        Toast.makeText(getContext(),"registered",Toast.LENGTH_SHORT).show();
        User currentUser = ChatSDK.core().currentUser();
        currentUser.setName(name);
        ChatSDK.core().pushUser().observeOn(RX.main()).subscribe(() -> {
            Log.d("pushing working", "success");
            Navigation.findNavController(requireView()).navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment());
        }, throwable -> {
            Log.d("pushingNot working", throwable.toString());
        });
    }

    private boolean checkingFields()
    {
        if(email.isEmpty()||name.isEmpty())
        {
            Toast.makeText(getContext(),"One of your fields is empty",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.isEmpty())
        {
            Toast.makeText(getContext(),"Password is empty",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!password.equals(passwordConfirm))
        {
            Toast.makeText(getContext(),"Your Password doesn't match",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

}