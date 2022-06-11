package com.example.moamoa.ui.mypage;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.moamoa.R;

public class CustomDialog extends Dialog {
    private TextView txt_contents;
    private Button shutdownClick;

    public CustomDialog(@NonNull Context context, String contents) {
        super(context);
        setContentView(R.layout.profileimage);

        shutdownClick = findViewById(R.id.close);
        shutdownClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
