package com.example.yuvaltamir.talki;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

import java.util.Objects;

class CustomDialog extends Dialog {

    public CustomDialog(Context context, int idOfLayout) {
        super(context);
        if (idOfLayout != R.layout.credit_dialog && idOfLayout != R.layout.change_content_text && idOfLayout != R.layout.table_of_animations) {
            Objects.requireNonNull(this.getWindow()).clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setContentView(idOfLayout);
    }

}
