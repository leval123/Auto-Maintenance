package com.example.jultrautomaintenance;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    Button ManageVehicles, VideoLessons, ShopsNearMe;
    private RecyclerView rv;
    private DatabaseReference db;
    private ArrayList<String> list;
    NotifRecyclerViewAdapter adapter;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    boolean bingbong=false;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        bingbong=false;
        ManageVehicles = (Button) findViewById(R.id.Manage_Vehicles);
        VideoLessons = (Button) findViewById(R.id.Video_Lessons);
        ShopsNearMe = (Button) findViewById(R.id.Shops_Near_Me);
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        VideoLessons.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,VideoTuts.class));
            }
        });

        ShopsNearMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,MapsActivity.class));
            }
        });

        rv= findViewById(R.id.notifHolder);
        db = FirebaseDatabase.getInstance().getReference("Vehicles");
        rv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<String>();
        adapter = new NotifRecyclerViewAdapter(getApplicationContext(),list,HomeActivity.this);
        rv.setAdapter(adapter);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    System.out.println("GETS HERE TOO");
                    CarModel car = dataSnapshot.getValue(CarModel.class);
                    String notification;
                    if(car.getOwner().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        System.out.println("GETS HERE");
                        notification = car.getYear()+"-"+car.getMake() + "-" + car.getModel()+"-";
                        if(Integer.parseInt(car.getMileage())>=150000){
                            list.add((notification+"Replace your spark plugs"));
                        }
                        if(Integer.parseInt(car.getMileage())>=100000){
                            list.add((notification+"Transmission flush recommended"));
                        }
                        if(Integer.parseInt(car.getMileage())>=12000){
                            list.add((notification+"Oil change should happen ever 12000 km! You should have changed approximately it "+Integer.parseInt(car.getMileage())/12000 +" times!"));
                        }
                        if(Integer.parseInt(car.getMileage())>=100000 && Integer.parseInt(car.getMileage())<=160000){
                            list.add((notification+"Belt and hose change recommended!"));
                        }
                        if(Integer.parseInt(car.getMileage())>=80000){
                            list.add((notification+"Cooling system flush recommended! You should have done this approximately "+Integer.parseInt(car.getMileage())/80000 +" times!"));
                        }

                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }

    public void ManageVehiclesOnClick(View view){
        startActivity(new Intent(HomeActivity.this,ManageVehicleActivity.class));
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel >9.5) {
                if(bingbong==false){
                    Toast.makeText(getApplicationContext(), "Shake event detected", Toast.LENGTH_SHORT).show();
                    System.out.println("bingbong");
                    bingbong=true;
                    Intent i = new Intent(HomeActivity.this,MapsActivity.class);
                    i.putExtra("ForClosestMechanic",true);
                    startActivity(i);

                }

            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }


}
