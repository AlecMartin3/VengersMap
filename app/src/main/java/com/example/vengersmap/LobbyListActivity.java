package com.example.vengersmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LobbyListActivity extends AppCompatActivity {

    DatabaseReference databaseHunt;
    ListView lvHunt;
    List<HuntItem> HuntList;
    private ValueEventListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_list);
        databaseHunt = FirebaseDatabase.getInstance().getReference("hunts");

        lvHunt= findViewById(R.id.lvHunt);
        HuntList = new ArrayList<HuntItem>();


        lvHunt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HuntItem hunts = HuntList.get(position);

                showPasswordDialog(
                        hunts.getId(),
                        hunts.getHuntPassword());
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        listener = databaseHunt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HuntList.clear();
                for (DataSnapshot huntSnapshot : dataSnapshot.getChildren()) {
                    HuntItem hunt = huntSnapshot.getValue(HuntItem.class);
                    hunt.setId(huntSnapshot.getKey());
                    hunt.setHuntName(huntSnapshot.child("Name").getValue().toString());
                    hunt.setHuntPark(huntSnapshot.child("Park").getValue().toString());
                    hunt.setHuntPassword(huntSnapshot.child("Password").getValue().toString());
                    HuntList.add(hunt);
            }

                HuntItemAdapter adapter = new HuntItemAdapter(LobbyListActivity.this, HuntList);
                lvHunt.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
    public  void onDestroy() {
        super.onDestroy();
        databaseHunt.removeEventListener(listener);
    }
    private void showPasswordDialog(final String id,  final String password){
        if(!(password.equals(""))) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.password_dialog, null);
            dialogBuilder.setView(dialogView);
            final EditText editPassword = dialogView.findViewById(R.id.editPassword);
            final Button btnPassword = dialogView.findViewById(R.id.btnPassword);
            dialogBuilder.setTitle("Enter a password: ");
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            btnPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String etPassword = editPassword.getText().toString().trim();
                    if (etPassword.equals(password)) {

                        Intent intent = new Intent(LobbyListActivity.this, ArtifactListActivity.class);
                        System.out.println(id);
                        intent.putExtra("StringID", id);
                        intent.putExtra("preload", true);
                        startActivity(intent);

                    }


                }
            });
        }else{
            Intent intent = new Intent(LobbyListActivity.this, ArtifactListActivity.class);
            intent.putExtra("StringID", id);
            intent.putExtra("preload", true);
            startActivity(intent);
        }
    }
}
