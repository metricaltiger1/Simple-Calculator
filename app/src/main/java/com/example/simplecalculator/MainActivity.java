package com.example.simplecalculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.mariuszgromada.math.mxparser.Expression;

@SuppressWarnings("MalformedFormatString")
public class MainActivity extends AppCompatActivity {
    private EditText display;
    private TextView displayR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.input);
        display.setShowSoftInputOnFocus(false);

        displayR = findViewById(R.id.displayR);

        display.setOnClickListener(v -> {
            if (getString(R.string.display).equals(display.getText().toString())) {
                display.setText("");
            }
        });

        display.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateExpression(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void updateText(String strToAdd) {
        String oldStr = display.getText().toString();
        int cursorPos = display.getSelectionStart();
        String leftStr = oldStr.substring(0, cursorPos);
        String rightStr = oldStr.substring(cursorPos);
        if (getString(R.string.display).equals(display.getText().toString())) {
            display.setText(strToAdd);
            display.setSelection(strToAdd.length());
        } else {
            String newText = String.format("%s%s%s", leftStr, strToAdd, rightStr);
            display.setText(newText);
            display.setSelection(leftStr.length() + strToAdd.length());
        }
    }

    public void zeroBTN(View view) {
        updateText("0");
    }

    public void oneBTN(View view) {
        updateText("1");
    }

    public void twoBTN(View view) {
        updateText("2");
    }

    public void threeBTN(View view) {
        updateText("3");
    }

    public void fourBTN(View view) {
        updateText("4");
    }

    public void fiveBTN(View view) {
        updateText("5");
    }

    public void sixBTN(View view) {
        updateText("6");
    }

    public void sevenBTN(View view) {
        updateText("7");
    }

    public void eightBTN(View view) {
        updateText("8");
    }

    public void nineBTN(View view) {
        updateText("9");
    }

    public void clearBTN(View view) {
        display.setText("0");
        displayR.setText("0");
    }

    public void addBTN(View view) {
        handleOperation("+");
    }

    public void subtractBTN(View view) {
        handleOperation("-");
    }

    public void divideBTN(View view) {
        handleOperation("÷");
    }

    public void multiplyBTN(View view) {
        handleOperation("×");
    }

    public void backspaceBTN(View view) {
        int cursorPos = display.getSelectionStart();
        int textLen = display.getText().length();

        if (cursorPos != 0 && textLen != 0) {
            display.getText().delete(cursorPos - 1, cursorPos);
            display.setSelection(cursorPos - 1);
        }
    }

    public void parenthesisBTN(View view) {
        int cursorPos = display.getSelectionStart();
        int openPar = 0;
        int closedPar = 0;
        int textLen = display.getText().length();

        for (int i = 0; i < cursorPos; i++) {
            if (display.getText().toString().charAt(i) == '(') {
                openPar += 1;
            }
            if (display.getText().toString().charAt(i) == ')') {
                closedPar += 1;
            }
        }

        if (openPar == closedPar || display.getText().toString().charAt(textLen - 1) == '(') {
            updateText("(");
            display.setSelection(cursorPos + 1);
        } else if (closedPar < openPar && display.getText().toString().charAt(textLen - 1) != '(') {
            updateText(")");
            display.setSelection(cursorPos + 1);
        }
    }

    public void exponentBTN(View view) {
        handleOperation("^");
    }

    public void pointBTN(View view) {
        updateText(".");
    }

    public void squareRootBTN(View view) {
        updateText("√");
    }

    public void sineBTN(View view) {
        String inputText = display.getText().toString();
        String newExpression = String.format("sin(", inputText);
        updateText(newExpression);
    }

    public void cosineBTN(View view) {
        String inputText = display.getText().toString();
        String newExpression = String.format("cos(", inputText);
        updateText(newExpression);
    }

    public void tangentBTN(View view) {
        String inputText = display.getText().toString();
        String newExpression = String.format("taan(", inputText);
        updateText(newExpression);
    }

    public void equalBTN(View view) {
        String result = displayR.getText().toString();
        display.setText(result);
        display.setSelection(result.length());
    }
    public void logBTN(View view) {
        String newExpression = "log10(";
        updateText(newExpression);
    }
    public void piBTN(View view) {
        updateText("π");
    }

    private void handleOperation(String operation) {
        String currentText = display.getText().toString();
        int cursorPos = display.getSelectionStart();

        if (currentText.length() > 0 && cursorPos > 0) {
            char prevChar = currentText.charAt(cursorPos - 1);
            if (isOperator(prevChar)) {
                display.setSelection(cursorPos);
            } else {
                updateText(operation);
            }
        }
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '×' || c == '÷';
    }

    @SuppressLint("DefaultLocale")
    private void calculateExpression(String expression) {
        // Replace division and multiplication signs with / and *
        expression = expression.replace("÷", "/");
        expression = expression.replace("×", "*");
        Expression exp = new Expression(expression);

        String result;
        if (exp.checkSyntax()) {
            double calculatedResult = exp.calculate();
            if (Double.isNaN(calculatedResult) || Double.isInfinite(calculatedResult)) {
                result = "Math Error";
            } else if (calculatedResult == (long) calculatedResult) {
                result = String.format("%d", (long) calculatedResult);
            } else {
                result = String.format("%.8f", calculatedResult).replaceAll("0*$", "").replaceAll("\\.$", "");
            }
        } else {
            result = "Error";
        }

        displayR.setText(result);
    }
}

