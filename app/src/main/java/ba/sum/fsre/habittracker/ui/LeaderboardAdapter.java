package ba.sum.fsre.habittracker.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.model.UserProfile;
import android.animation.ValueAnimator;
import android.content.Intent;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<UserProfile> users = new ArrayList<>();

    public void setUsers(List<UserProfile> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserProfile user = users.get(position);


        if (position == 0) {
            holder.tvRank.setText("🥇");
            holder.tvRank.setTextSize(24);
        } else if (position == 1) {
            holder.tvRank.setText("🥈");
            holder.tvRank.setTextSize(22);
        } else if (position == 2) {
            holder.tvRank.setText("🥉");
            holder.tvRank.setTextSize(22);
        } else {
            holder.tvRank.setText( (position + 1) + ".");
            holder.tvRank.setTextSize(16);
            holder.tvRank.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.rank_default));
            holder.tvRank.setBackground(null);
        }

        if (position <= 2) {
            holder.tvRank.setBackground(null);
        }

        String displayName = (user.getUsername() != null && !user.getUsername().isEmpty())
                ? user.getUsername()
                : "Korisnik";
        holder.tvUsername.setText(displayName);

        int finalPoints = (int) user.getPoints();


        ValueAnimator animator = ValueAnimator.ofInt(0, finalPoints);
        animator.setDuration(1000);
        animator.addUpdateListener(animation -> {
            holder.tvPoints.setText(animation.getAnimatedValue().toString() + " XP");
        });
        animator.start();


        if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
            Picasso.get()
                    .load(user.getAvatarUrl())
                    .placeholder(R.drawable.ic_default_profile)
                    .error(R.drawable.ic_default_profile)
                    .into(holder.imgAvatar);
        } else {
            holder.imgAvatar.setImageResource(R.drawable.ic_default_profile);
        }


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
            intent.putExtra("user_data", user);
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvUsername, tvPoints;
        CircleImageView imgAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            imgAvatar = itemView.findViewById(R.id.imgUserAvatar);
        }
    }
}