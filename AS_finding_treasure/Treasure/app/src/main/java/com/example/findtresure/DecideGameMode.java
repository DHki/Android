package com.example.findtresure;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DecideGameMode extends AppCompatActivity {

    static int mode;
    @Override
    protected void onCreate(Bundle s){
        super.onCreate(s);

        mode = getIntent().getIntExtra("mode", -1);
        setView();
    }

    private void setView(){
        switch (mode){
            case 0:
                setContentView(new Mode_Hide(this));
                break;
            case 1:
                setContentView(new Mode_Play(this));
                break;
        }
    }

}
