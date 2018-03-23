
/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/

package com.example.bishram.justjava;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    TextView quantityTextView;
    TextView priceTextView;
    TextView summaryTextView;
    EditText customerNameEditText;
    CheckBox whippedCreamCheckbox;
    CheckBox chocolateCheckbox;

    int quantity = 1;
    int summaryTextSize;
    int summaryTextColor;
    int count = 1;
    double pricePerCup = 7.25;
    double totalPrice;
    String summaryText = "";
    String customerName = "";
    boolean whippedCreamChecked;
    boolean chocolateChecked;

    private static final String KEY_QUANTITY = "key_quantity";
    private static final String KEY_TOTAL_PRICE = "key_total_price";
    private static final String KEY_SUMMARY_TEXT = "key_summary_text";
    private static final String KEY_SUMMARY_TEXT_SIZE = "key_summary_text_size";
    private static final String KEY_SUMMARY_TEXT_COLOR = "key_summary_text_color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Read all the views from the xml layout.
        initializeViews();

        totalPrice = calculatePrice();
        display(quantity);
        displayPrice(totalPrice);

        summaryText = getText(R.string.text_summary_pre_message).toString();
        summaryTextSize = 12;
        summaryTextColor = Color.RED;
        displayMessage(summaryText, summaryTextSize, summaryTextColor);
    }

    /**
     * This method will get all the views from the layout resource file.
     */
    public void initializeViews(){
        quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        priceTextView = (TextView) findViewById(R.id.price_text_view);
        whippedCreamCheckbox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        chocolateCheckbox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        summaryTextView = (TextView) findViewById(R.id.summary_text_view);
        customerNameEditText = (EditText) findViewById(R.id.edit_text_customer_name);
    }

    /**
     * This method will save the current state of the values which are not to changed
     * on orientation change or other change in state.
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_QUANTITY, quantity);
        outState.putDouble(KEY_TOTAL_PRICE, totalPrice);
        outState.putString(KEY_SUMMARY_TEXT, summaryText);
        outState.putInt(KEY_SUMMARY_TEXT_SIZE, summaryTextSize);
        outState.putInt(KEY_SUMMARY_TEXT_COLOR, summaryTextColor);
    }

    /**
     * This method restores the values saved on onSaveInstanceState.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        quantity = savedInstanceState.getInt(KEY_QUANTITY);
        totalPrice = savedInstanceState.getDouble(KEY_TOTAL_PRICE);
        summaryText = savedInstanceState.getString(KEY_SUMMARY_TEXT);
        summaryTextSize = savedInstanceState.getInt(KEY_SUMMARY_TEXT_SIZE);
        summaryTextColor = savedInstanceState.getInt(KEY_SUMMARY_TEXT_COLOR);

        display(quantity);
        displayPrice(totalPrice);
        displayMessage(summaryText, summaryTextSize, summaryTextColor);
    }

    /**
     * This method is called when PLUS BUTTON is clicked.
     */
    public void increment(View view) {
        quantity++;
        totalPrice = calculatePrice();
        display(quantity);
        displayPrice(totalPrice);

        if (count == 1) {
            summaryText = getText(R.string.text_summary_pre_message).toString();
            summaryTextSize = 12;
            summaryTextColor = Color.RED;
            displayMessage(summaryText, summaryTextSize, summaryTextColor);
            count++;
        }
    }

    /**
     * This method is called when MINUS BUTTON is clicked.
     */
    public void decrement(View view) {
        quantity--;
        totalPrice = calculatePrice();
        display(quantity);
        displayPrice(totalPrice);

        if (count==1) {
            summaryText = getText(R.string.text_summary_pre_message).toString();
            summaryTextSize = 12;
            summaryTextColor = Color.RED;
            displayMessage(summaryText, summaryTextSize, summaryTextColor);
            count++;
        }
    }

    /**
     * This method calculates the total price to be paid.
     */
    private double calculatePrice() {
        return pricePerCup * quantity;
    }

    /**
     * This method create the order summary text shown and send to customer's mail.
     *
     * @return orderSummary.
     */
    private String createOrderSummary() {
        customerName = customerNameEditText.getText().toString();
        String orderSummary = "Name : " + customerName;
        orderSummary = orderSummary + "\nQuantity : " + quantity;
        orderSummary = orderSummary + "\nTotal Price : \u20B9 " + totalPrice;
        orderSummary = orderSummary + "\nThank you for the Order!";
        return orderSummary;
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        count = 1;
        summaryText = createOrderSummary();
        summaryTextSize = 16;
        summaryTextColor = Color.BLACK;
        displayMessage(summaryText, summaryTextSize, summaryTextColor);

        count = 1;
        quantity = 1;
        display(quantity);
        totalPrice = 7.25;
        displayPrice(totalPrice);
        whippedCreamCheckbox.setChecked(false);
        chocolateCheckbox.setChecked(false);
        customerNameEditText.getText().clear();
    }

    /*
     * This method will handle the checkbox clicks
     */
    public void checkboxClicked(View view){
        whippedCreamChecked = whippedCreamCheckbox.isChecked();
        chocolateChecked = chocolateCheckbox.isChecked();
        if (count==1) {
            summaryText = getText(R.string.text_summary_pre_message).toString();
            summaryTextSize = 12;
            summaryTextColor = Color.RED;
            displayMessage(summaryText, summaryTextSize, summaryTextColor);
            count++;
        }

        switch (view.getId()){
            case R.id.whipped_cream_checkbox:
                if (whippedCreamChecked) {
                    if (chocolateChecked) {
                        quantity = 1;
                        pricePerCup = 10.25;
                        display(quantity);
                        displayPrice(pricePerCup);
                    }
                    else {
                        quantity = 1;
                        pricePerCup = 8.25;
                        display(quantity);
                        displayPrice(pricePerCup);
                    }
                }
                else {
                    if (chocolateChecked) {
                        quantity = 1;
                        pricePerCup = 9.25;
                        display(quantity);
                        displayPrice(pricePerCup);
                    }
                    else {
                        quantity = 1;
                        pricePerCup = 7.25;
                        display(quantity);
                        displayPrice(pricePerCup);
                    }
                }
                break;

            case R.id.chocolate_checkbox:
                if (chocolateChecked) {
                    if (whippedCreamChecked) {
                        quantity = 1;
                        pricePerCup = 10.25;
                        display(quantity);
                        displayPrice(pricePerCup);
                    }
                    else {
                        quantity = 1;
                        pricePerCup = 9.25;
                        display(quantity);
                        displayPrice(pricePerCup);
                    }
                }
                else {
                    if (whippedCreamChecked) {
                        quantity = 1;
                        pricePerCup = 8.25;
                        display(quantity);
                        displayPrice(pricePerCup);
                    }
                    else {
                        quantity = 1;
                        pricePerCup = 7.25;
                        display(quantity);
                        displayPrice(pricePerCup);
                    }
                }
                break;
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     *
     * @param number is the quantity of the cups.
     */
    private void display(int number) {
        if (number > 1)
            quantityTextView.setText(number + " " + getString(R.string.text_cups));
        else
            quantityTextView.setText(number + " " + getString(R.string.text_cup));
    }

    /**
     * This method displays the total price on the screen (TextView).
     *
     * @param number is the total price of the coffee ordered.
     */
    private void displayPrice(double number) {
        priceTextView.setText("\u20B9\u0020" + String.format("%.2f", number));
    }

    /**
     * This method displays the given text on the screen.
     *
     * @param message is just the message to be displayed.
     * @param size is the text size.
     * @param colorID is the color of the text.
     */
    private void displayMessage(String message, int size, int colorID) {
        summaryTextView.setText(message);
        summaryTextView.setTextSize(size);
        summaryTextView.setTextColor(colorID);
    }
}