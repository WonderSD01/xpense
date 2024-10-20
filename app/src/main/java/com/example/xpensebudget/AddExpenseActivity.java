package com.example.xpensebudget;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.xpensebudget.databinding.ActivityAddExpenseBinding;

import java.util.Calendar;
import java.util.UUID;

public class AddExpenseActivity extends AppCompatActivity {
    ActivityAddExpenseBinding binding;
    public String type;
    private ExpenseModel expenseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set the Toolbar as the ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);  // Using the correct method from AppCompatActivity

        // Fetch intent data (type and model)
        type = getIntent().getStringExtra("type");
        expenseModel = (ExpenseModel) getIntent().getSerializableExtra("model");

        // If no type is provided, treat this as an update case
        if (type == null && expenseModel != null) {
            type = expenseModel.getType();
            binding.amount.setText(String.valueOf(expenseModel.getAmount()));
            binding.category.setText(expenseModel.getCategory());
            binding.description.setText(expenseModel.getDescription());
        }

        // Set the selected radio button based on the type (Income or Expense)
        if ("Income".equals(type)) {
            binding.incomeRadio.setChecked(true);
        } else {
            binding.expenseRadio.setChecked(true);
        }

        // Set click listeners for radio buttons
        binding.incomeRadio.setOnClickListener(v -> type = "Income");
        binding.expenseRadio.setOnClickListener(v -> type = "Expense");
    }

    // Inflate the appropriate menu based on whether it's an add or update action
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        if (expenseModel == null) {
            // If model is null, we're adding a new expense
            menuInflater.inflate(R.menu.add_menu, menu);
        } else {
            // If model is not null, we're updating an existing expense
            menuInflater.inflate(R.menu.update_menu, menu);
        }

        return true;
    }

    // Handle menu item selections
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Handle Save action
        if (id == R.id.saveExpense) {
            if (expenseModel == null) {
                createExpense();  // Adding a new expense
            } else {
                updateExpense();  // Updating an existing expense
            }
            return true;
        }

        // Handle Delete action (only available in update_menu)
        if (id == R.id.deleteExpense) {
            deleteExpense();  // Delete the expense
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Function to delete an expense
    private void deleteExpense() {
        // TODO: Implement the logic to delete an expense
    }

    // Function to create a new expense
    private void createExpense() {
        String expenseId = UUID.randomUUID().toString();
        String amount = binding.amount.getText().toString();
        String description = binding.description.getText().toString();
        String category = binding.category.getText().toString();
        boolean incomeChecked = binding.incomeRadio.isChecked();

        if (incomeChecked) {
            type = "Income";
        } else {
            type = "Expense";
        }

        if (amount.trim().length() == 0) {
            binding.amount.setError("Empty");
            return;
        }

        ExpenseModel expenseModel = new ExpenseModel(
                expenseId, description, category, type,
                Long.parseLong(amount), Calendar.getInstance().getTimeInMillis()
        );

        // TODO: Add Firebase or database logic to store the new expense
    }

    // Function to update an existing expense
    private void updateExpense() {
        String expenseId = expenseModel.getExpenseId();
        String amount = binding.amount.getText().toString();
        String description = binding.description.getText().toString();
        String category = binding.category.getText().toString();
        boolean incomeChecked = binding.incomeRadio.isChecked();

        if (incomeChecked) {
            type = "Income";
        } else {
            type = "Expense";
        }

        if (amount.trim().length() == 0) {
            binding.amount.setError("Empty");
            return;
        }

        ExpenseModel updatedModel = new ExpenseModel(
                expenseId, description, category, type,
                Long.parseLong(amount), Calendar.getInstance().getTimeInMillis()
        );

        // TODO: Add Firebase or database logic to update the existing expense
    }
}
