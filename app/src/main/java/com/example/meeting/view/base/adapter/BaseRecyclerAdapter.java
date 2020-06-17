package com.example.meeting.view.base.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

abstract public class BaseRecyclerAdapter<T, H extends BaseViewHolder<T>> extends RecyclerView.Adapter<H> {

    protected List<T> itemList;
    protected Context context;
    protected OnItemClickListener onItemClickListener;

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull H holder, int position) {
        T item = getItem(position);
        holder.bind(context, item, position, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        if (this.itemList == null) {
            return 0;
        }
        return this.itemList.size();
    }

    public void updateItems(List<T> items) {
        if (this.itemList == null) {
            itemList = new ArrayList<>();
        }
        this.itemList.clear();
        this.itemList.addAll(items);

        notifyDataSetChanged();
    }

    public void addItems(List<T> items) {
        if (this.itemList == null) {
            this.itemList = items;
            notifyDataSetChanged();
        } else {
            int position = this.itemList.size();
            this.itemList.addAll(items);
            notifyItemRangeInserted(position, items.size());
        }
    }

    public void clearItems() {
        if (itemList != null) {
            itemList.clear();
            notifyDataSetChanged();
        }
    }

    public void removeItem(int position) {
        if (this.itemList != null && position < this.itemList.size()) {
            this.itemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, this.itemList.size());
        }
    }

    public T getItem(int position) {
        if (this.itemList == null) {
            return null;
        }

        return this.itemList.get(position);
    }
}
