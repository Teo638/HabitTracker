package ba.sum.fsre.habittracker.model;

import com.google.gson.annotations.SerializedName;

public class HabitLog {

    @SerializedName("id")
    private String id;

    @SerializedName("habit_id")
    private String habitId;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("completed_at")
    private String completedAt; 

    public HabitLog(String habitId, String userId, String completedAt) {
        this.habitId = habitId;
        this.userId = userId;
        this.completedAt = completedAt;
    }

    public String getHabitId() { return habitId; }

    public String getCompletedAt() { return completedAt; }


}
