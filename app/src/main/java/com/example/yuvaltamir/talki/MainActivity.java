package com.example.yuvaltamir.talki;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    public static final int LENGTH_OF_ARRAY = 10;
    AnimationDrawable animationDrawable;
    RelativeLayout relativeLayout;
    DrawerLayout drawerLayout;
    ImageView previewIcon;
    ImageView iconOfAction;
    TextView actionToShowText;
    Button prefixOne;
    Button prefixTwo;
    Button prefixThree;
    Button okButton;
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
    CustomDialog customDialog; // global dialog
    Boolean errorInText = false;
    Boolean isAnimationRunning = true;
    Boolean isTheUserChangedIcon = false;
    Boolean isPressed = false;

    public enum ArrayToHandle {ACTIONS, ICONS}
    public enum keyboardAction {OPEN, CLOSE}

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
        actionToShowText = findViewById(R.id.messageText);
        iconOfAction = findViewById(R.id.iconOfAction);

        // drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();

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
                            final CustomDialog customDialog = new CustomDialog(MainActivity.this, R.layout.credit_dialog);
                            customDialog.show();
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

    public void openActionDialogAfterPressingButton(View view) {
        Dialog whichCustomDialogPressed = null;
        if (view.getId() == R.id.prefixOne) {
            chosenPrefix = "Let's Go ";
            whichCustomDialogPressed = openPrefixDialog(R.layout.popup_prefix_one, R.id.content1_1, R.id.content1_2, R.id.content1_3, 1, 2, 3);
        }
        if (view.getId() == R.id.prefixTwo) {
            chosenPrefix = "I Want ";
            whichCustomDialogPressed = openPrefixDialog(R.layout.popup_prefix_two, R.id.content2_1, R.id.content2_2, R.id.content2_3, 4, 5, 6);
        }
        if (view.getId() == R.id.prefixThree) {
            chosenPrefix = "I Need ";
            whichCustomDialogPressed = openPrefixDialog(R.layout.popup_prefix_three, R.id.content3_1, R.id.content3_2, R.id.content3_3, 7, 8, 9);
        }
        try {
            whichCustomDialogPressed.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CustomDialog openPrefixDialog(int idOfLayout, int idOfB1, int idOfB2, int idOfB3, int place1, int place2, int place3) {
        prefixOne.setVisibility(View.INVISIBLE);
        prefixTwo.setVisibility(View.INVISIBLE);
        prefixThree.setVisibility(View.INVISIBLE);

        customDialog = new CustomDialog(MainActivity.this , idOfLayout);

        customDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                prefixOne.setVisibility(View.VISIBLE);
                prefixTwo.setVisibility(View.VISIBLE);
                prefixThree.setVisibility(View.VISIBLE);
            }
        });

        Button B1 = customDialog.findViewById(idOfB1);
        Button B2 = customDialog.findViewById(idOfB2);
        Button B3 = customDialog.findViewById(idOfB3);

        B1.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, Integer.valueOf(allIcons[place1]), 0);
        B2.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, Integer.valueOf(allIcons[place2]), 0);
        B3.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, Integer.valueOf(allIcons[place3]), 0);

        B1.setText(allAction[place1]);
        B2.setText(allAction[place2]);
        B3.setText(allAction[place3]);

        B1.setOnClickListener(onClickListener);
        B1.setOnLongClickListener(onLongClickListener);
        B2.setOnClickListener(onClickListener);
        B2.setOnLongClickListener(onLongClickListener);
        B3.setOnClickListener(onClickListener);
        B3.setOnLongClickListener(onLongClickListener);
        
        return customDialog;
    }

    public void putDefaultValuesInArray(String[] array, ArrayToHandle toHandle) {
        if (toHandle == ArrayToHandle.ACTIONS) { // default values of actions
            array[0] = "";
            array[1] = "Home";
            array[2] = "To The Bar";
            array[3] = "To The Bathroom";
            array[4] = "To Drink";
            array[5] = "To Eat";
            array[6] = "To Dance";
            array[7] = "To Go Home";
            array[8] = "To Pee";
            array[9] = "To Puke";
        }
        else {  // default values of icons
            array[0] = "";
            array[1] = String.valueOf(R.drawable.ic_05);
            array[2] = String.valueOf(R.drawable.ic_11);
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
        else if(toHandle == ArrayToHandle.ICONS){ // add to shared preferences string
            SharedPreferences icons = getSharedPreferences("iconsPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = icons.edit();
            editor.putString("Icon", stringBuilder.toString());
            editor.apply();
        }
    }

    public void loadContentToSharedPreferences(String[] arrayOfActions, String nameOfSharedPreferencesArray, String nameOfPreferences) {
        SharedPreferences elements = getSharedPreferences(nameOfPreferences, MODE_PRIVATE);

        String stringOfElements = elements.getString(nameOfSharedPreferencesArray, "");
        if (stringOfElements == null) // checks if the string is null
            throw new AssertionError();
        String[] splitElements = stringOfElements.split(","); // split the string from the shared preferences string

        System.arraycopy(splitElements, 0, arrayOfActions, 0, splitElements.length); // enter each word to the array of content / icon --> coping automatically the arrays
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() { // on click - when pressing on a contentx.x button
        @Override
        public void onClick(View view) {
            showsTheMessageOnScreen(view);
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() { // on long click - when pressing on a contentx.x button
        @Override
        public boolean onLongClick(View view) {
            final CustomDialog customDialog = new CustomDialog(MainActivity.this, R.layout.change_content_text);

            openOrCloseKeyboard(keyboardAction.OPEN);

            contentField = customDialog.findViewById(R.id.contentField); // changing content
            okButton = customDialog.findViewById(R.id.okButton);
            previewIcon = customDialog.findViewById(R.id.previewIcon);
            EditText textBox = customDialog.findViewById(R.id.textBox);

            contentButton = (Button) view;

            final String tagOfContentButton = (String) contentButton.getTag();

            previewIcon.setImageResource(parseInt(allIcons[parseInt(tagOfContentButton) + 1]));  // preview of the icon before changing it
            previewIcon.setVisibility(View.VISIBLE);

            textBox.setText(contentButton.getText().toString()); // preview of the text at the pressed button
            textBox.setSelection(textBox.getText().length()); // set cursor to end of text

            okButton.setOnClickListener(new View.OnClickListener() { // when pressing OK
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

                        if (!isTheUserChangedIcon) {
                            idOfSelectedIcon = parseInt(allIcons[parseInt(tagOfContentButton) + 1]);
                        }
                        allAction[parseInt(tagOfContentButton) + 1] = newTextInput; // I add 1 because there was a problem in loading default value at [0] place
                        allIcons[parseInt(tagOfContentButton) + 1] = String.valueOf(idOfSelectedIcon);

                        updateNewContentToSharedPreferences(allAction, ArrayToHandle.ACTIONS);
                        updateNewContentToSharedPreferences(allIcons, ArrayToHandle.ICONS);

                        openOrCloseKeyboard(keyboardAction.CLOSE);
                        customDialog.dismiss();
                    } else {
                        errorInText = false;
                    }
                }
            });
            customDialog.show();
            return true;
        }
    };

    public void changeIconButtonPressed(View view) { // opens table of icons
        openOrCloseKeyboard(keyboardAction.CLOSE);
        CustomDialog tempCustomDialog = new CustomDialog(MainActivity.this, R.layout.table_of_animations);
        tempCustomDialog.show();
        customDialog = tempCustomDialog;
    }

    public void setIdOfSelectedIcon(View view) { // when pressing on one of the icons from the table view
        switch (parseInt(view.getTag().toString()))
        {
            case 1:
                idOfSelectedIcon = R.drawable.ic_01;
                break;
            case 2:
                idOfSelectedIcon = R.drawable.ic_02;
                break;
            case 3:
                idOfSelectedIcon = R.drawable.ic_03;
                break;
            case 4:
                idOfSelectedIcon = R.drawable.ic_04;
                break;
            case 5:
                idOfSelectedIcon = R.drawable.ic_05;
                break;
            case 6:
                idOfSelectedIcon = R.drawable.ic_06;
                break;
            case 7:
                idOfSelectedIcon = R.drawable.ic_07;
                break;
            case 8:
                idOfSelectedIcon = R.drawable.ic_08;
                break;
            case 9:
                idOfSelectedIcon = R.drawable.ic_09;
                break;
            case 10:
                idOfSelectedIcon = R.drawable.ic_10;
                break;
            case 11:
                idOfSelectedIcon = R.drawable.ic_11;
                break;
            case 12:
                idOfSelectedIcon = R.drawable.ic_12;
                break;
            case 13:
                idOfSelectedIcon = R.drawable.ic_13;
                break;
            case 14:
                idOfSelectedIcon = R.drawable.ic_14;
                break;
            case 15:
                idOfSelectedIcon = R.drawable.ic_15;
                break;
            case 16:
                idOfSelectedIcon = R.drawable.ic_16;
                break;
            case 17:
                idOfSelectedIcon = R.drawable.ic_17;
                break;
            case 18:
                idOfSelectedIcon = R.drawable.ic_18;
                break;
            case 19:
                idOfSelectedIcon = R.drawable.ic_19;
                break;
            case 20:
                idOfSelectedIcon = R.drawable.ic_20;
                break;
        }
        isTheUserChangedIcon = true;

        previewIcon.setImageResource(idOfSelectedIcon);
        previewIcon.setVisibility(View.VISIBLE);

        customDialog.dismiss();

        openOrCloseKeyboard(keyboardAction.OPEN);
    }

    public void openOrCloseKeyboard(keyboardAction keyboardAction) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (keyboardAction == MainActivity.keyboardAction.OPEN)
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        else if (keyboardAction == MainActivity.keyboardAction.CLOSE)
            imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
    }

    public void showsTheMessageOnScreen(View view) {
        final Button pressedButton = (Button) view;
        String tagOfPressedButton = (String) pressedButton.getTag();

        isPressed = true;

        customDialog.dismiss();

        prefixOne.setVisibility(View.INVISIBLE);
        prefixTwo.setVisibility(View.INVISIBLE);
        prefixThree.setVisibility(View.INVISIBLE);

        actionToShowText.setText(chosenPrefix + pressedButton.getText().toString()); // show the final action on screen
        iconOfAction.setImageResource(parseInt(allIcons[parseInt(tagOfPressedButton) + 1])); // show the icon on the screen

        actionToShowText.setVisibility(View.VISIBLE);
        iconOfAction.setVisibility(View.VISIBLE);
    }

    public void closeMessage(View view) {
        prefixOne.setVisibility(View.VISIBLE);
        prefixTwo.setVisibility(View.VISIBLE);
        prefixThree.setVisibility(View.VISIBLE);

        actionToShowText.setVisibility(View.INVISIBLE);
        iconOfAction.setVisibility(View.INVISIBLE);

        actionToShowText.setText(" ");
        chosenPrefix = " ";
    }
}