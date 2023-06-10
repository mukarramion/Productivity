package com.example.productivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SuggestionTextAdapter extends RecyclerView.Adapter<SuggestionTextAdapter.TextViewHolder> {

    private List<TextModel> textList;

    public SuggestionTextAdapter(List<TextModel> textList) {
        this.textList = textList;
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion, parent,false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
        TextModel textModel = textList.get(position);
        holder.suggestionTitle.setText(textModel.getTitle());
        holder.suggestionDescription.setText(textModel.getDescription());
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }


    public static class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView suggestionTitle;
        private TextView suggestionDescription;
        public TextViewHolder(View itemView) {
            super(itemView);
            suggestionTitle =  itemView.findViewById(R.id.suggestion_title);
            suggestionDescription = itemView.findViewById(R.id.suggestion_content);
        }
    }
}


