package com.example.jultrautomaintenance;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CarRecyclerViewAdapter extends RecyclerView.Adapter<CarRecyclerViewAdapter.MyViewHolder> {


    Context context;

    ArrayList<CarModel> list;

    ManageVehicleActivity test;

    private String text = "";


    public CarRecyclerViewAdapter(Context context, ArrayList<CarModel> list, ManageVehicleActivity test) {
        this.context = context;
        this.list = list;
        this.test = test;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.vehicle_item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CarModel car = list.get(position);
        holder.carHolder.setText(car.getYear()+" "+car.getMake() + " "+ car.getModel());
        holder.mileageHolder.setText("Mileage: "+car.getMileage());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarModel carClicked = list.get(holder.getAdapterPosition());
                AlertDialog.Builder builder1 = new AlertDialog.Builder(test);
                builder1.setMessage("Are you sure you want to delete this vehicle from the list?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseDatabase.getInstance().getReference("Vehicles").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                            CarModel carFromDB = dataSnapshot.getValue(CarModel.class);

                                            if(carClicked.getOwner() == carFromDB.getOwner() && carClicked.getMileage()==carFromDB.getMileage()
                                                    && carClicked.getModel()==carFromDB.getModel()
                                                    && carClicked.getMake()==carFromDB.getMake()
                                                    && carClicked.getYear()==carFromDB.getYear()){
                                                dataSnapshot.getRef().removeValue(new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                        Toast.makeText(context.getApplicationContext(), "gaari deleted", Toast.LENGTH_SHORT).show();
                                                        notifyDataSetChanged();
                                                    }
                                                });

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }//
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(test);
                builder1.setMessage("Update your mileage here.");
                builder1.setCancelable(true);

                CarModel carClicked =list.get(holder.getAdapterPosition());
                final EditText input = new EditText(context.getApplicationContext());
                input.setText(carClicked.getMileage());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder1.setView(input);

                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        text = input.getText().toString();
                        System.out.println(text);
                        FirebaseDatabase.getInstance().getReference("Vehicles").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                    CarModel carFromDB = dataSnapshot.getValue(CarModel.class);

                                    if(carClicked.getOwner() == carFromDB.getOwner() && carClicked.getMileage()==carFromDB.getMileage()
                                            && carClicked.getModel()==carFromDB.getModel()
                                            && carClicked.getMake()==carFromDB.getMake()
                                            && carClicked.getYear()==carFromDB.getYear()){
                                        dataSnapshot.getRef().child("mileage").setValue(text,new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                Toast.makeText(context.getApplicationContext(), "mileage updated", Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();
                                            }
                                        });

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView carHolder, mileageHolder;
        ImageButton editButton, deleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            carHolder =itemView.findViewById(R.id.modelMakeHolder);
            mileageHolder = itemView.findViewById(R.id.mileageHolder);
            editButton = itemView.findViewById(R.id.imageButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

}
    }
}
