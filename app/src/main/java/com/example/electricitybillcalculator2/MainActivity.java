package com.example.electricitybillcalculator2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerMonth;
    private SeekBar seekBarRebate;
    private TextView txtRebateValue, txtMonthResult, txtChargesResult, txtRebateResult, txtFinalResult;
    private EditText inputUnit;
    private Button btnCalculate, btnHistory, btnAbout;
    private CardView cardResult;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        initViews();
        setupSpinner();
        setupSeekBar();
        setupButtons();
    }

    private void initViews() {
        spinnerMonth = findViewById(R.id.spinnerMonth);
        seekBarRebate = findViewById(R.id.seekBarRebate);
        txtRebateValue = findViewById(R.id.txtRebateValue);
        inputUnit = findViewById(R.id.inputUnit);

        btnCalculate = findViewById(R.id.btnCalculate);
        btnHistory = findViewById(R.id.btnHistory);
        btnAbout = findViewById(R.id.btnAbout);

        txtMonthResult = findViewById(R.id.txtMonthResult);
        txtChargesResult = findViewById(R.id.txtChargesResult);
        txtRebateResult = findViewById(R.id.txtRebateResult);
        txtFinalResult = findViewById(R.id.txtFinalResult);

        cardResult = findViewById(R.id.cardResult);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.months,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);
    }

    private void setupSeekBar() {
        txtRebateValue.setText(getString(R.string.label_selected_rebate, 0));
        seekBarRebate.setMax(5);

        seekBarRebate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtRebateValue.setText(getString(R.string.label_selected_rebate, progress));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setupButtons() {
        btnCalculate.setOnClickListener(v -> calculateBill());
        btnHistory.setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
        btnAbout.setOnClickListener(v -> startActivity(new Intent(this, AboutActivity.class)));
    }

    private void calculateBill() {
        String unitStr = inputUnit.getText().toString().trim();

        if (unitStr.isEmpty()) {
            inputUnit.setError(getString(R.string.error_enter_units));
            return;
        }

        try {
            double unit = Double.parseDouble(unitStr);

            if (unit <= 0) {
                inputUnit.setError(getString(R.string.error_units_positive));
                return;
            }

            if (unit > 1000) {
                inputUnit.setError(getString(R.string.error_units_max));
                return;
            }

            double totalCharges = calculateTariff(unit);
            int rebate = seekBarRebate.getProgress();
            double finalCost = totalCharges - (totalCharges * rebate / 100.0);

            displayResult(totalCharges, rebate, finalCost);

            dbHelper.addBill(new BillModel(
                    spinnerMonth.getSelectedItem().toString(),
                    unit,
                    totalCharges,
                    rebate,
                    finalCost
            ));

        } catch (NumberFormatException e) {
            inputUnit.setError(getString(R.string.error_invalid_number));
        }
    }

    /**
     * Tariff calculation (MATCH SAMPLE TABLE)
     */
    private double calculateTariff(double unit) {
        double total = 0;

        if (unit <= 200) {
            total = unit * 0.218;
        } else if (unit <= 300) {
            total = (200 * 0.218)
                    + ((unit - 200) * 0.334);
        } else if (unit <= 600) {
            total = (200 * 0.218)
                    + (100 * 0.334)
                    + ((unit - 300) * 0.516);
        } else { // 601 - 1000
            total = (200 * 0.218)
                    + (100 * 0.334)
                    + (300 * 0.516)
                    + ((unit - 600) * 0.546);
        }

        return total;
    }

    private void displayResult(double total, int rebate, double finalCost) {
        txtMonthResult.setText(getString(R.string.label_month) + " " + spinnerMonth.getSelectedItem());
        txtChargesResult.setText(String.format(getString(R.string.label_total_charges), total));
        txtRebateResult.setText(String.format(getString(R.string.label_rebate), rebate));
        txtFinalResult.setText(String.format(getString(R.string.label_final_cost), finalCost));

        cardResult.setVisibility(View.VISIBLE);
    }
}
