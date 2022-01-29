package com.example.jultrautomaintenance;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jultrautomaintenance.databinding.ActivityManageVehicleBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageVehicleActivity extends AppCompatActivity {

    private ActivityManageVehicleBinding binding;
    private RecyclerView rv;
    private DatabaseReference db;
    CarRecyclerViewAdapter adapter;
    ArrayList<CarModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityManageVehicleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddVehicleActivity.class));
            }
        });
        rv= findViewById(R.id.carContainer);
        db = FirebaseDatabase.getInstance().getReference("Vehicles");
        rv.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<CarModel>();
        adapter = new CarRecyclerViewAdapter(getApplicationContext(),list,ManageVehicleActivity.this);
        rv.setAdapter(adapter);


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    System.out.println("GETS HERE TOO");
                    CarModel car = dataSnapshot.getValue(CarModel.class);
                    if(car.getOwner().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        System.out.println("GETS HERE");
                        System.out.println(car.getOwner());
                        System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        list.add(car);
                    }
                }
                if(list.size()==0){
                    Toast.makeText(getApplicationContext(), "You have no cars registered!", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


    }


}