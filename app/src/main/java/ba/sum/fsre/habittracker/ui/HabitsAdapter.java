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

    private java.util.Set<String> completedToday = new java.util.HashSet<>();

    public void setCompletedToday(java.util.Set<String> ids) {
        this.completedToday = ids;
        notifyDataSetChanged();
    }

    public interface OnHabitActionListener {
        void onDelete(Habit habit);
        void onCheck(Habit habit, boolean isChecked);
    }

    private OnHabitActionListener listener;

    public HabitsAdapter(OnHabitActionListener listener) {
        this.listener = listener;
    }

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

        Habit habit = habits.get(position);

        holder.txtTitle.setText(habit.getTitle());


        holder.imgDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(habit);
            }
        });


        holder.cbDone.setOnCheckedChangeListener(null);

        boolean checked = completedToday.contains(habit.getId());
        holder.cbDone.setChecked(checked);

        holder.cbDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onCheck(habit, isChecked);
            }
        });
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

