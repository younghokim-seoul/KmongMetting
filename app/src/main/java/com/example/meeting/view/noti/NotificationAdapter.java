package com.example.meeting.view.noti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meeting.R;
import com.example.meeting.model.PushMessage;
import com.example.meeting.utils.DateUtils;
import com.example.meeting.view.base.adapter.BaseRecyclerAdapter;
import com.example.meeting.view.base.adapter.BaseViewHolder;
import com.example.meeting.view.base.adapter.OnItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends BaseRecyclerAdapter<PushMessage, NotificationAdapter.ViewHolder> {

    public NotificationAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.holder_noti_item, null, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends BaseViewHolder<PushMessage> {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_message)
        TextView tvMessage;
        @BindView(R.id.tv_date)
        TextView tvTimeAt;

        public ViewHolder(@NonNull View parent) {
            super(parent);
            ButterKnife.bind(this, parent);
        }

        @Override
        protected void bind(Context context, PushMessage data, int position, OnItemClickListener listener) {
            tvTitle.setText(data.getTitle());
            tvMessage.setText(data.getMessage());
            tvTimeAt.setText(DateUtils.getConvertCurTime(data.getTimeMils()));

//            itemView.setOnClickListener(v -> {
//                if (listener != null) listener.onItemClick(data);
//            });

        }
    }
}
