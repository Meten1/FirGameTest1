package au.edu.jcu.firgametest;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginSQLHelper loginSqlHelper = new LoginSQLHelper(MainActivity.this);
        db = loginSqlHelper.getWritableDatabase();


    }

    public void start(View view){
//        Intent intent = new Intent(MainActivity.this, GameActivity.class);
//        startActivity(intent);
        insertUser("Test2","123456",0);
    }

    private void insertUser(String name, String password, int score){
        String insertDataSql = "INSERT INTO user (name, password,score) VALUES (?,?,?)";
        db.execSQL(insertDataSql, new Object[]{name,password,score});
    }

    


}




