package com.projects.mychat.singleChat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.projects.mychat.R;

import java.util.List;

import sdk.chat.core.dao.User;

public class SingleChatAdapter extends

        RecyclerView.Adapter<SingleChatAdapter.ViewHolder> {

    private static final String TAG = SingleChatAdapter.class.getSimpleName();

    private Context context;



    private List<User> userListItems ;

    private OnItemClickListener onItemClickListener;

    public SingleChatAdapter(Context context,List<User> list,

                             OnItemClickListener onItemClickListener) {

        this.context = context;
        this.userListItems = list;
        this.onItemClickListener = onItemClickListener;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView  textViewUserName;
        ImageView imageViewUserImage;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textView_name);
            imageViewUserImage = itemView.findViewById(R.id.imageView_profile_pic);

        }

        public void bind(final User currentUser,

                         final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {

                    listener.onItemClick(currentUser);

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

        User item = userListItems.get(position);
        holder.textViewUserName.setText(item.getName());
        Glide.with(context).load(item.getAvatarURL()).into(holder.imageViewUserImage);
        holder.bind(item, onItemClickListener);

    }
    public void setUserListItems(List<User> userListItems) {
        this.userListItems=userListItems;
        notifyDataSetChanged();
    }
    @Override

    public int getItemCount() {

        return userListItems.size();

    }

    public interface OnItemClickListener {

        void onItemClick(User currentUser);

    }

}