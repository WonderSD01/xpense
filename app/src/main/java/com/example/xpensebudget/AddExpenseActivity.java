package com.example.xpensebudget;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.xpensebudget.databinding.ActivityAddExpenseBinding;

import java.util.Calendar;
import java.util.UUID;

public class AddExpenseActivity extends AppCompatActivity {

    private ActivityAddExpenseBinding binding;
    private String type;
    private ExpenseModel expenseModel;
    private SharedPreferences sharedPreferences;

    private long totalIncome;
    private long totalExpense;

    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 100;
    private Uri imageUri;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    binding.receiptImageView.setImageURI(imageUri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        initializeViews();
        initializeSharedPreferences();
        handleIntentData();
        setRadioButtonListeners();
        binding.saveBudgetButton.setOnClickListener(v -> saveDailyBudget());
        binding.selectImageButton.setOnClickListener(v -> handleImageUpload());
        checkPermission();
    }

    private void initializeViews() {
        binding.dailyBudget.setText("");
    }

    private void initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences("BudgetPrefs", MODE_PRIVATE);
    }

    private void handleIntentData() {
        type = getIntent().getStringExtra("type");
        expenseModel = (ExpenseModel) getIntent().getSerializableExtra("model");

        if (expenseModel != null) {
            type = expenseModel.getType();
            binding.amount.setText(String.valueOf(expenseModel.getAmount()));
            binding.category.setText(expenseModel.getCategory());
            binding.description.setText(expenseModel.getDescription());
        }

        updateRadioButtons();
    }

    private void updateRadioButtons() {
        boolean isIncome = "Income".equals(type);
        binding.incomeRadio.setChecked(isIncome);
        binding.expenseRadio.setChecked(!isIncome);
    }

    private void setRadioButtonListeners() {
        binding.incomeRadio.setOnClickListener(v -> type = "Income");
        binding.expenseRadio.setOnClickListener(v -> type = "Expense");
    }

    private void saveDailyBudget() {
        String budget = binding.dailyBudget.getText().toString();
        if (!budget.isEmpty()) {
            sharedPreferences.edit().putString("daily_budget", budget).apply();
            binding.dailyBudget.setText("");
            showToast("Budget saved!");
        } else {
            showToast("Please enter a valid budget");
        }
    }

    private void handleImageUpload() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openImagePicker();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                showToast("Permission denied to read storage");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(expenseModel == null ? R.menu.add_menu : R.menu.update_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.saveExpense) {
            if (expenseModel == null) {
                createExpense(); // Create new expense
            } else {
                updateExpense(); // Update existing expense
            }
            return true;
        } else if (item.getItemId() == R.id.deleteExpense) {
            deleteExpense(); // Delete the expense
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void createExpense() {
        if (validateAmount()) {
            long amount = Long.parseLong(binding.amount.getText().toString());
            String expenseId = UUID.randomUUID().toString();
            updateTotalAmount(amount);
            // TODO: Add logic to save the new expense to Firebase or database
            showToast("Expense added!");
        }
    }

    private void updateExpense() {
        if (validateAmount()) {
            long amount = Long.parseLong(binding.amount.getText().toString());
            updateTotalAmount(amount);
            if (expenseModel != null) {
                ExpenseModel updatedModel = new ExpenseModel(
                        expenseModel.getExpenseId(),
                        binding.description.getText().toString(),
                        binding.category.getText().toString(),
                        type,
                        amount,
                        Calendar.getInstance().getTimeInMillis()
                );
                // TODO: Add logic to update the expense in Firebase or database
                showToast("Expense updated!");
            } else {
                showToast("No expense to update.");
            }
        }
    }

    private boolean validateAmount() {
        String amountStr = binding.amount.getText().toString();
        if (amountStr.trim().isEmpty()) {
            binding.amount.setError("Empty");
            return false;
        }
        return true;
    }

    private void deleteExpense() {
        // TODO: Implement the logic to delete an expense
        showToast("Expense deleted!");
    }

    private void updateTotalAmount(long amount) {
        if ("Expense".equals(type)) {
            totalExpense = getCurrentTotalExpenses() + amount;
            saveTotalExpense(totalExpense);
        } else {
            totalIncome = getCurrentTotalIncome() + amount;
            saveTotalIncome(totalIncome);
        }
    }

    private long getCurrentTotalExpenses() {
        return sharedPreferences.getLong("total_expense", 0);
    }

    private long getCurrentTotalIncome() {
        return sharedPreferences.getLong("total_income", 0);
    }

    private void saveTotalExpense(long totalExpense) {
        sharedPreferences.edit().putLong("total_expense", totalExpense).apply();
    }

    private void saveTotalIncome(long totalIncome) {
        sharedPreferences.edit().putLong("total_income", totalIncome).apply();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
