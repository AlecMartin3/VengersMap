package com.example.vengersmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ArtifactListActivity extends AppCompatActivity {

    DatabaseReference databaseArtifact;
    ListView lvArtifact;
    ArrayList<Artifact> ArtifactList;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_list);
        id = getIntent().getExtras().getString("StringID");
        databaseArtifact = FirebaseDatabase.getInstance().getReference("hunts").child(id);
        lvArtifact= findViewById(R.id.lvArtifacts);
        ArtifactList = new ArrayList<Artifact>();
    }
    @Override
    protected void onStart() {
        super.onStart();

        databaseArtifact.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArtifactList.clear();
                    for (DataSnapshot CountSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot NameSnapshot : CountSnapshot.getChildren()) {
                            Artifact Artifact = NameSnapshot.getValue(Artifact.class);
                            Artifact.setArtName(NameSnapshot.child("artName").getValue().toString());
                            ArtifactList.add(Artifact);
                        }
                    }

                ArtifactAdapter adapter = new ArtifactAdapter(ArtifactListActivity.this, ArtifactList);
                lvArtifact.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
