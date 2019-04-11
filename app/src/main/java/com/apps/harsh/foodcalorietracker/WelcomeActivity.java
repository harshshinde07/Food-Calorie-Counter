package com.apps.harsh.foodcalorietracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class WelcomeActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    EditText etName, etAge, etGender, etWeight, etHeight, etGoal, etBlood;
    private ViewPager viewPager;
    //    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnNext;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        // Making notification bar transparent
//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        }

        setContentView(R.layout.activity_welcome);

//        viewPager = findViewById(R.id.view_pager);
//        dotsLayout = findViewById(R.id.layoutDots);
        btnNext = findViewById(R.id.btn_next);

        etName = findViewById(R.id.name);
        etAge = findViewById(R.id.age);
        etBlood = findViewById(R.id.blood);
        etGender = findViewById(R.id.gender);
        etWeight = findViewById(R.id.weight);
        etHeight = findViewById(R.id.height);
        etGoal = findViewById(R.id.goal);


        // layouts of all welcome sliders
        // add few more layouts if you want
//        layouts = new int[]{
//                R.layout.welcome_slide0,
//                R.layout.welcome_slide1,
//                R.layout.welcome_slide2,
//                R.layout.welcome_slide3,
//                R.layout.welcome_slide4,
//                R.layout.welcome_slide5,
//                R.layout.welcome_slide6,
//                R.layout.welcome_slide7};
//
//        // adding bottom dots
//        addBottomDots(0);
//
//        // making notification bar transparent
//        changeStatusBarColor();
//
//        myViewPagerAdapter = new MyViewPagerAdapter();
//        viewPager.setAdapter(myViewPagerAdapter);
//        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
//                int current = getItem(+1);
//                if (current < layouts.length) {
//                    // move to next screen
//                    viewPager.setCurrentItem(current);
//                } else {
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("prefName", etName.getText().toString());
                editor.putString("prefGender", etGender.getText().toString());
                editor.putString("prefBlood", etBlood.getText().toString());
                editor.putString("prefWeight", etWeight.getText().toString());
                editor.putString("prefHeight", etHeight.getText().toString());
                editor.putString("prefAge", etAge.getText().toString());
                editor.putString("prefGoal", etGoal.getText().toString());
                editor.apply();
                launchHomeScreen();
//                }
            }
        });
    }

//    private void addBottomDots(int currentPage) {
//        dots = new TextView[layouts.length];
//
//        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
//        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
//
//        dotsLayout.removeAllViews();
//        for (int i = 0; i < dots.length; i++) {
//            dots[i] = new TextView(this);
//            dots[i].setText(Html.fromHtml("&#8226;"));
//            dots[i].setTextSize(35);
//            dots[i].setTextColor(colorsInactive[currentPage]);
//            dotsLayout.addView(dots[i]);
//        }
//
//        if (dots.length > 0)
//            dots[currentPage].setTextColor(colorsActive[currentPage]);
//    }
//
//    private int getItem(int i) {
//        return viewPager.getCurrentItem() + i;
//    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, SplashActivity.class));
        finish();
    }

    //	viewpager change listener
//    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
//
//        @Override
//        public void onPageSelected(int position) {
//            addBottomDots(position);
//
//            // changing the next button text 'NEXT' / 'GOT IT'
//            if (position == layouts.length - 1) {
//                // last page. make button text to GOT IT
//                btnNext.setText(getString(R.string.start));
//            } else {
//                // still pages are left
//                btnNext.setText(getString(R.string.next));
//            }
//        }
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//
//        }
//    };

    /**
     * Making notification bar transparent
     */
//    private void changeStatusBarColor() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
//    }

    /**
     * View pager adapter
     */
//    public class MyViewPagerAdapter extends PagerAdapter {
//        private LayoutInflater layoutInflater;
//
//        public MyViewPagerAdapter() {
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            View view = layoutInflater.inflate(layouts[position], container, false);
//            container.addView(view);
//
//            return view;
//        }
//
//        @Override
//        public int getCount() {
//            return layouts.length;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object obj) {
//            return view == obj;
//        }
//
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            View view = (View) object;
//            container.removeView(view);
//        }
//    }
}
