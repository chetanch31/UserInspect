package visual.camp.sample.app.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import visual.camp.sample.app.R;

public class ShowActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ArrayList<Bitmap> screenshots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        // Get the screenshots ArrayList from the intent
        screenshots = getIntent().getParcelableArrayListExtra("screenshots");
        Toast.makeText(ShowActivity.this, Integer.toString(screenshots.size()), Toast.LENGTH_SHORT).show();

        // Initialize the ViewPager and set the adapter
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ScreenshotsPagerAdapter(screenshots));
    }

    private class ScreenshotsPagerAdapter extends PagerAdapter {
        private ArrayList<Bitmap> screenshots;

        public ScreenshotsPagerAdapter(ArrayList<Bitmap> screenshots) {
            this.screenshots = screenshots;
        }

        @Override
        public int getCount() {
            return screenshots.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            // Inflate the layout for the screenshot
            View view = LayoutInflater.from(ShowActivity.this).inflate(R.layout.item_screenshot, container, false);

            // Get the ImageView and set the screenshot
            ImageView imageView = view.findViewById(R.id.imageView);
            imageView.setImageBitmap(screenshots.get(position));

            // Add the view to the container
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            // Remove the view from the container
            container.removeView((View) object);
        }
    }
}
