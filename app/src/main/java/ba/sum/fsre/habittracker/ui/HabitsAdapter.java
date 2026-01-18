package ba.sum.fsre.habittracker.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.model.Habit;

public class HabitsAdapter extends RecyclerView.Adapter<HabitsAdapter.HabitViewHolder> {

    // Student 2: klik na CHECK (nema odznacavanja)
    public interface OnHabitActionListener {
        void onDelete(Habit habit);
        void onCheckIn(Habit habit); // check-in za danas
    }

    private final OnHabitActionListener listener;

    private List<Habit> habits = new ArrayList<>();

    // habitId -> set datuma ("YYYY-MM-DD") za trenutnu sedmicu
    private Map<String, Set<String>> weeklyDone = new HashMap<>();

    private LocalDate today = LocalDate.now();
    private LocalDate weekStart = today.with(DayOfWeek.MONDAY); // ponedjeljak

    public HabitsAdapter(OnHabitActionListener listener) {
        this.listener = listener;
    }

    public void setHabits(List<Habit> habits) {
        this.habits = (habits != null) ? habits : new ArrayList<>();
        notifyDataSetChanged();
    }

    // Fragment treba pozvati ovo kad ucita weekly logove
    public void setWeeklyDone(Map<String, Set<String>> weeklyDone) {
        this.weeklyDone = (weeklyDone != null) ? weeklyDone : new HashMap<>();
        notifyDataSetChanged();
    }

    // Fragment moze pozvati kad se promijeni sedmica ili kad se vrati u app
    public void setWeek(LocalDate weekStart, LocalDate today) {
        if (weekStart != null) this.weekStart = weekStart;
        if (today != null) this.today = today;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_habit, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.txtTitle.setText(habit.getTitle());

        // Delete
        holder.imgDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(habit);
        });

        // Dohvati set odradjenih datuma za ovaj habit
        Set<String> doneDates = weeklyDone.get(habit.getId());
        if (doneDates == null) doneDates = new HashSet<>();

        // Oboji 7 krugova (Pon..Ned)
        paintWeek(holder, doneDates);

        // Streak (uzastopni dani unutar sedmice do danas)
        int streak = computeStreak(doneDates);
        holder.txtStreak.setText("Streak: " + streak);

        // CHECK logika: ako danas vec postoji log -> checked + disabled
        boolean doneToday = doneDates.contains(today.toString());

        holder.cbDone.setOnCheckedChangeListener(null);
        holder.cbDone.setChecked(doneToday);
        holder.cbDone.setEnabled(!doneToday);
        holder.cbDone.setAlpha(doneToday ? 0.4f : 1f);

        // Nema odznacavanja: cekiraj => callback; odcekiraj => vrati na cekirano
        holder.cbDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (doneToday) return; // vec odradjeno, ignorisi

            if (isChecked) {
                // korisnik radi check-in
                if (listener != null) listener.onCheckIn(habit);
            } else {
                // ne dozvoljavamo odznacavanje
                holder.cbDone.setOnCheckedChangeListener(null);
                holder.cbDone.setChecked(true);
                holder.cbDone.setOnCheckedChangeListener((bv, chk) -> {
                    if (!doneToday && chk && listener != null) listener.onCheckIn(habit);
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    // ===== Weekly paint + streak helpers =====

    private void paintWeek(HabitViewHolder holder, Set<String> doneDates) {
        TextView[] circles = new TextView[]{
                holder.dayMon, holder.dayTue, holder.dayWed, holder.dayThu,
                holder.dayFri, holder.daySat, holder.daySun
        };

        for (int i = 0; i < 7; i++) {
            LocalDate d = weekStart.plusDays(i);
            String ds = d.toString();

            if (doneDates.contains(ds)) {
                tintCircle(circles[i], "#34C759"); // ZELENO
            } else if (d.isBefore(today)) {
                tintCircle(circles[i], "#FF3B30"); // CRVENO
            } else {
                tintCircle(circles[i], "#777777"); // SIVO
            }
        }
    }

    private int computeStreak(Set<String> doneDates) {
        int streak = 0;
        LocalDate cursor = today;

        // brojimo unazad od danas do ponedjeljka
        while (!cursor.isBefore(weekStart)) {
            if (doneDates.contains(cursor.toString())) streak++;
            else break;
            cursor = cursor.minusDays(1);
        }
        return streak;
    }

    private void tintCircle(TextView tv, String hex) {
        if (tv == null) return;
        int color = Color.parseColor(hex);

        Drawable bg = tv.getBackground();
        if (bg != null) {
            Drawable wrapped = DrawableCompat.wrap(bg.mutate());
            DrawableCompat.setTint(wrapped, color);
            tv.setBackground(wrapped);
        }
    }

    // ===== ViewHolder =====
    static class HabitViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtStreak;

        CheckBox cbDone;
        ImageView imgDelete;

        TextView dayMon, dayTue, dayWed, dayThu, dayFri, daySat, daySun;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtStreak = itemView.findViewById(R.id.txtStreak);

            cbDone = itemView.findViewById(R.id.cbDone);
            imgDelete = itemView.findViewById(R.id.imgDelete);

            dayMon = itemView.findViewById(R.id.dayMon);
            dayTue = itemView.findViewById(R.id.dayTue);
            dayWed = itemView.findViewById(R.id.dayWed);
            dayThu = itemView.findViewById(R.id.dayThu);
            dayFri = itemView.findViewById(R.id.dayFri);
            daySat = itemView.findViewById(R.id.daySat);
            daySun = itemView.findViewById(R.id.daySun);
        }
    }
}
