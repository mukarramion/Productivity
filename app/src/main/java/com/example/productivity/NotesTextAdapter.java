package com.example.productivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesTextAdapter extends RecyclerView.Adapter<NotesTextAdapter.TextViewHolder>{
    private List<NotesTextModel> textList;
    private OnItemClickListener listener;

    public NotesTextAdapter(List<NotesTextModel> textList) {
        this.textList = textList;
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes, parent,false);
        return new TextViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
        NotesTextModel textModel = textList.get(position);
        holder.notesTitle.setText(textModel.getTitle());
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView notesTitle;
        public TextViewHolder(View itemView) {
            super(itemView);
            notesTitle =  itemView.findViewById(R.id.notes_title);
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
