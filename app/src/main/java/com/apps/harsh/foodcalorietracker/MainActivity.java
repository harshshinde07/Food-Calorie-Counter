package com.apps.harsh.foodcalorietracker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.harsh.foodcalorietracker.database.DBHelper;
import com.apps.harsh.foodcalorietracker.fragments.ChartFragment;
import com.apps.harsh.foodcalorietracker.fragments.ProfileFragment;
import com.apps.harsh.foodcalorietracker.model.InfoModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    BottomAppBar bar;
    FloatingActionButton fab;
    FabSpeedDial fabSpeedDial;
    private FatSecretSearch mFatSecretSearch;
    private ImageClassifier classifier;

    private RecyclerView recyclerView;
    private RecyclerAdapter mAdapter;
    DBHelper dbHelper;
    List<InfoModel> itemList;
    TextView textView;
    String f_name, f_date, f_fat, f_proteins, f_carbs, f_cal, f_amt, f_time;
    ProgressBar progressBar;
    ImageView ivImage;

    static final int GALLERY_REQUEST = 1;
    static final int REQUEST_CAMERA = 0;
    private static int CURRENT_PAGE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        fab = findViewById(R.id.fab);
        bar = findViewById(R.id.bar);
        bar.replaceMenu(R.menu.bottom_bar_menu);
        textView = findViewById(R.id.empty);
        ivImage = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.log_list);
        dbHelper = new DBHelper(this);
        itemList = dbHelper.getAllLogs();
        checkEmpty();
        mAdapter = new RecyclerAdapter(itemList, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mFatSecretSearch = new FatSecretSearch();

        getTodaysComsumption();

        fabSpeedDial = findViewById(R.id.fab_speed_dial);

        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_save:
                        galleryIntent();
                        break;

                    case R.id.action_scan:
                        cameraIntent();
                        break;
                }
                //TODO: Start some activity
                return false;
            }
        });

        bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.app_bar_fav:
                        startActivity(new Intent(MainActivity.this, ProfileFragment.class));
                        return true;
                    case  R.id.bmi:
                        // get prompts.xml view
                        LayoutInflater li = LayoutInflater.from(MainActivity.this);
                        View promptsView = li.inflate(R.layout.bmi_prompt, null);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                MainActivity.this);
                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(promptsView);
                        final EditText userWeight =  promptsView
                                .findViewById(R.id.weight);
                        final EditText userHeight =  promptsView
                                .findViewById(R.id.height);
                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("Calculate",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                String str1 = userWeight.getText().toString();
                                                String str2 = userHeight.getText().toString();

                                                if(TextUtils.isEmpty(str1)){
                                                    userWeight.setError("Please enter your weight");
                                                    userWeight.requestFocus();
                                                    return;
                                                }

                                                if(TextUtils.isEmpty(str2)){
                                                    userHeight.setError("Please enter your height");
                                                    userHeight.requestFocus();
                                                    return;
                                                }

                                                float weight = Float.parseFloat(str1);
                                                float height = Float.parseFloat(str2)/100;
                                                float bmiValue = calculateBMI(weight, height);
                                                String bmiInterpretation = interpretBMI(bmiValue);
                                                Toast.makeText(MainActivity.this, "BMI: "+ String.valueOf(bmiValue + " " + bmiInterpretation), Toast.LENGTH_LONG).show();
                                            }
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                dialog.cancel();
                                            }
                                        });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                        return true;
                }
                return false;
            }
        });
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ChartFragment.class));
            }
        });

        try {
            classifier = new ImageClassifier(this);
        } catch (IOException e) {
            Log.e("Image Classifier", e.toString());
        }
    }

    //opens gallery
    private void galleryIntent() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    //intent to camera
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    //checks if it is taken from camera or selected from device
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
        //ivImage.setVisibility(View.VISIBLE);
    }

    //displays the image taken from camera by first creating the file of image then it displays that
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivImage.setImageBitmap(thumbnail);
        classifyFrame();
    }

    //displays the image selected from gallery
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ivImage.setImageBitmap(bm);
        classifyFrame();
    }

    public void getDetails(String name) {
        if (name.length() > 0) {
            searchFood(name, CURRENT_PAGE);
        } else {
            Toast.makeText(MainActivity.this, "Nothing Found!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Classifies a frame from the preview stream.
     */
    private void classifyFrame() {
        if (classifier == null) {
            Log.e("camera", "no");
            return;
        }
        Bitmap bitmap = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
        Bitmap b = Bitmap.createScaledBitmap(bitmap, ImageClassifier.DIM_IMG_SIZE_X, ImageClassifier.DIM_IMG_SIZE_Y, false);
        try {
            classifier = new ImageClassifier(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String textToShow = classifier.classifyFrame(b);
        Log.e("Camera", textToShow);

        String[] split = textToShow.split(":");
        f_name = split[0].toUpperCase().trim();
        getDetails(textToShow);
    }

    /**
     * FatSecret Search
     */
    String food_description;
    String brand;

    @SuppressLint("StaticFieldLeak")
    private void searchFood(final String item, final int page_num) {
        new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... arg0) {
                JSONObject food = mFatSecretSearch.searchFood(item, page_num);
                JSONArray FOODS_ARRAY;
                try {
                    if (food != null) {
                        FOODS_ARRAY = food.getJSONArray("food");
                        if (FOODS_ARRAY != null) {
                            for (int i = 0; i < FOODS_ARRAY.length(); i++) {
                                JSONObject food_items = FOODS_ARRAY.optJSONObject(i);
                                food_description = food_items.getString("food_description");
                                String id = food_items.getString("food_type");
                                if (id.equals("Brand")) {
                                    brand = food_items.getString("brand_name");
                                }
                                if (id.equals("Generic")) {
                                    brand = "Generic";
                                }
                            }
                        }
                    }
                } catch (JSONException exception) {
                    return "Error";
                }
                return food_description;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                String[] parts = result.split(" | ");
                progressBar.setVisibility(View.INVISIBLE);
                f_date = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
                f_time = DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());
                if (parts[3].equals("-")) {
                    f_amt = parts[1];
                    f_cal = parts[5];
                    f_carbs = parts[11];
                    f_proteins = parts[14];
                    f_fat = parts[8];
                } else {
                    f_amt = parts[1];
                    f_cal = parts[4];
                    f_carbs = parts[10];
                    f_proteins = parts[13];
                    f_fat = parts[7];
                }

                //Trimming the strings to include only required values
                f_cal = f_cal.substring(0, f_cal.length() - 4);
                f_carbs = f_carbs.substring(0, f_carbs.length() - 1);
                f_proteins = f_proteins.substring(0, f_proteins.length() - 1);
                f_fat = f_fat.substring(0, f_fat.length() - 1);

//                textView.setText(f_name);
//                linearLayout.setVisibility(View.VISIBLE);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Do you really want to Eat?");
                builder.setMessage(f_name);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, " Added Successfully! ", Toast.LENGTH_SHORT).show();
                        dbHelper.addDetails(f_name, f_date, f_time, Float.valueOf(f_fat), Float.valueOf(f_proteins), Float.valueOf(f_cal), Float.valueOf(f_carbs));
                        dialog.dismiss();
//                        ivImage.setVisibility(View.GONE);
                        itemList = dbHelper.getAllLogs();
                        mAdapter = new RecyclerAdapter(itemList, getApplicationContext());
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapter);
                        checkEmpty();
                        getTodaysComsumption();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        ivImage.setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }.execute();
    }

    public void checkEmpty() {
        if(itemList.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    public void getTodaysComsumption() {
        String currDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        String today = dbHelper.getDailyCal(currDate);
        Toast.makeText(this, "Today's Consumption: "+today, Toast.LENGTH_SHORT).show();
    }

    //Calculate BMI
    private float calculateBMI (float weight, float height) {
        return (float) (weight / (height * height));
    }

    // Interpret what BMI means
    private String interpretBMI(float bmiValue) {

        if (bmiValue < 16) {
            return "Severely underweight";
        } else if (bmiValue < 18.5) {

            return "Underweight";
        } else if (bmiValue < 25) {

            return "Normal";
        } else if (bmiValue < 30) {

            return "Overweight";
        } else {
            return "Obese";
        }
    }
}