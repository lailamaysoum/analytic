package com.example.noteanalytic1;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.ViewHolder> {

    private List<Category> cData;
    private LayoutInflater mInflater;
    private ItemClickListener cClickListener;

    CatAdapter(Context context, List<Category> data , ItemClickListener onClick) {
        this.mInflater = LayoutInflater.from(context);
        this.cData = data;
        this.cClickListener = onClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cat_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.cat.setText(cData.get(position).getName());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cClickListener.onItemClick(holder.getAdapterPosition(), cData.get(position).id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView cat;
        public CardView card;

        ViewHolder(View itemView) {
            super(itemView);
            this.cat = itemView.findViewById(R.id.cat);
            this.card = itemView.findViewById(R.id.card);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // يمكنك إضافة السلوك المطلوب هنا في حالة النقر على عنصر القائمة
        }
    }

    Category getItem(int id) {
        return cData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.cClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position, String id);
    }
}
