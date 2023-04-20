package com.example.weather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(View view, @Nullable String url) {
        if (url != null && !url.isEmpty()) {
            Picasso
                .get()
                .load(url)
                .into((ImageView) view);
        }
    }
}
