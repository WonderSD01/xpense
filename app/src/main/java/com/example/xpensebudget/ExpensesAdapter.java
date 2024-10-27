package com.example.xpensebudget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import com.example.xpensebudget.ExpenseModel; // Adjust the package as necessary


public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.MyViewHolder> {
    private final List<ExpenseModel> expenseModelList;
    private final Context context;
    private final OnItemClickListener onItemsClick;

    public interface OnItemClickListener {
        void onClick(ExpenseModel expenseModel);
    }

    public ExpensesAdapter(List<ExpenseModel> expenseModelList, Context context, OnItemClickListener onItemsClick) {
        this.expenseModelList = expenseModelList;
        this.context = context;
        this.onItemsClick = onItemsClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ExpenseModel expenseModel = expenseModelList.get(position);
        holder.description.setText(expenseModel.getDescription());
        holder.category.setText(expenseModel.getCategory());
        holder.amount.setText(String.valueOf(expenseModel.getAmount()));

        // Load the image using Glide
        if (expenseModel.getImageUri() != null && !expenseModel.getImageUri().isEmpty()) {
            Glide.with(context)
                    .load(expenseModel.getImageUri())
                    .into(holder.receiptImageView);
            holder.receiptImageView.setVisibility(View.VISIBLE);
        } else {
            holder.receiptImageView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> onItemsClick.onClick(expenseModel));
    }

    @Override
    public int getItemCount() {
        return expenseModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView description;
        private final TextView category;
        private final TextView amount;
        private final ImageView receiptImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            category = itemView.findViewById(R.id.category);
            amount = itemView.findViewById(R.id.amount);
            receiptImageView = itemView.findViewById(R.id.receiptImageView);
        }
    }
}
