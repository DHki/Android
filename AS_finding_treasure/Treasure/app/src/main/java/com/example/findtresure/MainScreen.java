package com.example.findtresure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
    }

    public void main_screen_button(View view){
        switch (view.getId()){
            case R.id.play_solo:
                Intent intent = new Intent(getApplicationContext(), DecideGameMode.class);
                intent.putExtra("mode", 1);

                startActivity(intent);
                break;
            case R.id.play_couple:
                Intent intent1 = new Intent(getApplicationContext(), DecideGameMode.class);
                intent1.putExtra("mode", 0);

                startActivity(intent1);
                break;
            case R.id.help:
                startActivity(new Intent(getApplicationContext(), HelpScreen.class));
                break;
        }
    }
}