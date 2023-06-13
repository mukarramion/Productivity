package com.example.productivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.PdfViewHolder> {

    private List<PdfItem> pdfList;
    private PdfItemClickListener itemClickListener;

    public PdfAdapter(PdfItemClickListener itemClickListener) {
        this.pdfList = new ArrayList<>();
        this.itemClickListener = itemClickListener;
    }

    public void setPdfList(List<PdfItem> pdfList) {
        this.pdfList = pdfList;
        notifyDataSetChanged();
    }

    @Override
    public PdfViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf, parent, false);
        return new PdfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PdfViewHolder holder, int position) {
        PdfItem pdfItem = pdfList.get(position);
        holder.bind(pdfItem);
    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    interface PdfItemClickListener {
        void onItemClick(PdfItem pdfItem);
    }

    class PdfViewHolder extends RecyclerView.ViewHolder {

        private TextView pdfTitleTextView;

        public PdfViewHolder(View itemView) {
            super(itemView);
            pdfTitleTextView = itemView.findViewById(R.id.pdfTitleTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        PdfItem pdfItem = pdfList.get(position);
                        itemClickListener.onItemClick(pdfItem);
                    }
                }
            });
        }

        public void bind(PdfItem pdfItem) {
            pdfTitleTextView.setText(pdfItem.getFileName());
        }
    }
}

