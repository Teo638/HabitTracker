package ba.sum.fsre.habittracker.ui;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.habittracker.MainActivity;
import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.api.SupabaseAuthApi;
import ba.sum.fsre.habittracker.api.SupabaseClient;
import ba.sum.fsre.habittracker.model.LoginRequest;
import ba.sum.fsre.habittracker.model.UserResponse;
import ba.sum.fsre.habittracker.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterLink = findViewById(R.id.tvRegisterLink);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                performLogin(email, password);
            } else {
                Toast.makeText(this, "Molimo unesite podatke", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegisterLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }



    private void performLogin(String email, String password) {

        SupabaseAuthApi api = SupabaseClient.getClient().create(SupabaseAuthApi.class);


        LoginRequest request = new LoginRequest(email, password);


        String authUrl = "https://jyzdhvwfgpdtykwpexvt.supabase.co/auth/v1/token";


        Call<UserResponse> call = api.loginUser(authUrl, request, "password");

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    String token = response.body().getAccessToken();
                    String userId = response.body().getUserId();

                    SessionManager sessionManager = new SessionManager(LoginActivity.this);
                    sessionManager.saveSession(token, userId);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                    finish();





                } else {

                    Toast.makeText(LoginActivity.this, "Neuspješna prijava. Provjerite podatke.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

                Toast.makeText(LoginActivity.this, "Greška mreže: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}