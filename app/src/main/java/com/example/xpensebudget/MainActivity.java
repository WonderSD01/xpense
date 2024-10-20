package com.example.xpensebudget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.xpensebudget.databinding.ActivityMainBinding;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemsCLick {
    ActivityMainBinding binding;
    private ExpensesAdapter expensesAdapter;
    private Intent intent;

    // Declare income and expense variables
    private float income = 0; // Initialize as needed
    private float expense = 0; // Initialize as needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup RecyclerView
        expensesAdapter = new ExpensesAdapter(this, this);
        binding.recycler.setAdapter(expensesAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        // Setup Intent for adding income/expense
        intent = new Intent(MainActivity.this, AddExpenseActivity.class);

        // Handle add income button click
        binding.addIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type", "Income");
                startActivity(intent);
            }
        });

        // Handle add expense button click
        binding.addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("type", "Expense");
                startActivity(intent);
            }
        });

        // Call the method to set up the graph
        setUpGraph();
    }

    private void setUpGraph() {
        List<PieEntry> pieEntryList = new ArrayList<>();
        List<Integer> colorList = new ArrayList<>(); // Use Integer for colorList

        // Add income entry if income is greater than 0
        if (income != 0) {
            pieEntryList.add(new PieEntry(income, "Income"));
            colorList.add(ContextCompat.getColor(this, R.color.green)); // Use ContextCompat
        }
        // Add expense entry if expense is greater than 0
        if (expense != 0) {
            pieEntryList.add(new PieEntry(expense, "Expense"));
            colorList.add(ContextCompat.getColor(this, R.color.orange)); // Use ContextCompat
        }

        // Create a PieDataSet and set the colors
        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "Expenses");
        pieDataSet.setColors(colorList);

        // Create PieData and set it to the chart
        PieData pieData = new PieData(pieDataSet);
        binding.pieChart.setData(pieData);
        binding.pieChart.invalidate(); // Refresh the chart
    }

    @Override
    public void onClick(ExpenseModel expenseModel) {
        intent.putExtra("model", expenseModel);
        startActivity(intent);
    }

    // Uncomment and implement onStart if needed for Firebase or other data initialization
    // @Override
    // protected void onStart() {
    //     super.onStart();
    //     // Logic to fetch data and update income and expense values
    //     // Call setUpGraph() after updating income and expense values
    // }
}
