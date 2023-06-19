package com.example.productivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TextViewHolder>{
    private List<TodoModel> textList;
    private OnItemClickListener listener;

    public TodoAdapter(List<TodoModel> textList) {
        this.textList = textList;
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent,false);
        return new TextViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
        TodoModel textModel = textList.get(position);
        holder.todoTitle.setText(textModel.getTitle());
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView todoTitle;
        public TextViewHolder(View itemView) {
            super(itemView);
            todoTitle =  itemView.findViewById(R.id.todoI_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
