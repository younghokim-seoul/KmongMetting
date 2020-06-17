package com.example.meeting.view.base.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<D> extends RecyclerView.ViewHolder {
    private Context context;

    public BaseViewHolder(@NonNull View parent) {
        super(parent);
        context = itemView.getContext();
    }

    protected abstract void bind(Context context, D data, int position, OnItemClickListener listener);

}
