package com.example.jultrautomaintenance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddVehicleActivity extends AppCompatActivity {
    public List<String> UniqueMakes, UniqueModels;
    JSONObject vehicleData;
    JSONArray m_jArray;
    ArrayList <String> models;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        String json = null;
        try {
            InputStream is = getApplication().getAssets().open("vehicles.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            vehicleData = new JSONObject(json);
            m_jArray = vehicleData.getJSONArray("vehicles");
            ArrayList <String> makes = new ArrayList<String>();

            for (int i=0;i<m_jArray.length();i++){
                JSONObject car = m_jArray.getJSONObject(i);
                String make = car.getString("make");
                makes.add(make);
            }
            UniqueMakes = makes.stream().distinct().collect(Collectors.toList());
            System.out.println(UniqueMakes);
            UniqueMakes.sort(String::compareToIgnoreCase);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Spinner makeOption =  (Spinner) findViewById(R.id.add_car_make);
        SpinAdapter adapter = new SpinAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, UniqueMakes);
        makeOption.setAdapter(adapter);
        makeOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                models = new ArrayList<String>();
                models.clear();
                String makeSelected = adapter.getItem(position);
                for (int i=0;i<m_jArray.length();i++){
                    try {
                        JSONObject car = m_jArray.getJSONObject(i);
                        String make = car.getString("make");
                        if(make.equalsIgnoreCase(makeSelected)){
                            String model = car.getString("model");
                            models.add(model);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                UniqueModels = models.stream().distinct().collect(Collectors.toList());
                UniqueModels.sort(String::compareToIgnoreCase);
                System.out.println(UniqueModels);
                Spinner modelOption =  (Spinner) findViewById(R.id.add_car_model);
                SpinAdapter adapter = new SpinAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, UniqueModels);
                modelOption.setAdapter(adapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });


    }
    public void addCarToDB(View view ){
        TextView yearInput = findViewById(R.id.add_car_year);
        Spinner modelOption =  (Spinner) findViewById(R.id.add_car_model);
        Spinner makeOption =  (Spinner) findViewById(R.id.add_car_make);
        TextView mileageInput = findViewById(R.id.add_car_mileage);

        String modelChosen = modelOption.getSelectedItem().toString();
        String makeChosen = makeOption.getSelectedItem().toString();
        String yearChosen = yearInput.getText().toString();
        String mileageChosen = mileageInput.getText().toString();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String owner = firebaseUser.getUid();
        CarModel vehicleToPush = new CarModel(modelChosen,makeChosen,mileageChosen,yearChosen,owner);

        FirebaseDatabase.getInstance().getReference().child("Vehicles").push().setValue(vehicleToPush);
        Toast.makeText(this, "Car Added!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(),ManageVehicleActivity.class));
    }
}