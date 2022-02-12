package com.example.bofteam24;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bofteam24.db.AppDatabase;
import com.example.bofteam24.db.CourseRoom;
import com.example.bofteam24.db.User;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = getIntent().getExtras();
        String userId = extras == null ? null : extras.getString("user_id", null);

        boolean differentUser = !"id".equals(userId);
        if (differentUser) {
            //change "my courses" -> "<other_user>'s courses"
            TextView coursesLabel = findViewById(R.id.my_courses_tv);
            coursesLabel.setText("(user_name)'s courses");

            //hide add course button
            Button addCourseButton = findViewById(R.id.add_course_button);
            addCourseButton.setVisibility(View.GONE);


            showProfilePictureFromURL("https://upload.wikimedia.org/wikipedia/commons/1/13/Ia-never-gonna-give-you-up-rick-astley-trionfale-remaster-4k-v3-500421.jpg");
        }

        //TODO:
        //list other user's courses
        //set profile pic to user's
        //setTitle to name of user

        //update
        setTitle("Update to name of user");

        db = AppDatabase.singleton(this);
        List<CourseRoom> courses = db.courseDao().getAll();

        System.out.println("printing courses");
        for (CourseRoom course : courses) {
            System.out.println(course.toString());
        }

        //Set up the recycler view to show our database contents.
        coursesRecyclerView = findViewById(R.id.coursesRecyclerView);
        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        coursesViewAdapter = new CoursesViewAdapter(courses, (course) -> {
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

    public void onAddCourseClicked(View view) {
        Intent intent = new Intent(this, AddClassesActivity.class);
        startActivity(intent);
    }

    public void onBackClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}