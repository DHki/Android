package com.example.magicbooks;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.help_screen);

        TextView text = (TextView) findViewById(R.id.helpScript);

        String script = "질문을 말하고, 화면을 터치하면 책이 대답을 해드립니다.\n";
        script += "각 책들에는 200개 이상의 대답이 있습니다.\n";
        script += "재미로 즐겨주세요 ^^*\n";
        script += "\n\n\n\n\n 만든이 : 김동하\n 오류 문의 : kdh101800@gmail.com";

        text.setText(script);
    }

    public void returnStart(View view){
        finish();
    }
}
