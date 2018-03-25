package com.example.bishram.justjava;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    TextView quantityTextView;
    TextView priceTextView;
    TextView summaryTextView;
    EditText customerNameEditText;
    CheckBox whippedCreamCheckbox;
    CheckBox chocolateCheckbox;
    CheckBox iceCreamCheckbox;
    CheckBox mintFlavourCheckbox;

    int quantity = 1;
    int count = 1;
    double pricePerCup = 7.25;
    int summaryTextSize;
    int summaryTextColor;
    double totalPrice;
    String summaryText = "";
    String customerName = "";
    boolean whippedCreamChecked;
    boolean chocolateChecked;
    boolean iceCreamChecked;
    boolean mintFlavourChecked;

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_QUANTITY, quantity);
        outState.putDouble(KEY_TOTAL_PRICE, totalPrice);
        outState.putString(KEY_SUMMARY_TEXT, summaryText);
        outState.putInt(KEY_SUMMARY_TEXT_SIZE, summaryTextSize);
        outState.putInt(KEY_SUMMARY_TEXT_COLOR, summaryTextColor);
    }

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
     * This method will get all the views from the layout resource file.
     */
    public void initializeViews(){
        quantityTextView = findViewById(R.id.quantity_text_view);
        priceTextView = findViewById(R.id.price_text_view);
        whippedCreamCheckbox = findViewById(R.id.whipped_cream_checkbox);
        chocolateCheckbox = findViewById(R.id.chocolate_checkbox);
        iceCreamCheckbox = findViewById(R.id.ice_cream_checkbox);
        mintFlavourCheckbox = findViewById(R.id.mint_flavour_checkbox);
        summaryTextView = findViewById(R.id.order_summary_text_view);
        customerNameEditText = findViewById(R.id.edit_text_customer_name);
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
     * This method checks whether checkboxes are checked or not.
     */
    public void checkCheckbox() {
        whippedCreamChecked = whippedCreamCheckbox.isChecked();
        chocolateChecked = chocolateCheckbox.isChecked();
        iceCreamChecked = iceCreamCheckbox.isChecked();
        mintFlavourChecked = mintFlavourCheckbox.isChecked();
    }

    /**
     * This method create the order summary text shown and send to customer's mail.
     */
    private String createOrderSummary() {
        customerName = customerNameEditText.getText().toString();
        checkCheckbox();

        String orderSummary = getString(R.string.text_name) + customerName;
        if (quantity == 1) {
            orderSummary += "\n" + getString(R.string.text_no_of_cups) +  " " + quantity + " " + getString(R.string.text_cup);
        }
        else {
            orderSummary += "\n" + getString(R.string.text_no_of_cups) + " " + quantity + " " + getString(R.string.text_cups);
        }
        orderSummary += "\n" + getString(R.string.text_total_price) + " \u20B9 " + String.format(Locale.getDefault(),"%.2f", totalPrice);
        orderSummary += "\n";
        orderSummary += "\n" + getString(R.string.text_question_whipped_cream) + " " + whippedCreamChecked;
        orderSummary += "\n" + getString(R.string.text_question_chocolate) + " " + chocolateChecked;
        orderSummary += "\n" + getString(R.string.text_question_ice_cream) + " " + iceCreamChecked;
        orderSummary += "\n" + getString(R.string.text_question_mint_flavour) + " " + mintFlavourChecked;
        orderSummary += "\n";
        orderSummary += "\n" + getString(R.string.text_thank_you);
        return orderSummary;
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        count = 1;
        quantity = 1;
        pricePerCup = 7.25;
        summaryText = createOrderSummary();
        display(quantity);
        displayPrice(pricePerCup);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.text_email_subject));
        intent.putExtra(Intent.EXTRA_TEXT, summaryText);
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
    }

    /**
     * This method will handle the checkbox clicks
     */
    public void checkboxClicked(View view){
        checkCheckbox();

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
                    int toppingPrice = quantity;
                    totalPrice += toppingPrice;
                    displayPrice(totalPrice);
                    pricePerCup += 1;
                }
                else {
                    int toppingPrice = quantity;
                    totalPrice -= toppingPrice;
                    displayPrice(totalPrice);
                    pricePerCup -= 1;
                }
                break;

            case R.id.chocolate_checkbox:
                if (chocolateChecked) {
                    int toppingPrice = 2 * quantity;
                    totalPrice += toppingPrice;
                    displayPrice(totalPrice);
                    pricePerCup += 2;
                }
                else {
                    int toppingPrice = 2 * quantity;
                    totalPrice -= toppingPrice;
                    displayPrice(totalPrice);
                    pricePerCup -= 2;
                }
                break;

            case R.id.ice_cream_checkbox:
                if (iceCreamChecked) {
                    int toppingPrice = 3 * quantity;
                    totalPrice += toppingPrice;
                    displayPrice(totalPrice);
                    pricePerCup += 3;
                }
                else {
                    int toppingPrice = 3 * quantity;
                    totalPrice -= toppingPrice;
                    displayPrice(totalPrice);
                    pricePerCup -= 3;
                }
                break;

            case R.id.mint_flavour_checkbox:
                if (mintFlavourChecked) {
                    int toppingPrice = 4 * quantity;
                    totalPrice += toppingPrice;
                    displayPrice(totalPrice);
                    pricePerCup += 4;
                }
                else {
                    int toppingPrice = 4 * quantity;
                    totalPrice -= toppingPrice;
                    displayPrice(totalPrice);
                    pricePerCup -= 4;
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
        if (number > 1){
            String textStr = number + " " + getString(R.string.text_cups);
            quantityTextView.setText(textStr);
        }
        else{
            String textStr = number + " " + getString(R.string.text_cup);
            quantityTextView.setText(textStr);
        }
    }

    /**
     * This method displays the total price on the screen (TextView).
     *
     * @param number is the total price of the coffee ordered.
     */
    private void displayPrice(double number) {
        String textStr = "\u20B9\u0020" + String.format(Locale.getDefault(),"%.2f", number);
        priceTextView.setText(textStr);
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