package sg.edu.np.mad.madpractical5;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private User user;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize DBHandler
        dbHandler = new DatabaseHandler(this);

        // Get the random integer from the intent
        int randomNumber = getIntent().getIntExtra("random_integer", 0);

        // Initialize a new User object from the database
        user = dbHandler.getUser(1); // For simplicity, assuming there's a user with ID 1

        // Get the TextViews and Button from the layout
        TextView tvName = findViewById(R.id.tvName);
        TextView tvDescription = findViewById(R.id.tvDescription);
        Button btnFollow = findViewById(R.id.btnFollow);
        Button btnMessage = findViewById(R.id.btnMessage);

        // Set initial user data
        tvName.setText(user.getName());
        tvDescription.setText(user.getDescription());
        btnFollow.setText(user.isFollowed() ? "Unfollow" : "Follow");

        // Set OnClickListener for the Follow/Unfollow button
        btnFollow.setOnClickListener(view -> {
            // Toggle the followed status
            user.toggleFollowStatus();

            // Update the database
            dbHandler.updateUser(user);

            // Update the button text accordingly
            btnFollow.setText(user.isFollowed() ? "Unfollow" : "Follow");

            // Show a Toast message indicating the action
            Toast.makeText(MainActivity.this, user.isFollowed() ? "Followed" : "Unfollowed", Toast.LENGTH_SHORT).show();
        });

        // Set OnClickListener for the Message button (example)
        btnMessage.setOnClickListener(view -> {
            // Example action
            Toast.makeText(MainActivity.this, "Message button clicked", Toast.LENGTH_SHORT).show();
        });
    }
}