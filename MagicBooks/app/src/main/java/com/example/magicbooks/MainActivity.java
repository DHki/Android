package com.example.magicbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startApp(View view){
        switch(view.getId()){
            case R.id.normal:
                Intent intent1 = new Intent(getApplicationContext(), BookActivity.class);
                intent1.putExtra("sort", 1);
                startActivity(intent1);
                break;
            case R.id.concern:
                Intent intent2 = new Intent(getApplicationContext(), BookActivity.class);
                intent2.putExtra("sort", 2);
                startActivity(intent2);
                break;
            case R.id.lover:
                Intent intent3 = new Intent(getApplicationContext(), BookActivity.class);
                intent3.putExtra("sort", 3);
                startActivity(intent3);
                break;
            case R.id.help:
                startActivity(new Intent(getApplicationContext(), HelpActivity.class));
                break;
        }
    }
}