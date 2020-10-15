package com.projects.mychat.singleChat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.projects.mychat.R;

import java.util.List;

import sdk.chat.core.dao.User;
import sdk.chat.core.interfaces.UserListItem;

public class SingleChatAdapter extends

        RecyclerView.Adapter<SingleChatAdapter.ViewHolder> {

    private static final String TAG = SingleChatAdapter.class.getSimpleName();

    private Context context;



    private List<User> userListItems;

    private OnItemClickListener onItemClickListener;

    public SingleChatAdapter(Context context,

                             OnItemClickListener onItemClickListener) {

        this.context = context;
//
//        this.userListItems = list;

        this.onItemClickListener = onItemClickListener;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {

            super(itemView);

        }

        public void bind(final UserListItem model,

                         final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {

                    listener.onItemClick(getLayoutPosition());

                }

            });

        }

    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.contact_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int position) {

        UserListItem item = userListItems.get(position);

        holder.bind(item, onItemClickListener);

    }
    public void setUserListItems(List<User> userListItems) {
        this.userListItems = userListItems;
        notifyDataSetChanged();
    }
    @Override

    public int getItemCount() {

        return userListItems.size();

    }

    public interface OnItemClickListener {

        void onItemClick(int position);

    }

}