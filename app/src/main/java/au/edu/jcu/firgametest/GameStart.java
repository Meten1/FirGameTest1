package au.edu.jcu.firgametest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class GameStart extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private TextView welcome_text;
    private TextView introduction_text;
    private String name;
    private int score;

    private UserSQLHelper userSqlHelper;
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
        userSqlHelper = new UserSQLHelper(this);


        welcome_user();
    }

    private void welcome_user() {
        welcome_text.setText(String.format(
                "Welcome play HitPlane %s", name));
        introduction_text.setText(String.format("Up to now, your best score is: %s",score));

    }

    // 创建一个 ActivityResultLauncher 对象
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // 处理返回的结果
                    welcome_text.setText("The game is over. Thank you very much for playing!");
                    int newScore = data.getIntExtra("score",0);
                    if (newScore > score){
                        score = newScore;
                        introduction_text.setText(String.format("Up to now, your best score is: %s",score));
                        SQLiteDatabase db = userSqlHelper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("score", score);

                        System.out.println(db.update("user",contentValues,"name = ?",new String[]{name}));;
                    }

                }
            });

    public void start(View view){
        // 创建一个 Intent 对象
        Intent intent = new Intent(this,GameActivity.class);
        // 启动活动并等待结果
        launcher.launch(intent);
    }
}