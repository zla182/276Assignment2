package com.example.myapplication.UI;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.myapplication.Model.Lens;
import com.example.myapplication.Model.LensManager;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private LensManager manager=LensManager.getInstance();
    private List<Lens> ListofLen =new ArrayList<>();
    Lens newlens;
    ListView list;
    ArrayAdapter<Lens> adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Depth of Field Calculator");
        addLaunchButton();
        TextView tell=findViewById(R.id.feature);
        tell.setText("Optional Features:" + "\n" + "3.1 App Bar Buttons" + "\n" + "3.2 Edit and Delete Lens" + "\n" + "3.3 Error Checking Inputs" + "\n" + "3.4 Auto-recalculate" + "\n" + "3.7 Empty State");
        Calculate();
        populateList();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 42:
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    String make = data.getStringExtra("make");
                    double aperture = data.getDoubleExtra("aperture", 0);
                    int focal= data.getIntExtra("focal",0);
                    newlens = new Lens(make , aperture ,focal );
                    ListofLen.add(newlens);
                    manager.add(newlens);
                    viewList();
                    if (manager.size() != 0) {
                        TextView report = findViewById(R.id.report);
                        report.setText("");
                        TextView hint = findViewById(R.id.hint);
                        hint.setText("");
                    }
                }
                break;
            case 43:
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    int num = data.getIntExtra("return_num", 0);
                    manager.remove(num);
                    ListofLen.remove(num);
                    viewList();
                    if (manager.size() == 0) {
                        TextView report = findViewById(R.id.report);
                        report.setText("Error! No Len can be deleted !!");
                        report.setTextColor(Color.RED);
                        TextView hint = findViewById(R.id.hint);
                        hint.setText(" + Add new len ------->");
                        hint.setTextColor(Color.RED);
                    }
                }else if(resultCode == RESULT_CANCELED)
                {
                    viewList();
                }
                break;
            default:
        }
    }

    private void viewList()
    {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Calculate() {
        ListView list=findViewById(R.id.listViewMainActivity);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= activity_calculator.createIntent(MainActivity.this,position);
                //intent.putExtra("position", position);
                startActivityForResult(intent,43);
            }
        });
    }

    private void populateList() {
        for (Lens lens:manager)
            ListofLen.add(lens);
        adapter=new ArrayAdapter<>( this, R.layout.single_item, ListofLen);
        adapter.notifyDataSetChanged();
        list= findViewById(R.id.listViewMainActivity);
        list.setAdapter(adapter);
    }

    private void addLaunchButton() {
        FloatingActionButton btn=findViewById(R.id.fab);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this, lens_information.class);
                startActivityForResult(intent,42);
            }
        });
    }
    //finish 
}
