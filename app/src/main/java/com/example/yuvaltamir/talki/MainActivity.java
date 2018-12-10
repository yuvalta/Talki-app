package com.example.yuvaltamir.talki;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

// TODO check what about the return button on the device
// TODO create different theme
// TODO fix line 100
// TODO why all of the new content is in CAPS?!

public class MainActivity extends AppCompatActivity {

    Dialog dialogPrefix;
    Dialog changeContentDialog;
    TextView actionText;
    ImageView prefixOne;
    ImageView prefixTwo;
    ImageView prefixThree;
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
    Boolean errorInText = false;
    boolean isThemeChanged = false;

    private String[] allAction = {"a","b","c","d","e","f","g","h","i"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        // drawer layout
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        if (menuItem.getItemId() == R.id.delete) {
                            actions.edit().clear().apply();

                            for (int i = 0; i < allAction.length; i++) {
                                allAction[i] = Integer.parseInt('a' + i); // update to default values - with ascii maybe
                                Toast.makeText(getApplicationContext(), "All actions removed!", Toast.LENGTH_LONG).show();
                            }

                        }

                        if (menuItem.getItemId() == R.id.theme && !isThemeChanged) { // changing background color
                            mainRelativeLayout.setBackgroundColor(getResources().getColor(R.color.shadowstart));
                            isThemeChanged = true;
                        } else {
                            mainRelativeLayout.setBackgroundColor(getResources().getColor(R.color.white));
                            isThemeChanged = false;
                        }
                        return true;
                    }
                });
    }

    public void customPopupPrefix(View view) {

        if(view.getId() == R.id.prefixOne) {
            dialogPrefix.setContentView(R.layout.popup_prefix_one);

            content1_1 = dialogPrefix.findViewById(R.id.content1_1);
            content1_2 = dialogPrefix.findViewById(R.id.content1_2);
            content1_3 = dialogPrefix.findViewById(R.id.content1_3);

            content1_1.setText(allAction[0]);
            content1_2.setText(allAction[1]);
            content1_3.setText(allAction[2]);

            content1_1.setOnClickListener(onClickListener);
            content1_1.setOnLongClickListener(onLongClickListener);
            content1_2.setOnClickListener(onClickListener);
            content1_2.setOnLongClickListener(onLongClickListener);
            content1_3.setOnClickListener(onClickListener);
            content1_3.setOnLongClickListener(onLongClickListener);
        }

        if(view.getId() == R.id.prefixTwo) {
            dialogPrefix.setContentView(R.layout.popup_prefix_two);

            content2_1 = dialogPrefix.findViewById(R.id.content2_1);
            content2_2 = dialogPrefix.findViewById(R.id.content2_2);
            content2_3 = dialogPrefix.findViewById(R.id.content2_3);

            content2_1.setText(allAction[3]);
            content2_2.setText(allAction[4]);
            content2_3.setText(allAction[5]);

            content2_1.setOnClickListener(onClickListener);
            content2_1.setOnLongClickListener(onLongClickListener);
            content2_2.setOnClickListener(onClickListener);
            content2_2.setOnLongClickListener(onLongClickListener);
            content2_3.setOnClickListener(onClickListener);
            content2_3.setOnLongClickListener(onLongClickListener);
        }

        if(view.getId() == R.id.prefixThree) {
            dialogPrefix.setContentView(R.layout.popup_prefix_three);

            content3_1 = dialogPrefix.findViewById(R.id.content3_1);
            content3_2 = dialogPrefix.findViewById(R.id.content3_2);
            content3_3 = dialogPrefix.findViewById(R.id.content3_3);

            content3_1.setText(allAction[6]);
            content3_2.setText(allAction[7]);
            content3_3.setText(allAction[8]);

            content3_1.setOnClickListener(onClickListener);
            content3_1.setOnLongClickListener(onLongClickListener);
            content3_2.setOnClickListener(onClickListener);
            content3_2.setOnLongClickListener(onLongClickListener);
            content3_3.setOnClickListener(onClickListener);
            content3_3.setOnLongClickListener(onLongClickListener);
        }
        dialogPrefix.show();
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
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

            contentField = changeContentDialog.findViewById(R.id.contentField);
            OKButton = changeContentDialog.findViewById(R.id.OKButton);
            contentButton = (Button) view;
            final String tagOfContentButton = (String) contentButton.getTag();

            OKButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    pressedButton = (Button) view;
                    newTextInput = contentField.getEditText().getText().toString().trim(); // get the text from the text box

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

                        imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN,0); // close keyboard

                        changeContentDialog.dismiss();
                    }
                    else{
                        errorInText = false;
                        return;
                    }
                }
            });

            changeContentDialog.show();

            return true;
        }
    };

    public void updateNewContentToSharedPreferences(String[] arrayOfActions) {
        StringBuilder stringBuilder = new StringBuilder();
        for(String s : arrayOfActions) {
            stringBuilder.append(s);
            stringBuilder.append(",");
        }

        SharedPreferences actions = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = actions.edit();
        editor.putString("Action", stringBuilder.toString());
        editor.commit();
    }

    public void loadContentToSharedPreferences(String[] arrayOfActions) {

        SharedPreferences actions = getSharedPreferences("preferences", MODE_PRIVATE);
        String stringOfActions = actions.getString("Action", "");
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

        actionText.setText(pressedButton.getText().toString());
        actionText.setVisibility(View.VISIBLE);
    }

    public void closeAction(View view) {
        prefixOne.setVisibility(View.VISIBLE);
        prefixTwo.setVisibility(View.VISIBLE);
        prefixThree.setVisibility(View.VISIBLE);
        actionText.setVisibility(View.INVISIBLE);
        actionText.setText(" ");
    }

}

