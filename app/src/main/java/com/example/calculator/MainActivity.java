package com.example.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Locale;

public class MainActivity extends Activity {
    private TextView display;
    private double first = 0;
    private String operator = "";
    private boolean newNumber = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setPadding(20, 30, 20, 20);
        main.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("Calculator");
        title.setTextSize(24);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        main.addView(title, new LinearLayout.LayoutParams(-1, 70));

        display = new TextView(this);
        display.setText("0");
        display.setTextSize(42);
        display.setTextColor(Color.BLACK);
        display.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        display.setPadding(10, 10, 10, 20);
        main.addView(display, new LinearLayout.LayoutParams(-1, 0, 1));

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(4);
        grid.setRowCount(5);

        String[] keys = {
            "C", "⌫", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "−",
            "1", "2", "3", "+",
            "0", ".", "="
        };

        for (String key : keys) {
            Button b = new Button(this);
            b.setText(key);
            b.setTextSize(22);
            b.setOnClickListener(v -> press(((Button) v).getText().toString()));

            GridLayout.LayoutParams p = new GridLayout.LayoutParams();
            p.width = 0;
            p.height = 0;
            p.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            p.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            p.setMargins(4, 4, 4, 4);
            grid.addView(b, p);
        }

        main.addView(grid, new LinearLayout.LayoutParams(-1, 0, 2));
        setContentView(main);
    }

    private void press(String key) {
        if (key.matches("[0-9]") || key.equals(".")) {
            if (newNumber) {
                display.setText(key.equals(".") ? "0." : key);
                newNumber = false;
            } else if (key.equals(".") && display.getText().toString().contains(".")) {
                return;
            } else {
                display.append(key);
            }
            return;
        }

        if (key.equals("C")) {
            first = 0;
            operator = "";
            newNumber = true;
            display.setText("0");
            return;
        }

        if (key.equals("⌫")) {
            String s = display.getText().toString();
            if (s.length() > 1) {
                display.setText(s.substring(0, s.length() - 1));
            } else {
                display.setText("0");
            }
            return;
        }

        if (key.equals("%")) {
            double value = Double.parseDouble(display.getText().toString()) / 100.0;
            display.setText(format(value));
            newNumber = true;
            return;
        }

        if ("÷×−+".contains(key)) {
            first = Double.parseDouble(display.getText().toString());
            operator = key;
            newNumber = true;
            return;
        }

        if (key.equals("=") && !operator.isEmpty()) {
            double second = Double.parseDouble(display.getText().toString());
            double result;

            if (operator.equals("+")) result = first + second;
            else if (operator.equals("−")) result = first - second;
            else if (operator.equals("×")) result = first * second;
            else {
                if (second == 0) {
                    display.setText("Error");
                    operator = "";
                    newNumber = true;
                    return;
                }
                result = first / second;
            }

            display.setText(format(result));
            operator = "";
            newNumber = true;
        }
    }

    private String format(double value) {
        if (value == (long) value) {
            return String.format(Locale.US, "%d", (long) value);
        }
        return String.format(Locale.US, "%.8f", value)
                .replaceAll("0+$", "")
                .replaceAll("\\.$", "");
    }
}
