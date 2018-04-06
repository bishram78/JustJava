package com.example.bishram.justjava;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TextWatcher {

    private Button buttonConfirm, buttonIncrement, buttonDecrement;
    private TextView quantityTextView, priceTextView;
    private EditText customerNameEditText;
    private CheckBox whippedCreamCheckbox, chocolateCheckbox, iceCreamCheckbox, mintFlavourCheckbox;

    private int quantity = 1;
    private double pricePerCup = 7.25;
    private double totalPrice;
    private boolean whippedCreamChecked, chocolateChecked, iceCreamChecked, mintFlavourChecked;

    Resources resources;

    private static final String KEY_QUANTITY = "key_quantity";
    private static final String KEY_TOTAL_PRICE = "key_total_price";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        resources = getResources();
        customerNameEditText.addTextChangedListener(this);
        totalPrice = calculatePrice();
        display(quantity);
        displayPrice(totalPrice);
        buttonConfirm.setTextColor(resources.getColor(R.color.colorButtonDisable));
        buttonConfirm.setEnabled(false);
        buttonDecrement.setTextColor(resources.getColor(R.color.colorButtonDisable));
        buttonDecrement.setEnabled(false);
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
        buttonIncrement = findViewById(R.id.id_button_increment);
        buttonDecrement = findViewById(R.id.id_button_decrement);
        buttonConfirm = findViewById(R.id.id_button_confirm);
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
        quantity++;
        if (quantity >= 100){
            String positiveText = getString(R.string.text_more_cup);
            mToast(positiveText);
            totalPrice = calculatePrice();
            display(quantity);
            displayPrice(totalPrice);
            buttonIncrement.setTextColor(resources.getColor(R.color.colorButtonDisable));
            buttonIncrement.setEnabled(false);
        }
        else {
            totalPrice = calculatePrice();
            display(quantity);
            displayPrice(totalPrice);
            buttonDecrement.setTextColor(resources.getColor(R.color.colorWhite));
            buttonDecrement.setEnabled(true);
        }
    }

    /**
     * This method is called when MINUS BUTTON is clicked.
     */
    public void decrement(View view) {
        quantity--;
        if (quantity <= 1){
            String negativeText = getString(R.string.text_less_cup);
            mToast(negativeText);
            totalPrice = calculatePrice();
            display(quantity);
            displayPrice(totalPrice);
            buttonDecrement.setTextColor(resources.getColor(R.color.colorButtonDisable));
            buttonDecrement.setEnabled(false);
        }
        else {
            totalPrice = calculatePrice();
            display(quantity);
            displayPrice(totalPrice);
            buttonIncrement.setTextColor(resources.getColor(R.color.colorWhite));
            buttonIncrement.setEnabled(true);
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
    @SuppressLint("StringFormatInvalid")
    private String createOrderSummary() {
        String customerName = customerNameEditText.getText().toString();
        checkCheckbox();

        String orderSummary;
        orderSummary = getString(R.string.txt_os_add_name, customerName);
        orderSummary += getString(R.string.txt_os_add_smry);

        if (quantity == 1) {
            orderSummary += getString(R.string.txt_os_add_quantity, quantity, getString(R.string.text_cup));
        }
        else {
            orderSummary += getString(R.string.txt_os_add_quantity, quantity, getString(R.string.text_cups));
        }
        orderSummary += getString(R.string.txt_os_add_price, totalPrice);
        if (whippedCreamChecked)
            orderSummary += getString(R.string.txt_os_add_wct, getString(R.string.text_yes));
        else
            orderSummary += getString(R.string.txt_os_add_wct, getString(R.string.text_no));

        if (chocolateChecked)
            orderSummary += getString(R.string.txt_os_add_ct, getString(R.string.text_yes));
        else
            orderSummary += getString(R.string.txt_os_add_ct, getString(R.string.text_no));

        if (iceCreamChecked)
            orderSummary += getString(R.string.txt_os_add_ict, getString(R.string.text_yes));
        else
            orderSummary += getString(R.string.txt_os_add_ict, getString(R.string.text_no));

        if (mintFlavourChecked)
            orderSummary += getString(R.string.txt_os_add_mft, getString(R.string.text_yes));
        else
            orderSummary += getString(R.string.txt_os_add_mft, getString(R.string.text_no));
        orderSummary += getString(R.string.txt_os_add_ty);

        return orderSummary;
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        String summaryText = createOrderSummary();
//
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
            String textStr = getString(R.string.txt_dsp_nmbr_frmt, number, getString(R.string.text_cups));
            quantityTextView.setText(textStr);
        }
        else{
            String textStr = getString(R.string.txt_dsp_nmbr_frmt, number, getString(R.string.text_cup));
            quantityTextView.setText(textStr);
        }
    }

    /**
     * This method displays the total price on the screen (TextView).
     *
     * @param number is the total price of the coffee ordered.
     */
    private void displayPrice(double number) {
        String textStr = getString(R.string.txt_dsp_prc_frmt, number);
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
     *
     * @param text is the message to be shown in the Toast.
     */
    public void mToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        int textLength = editable.toString().length();
        if (textLength == 0) {
            customerNameEditText.setError(getString(R.string.text_name_warning));
            buttonConfirm.setTextColor(getResources().getColor(R.color.colorButtonDisable));
            buttonConfirm.setEnabled(false);
        }
        else {
            buttonConfirm.setEnabled(true);
            buttonConfirm.setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }
}