package com.example.yuvaltamir.talki;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import static java.lang.Integer.parseInt;

//TODO fix warnings

public class MainActivity extends AppCompatActivity {

    AnimationDrawable animationDrawable;
    RelativeLayout relativeLayout;
    DrawerLayout mDrawerLayout;
    ImageView previewIcon;
    ImageView iconOfAction;
    Dialog dialogPrefix;
    Dialog changeContentDialog;
    Dialog creditDialog;
    Dialog iconsDialog;
    TextView actionText;
    Button prefixOne;
    Button prefixTwo;
    Button prefixThree;
    Button OKButton;
    Button content1_1;
    Button content1_2;
    Button content1_3;
    Button content2_1;
    Button content2_2;
    Button content2_3;
    Button content3_1;
    Button content3_2;
    Button content3_3;
    Button pressedButton;
    Button contentButton;
    Button changeAnimationButton;
    TextInputLayout contentField;
    String newTextInput;
    String chosenPrefix;
    int idOfSelectedIcon;
    Boolean errorInText = false;
    Boolean isAnimationRunning = true;
    Boolean isTheUserChangedIcon = false;

    public enum ArrayToHandle {ACTIONS, ICONS}

    public static final int LENGTH_OF_ARRAY = 10;

    public String[] allAction = new String[LENGTH_OF_ARRAY];
    public String[] allIcons = new String[LENGTH_OF_ARRAY];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Animation background
        relativeLayout = findViewById(R.id.main_relative_layout);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(2500);

        putDefaultValuesInArray(allAction, ArrayToHandle.ACTIONS); // Insert default values to all buttons
        putDefaultValuesInArray(allIcons, ArrayToHandle.ICONS); // Insert default values to all buttons

        final SharedPreferences actions = getSharedPreferences("preferences", MODE_PRIVATE);
        final SharedPreferences icons = getSharedPreferences("iconsPreferences", MODE_PRIVATE);

        loadContentToSharedPreferences(allAction, "Action", "preferences");
        loadContentToSharedPreferences(allIcons, "Icon", "iconsPreferences");

        // buttons
        prefixOne = findViewById(R.id.prefixOne);
        prefixTwo = findViewById(R.id.prefixTwo);
        prefixThree = findViewById(R.id.prefixThree);

        // text view of action + ImageView of icon
        actionText = findViewById(R.id.messageText);
        iconOfAction = findViewById(R.id.iconOfAction);

        // dialog
        dialogPrefix = new Dialog(this);
        changeContentDialog = new Dialog(this);
        creditDialog = new Dialog(this);
        iconsDialog = new Dialog(this);

        // drawer layout
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        if (menuItem.getItemId() == R.id.delete) { // return to default values
                            actions.edit().clear().apply();
                            putDefaultValuesInArray(allAction, ArrayToHandle.ACTIONS); // put default values in allAction

                            icons.edit().clear().apply();
                            putDefaultValuesInArray(allIcons, ArrayToHandle.ICONS); // put default values in allIcons

                            Toast.makeText(getApplicationContext(), "All actions removed!", Toast.LENGTH_LONG).show();
                        }

                        if (menuItem.getItemId() == R.id.startStopAnimation && isAnimationRunning) { // pausing / resuming background animation
                            animationDrawable.stop();
                            menuItem.setTitle("Resume background animation");
                            isAnimationRunning = false;
                        } else if (menuItem.getItemId() == R.id.startStopAnimation && !isAnimationRunning) {
                            menuItem.setTitle("Pause background animation");
                            animationDrawable.run();
                            isAnimationRunning = true;
                        }

