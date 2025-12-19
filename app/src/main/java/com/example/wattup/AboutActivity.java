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

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("About WattUp");
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        TextView tvFullName = findViewById(R.id.tv_about_fullname);
        TextView tvStudentId = findViewById(R.id.tv_about_studentid);
        TextView tvCourse = findViewById(R.id.tv_about_course);
        TextView tvCopyright = findViewById(R.id.tv_about_copyright);
        TextView tvUrl = findViewById(R.id.tv_about_url);

        tvFullName.setText("Zureen Salsabila binti Zaiful Amri");
        tvStudentId.setText("2023400834");
        tvCourse.setText("ICT602 Mobile Technology and Development");
        tvCopyright.setText("Copyright © 2025 WATTUP. All rights reserved.");

        String githubLink = "<a href='https://github.com/szurene/WattUp'>View GitHub Repository</a>";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tvUrl.setText(Html.fromHtml(githubLink, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvUrl.setText(Html.fromHtml(githubLink));
        }

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