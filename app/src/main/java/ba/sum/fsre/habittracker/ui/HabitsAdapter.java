package ba.sum.fsre.habittracker.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.model.Habit;

public class HabitsAdapter extends RecyclerView.Adapter<HabitsAdapter.HabitViewHolder> {

    private List<Habit> habits = new ArrayList<>();

    public void setHabits(List<Habit> habits) {
        this.habits = habits;
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

        holder.txtTitle.setText(habits.get(position).getTitle());

        Habit habit = habits.get(position);
        holder.txtTitle.setText(habit.getTitle());

    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        CheckBox cbDone;
        ImageView imgDelete;

        public HabitViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            cbDone = itemView.findViewById(R.id.cbDone);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }



}

