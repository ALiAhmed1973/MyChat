package com.projects.mychat.privatechat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.projects.mychat.R;

import java.util.List;

import sdk.chat.core.dao.Message;

public class PrivateChatAdapter extends

        RecyclerView.Adapter<PrivateChatAdapter.ViewHolder> {

    private static final String TAG = PrivateChatAdapter.class.getSimpleName();

    private Context context;

    public void setMessageList(List<Message> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private List<Message> list;

    private OnItemClickListener onItemClickListener;

    public PrivateChatAdapter(Context context, List<Message> list) {

        this.context = context;

        this.list = list;

//        this.onItemClickListener = onItemClickListener;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewChat ;
        ImageView imageViewProfilePic;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewChat=itemView.findViewById(R.id.textView_chat_text);
            imageViewProfilePic=itemView.findViewById(R.id.imageView_chat_profile_pic);
        }

        public void bind(final Message model, final OnItemClickListener listener) {

//            itemView.setOnClickListener(v ->
//                    listener.onItemClick(getLayoutPosition()));

        }

    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.chat_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;

    }

    @Override

    public void onBindViewHolder(ViewHolder holder, int position) {
        Message item = list.get(position);
        holder.textViewChat.setText(item.getText());
        Glide.with(context).load(item.getSender().getAvatarURL()).into(holder.imageViewProfilePic);
        holder.bind(item, onItemClickListener);

    }

    @Override
    public int getItemCount() {

        return list.size();

    }

    public interface OnItemClickListener {

        void onItemClick(int position);

    }

}