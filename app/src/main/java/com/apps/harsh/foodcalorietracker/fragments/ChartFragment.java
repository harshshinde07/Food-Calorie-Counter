package com.apps.harsh.foodcalorietracker.fragments;

import android.os.Bundle;
import com.apps.harsh.foodcalorietracker.R;
import com.apps.harsh.foodcalorietracker.database.DBHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class ChartFragment extends AppCompatActivity {

    DBHelper dbHelper;
    BarChart chart;
    ArrayList<com.github.mikephil.charting.data.BarEntry> BarEntry;
    ArrayList<String> BarEntryLabels;
    BarDataSet Bardataset;
    com.github.mikephil.charting.data.BarData BarData;
    Float cal_1 = 0f, cal_2 = 0f, cal_3 = 0f, cal_4 = 0f, cal_5 = 0f, cal_6 = 0f, cal_7 = 0f;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Summary");

        dbHelper = new DBHelper(this);

        Calendar now_1 = Calendar.getInstance();
        now_1.add(Calendar.DATE, -1);
        String today_1 = DateFormat.getDateInstance().format(now_1.getTime());
        cal_1 = Float.valueOf(dbHelper.getDailyCal(today_1));

        Calendar now_2 = Calendar.getInstance();
        now_2.add(Calendar.DATE, -2);
        String today_2 = DateFormat.getDateInstance().format(now_2.getTime());
        cal_2 = Float.valueOf(dbHelper.getDailyCal(today_2));

        Calendar now_3 = Calendar.getInstance();
        now_3.add(Calendar.DATE, -3);
        String today_3 = DateFormat.getDateInstance().format(now_3.getTime());
        cal_3 = Float.valueOf(dbHelper.getDailyCal(today_3));

        Calendar now_4 = Calendar.getInstance();
        now_4.add(Calendar.DATE, -4);
        String today_4 = DateFormat.getDateInstance().format(now_4.getTime());
        cal_4 = Float.valueOf(dbHelper.getDailyCal(today_4));

        Calendar now_5 = Calendar.getInstance();
        now_5.add(Calendar.DATE, -5);
        String today_5 = DateFormat.getDateInstance().format(now_5.getTime());
        cal_5 = Float.valueOf(dbHelper.getDailyCal(today_5));

        Calendar now_6 = Calendar.getInstance();
        now_6.add(Calendar.DATE, -6);
        String today_6 = DateFormat.getDateInstance().format(now_6.getTime());
        cal_6 = Float.valueOf(dbHelper.getDailyCal(today_6));

        Calendar now_7 = Calendar.getInstance();
        now_7.add(Calendar.DATE, -7);
        String today_7 = DateFormat.getDateInstance().format(now_7.getTime());
        cal_7 = Float.valueOf(dbHelper.getDailyCal(today_7));

        chart = findViewById(R.id.chart);

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        BarEntry = new ArrayList<>();

        BarEntryLabels = new ArrayList<String>();

        BarEntry.add(new BarEntry(cal_7, 0));
        BarEntry.add(new BarEntry(cal_6, 1));
        BarEntry.add(new BarEntry(cal_5, 2));
        BarEntry.add(new BarEntry(cal_4, 3));
        BarEntry.add(new BarEntry(cal_3, 4));
        BarEntry.add(new BarEntry(cal_2, 5));
        BarEntry.add(new BarEntry(cal_1, 6));

        BarEntryLabels.add(today_7);
        BarEntryLabels.add(today_6);
        BarEntryLabels.add(today_5);
        BarEntryLabels.add(today_4);
        BarEntryLabels.add(today_3);
        BarEntryLabels.add(today_2);
        BarEntryLabels.add(today_1);

        Bardataset = new BarDataSet(BarEntry, "Calories");
        BarData = new BarData(BarEntryLabels, Bardataset);
        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(BarData);
        chart.animateY(3000);
    }
}