package au.edu.jcu.firgametest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private LoginSQLHelper loginSqlHelper;
    EditText name_input;
    EditText password_input;
    ImageView login_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginSqlHelper = new LoginSQLHelper(MainActivity.this);
        name_input = findViewById(R.id.name_input);
        password_input = findViewById(R.id.password_input);
        login_icon = findViewById(R.id.login_icon);
        login_icon.setAdjustViewBounds(true);


    }

    public void login(View view) {
        String name = name_input.getText().toString();
        String password = password_input.getText().toString();
        if (!name.equals("") && !password.equals("")) {
            SQLiteDatabase db = loginSqlHelper.getReadableDatabase();
            String[] columns = {"name", "password"};
            Cursor cursor = db.query("user", columns,
                    String.format("name = '%s'", name),
                    null, null, null, null);
            if (cursor.moveToNext()) {
                if (password.equals(cursor.getString(1))) {
                    Toast.makeText(MainActivity.this,
                            "Login Success", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(
                            MainActivity.this, GameStart.class);
                    intent.putExtra("name", name);
                    int score = cursor.getInt(0);
                    intent.putExtra("score", score);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this,
                            "Password error, please check your name and password!",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MainActivity.this,
                        "The user does not exist!", Toast.LENGTH_LONG).show();
            }
            cursor.close();
            db.close();
            password_input.setText("");
        } else {
            Toast.makeText(MainActivity.this,
                    "Username and password cannot be empty!", Toast.LENGTH_LONG).show();
        }
    }

    public void register(View view) {
        String username = name_input.getText().toString();
        String password = password_input.getText().toString();
        if (!username.equals("") && !password.equals("")) {
            SQLiteDatabase db = loginSqlHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", username);
            contentValues.put("password", password);
            contentValues.put("score", 0);
            if (db.insert("user", null, contentValues) != -1) {
                Toast.makeText(MainActivity.this,
                        "Registration succeeded, please log in~", Toast.LENGTH_LONG).show();
            }
            db.close();
            password_input.setText("");
        } else {
            Toast.makeText(MainActivity.this,
                    "Username and password cannot be empty!", Toast.LENGTH_LONG).show();
        }

    }

}




