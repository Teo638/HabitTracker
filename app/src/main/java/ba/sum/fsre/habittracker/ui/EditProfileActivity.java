package ba.sum.fsre.habittracker.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.model.UserProfile;
import ba.sum.fsre.habittracker.repo.UserProfileRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etUsername, etBio;
    private Button btnSaveProfile;
    private ProgressBar progressBar;
    private UserProfileRepository repository;
    private UserProfile currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etUsername = findViewById(R.id.etUsername);
        etBio = findViewById(R.id.etBio);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        progressBar = findViewById(R.id.progressBar);

        repository = new UserProfileRepository(this);

        loadCurrentProfile();


        btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    private void loadCurrentProfile() {
        progressBar.setVisibility(View.VISIBLE);
        repository.getMyProfile(new Callback<List<UserProfile>>() {
            @Override
            public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    currentUser = response.body().get(0);
                    etUsername.setText(currentUser.getUsername());

                    if (currentUser.getBio() != null) {
                        etBio.setText(currentUser.getBio());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserProfile>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(EditProfileActivity.this, "Greška pri učitavanju", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfile() {
        String newUsername = etUsername.getText().toString().trim();
        String newBio = etBio.getText().toString().trim();

        if (newUsername.isEmpty()) {
            etUsername.setError("Obavezno polje");
            return;
        }

        if (currentUser == null) {
            currentUser = new UserProfile();
        }


        currentUser.setUsername(newUsername);
        currentUser.setBio(newBio);

        progressBar.setVisibility(View.VISIBLE);
        btnSaveProfile.setEnabled(false);

        repository.updateMyProfile(currentUser, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                btnSaveProfile.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Profil ažuriran", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Greška: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSaveProfile.setEnabled(true);
                Toast.makeText(EditProfileActivity.this, "Greška mreže", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

