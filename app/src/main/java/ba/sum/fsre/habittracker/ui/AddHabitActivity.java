package ba.sum.fsre.habittracker.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.model.Habit;
import ba.sum.fsre.habittracker.repo.HabitRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddHabitActivity extends AppCompatActivity {

    private EditText edtTitle, edtDescription;
    private HabitRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        repository = new HabitRepository(this);

        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);

        Button btnCreate = findViewById(R.id.btnCreateHabit);


        btnCreate.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String desc = edtDescription.getText().toString().trim();

            String freq = "Daily";

            if (title.isEmpty()) {
                edtTitle.setError("Naslov je obavezan");
                return;
            }


            Habit newHabit = new Habit(null, title, desc, freq);

            repository.createHabit(newHabit, new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddHabitActivity.this, "Navika kreirana!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddHabitActivity.this,
                                "Greška: " + response.code(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(AddHabitActivity.this,
                            "Greška mreže: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
