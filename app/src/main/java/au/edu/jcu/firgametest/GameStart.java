package au.edu.jcu.firgametest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class GameStart extends AppCompatActivity {
    private TextView welcome_text;
    private TextView introduction_text;
    private String name;
    private int score;
//    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        welcome_text = findViewById(R.id.welcome_text);
        introduction_text = findViewById(R.id.introduction_text);
//        audioManager = new AudioManager(this);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        score = intent.getIntExtra("score",0);


        welcome_user();
    }

    private void welcome_user() {
        welcome_text.setText(String.format(
                "Welcome play HitPlane %s", name));
        introduction_text.setText(String.format("Up to now, your best score is: %s",score));

    }
}