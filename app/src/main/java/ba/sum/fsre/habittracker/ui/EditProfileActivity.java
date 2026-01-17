    package ba.sum.fsre.habittracker.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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


    private ImageView imgEditAvatar;

    private UserProfileRepository repository;
    private UserProfile currentUser;

    private byte[] selectedImageBytes;

    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imgEditAvatar.setImageURI(uri);
                    selectedImageBytes = uriToBytes(uri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etUsername = findViewById(R.id.etUsername);
        etBio = findViewById(R.id.etBio);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        progressBar = findViewById(R.id.progressBar);


        imgEditAvatar = findViewById(R.id.imgEditAvatar);

        repository = new UserProfileRepository(this);

        imgEditAvatar.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        loadCurrentProfile();

        btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    private byte[] uriToBytes(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
                    if (currentUser.getAvatarUrl() != null && !currentUser.getAvatarUrl().isEmpty()) {
                        Picasso.get().load(currentUser.getAvatarUrl()).placeholder(R.drawable.ic_default_profile).into(imgEditAvatar);
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
        progressBar.setVisibility(View.VISIBLE);
        btnSaveProfile.setEnabled(false);


        if (selectedImageBytes != null) {
            repository.uploadAvatar(selectedImageBytes, new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {

                    String newAvatarUrl = response.body();
                    if (currentUser == null) currentUser = new UserProfile();

                    currentUser.setAvatarUrl(newAvatarUrl);


                    updateUserData();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    btnSaveProfile.setEnabled(true);
                    Toast.makeText(EditProfileActivity.this, "Greška kod uploada slike", Toast.LENGTH_SHORT).show();
                }
            });
        } else {

            updateUserData();
        }
    }


    private void updateUserData() {
        String newUsername = etUsername.getText().toString().trim();
        String newBio = etBio.getText().toString().trim();

        if (newUsername.isEmpty()) {
            etUsername.setError("Obavezno polje");
            progressBar.setVisibility(View.GONE);
            btnSaveProfile.setEnabled(true);
            return;
        }

        if (currentUser == null) {
            currentUser = new UserProfile();
        }

        currentUser.setUsername(newUsername);
        currentUser.setBio(newBio);

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