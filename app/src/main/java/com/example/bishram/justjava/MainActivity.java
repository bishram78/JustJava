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
import android.widget.Toast;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    TextView quantityTextView;
    TextView priceTextView;
    EditText customerNameEditText;
    CheckBox whippedCreamCheckbox;
    CheckBox chocolateCheckbox;
    CheckBox iceCreamCheckbox;
    CheckBox mintFlavourCheckbox;

    int quantity = 1;
    double pricePerCup = 7.25;
    double totalPrice;
    String summaryText = "";
    String customerName = "";
    boolean whippedCreamChecked;
    boolean chocolateChecked;
    boolean iceCreamChecked;
    boolean mintFlavourChecked;

    private static final String KEY_QUANTITY = "key_quantity";
    private static final String KEY_TOTAL_PRICE = "key_total_price";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        totalPrice = calculatePrice();
        display(quantity);
        displayPrice(totalPrice);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_QUANTITY, quantity);
        outState.putDouble(KEY_TOTAL_PRICE, totalPrice);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        quantity = savedInstanceState.getInt(KEY_QUANTITY);
        totalPrice = savedInstanceState.getDouble(KEY_TOTAL_PRICE);

        display(quantity);
        displayPrice(totalPrice);
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
        customerNameEditText = findViewById(R.id.edit_text_customer_name);
    }
    /**
     * This method is called when PLUS BUTTON is clicked.
     */
    public void increment(View view) {
        if (quantity >= 100){
            String positiveText = getString(R.string.text_more_cup);
            mToast(positiveText);
        }
        else {
            quantity++;
            totalPrice = calculatePrice();
            display(quantity);
            displayPrice(totalPrice);
        }
    }

    /**
     * This method is called when MINUS BUTTON is clicked.
     */
    public void decrement(View view) {
        if (quantity <= 1){
            String negativeText = getString(R.string.text_less_cup);
            mToast(negativeText);
        }
        else {
            quantity--;
            totalPrice = calculatePrice();
            display(quantity);
            displayPrice(totalPrice);
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

        String orderSummary = getString(R.string.text_name) + " " + customerName;
        if (quantity == 1) {
            orderSummary += "\n" + getString(R.string.text_no_of_cups) +  " " + quantity + " " + getString(R.string.text_cup);
        }
        else {
            orderSummary += "\n" + getString(R.string.text_no_of_cups) + " " + quantity + " " + getString(R.string.text_cups);
        }
        orderSummary += "\n" + getString(R.string.text_total_price) + " \u20B9 " + String.format(Locale.getDefault(),"%.2f", totalPrice);
        orderSummary += "\n";
        if (whippedCreamChecked)
            orderSummary += "\n" + getString(R.string.text_question_whipped_cream) + " " + getString(R.string.text_yes);
        else
            orderSummary += "\n" + getString(R.string.text_question_whipped_cream) + " " + getString(R.string.text_no);

        if (chocolateChecked)
            orderSummary += "\n" + getString(R.string.text_question_chocolate) + " " + getString(R.string.text_yes);
        else
            orderSummary += "\n" + getString(R.string.text_question_chocolate) + " " + getString(R.string.text_no);

        if (iceCreamChecked)
            orderSummary += "\n" + getString(R.string.text_question_ice_cream) + " " + getString(R.string.text_yes);
        else
            orderSummary += "\n" + getString(R.string.text_question_ice_cream) + " " + getString(R.string.text_no);

        if (mintFlavourChecked)
            orderSummary += "\n" + getString(R.string.text_question_mint_flavour) + " " + getString(R.string.text_yes);
        else
            orderSummary += "\n" + getString(R.string.text_question_mint_flavour) + " " + getString(R.string.text_no);
        orderSummary += "\n";
        orderSummary += "\n" + getString(R.string.text_thank_you);
        return orderSummary;
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        customerName = customerNameEditText.getText().toString();
        int length = customerName.length();
        if(length > 0) {
            summaryText = createOrderSummary();

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.text_email_subject));
            intent.putExtra(Intent.EXTRA_TEXT, summaryText);
            if (intent.resolveActivity(getPackageManager()) != null)
                startActivity(intent);

            quantity = 1;
            pricePerCup = 7.25;
            display(quantity);
            displayPrice(pricePerCup);
        }
        else {
            customerName = getString(R.string.text_name_warning);
            mToast(customerName);
        }
    }

    /**
     * This method will handle the checkbox clicks
     */
    public void checkboxClicked(View view){
        checkCheckbox();

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
     *
    private void displayMessage(String message, int size, int colorID) {
        summaryTextView.setText(message);
        summaryTextView.setTextSize(size);
        summaryTextView.setTextColor(colorID);
    }
    */

    /**
     * This is toast method.
     */
    public void mToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}