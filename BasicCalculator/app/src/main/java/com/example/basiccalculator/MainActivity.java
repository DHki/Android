package com.example.basiccalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button but = (Button) findViewById(R.id.but); // button for check answer

        butListen bL = new  butListen();
        but.setOnClickListener(bL);
    }

    class butListen implements View.OnClickListener {
        public void onClick(View v) {
            EditText e1 = (EditText) findViewById(R.id.edit1); // first number
            EditText e2 = (EditText) findViewById(R.id.edit2); // second number
            TextView ans = (TextView) findViewById(R.id.ans); // answer

            String input1 = e1.getText().toString();
            String input2 = e2.getText().toString();
            int sum = Integer.parseInt(input1) + Integer.parseInt(input2);
            ans.setText(Integer.toString(sum));
        }
    }
}