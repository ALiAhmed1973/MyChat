package com.projects.mychat.singleChat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.projects.mychat.R;
import com.projects.mychat.databinding.AddUserPopupBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import io.reactivex.disposables.Disposable;
import sdk.chat.core.dao.User;
import sdk.chat.core.session.ChatSDK;
import sdk.chat.core.types.ConnectionType;
import sdk.guru.common.RX;

public class AddUserPopUp {
Context context;

public AddUserPopUp(Context context){
    this.context = context;
}

    @SuppressLint("CheckResult")
    public void showPopupWindow(View view)
    {
       LayoutInflater inflater= (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
       View popUpView = inflater.inflate(R.layout.add_user_popup,null);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;


        final PopupWindow popupWindow = new PopupWindow(popUpView,width,height,true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        EditText editTextSearchUserMail = popUpView.findViewById(R.id.editText_search_mail);


        Button buttonAddUser = popUpView.findViewById(R.id.button_add_user);
        buttonAddUser.setOnClickListener(v -> {
            SearchForAddUser(editTextSearchUserMail.getText().toString().trim(),popupWindow);


        });

        popUpView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }

    private void SearchForAddUser(String searchEmail,PopupWindow popupWindow) {
        ChatSDK.search().usersForIndex(searchEmail).observeOn(RX.main()).subscribe(new io.reactivex.Observer<User>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(User user) {
                Log.d("searchAddUserworking", user.getName());
                if(!ChatSDK.contact().exists(user)&&!user.isMe())
                {
                    ChatSDK.contact().addContact(user, ConnectionType.Contact).observeOn(RX.main()).subscribe(() -> {
                        popupWindow.dismiss();
                        Toast.makeText(context,"Contact Added",Toast.LENGTH_LONG).show();
                    }, T -> {
                        Log.d("Added notworking", T.toString());
                        Toast.makeText(context, T.toString(),Toast.LENGTH_LONG).show();
                    });
                }


            }

            @Override
            public void onError(Throwable e) {
                Log.d("searchAddUsernotworking", e.getLocalizedMessage());
                Toast.makeText(context,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onComplete() {

            }
        });


    }


}
