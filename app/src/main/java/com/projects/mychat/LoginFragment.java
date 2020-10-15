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

import com.projects.mychat.databinding.FragmentLoginBinding;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import sdk.chat.core.dao.User;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.types.AccountDetails;
import sdk.chat.core.types.ConnectionType;
import sdk.guru.common.RX;


public class LoginFragment extends Fragment {

FragmentLoginBinding binding;
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

            String email = binding.editTextEmailAddress.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            AccountDetails details = AccountDetails.username(email, password);
            ChatSDK.auth().authenticate(details)
                    .observeOn(RX.main())
                    .subscribe(()-> {
                        Toast.makeText(view.getContext(),"Login",Toast.LENGTH_SHORT).show();
                      //    Navigation.findNavController(view).navigate(LoginFragmentDirections.actionLoginFragmentToSingleChatFragment());
                    }, throwable -> {
                        Toast.makeText(view.getContext(),throwable.toString(),Toast.LENGTH_LONG).show();
                    });
        });

        binding.buttonLogout.setOnClickListener(view -> {
//            ChatSDK.auth().logout()
//                    .observeOn(RX.main())
//                    .subscribe(()->Log.d("logout","Logout"),
//                            t->Log.e("login Not working", t.toString()));

            ChatSDK.search().usersForIndexes("eng@j.com").observeOn(RX.main()).subscribe(new Observer<User>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(User user) {
                    Log.d("Searching working",user.getEmail());
                }

                @Override
                public void onError(Throwable e) {
                    Log.d("Searching working",e.toString());
                }

                @Override
                public void onComplete() {
                    Log.d("Searching working","completed");
                }
            });
// A               dd contact
//                ChatSDK.contact().addContact(user, ConnectionType.Contact).observeOn(RX.main()).subscribe(() -> {
//                    // Contact added
//                    Toast.makeText(view.getContext(),"Contact Added",Toast.LENGTH_LONG).show();
//                }, T->Log.d("Added notworking", T.toString()));
        });


        if (ChatSDK.auth().isAuthenticated()) {
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
}