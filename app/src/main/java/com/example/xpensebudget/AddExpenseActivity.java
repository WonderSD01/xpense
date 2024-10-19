package com.example.xpensebudget;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        binding=ActivityAddExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        type=getIntent().getStringExtra("type");
        expenseModel=(ExpenseModel) getIntent().getSerializableExtra("model");

        if (type==null){
            type= expenseModel.getType();
            binding.amount.setText(String.valueOf(expenseModel.getAmount()));
            binding.category.setText(expenseModel.getCategory());
            binding.description.setText(expenseModel.getDescription());
        }

        if(type.equals("Income")){
            binding.incomeRadio.setChecked(true);
        }else {
            binding.expenseRadio.setChecked(true);
        }

        binding.incomeRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type= "Income";
            }
        });
        binding.expenseRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type= "Expense";
            }
        });



    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater menuInflater=getMenuInflater();
            if (type!=null){

                menuInflater.inflate(R.menu.add_menu,menu);

            }else {
                menuInflater.inflate(R.menu.update_menu,menu);
            }

            return true;
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.saveExpense){
            if (type!=null){
                createExpense();
            }else {
                updateExpense();
            }

            return true;
        }
        if (id==R.id.deleteExpense){
            deleteExpense();
        }
        return false;
    }

    private void deleteExpense() {
    }

    private void createExpense() {

        String expenseId= UUID.randomUUID().toString();
        String amount=binding.amount.getText().toString();
        String description=binding.description.getText().toString();
        String category=binding.category.getText().toString();
        boolean incomeChecked=binding.incomeRadio.isChecked();
        if (incomeChecked){
            type="Income";
        }else{
            type= "Expense";
        }

        if (amount.trim().length()==0){
            binding.amount.setError("Empty");
            return;
        }
        ExpenseModel expenseModel=new ExpenseModel(expenseId,description,category,type,Long.parseLong(amount), Calendar.getInstance().getTimeInMillis());

        //Firebase Inclusion
    }
    private void updateExpense() {

        String expenseId= expenseModel.getExpenseId();
        String amount=binding.amount.getText().toString();
        String description=binding.description.getText().toString();
        String category=binding.category.getText().toString();
        boolean incomeChecked=binding.incomeRadio.isChecked();

        if (incomeChecked){
            type="Income";
        }else{
            type= "Expense";
        }

        if (amount.trim().length()==0){
            binding.amount.setError("Empty");
            return;
        }
        ExpenseModel model=new ExpenseModel(expenseId,description,category,type,Long.parseLong(amount), Calendar.getInstance().getTimeInMillis());

        //Firebase Inclusion
    }
}