                        if (menuItem.getItemId() == R.id.credits) {
                            creditDialog.setContentView(R.layout.credit_dialog);
                            creditDialog.show();
                        }
                        return true;
                    }
                });
    }

    // for the background animation while life cycle

    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning())
            animationDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning())
            animationDrawable.stop();
    }

    public void customPopupPrefix(View view) {

        if (view.getId() == R.id.prefixOne) {
            chosenPrefix = "Let's Go To ";

            dialogPrefix.setContentView(R.layout.popup_prefix_one);

            content1_1 = dialogPrefix.findViewById(R.id.content1_1);
            content1_2 = dialogPrefix.findViewById(R.id.content1_2);
            content1_3 = dialogPrefix.findViewById(R.id.content1_3);

            content1_1.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, Integer.valueOf(allIcons[1]), 0);
            content1_2.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, Integer.valueOf(allIcons[2]), 0);
            content1_3.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, Integer.valueOf(allIcons[3]), 0);

            content1_1.setText(allAction[1]);
            content1_2.setText(allAction[2]);
            content1_3.setText(allAction[3]);

            content1_1.setOnClickListener(onClickListener);
            content1_1.setOnLongClickListener(onLongClickListener);
            content1_2.setOnClickListener(onClickListener);
            content1_2.setOnLongClickListener(onLongClickListener);
            content1_3.setOnClickListener(onClickListener);
            content1_3.setOnLongClickListener(onLongClickListener);
        }

        if (view.getId() == R.id.prefixTwo) {
            chosenPrefix = "I Want ";

            dialogPrefix.setContentView(R.layout.popup_prefix_two);

            content2_1 = dialogPrefix.findViewById(R.id.content2_1);
            content2_2 = dialogPrefix.findViewById(R.id.content2_2);
            content2_3 = dialogPrefix.findViewById(R.id.content2_3);

            content2_1.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, Integer.valueOf(allIcons[4]), 0);
            content2_2.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, Integer.valueOf(allIcons[5]), 0);
            content2_3.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, Integer.valueOf(allIcons[6]), 0);

            content2_1.setText(allAction[4]);
            content2_2.setText(allAction[5]);
            content2_3.setText(allAction[6]);

            content2_1.setOnClickListener(onClickListener);
            content2_1.setOnLongClickListener(onLongClickListener);
            content2_2.setOnClickListener(onClickListener);
            content2_2.setOnLongClickListener(onLongClickListener);
            content2_3.setOnClickListener(onClickListener);
            content2_3.setOnLongClickListener(onLongClickListener);
        }

        if (view.getId() == R.id.prefixThree) {
            chosenPrefix = "I Need ";

            dialogPrefix.setContentView(R.layout.popup_prefix_three);

            content3_1 = dialogPrefix.findViewById(R.id.content3_1);
            content3_2 = dialogPrefix.findViewById(R.id.content3_2);
            content3_3 = dialogPrefix.findViewById(R.id.content3_3);

            content3_1.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, Integer.valueOf(allIcons[7]), 0);
            content3_2.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, Integer.valueOf(allIcons[8]), 0);
            content3_3.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, Integer.valueOf(allIcons[9]), 0);

            content3_1.setText(allAction[7]);
            content3_2.setText(allAction[8]);
            content3_3.setText(allAction[9]);

            content3_1.setOnClickListener(onClickListener);
            content3_1.setOnLongClickListener(onLongClickListener);
            content3_2.setOnClickListener(onClickListener);
            content3_2.setOnLongClickListener(onLongClickListener);
            content3_3.setOnClickListener(onClickListener);
            content3_3.setOnLongClickListener(onLongClickListener);
        }
        dialogPrefix.show();
    }

    public void putDefaultValuesInArray(String[] array, ArrayToHandle toHandle) {

        if (toHandle == ArrayToHandle.ACTIONS) { // default values of actions
            array[0] = " ";
            array[1] = "The Bar";
            array[2] = "The Stage";
            array[3] = "The Bathroom";
            array[4] = "To Drink";
            array[5] = "To Eat";
            array[6] = "To Dance";
            array[7] = "To Go Home";
            array[8] = "To Pee";
            array[9] = "To Puke";
        }
        else {  // default values of icons
            array[0] = " ";
            array[1] = String.valueOf(R.drawable.ic_11);
            array[2] = String.valueOf(R.drawable.ic_16);
            array[3] = String.valueOf(R.drawable.ic_20);
            array[4] = String.valueOf(R.drawable.ic_10);
            array[5] = String.valueOf(R.drawable.ic_19);
            array[6] = String.valueOf(R.drawable.ic_06);
            array[7] = String.valueOf(R.drawable.ic_14);
            array[8] = String.valueOf(R.drawable.ic_20);
            array[9] = String.valueOf(R.drawable.ic_09);
        }
    }

    // shared preferences functions
    public void updateNewContentToSharedPreferences(String[] arrayOfActions, ArrayToHandle toHandle) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : arrayOfActions) { // merge the string at the ','
            stringBuilder.append(s);
            stringBuilder.append(",");
        }

        if(toHandle == ArrayToHandle.ACTIONS) { // add to shared preferences string
            SharedPreferences actions = getSharedPreferences("preferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = actions.edit();
            editor.putString("Action", stringBuilder.toString());
            editor.apply();
        }
        else { // add to shared preferences string
            SharedPreferences icons = getSharedPreferences("iconsPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = icons.edit();
            editor.putString("Icon", stringBuilder.toString());
            editor.apply();
        }
    }

    public void loadContentToSharedPreferences(String[] arrayOfActions, String s, String nameOfPreferences) {

        SharedPreferences elements = getSharedPreferences(nameOfPreferences, MODE_PRIVATE);

        String stringOfElements = elements.getString(s, "");
        if (stringOfElements == null) // checks if the string is null
            throw new AssertionError();
        String[] splitElements = stringOfElements.split(","); // split the string from the shared preferences string


        // enter each word to the array of content / icon --> coping automatically the arrays
        System.arraycopy(splitElements, 0, arrayOfActions, 0, splitElements.length);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() { // on click - when pressing on a contentx.x button
        @Override
        public void onClick(View view) {
            customPopupAction(view);
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() { // on long click - when pressing on a contentx.x button
        @Override
        public boolean onLongClick(View view) {

            // open layout of change content
            changeContentDialog.setContentView(R.layout.change_content_text);

            // open keyboard
            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            // changing content
            contentField = changeContentDialog.findViewById(R.id.contentField);
            OKButton = changeContentDialog.findViewById(R.id.OKButton);
            previewIcon = changeContentDialog.findViewById(R.id.previewIcon);

            EditText textBox = changeContentDialog.findViewById(R.id.textBox);

            contentButton = (Button) view;

            final String tagOfContentButton = (String) contentButton.getTag();

            // preview of the icon before changing it
            previewIcon.setImageResource(parseInt(allIcons[parseInt(tagOfContentButton) + 1]));
            previewIcon.setVisibility(View.VISIBLE);

            // preview of the text at the pressed button
            textBox.setText(contentButton.getText().toString());
            textBox.setSelection(textBox.getText().length()); // set cursor to end of text

            OKButton.setOnClickListener(new View.OnClickListener() { // when pressing OK
                @Override
                public void onClick(View view) {

                    pressedButton = (Button) view;
                    newTextInput = Objects.requireNonNull(contentField.getEditText()).getText().toString().trim(); // get the text from the text box

                    if (newTextInput.isEmpty()) { // checks if the text is legal
                        errorInText = true;
                        contentField.setError("Field can't be empty");

                    } else if (newTextInput.length() > 30) {
                        errorInText = true;
                        contentField.setError("Text too long");
                    }

                    if (!errorInText) { // if the text is legal, continue
                        contentButton.setText(newTextInput); // change the text in the chosen button

                        if (isTheUserChangedIcon) { // set the preview icon
                            contentButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, idOfSelectedIcon, 0);
                        }
                        isTheUserChangedIcon = false;

                        //add the new action / icon to each array
                        allAction[parseInt(tagOfContentButton) + 1] = newTextInput; // I add 1 because there was a problem in loading default value at [0] place
                        allIcons[parseInt(tagOfContentButton) + 1] = String.valueOf(idOfSelectedIcon);

                        //update shared preferences
                        updateNewContentToSharedPreferences(allAction, ArrayToHandle.ACTIONS);
                        updateNewContentToSharedPreferences(allIcons, ArrayToHandle.ICONS);

                        imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0); // close keyboard

                        changeContentDialog.dismiss();
                    } else {
                        errorInText = false;
                    }
                }
            });
            changeContentDialog.show();

            return true;
        }
    };

    public void changeIconButtonPressed(View view) { // opens table of icons

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // close keyboard
        imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);

        iconsDialog.setContentView(R.layout.table_of_animations);

        iconsDialog.show();
    }

    public void iconHasChosen (View view) { // when pressing on one of the icons from the table view
        switch (view.getId())
        {
            case R.id.ImageView01:
                idOfSelectedIcon = R.drawable.ic_01;
                break;
            case R.id.ImageView02:
                idOfSelectedIcon = R.drawable.ic_02;
                break;
            case R.id.ImageView03:
                idOfSelectedIcon = R.drawable.ic_03;
                break;
            case R.id.ImageView04:
                idOfSelectedIcon = R.drawable.ic_04;
                break;
            case R.id.ImageView05:
                idOfSelectedIcon = R.drawable.ic_05;
                break;
            case R.id.ImageView06:
                idOfSelectedIcon = R.drawable.ic_06;
                break;
            case R.id.ImageView07:
                idOfSelectedIcon = R.drawable.ic_07;
                break;
            case R.id.ImageView08:
                idOfSelectedIcon = R.drawable.ic_08;
                break;
            case R.id.ImageView09:
                idOfSelectedIcon = R.drawable.ic_09;
                break;
            case R.id.ImageView10:
                idOfSelectedIcon = R.drawable.ic_10;
                break;
            case R.id.ImageView11:
                idOfSelectedIcon = R.drawable.ic_11;
                break;
            case R.id.ImageView12:
                idOfSelectedIcon = R.drawable.ic_12;
                break;
            case R.id.ImageView13:
                idOfSelectedIcon = R.drawable.ic_13;
                break;
            case R.id.ImageView14:
                idOfSelectedIcon = R.drawable.ic_14;
                break;
            case R.id.ImageView15:
                idOfSelectedIcon = R.drawable.ic_15;
                break;
            case R.id.ImageView16:
                idOfSelectedIcon = R.drawable.ic_16;
                break;
            case R.id.ImageView17:
                idOfSelectedIcon = R.drawable.ic_17;
                break;
            case R.id.ImageView18:
                idOfSelectedIcon = R.drawable.ic_18;
                break;
            case R.id.ImageView19:
                idOfSelectedIcon = R.drawable.ic_19;
                break;
            case R.id.ImageView20:
                idOfSelectedIcon = R.drawable.ic_20;
                break;
        }

        isTheUserChangedIcon = true;

        previewIcon.setImageResource(idOfSelectedIcon);
        previewIcon.setVisibility(View.VISIBLE);

        iconsDialog.dismiss();

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // show keyboard
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void customPopupAction(View view) {
        final Button pressedButton = (Button) view;
        String tagOfPressedButton = (String) pressedButton.getTag();

        dialogPrefix.dismiss();

        prefixOne.setVisibility(View.INVISIBLE);
        prefixTwo.setVisibility(View.INVISIBLE);
        prefixThree.setVisibility(View.INVISIBLE);

        actionText.setText(chosenPrefix + pressedButton.getText().toString()); // show the final action on screen
        iconOfAction.setImageResource(parseInt(allIcons[parseInt(tagOfPressedButton) + 1])); // show the icon on the screen

        actionText.setVisibility(View.VISIBLE);
        iconOfAction.setVisibility(View.VISIBLE);
    }

    public void closeAction(View view) {
        prefixOne.setVisibility(View.VISIBLE);
        prefixTwo.setVisibility(View.VISIBLE);
        prefixThree.setVisibility(View.VISIBLE);

        actionText.setVisibility(View.INVISIBLE);
        iconOfAction.setVisibility(View.INVISIBLE);

        actionText.setText(" ");
        chosenPrefix = " ";
    }
}
