package ba.sum.fsre.habittracker.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import ba.sum.fsre.habittracker.R;
import ba.sum.fsre.habittracker.model.UserProfile;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        CircleImageView imgAvatar = findViewById(R.id.imgDetailAvatar);
        TextView tvUsername = findViewById(R.id.tvDetailUsername);
        TextView tvPoints = findViewById(R.id.tvDetailPoints);
        TextView tvBio = findViewById(R.id.tvDetailBio);


        UserProfile user = (UserProfile) getIntent().getSerializableExtra("user_data");

        if (user != null) {
            tvUsername.setText(user.getUsername());
            tvPoints.setText(user.getPoints() + " XP");

            if (user.getBio() != null && !user.getBio().isEmpty()) {
                tvBio.setText(user.getBio());
            } else {
                tvBio.setText("Korisnik nije napisao opis.");
            }

            if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                Picasso.get()
                        .load(user.getAvatarUrl())
                        .placeholder(R.drawable.ic_default_profile)
                        .error(R.drawable.ic_default_profile)
                        .into(imgAvatar);
            }
        }
    }
}