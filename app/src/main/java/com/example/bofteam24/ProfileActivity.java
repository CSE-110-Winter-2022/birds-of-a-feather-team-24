package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.User;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView coursesRecyclerView;
    private RecyclerView.LayoutManager coursesLayoutManager;
    private CoursesViewAdapter coursesViewAdapter;
    private AppDatabase db;
    private User user;

    public static boolean differentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        db = AppDatabase.singleton(this);

        String userId = getIntent().getStringExtra("user_id");
        String myId = UserSelf.getInstance(this).getUserId();
        differentUser = !userId.equals(myId);
        if (differentUser) {
            //change "my courses" -> "<other_user>'s courses"
            Button waveButton = (Button) findViewById(R.id.wave_button);
            waveButton.setVisibility(View.VISIBLE);

            TextView coursesLabel = findViewById(R.id.my_courses_tv);
            coursesLabel.setText("(user_name)'s courses");

            //hide add course button
            Button addCourseButton = findViewById(R.id.add_course_button);
            addCourseButton.setVisibility(View.GONE);

//            showProfilePictureFromURL("https://upload.wikimedia.org/wikipedia/commons/1/13/Ia-never-gonna-give-you-up-rick-astley-trionfale-remaster-4k-v3-500421.jpg");
        }

        //TODO:
        //list other user's courses
        //set profile pic to user's
        //setTitle to name of user

//        //update
        user = db.userDao().getUserWithId(userId);
        setTitle(user.getName());

        TextView name_tv = findViewById(R.id.name_place_holder_tv);
        name_tv.setText(user.getName());

        showProfilePictureFromURL(user.getPhotoUrl());

        TextView username_courses_label = findViewById(R.id.my_courses_tv);
        username_courses_label.setText(user.getName() + "'s courses");

//        List<CourseRoom> users_courses = db.courseDao().getAll();
        List<CourseRoom> users_courses = db.courseDao().getForUser(userId);

        System.out.println("printing courses");
        for (CourseRoom course : users_courses) {
            System.out.println(course.toString());
        }

        //Set up the recycler view to show our database contents.
        coursesRecyclerView = findViewById(R.id.coursesRecyclerView);
        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        coursesViewAdapter = new CoursesViewAdapter(users_courses, (course) -> {
            db.courseDao().delete(course);
        }, differentUser);
        coursesRecyclerView.setAdapter(coursesViewAdapter);
    }

    private void showProfilePictureFromURL(String url) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                InputStream is = (InputStream) new URL(url).getContent();
                Drawable img = Drawable.createFromStream(is, null);
                runOnUiThread(() -> {
                    ImageView pfp = (ImageView) findViewById(R.id.profile_picture);
                    pfp.setImageDrawable(img);
                });
            } catch (Exception e) {
                Log.e("ProfileActivity", "Failed to load " + url);
                Log.e("ProfileActivity", e.toString());
            }
        });
    }

    private boolean isDifferentUser(String userId) {
        SharedPreferences pref = getSharedPreferences("USER_SHARED_PREF", MODE_PRIVATE);
        String myUserId = pref.getString("ID", "not found");
        if (userId != myUserId) {
            return true;
        }
        return false;
    }

    public void onAddCourseClicked(View view) {
        Intent intent = new Intent(this, AddClassesActivity.class);
        startActivity(intent);
    }

    public void onBackClicked(View view) {
        Intent intent;// changed
        if(ProfileActivity.differentUser) {
            intent = new Intent(this, StudentsListActivity.class);
        }
        else {
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
    }

    public void onWaveClicked(View view) {
        Log.d(ParseUtils.TAG, "----------- I just clicked wave");
        Intent intent = new Intent(this, StudentsListActivity.class);

        // String myId = UserSelf.getInstance(this).getUserId();
        String userId = user.getUserId();

        String waveString = userId + ",wave,,,\n";
        Log.d(ParseUtils.TAG, "----------- wave string is: " + waveString);
        intent.putExtra("my_msg_addition", waveString);
        intent.putExtra("wave_sent", "true");

        Toast.makeText(this, "Wave sent to: " + user.getName(),
                Toast.LENGTH_SHORT).show();
        startActivity(intent);

    }
}
