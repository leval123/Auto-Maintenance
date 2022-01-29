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

public class NotifRecyclerViewAdapter extends RecyclerView.Adapter<NotifRecyclerViewAdapter.MyViewHolder> {


    Context context;

    ArrayList<String> list;

    HomeActivity test;

    private String text = "";


    public NotifRecyclerViewAdapter(Context context, ArrayList<String> list, HomeActivity test) {
        this.context = context;
        this.list = list;
        this.test = test;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notif_item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String[] bingbong = list.get(position).split("-");
            holder.modelMakeNotifHolder.setText(bingbong[0]+" "+bingbong[1]+" "+bingbong[2]);
            holder.notificationTextHolder.setText(bingbong[3]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView notificationTextHolder, modelMakeNotifHolder;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            notificationTextHolder= itemView.findViewById(R.id.notificationTextHolder);
            modelMakeNotifHolder = itemView.findViewById(R.id.modelMakeNotifHolder);


        }

    }
}
