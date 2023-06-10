package com.example.productivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DeadlineTextAdapter extends RecyclerView.Adapter<DeadlineTextAdapter.TextViewHolder> {

    private List<TextModel> textList;

    public DeadlineTextAdapter(List<TextModel> textList) {
        this.textList = textList;
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deadline, parent,false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
        TextModel textModel = textList.get(position);
        holder.deadlineTitle.setText(textModel.getTitle());
        holder.deadlineDescription.setText(textModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }


    public static class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView deadlineTitle;
        private TextView deadlineDescription;
        public TextViewHolder(View itemView) {
            super(itemView);
            deadlineTitle =  itemView.findViewById(R.id.deadline_title);
            deadlineDescription = itemView.findViewById(R.id.deadline_date);
        }
    }
}


