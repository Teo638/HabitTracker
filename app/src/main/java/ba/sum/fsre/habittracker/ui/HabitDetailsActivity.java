package ba.sum.fsre.habittracker.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.model.Habit;
import ba.sum.fsre.habittracker.model.HabitLog;
import ba.sum.fsre.habittracker.repo.HabitRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HabitDetailsActivity extends AppCompatActivity {

    private TextView tvTitle, tvDesc, tvStreak;
    private MaterialCalendarView calendarView;
    private HabitRepository repository;
    private Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_details);

        tvTitle = findViewById(R.id.tvDetailTitle);
        tvDesc = findViewById(R.id.tvDetailDesc);
        tvStreak = findViewById(R.id.tvDetailStreak);
        calendarView = findViewById(R.id.calendarView);

        repository = new HabitRepository(this);

        habit = (Habit) getIntent().getSerializableExtra("habit_data");
        int streak = getIntent().getIntExtra("habit_streak", 0);

        if (habit != null) {
            tvTitle.setText(habit.getTitle());
            tvDesc.setText(habit.getDescription());
            tvStreak.setText("Streak: " + streak);

            calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
            loadLogs();
        }
    }

    private void loadLogs() {

        String startDate = LocalDate.now().withDayOfYear(1).toString();
        String endDate = LocalDate.now().toString();


        List<String> habitIds = new ArrayList<>();
        habitIds.add(habit.getId());


        repository.getWeeklyLogs(habitIds, startDate, endDate, new Callback<List<HabitLog>>() {
            @Override
            public void onResponse(Call<List<HabitLog>> call, Response<List<HabitLog>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setupCalendar(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<HabitLog>> call, Throwable t) {
                Toast.makeText(HabitDetailsActivity.this, "Greška pri učitavanju", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setupCalendar(List<HabitLog> logs) {
        List<CalendarDay> greenDays = new ArrayList<>();
        List<CalendarDay> redDays = new ArrayList<>();

        LocalDate today = LocalDate.now();

        LocalDate creationDate = LocalDate.now();
        try {
            if (habit.getCreatedAt() != null && habit.getCreatedAt().length() >= 10) {
                creationDate = LocalDate.parse(habit.getCreatedAt().substring(0, 10));
            }
        } catch (Exception e) {}


        for (HabitLog log : logs) {
            if (log.getHabitId().equals(habit.getId())) {
                LocalDate date = LocalDate.parse(log.getCompletedAt());
                greenDays.add(CalendarDay.from(date.getYear(), date.getMonthValue(), date.getDayOfMonth()));
            }
        }


        for (LocalDate date = creationDate; date.isBefore(today); date = date.plusDays(1)) {
            CalendarDay day = CalendarDay.from(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            if (!greenDays.contains(day)) {
                redDays.add(day);
            }
        }

        calendarView.addDecorator(new EventDecorator(Color.parseColor("#4CAF50"), greenDays));
        calendarView.addDecorator(new EventDecorator(Color.parseColor("#F44336"), redDays));
    }
}