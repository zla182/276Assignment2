package com.example.myapplication.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.example.myapplication.Model.Depth_of_field_calculator;
import com.example.myapplication.Model.Lens;
import com.example.myapplication.Model.LensManager;
import com.example.myapplication.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Objects;

public class activity_calculator extends AppCompatActivity {

    private Lens lens;
    double COC = 0.029;
    boolean click = false;

    int num;
    private LensManager manager = LensManager.getInstance();

    private EditText editTextCOC;
    private EditText editTextDistance;
    private EditText editTextAperture;


    private void deleteLen() {
        Button btn = findViewById(R.id.buttonDelete);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("return_num", num);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void getIntExtraFromIntent() {
        Intent intent = getIntent();
        num = intent.getIntExtra("num", 0);
    }

    private String formatM(double distanceInM) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(distanceInM);
    }

    @SuppressLint({"WrongViewCast", "CutPasteId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Calculate DoF");
        EditText coc = findViewById(R.id.COC);
        coc.setText("" + COC);
        TextView select = findViewById(R.id.select);
        getIntExtraFromIntent();
        lens = manager.get(num);
        select.setText(lens.toString());
        deleteLen();
        EditLens();
        Toolbar mToolbarTb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editTextCOC = (EditText) findViewById(R.id.COC);
        editTextDistance = (EditText) findViewById(R.id.taget_distance);
        editTextAperture = (EditText) findViewById(R.id.input_aperture);

        editTextCOC.addTextChangedListener(Wather);
        editTextDistance.addTextChangedListener(Wather);
        editTextAperture.addTextChangedListener(Wather);
    }

    private TextWatcher Wather = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }


        @Override
        public void afterTextChanged(Editable s) {
            if(click) {
                return;
            }
            click = true;
            Calculate();
            click = false;
        }
    };

    private void Calculate() {
        TextView DepthOfField = findViewById(R.id.DepthOfField);
        TextView FarFocalPoint = findViewById(R.id.FarFocalPoint);
        TextView NearFocalPoint = findViewById(R.id.NearFocalPoint);
        TextView HyperFocalDistance = findViewById(R.id.HyperFocalDistance);

        String coc = editTextCOC.getText().toString();
        double new_coc = Double.parseDouble(coc);
        editTextCOC.setText(String.valueOf(new_coc));


        if (TextUtils.isEmpty(editTextDistance.getText()) || TextUtils.isEmpty(editTextAperture.getText()) || TextUtils.isEmpty(editTextCOC.getText())) {
            Toast.makeText(activity_calculator.this, "Empty is not allowed. ", Toast.LENGTH_SHORT).show();
        } else {
            String distance_str = editTextDistance.getText().toString();
            double focal_distance = Double.parseDouble(distance_str);
            String aperture_str = editTextAperture.getText().toString();
            double aperture = Double.parseDouble(aperture_str);
            Depth_of_field_calculator tempCalculator = new Depth_of_field_calculator(lens, aperture, focal_distance, new_coc);

            //1. Add Lens Activity:
            //a. Make length is > 0
            //b. Focal length is > 0
            //c. Aperture >= 1.4

            if (aperture < 1.4 || focal_distance <= 0) {
                Toast.makeText(activity_calculator.this, "aperture must >= 1.4  and  distance must >0", Toast.LENGTH_SHORT).show();
            } else {
                //2. Calculate Activity:
                //a. Circle of Confusion must be > 0
                //b. Distance to subject > 0
                //c. Selected aperture >= 1.4
                if (new_coc == 0) {
                    HyperFocalDistance.setText("NaN");
                    DepthOfField.setText("NaN");
                    FarFocalPoint.setText("NaN");
                    NearFocalPoint.setText("NaN");
                } else if (lens.getMaximum_aperture() > aperture) {
                    HyperFocalDistance.setText("Invalid Aperture");
                    DepthOfField.setText("Invalid Aperture");
                    FarFocalPoint.setText("Invalid Aperture");
                    NearFocalPoint.setText("Invalid Aperture");
                } else {
                    double farFocalPoint = Depth_of_field_calculator.getFarFocalPoint();
                    double nearFocalPoint = Depth_of_field_calculator.getNearFocalPoint();
                    double depthOfField = Depth_of_field_calculator.getDepthOfField();
                    double hyperFocalDistance = Depth_of_field_calculator.getHyperFocalDistance();
                    HyperFocalDistance.setText(formatM(hyperFocalDistance) + "m");
                    DepthOfField.setText(formatM(depthOfField) + "m");
                    FarFocalPoint.setText(formatM(farFocalPoint) + "m");
                    NearFocalPoint.setText(formatM(nearFocalPoint) + "m");
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            assert data != null;
            String make = data.getStringExtra("make");
            double aperture = data.getDoubleExtra("aperture", 0);
            int focal= data.getIntExtra("focal",0);
            lens.setFocal_length(focal);
            lens.setMaximum_aperture(aperture);
            lens.setMake(make);
            setResult(RESULT_CANCELED,data);
            finish();
        }
    }

    private void EditLens() {
        Button btn = findViewById(R.id.buttonEDIT);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_calculator.this, lens_information.class);
                intent.putExtra("Make", lens.getMake());
                intent.putExtra("Aperture", lens.getMaximum_aperture());
                intent.putExtra("Focal", lens.getFocal_length());
                startActivityForResult(intent,1);
            }
        });
    }

    public static Intent createIntent(Context context, int num) {
        Intent intent=new Intent(context, activity_calculator.class);
        intent.putExtra("num",num);
        return intent;
    }
}