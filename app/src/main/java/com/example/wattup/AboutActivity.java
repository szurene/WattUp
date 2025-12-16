package com.example.wattup;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.text.method.LinkMovementMethod;

import com.google.android.material.appbar.MaterialToolbar;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("About WattUp");
        }

        TextView tvFullName = findViewById(R.id.tv_about_fullname);
        TextView tvStudentId = findViewById(R.id.tv_about_studentid);
        TextView tvCourse = findViewById(R.id.tv_about_course);
        TextView tvCopyright = findViewById(R.id.tv_about_copyright);
        TextView tvUrl = findViewById(R.id.tv_about_url);

        tvFullName.setText("Zureen Salsabila binti Zaiful Amri");
        tvStudentId.setText("2023400834");
        tvCourse.setText("ICT602 Mobile Technology and Development");

        tvCopyright.setText("Copyright © 2025 WATTUP. All rights reserved.");

        String githubLink = "<a href='https://github.com/szurene/WattUp</a>";
        tvUrl.setText(android.text.Html.fromHtml(githubLink));
        tvUrl.setMovementMethod(LinkMovementMethod.getInstance());

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}