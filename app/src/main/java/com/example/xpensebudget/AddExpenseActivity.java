package com.example.xpensebudget;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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
    private EditText dailyBudgetInput;
    private Button saveBudgetButton;
    SharedPreferences sharedPreferences;

    private float totalIncome;
    private float totalExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set the Toolbar as the ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize views
        dailyBudgetInput = findViewById(R.id.dailyBudget);
        saveBudgetButton = findViewById(R.id.saveBudgetButton);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("BudgetPrefs", MODE_PRIVATE);

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

        // Save the daily budget when save button is clicked
        saveBudgetButton.setOnClickListener(v -> {
            String budget = dailyBudgetInput.getText().toString();
            if (!budget.isEmpty()) {
                // Save budget to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("daily_budget", budget);
                editor.apply();
                dailyBudgetInput.setText("");  // Clear the input field
                Toast.makeText(AddExpenseActivity.this, "Budget saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddExpenseActivity.this, "Please enter a valid budget", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Inflate the appropriate menu based on whether it's an add or update action
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if (expenseModel == null) {
            menuInflater.inflate(R.menu.add_menu, menu);  // Adding a new expense
        } else {
            menuInflater.inflate(R.menu.update_menu, menu);  // Updating an existing expense
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
        Toast.makeText(this, "Expense deleted!", Toast.LENGTH_SHORT).show();  // Placeholder
    }

    // Function to create a new expense
    private void createExpense() {
        String expenseId = UUID.randomUUID().toString();
        String amount = binding.amount.getText().toString();
        String description = binding.description.getText().toString();
        String category = binding.category.getText().toString();
        boolean incomeChecked = binding.incomeRadio.isChecked();

        type = incomeChecked ? "Income" : "Expense";

        if (amount.trim().length() == 0) {
            binding.amount.setError("Empty");
            return;
        }

        ExpenseModel newExpenseModel = new ExpenseModel(
                expenseId, description, category, type,
                Long.parseLong(amount), Calendar.getInstance().getTimeInMillis()
        );

        // Update income or expense totals
        if (type.equals("Expense")) {
            totalExpense = getCurrentTotalExpenses() + Long.parseLong(amount);
            saveTotalExpense(totalExpense);
        } else {
            totalIncome = getCurrentTotalIncome() + Long.parseLong(amount);
            saveTotalIncome(totalIncome);
        }

        // TODO: Add Firebase or database logic to store the new expense
        Toast.makeText(this, "Expense added!", Toast.LENGTH_SHORT).show();  // Placeholder
    }

    // Function to update an existing expense
    private void updateExpense() {
        String expenseId = expenseModel.getExpenseId();
        String amount = binding.amount.getText().toString();
        String description = binding.description.getText().toString();
        String category = binding.category.getText().toString();
        boolean incomeChecked = binding.incomeRadio.isChecked();

        type = incomeChecked ? "Income" : "Expense";

        if (amount.trim().length() == 0) {
            binding.amount.setError("Empty");
            return;
        }

        // Calculate the new totals based on type
        if (type.equals("Expense")) {
            totalExpense = getCurrentTotalExpenses() + Long.parseLong(amount);
            saveTotalExpense(totalExpense);
        } else {
            totalIncome = getCurrentTotalIncome() + Long.parseLong(amount);
            saveTotalIncome(totalIncome);
        }

        ExpenseModel updatedModel = new ExpenseModel(
                expenseId, description, category, type,
                Long.parseLong(amount), Calendar.getInstance().getTimeInMillis()
        );

        // TODO: Add Firebase or database logic to update the existing expense
        Toast.makeText(this, "Expense updated!", Toast.LENGTH_SHORT).show();  // Placeholder
    }

    // Method to get current total expenses from SharedPreferences
    private float getCurrentTotalExpenses() {
        return sharedPreferences.getFloat("total_expense", 0);
    }

    // Method to get current total income from SharedPreferences
    private float getCurrentTotalIncome() {
        return sharedPreferences.getFloat("total_income", 0);
    }

    // Method to save total expense to SharedPreferences
    private void saveTotalExpense(float totalExpense) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("total_expense", totalExpense);
        editor.apply();
    }

    // Method to save total income to SharedPreferences
    private void saveTotalIncome(float totalIncome) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("total_income", totalIncome);
        editor.apply();
    }
}
