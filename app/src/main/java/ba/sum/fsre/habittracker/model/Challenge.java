package ba.sum.fsre.habittracker.model;

import com.google.gson.annotations.SerializedName;

public class Challenge {
    @SerializedName("id")
    private String id;

    @SerializedName("creator_id")
    private String creatorId;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("start_date")
    private String startDate;

    public Challenge() {}

    public Challenge(String title, String description, String startDate) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStartDate() { return startDate; }
}