package com.example.electricitybillcalculator2;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class EditActivity extends AppCompatActivity {

    private Spinner spinnerMonth;
    private SeekBar seekBarRebate;
    private TextView txtRebateValue;
    private EditText inputUnit;
    private Button btnSave, btnCancel;
    private CardView cardEditForm;

    private DatabaseHelper dbHelper;
    private BillModel currentBill;
    private int billId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        initViews();

        dbHelper = new DatabaseHelper(this);
        billId = getIntent().getIntExtra("BILL_ID", -1);

        if (billId == -1) {
            Toast.makeText(this, "Invalid bill ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupSpinner();
        setupSeekBar();
        loadBillData();
        setupButtons();
    }

    private void initViews() {
        spinnerMonth = findViewById(R.id.spinnerMonth);
        seekBarRebate = findViewById(R.id.seekBarRebate);
        txtRebateValue = findViewById(R.id.txtRebateValue);
        inputUnit = findViewById(R.id.inputUnit);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        cardEditForm = findViewById(R.id.cardEditForm);
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

    private void loadBillData() {
        currentBill = dbHelper.getBill(billId);

        if (currentBill != null) {
            String month = currentBill.getMonth();
            ArrayAdapter<CharSequence> adapter =
                    (ArrayAdapter<CharSequence>) spinnerMonth.getAdapter();

            int pos = adapter.getPosition(month);
            if (pos >= 0) spinnerMonth.setSelection(pos);

            inputUnit.setText(String.valueOf(currentBill.getUnit()));

            int rebate = (int) currentBill.getRebate();
            seekBarRebate.setProgress(rebate);
            txtRebateValue.setText(getString(R.string.label_selected_rebate, rebate));
        }
    }

    private void setupButtons() {
        btnSave.setOnClickListener(v -> updateBill());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void updateBill() {
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

            int rebate = seekBarRebate.getProgress();
            double totalCharges = calculateTariff(unit);
            double finalCost = totalCharges - (totalCharges * rebate / 100.0);

            BillModel updated = new BillModel();
            updated.setId(billId);
            updated.setMonth(spinnerMonth.getSelectedItem().toString());
            updated.setUnit(unit);
            updated.setRebate(rebate);
            updated.setTotalCharges(totalCharges);
            updated.setFinalCost(finalCost);

            if (dbHelper.updateBill(updated)) {
                Toast.makeText(this, "Bill updated successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException e) {
            inputUnit.setError(getString(R.string.error_invalid_number));
        }
    }

    /**
     * SAME tariff logic as MainActivity
     */
    private double calculateTariff(double unit) {
        if (unit <= 200) {
            return unit * 0.218;
        } else if (unit <= 300) {
            return (200 * 0.218)
                    + ((unit - 200) * 0.334);
        } else if (unit <= 600) {
            return (200 * 0.218)
                    + (100 * 0.334)
                    + ((unit - 300) * 0.516);
        } else {
            return (200 * 0.218)
                    + (100 * 0.334)
                    + (300 * 0.516)
                    + ((unit - 600) * 0.546);
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
