package ba.sum.fsre.habittracker.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.model.Challenge;
import ba.sum.fsre.habittracker.repo.ChallengeRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.time.LocalDate;


public class CreateChallengeActivity extends AppCompatActivity {

    private EditText etTitle, etDesc;
    private Button btnCreate;
    private ChallengeRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);

        TextInputEditText edtTitle = findViewById(R.id.edtChallengeTitle);
        TextInputEditText edtDescription = findViewById(R.id.edtChallengeDescription);
        MaterialButton btnCreate = findViewById(R.id.btnCreateChallenge);
        repository = new ChallengeRepository(this);

        btnCreate.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String desc = etDesc.getText().toString();

            String date = LocalDate.now().toString();

            if (title.isEmpty()) {
                Toast.makeText(this, "Naziv je obavezan", Toast.LENGTH_SHORT).show();
                return;
            }

            Challenge newChallenge = new Challenge(title, desc, date);

            repository.createChallenge(newChallenge, new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CreateChallengeActivity.this, "Izazov kreiran!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CreateChallengeActivity.this, "Greška: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(CreateChallengeActivity.this, "Greška mreže", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}