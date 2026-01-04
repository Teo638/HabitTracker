package ba.sum.fsre.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.habittracker.ui.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.btnLogout);


        btnLogout.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);


            finish();
        });
    }
}