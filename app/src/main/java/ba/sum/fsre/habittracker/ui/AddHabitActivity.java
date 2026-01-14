package ba.sum.fsre.habittracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.habittracker.R;

public class AddHabitActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_DESC = "extra_desc";
    public static final String EXTRA_FREQ = "extra_freq";

    private EditText edtTitle, edtDescription;
    private Spinner spFrequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        spFrequency = findViewById(R.id.spFrequency);
        Button btnCreate = findViewById(R.id.btnCreateHabit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.habit_frequencies,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFrequency.setAdapter(adapter);

        btnCreate.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String desc = edtDescription.getText().toString().trim();
            String freq = spFrequency.getSelectedItem().toString();

            if (title.isEmpty()) {
                edtTitle.setError("Naslov je obavezan");
                return;
            }

            Intent data = new Intent();
            data.putExtra(EXTRA_TITLE, title);
            data.putExtra(EXTRA_DESC, desc);
            data.putExtra(EXTRA_FREQ, freq);

            setResult(RESULT_OK, data);
            finish();
        });
    }
}
