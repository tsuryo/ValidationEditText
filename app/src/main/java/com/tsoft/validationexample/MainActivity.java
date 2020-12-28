package com.tsoft.validationexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tsoft.validationedittext.utils.EditTextValidator;

public class MainActivity extends AppCompatActivity implements EditTextValidator.ValidationListener {
    private EditTextValidator mValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.btn);
        mValidator = new EditTextValidator(this);
        mValidator.setLayout(this, R.layout.activity_main);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValidator.validate();
            }
        });
    }

    @Override
    public void onValidated() {
        Toast.makeText(this, "onValidated", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailedToValidate() {
        Toast.makeText(this, "onFailedToValidate", Toast.LENGTH_LONG).show();
    }
}
