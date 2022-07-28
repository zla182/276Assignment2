package com.example.myapplication.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.Model.Lens;
import com.example.myapplication.Model.LensManager;
import com.example.myapplication.R;

import java.util.Objects;

import static com.example.myapplication.R.layout.activity_lens_information;


public class lens_information extends AppCompatActivity {


    EditText newMake;
    EditText newAperture;
    EditText newFocal;
    String make;

    @Override
    protected void onCreate(Bundle information) {

        super.onCreate(information);
        setContentView(activity_lens_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Lens Details");
        Intent intent = getIntent();
        newMake = findViewById(R.id.editMake);
        newAperture = findViewById(R.id.editAperture);
        newFocal = findViewById(R.id.editFocal);
        make = intent.getStringExtra("Make");
        double aperture = intent.getDoubleExtra("Aperture", 0);
        int F = intent.getIntExtra("Focal",0);
        if(make != null) {
            String ApertureValue = new Double(aperture).toString();
            String F_Value = new Integer(F).toString();
            newMake.setText(make);
            newAperture.setText(ApertureValue);
            newFocal.setText(F_Value);
        }

        Toolbar mToolbarTb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lens_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.save_botton:
                if(TextUtils.isEmpty(newMake.getText()) || TextUtils.isEmpty(newAperture.getText())||TextUtils.isEmpty(newFocal.getText())){
                    Toast.makeText(lens_information.this," Invalid input ",Toast.LENGTH_SHORT).show();
                }else if(make != null){
                    String insertMake = newMake.getText().toString();
                    double insertAperture = Double.parseDouble(newAperture.getText().toString());
                    int insertFocal = Integer.parseInt(newFocal.getText().toString());
                    Intent intent = new Intent();
                    intent.putExtra("make",insertMake);
                    intent.putExtra("aperture", insertAperture);
                    intent.putExtra("focal",insertFocal);
                    setResult(RESULT_OK,intent);
                    finish();
                }
                else{
                    String insertMake = newMake.getText().toString();
                    double insertAperture = Double.parseDouble(newAperture.getText().toString());
                    int insertFocal = Integer.parseInt(newFocal.getText().toString());
                    if( insertMake.length() <= 0 || insertAperture < 1.4 || insertFocal <=0){
                        Toast.makeText(lens_information.this,"a. Make length is > 0\n" +
                                "b. Focal length is > 0\n" +
                                "c. Aperture >= 1.4",Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent();
                        intent.putExtra("make",insertMake);
                        intent.putExtra("aperture", insertAperture);
                        intent.putExtra("focal",insertFocal);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}