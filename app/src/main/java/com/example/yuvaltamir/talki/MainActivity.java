package com.example.yuvaltamir.talki;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

//TODO fix the stop of animation while dialog is open - 2 different threads or AsyncTask

public class MainActivity extends AppCompatActivity {

    AnimationDrawable animationDrawable;
    RelativeLayout relativeLayout;
    Dialog dialogPrefix;
    Dialog changeContentDialog;
    Dialog creditDialog;
    TextView actionText;
    Button prefixOne;
    Button prefixTwo;
    Button prefixThree;
    DrawerLayout mDrawerLayout;
    RelativeLayout mainRelativeLayout;
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
    TextInputLayout contentField;
    String newTextInput;
    String chosenPrefix;
    Boolean errorInText = false;
    boolean isThemeChanged = false;

    public static final int LENGTH_OF_ARRAY = 10;

    public String[] allAction = new String[LENGTH_OF_ARRAY];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Animation background
        relativeLayout = findViewById(R.id.main_relative_layout);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(3000);

        putDefaultValuesInArray(allAction); // Insert default values to all buttons

        final SharedPreferences actions = getSharedPreferences("preferences", MODE_PRIVATE);

        loadContentToSharedPreferences(allAction);

        // relative layout
        mainRelativeLayout = findViewById(R.id.main_relative_layout);
        // imageView
        prefixOne = findViewById(R.id.prefixOne);
        prefixTwo = findViewById(R.id.prefixTwo);
        prefixThree = findViewById(R.id.prefixThree);
        // text view
        actionText = findViewById(R.id.messageText);
        // dialog
        dialogPrefix = new Dialog(this);
        changeContentDialog = new Dialog(this);
        creditDialog = new Dialog(this);
        // drawer layout
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        if (menuItem.getItemId() == R.id.delete) {
                            actions.edit().clear().apply();
                            putDefaultValuesInArray(allAction); // put default values in allAction
                            Toast.makeText(getApplicationContext(), "All actions removed!", Toast.LENGTH_LONG).show();
                        }

                        if (menuItem.getItemId() == R.id.theme && !isThemeChanged) { // changing background color
                            mainRelativeLayout.setBackgroundColor(getResources().getColor(R.color.shadowstart));
                            isThemeChanged = true;
                        } else {
                            mainRelativeLayout.setBackgroundColor(getResources().getColor(R.color.white));
                            isThemeChanged = false;
                        }

                        if (menuItem.getItemId() == R.id.credits) {
                            creditDialog.setContentView(R.layout.credit_dialog);
                            creditDialog.show();
                        }
                        return true;
                    }
                });
    }

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

    public void putDefaultValuesInArray(String[] array) {
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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            customPopupAction(view);
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {

            changeContentDialog.setContentView(R.layout.change_content_text);

            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            contentField = changeContentDialog.findViewById(R.id.contentField);
            OKButton = changeContentDialog.findViewById(R.id.OKButton);
            contentButton = (Button) view;
            final String tagOfContentButton = (String) contentButton.getTag();

            OKButton.setOnClickListener(new View.OnClickListener() {
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

                        allAction[Integer.parseInt(tagOfContentButton)] = newTextInput;

                        updateNewContentToSharedPreferences(allAction);

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

    public void updateNewContentToSharedPreferences(String[] arrayOfActions) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : arrayOfActions) {
            stringBuilder.append(s);
            stringBuilder.append(",");
        }

        SharedPreferences actions = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = actions.edit();
        editor.putString("Action", stringBuilder.toString());
        editor.apply();
    }

    public void loadContentToSharedPreferences(String[] arrayOfActions) {

        SharedPreferences actions = getSharedPreferences("preferences", MODE_PRIVATE);
        String stringOfActions = actions.getString("Action", "");
        if (stringOfActions == null) // checks if the string is null
            throw new AssertionError();
        String[] splitActions = stringOfActions.split(",");

        for (int i = 0; i < splitActions.length; i++) {
            arrayOfActions[i] = splitActions[i];
        }

    }

    public void customPopupAction(View view) {
        final Button pressedButton = (Button) view;

        dialogPrefix.dismiss();

        prefixOne.setVisibility(View.INVISIBLE);
        prefixTwo.setVisibility(View.INVISIBLE);
        prefixThree.setVisibility(View.INVISIBLE);

        actionText.setText(chosenPrefix + pressedButton.getText().toString());
        actionText.setVisibility(View.VISIBLE);
    }

    public void closeAction(View view) {
        prefixOne.setVisibility(View.VISIBLE);
        prefixTwo.setVisibility(View.VISIBLE);
        prefixThree.setVisibility(View.VISIBLE);
        actionText.setVisibility(View.INVISIBLE);
        actionText.setText(" ");
        chosenPrefix = " ";
    }

}

