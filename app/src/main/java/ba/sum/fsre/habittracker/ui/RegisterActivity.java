package ba.sum.fsre.habittracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.api.SupabaseAuthApi;
import ba.sum.fsre.habittracker.api.SupabaseClient;
import ba.sum.fsre.habittracker.model.LoginRequest;
import ba.sum.fsre.habittracker.model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (password.length() < 6) {
                Toast.makeText(this, "Lozinka mora imati barem 6 znakova", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.isEmpty()) {
                performRegister(email, password);
            } else {
                Toast.makeText(this, "Unesite email", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performRegister(String email, String password) {
        SupabaseAuthApi api = SupabaseClient.getClient(this).create(SupabaseAuthApi.class);
        LoginRequest request = new LoginRequest(email, password);

        String url = "https://jyzdhvwfgpdtykwpexvt.supabase.co/auth/v1/signup";

        Call<UserResponse> call = api.registerUser(url, request);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Registracija uspješna! Prijavite se.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Greška: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Greška mreže", Toast.LENGTH_SHORT).show();
            }
        });
    }
}