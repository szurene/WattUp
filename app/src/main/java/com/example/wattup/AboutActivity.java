package com.example.wattup;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import androidx.annotation.NonNull;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // 1. Initialize Toolbar FIRST
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        // 2. Enable Back Button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("About WattUp");
        }

        // Handle Toolbar navigation click (the back arrow)
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // 3. Initialize Views
        TextView tvFullName = findViewById(R.id.tv_about_fullname);
        TextView tvStudentId = findViewById(R.id.tv_about_studentid);
        TextView tvCourse = findViewById(R.id.tv_about_course);
        TextView tvCopyright = findViewById(R.id.tv_about_copyright);
        TextView tvUrl = findViewById(R.id.tv_about_url);

        // 4. Set Static Text
        tvFullName.setText("Zureen Salsabila binti Zaiful Amri");
        tvStudentId.setText("2023400834");
        tvCourse.setText("ICT602 Mobile Technology and Development");
        tvCopyright.setText("Copyright © 2025 WATTUP. All rights reserved.");

        // 5. Setup Hyperlink (Fixed HTML string)
        String githubLink = "<a href='https://github.com/szurene/WattUp'>View GitHub Repository</a>";

        // Use FROM_HTML_MODE_LEGACY for newer Android versions compatibility
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tvUrl.setText(Html.fromHtml(githubLink, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvUrl.setText(Html.fromHtml(githubLink));
        }

        // Makes the link clickable
        tvUrl.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}