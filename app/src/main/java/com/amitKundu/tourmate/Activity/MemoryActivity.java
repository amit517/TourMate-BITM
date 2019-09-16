package com.amitKundu.tourmate.Activity;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.amitKundu.tourmate.Adapter.MomentAdapter;
import com.amitKundu.tourmate.BottomSheet.BottomSheet_AddMemory;
import com.amitKundu.tourmate.Classes.MemoryClass;
import com.amitKundu.tourmate.R;

import java.util.ArrayList;
import java.util.List;

public class MemoryActivity extends AppCompatActivity {

   private DatabaseReference database;
   private MomentAdapter momentAdapter;
    private List<MemoryClass> memorylist;
    public String eventId;
    String currentuser;
    ProgressBar progressBar;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
   // private EventIdClass eventIdClass;


    private BottomSheet_AddMemory bottomSheet_addMemory;
    private FloatingActionButton floatingActionButtonMemory;
    private RecyclerView memoryRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        eventId = getIntent().getStringExtra("curre");
        memorylist = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentuser = firebaseAuth.getCurrentUser().getUid();
        floatingActionButtonMemory = findViewById(R.id.fabMemory);
        memoryRecycler = findViewById(R.id.memoryRecyclerView);

        memoryRecycler.setLayoutManager(new LinearLayoutManager(null));
        Toast.makeText(this, ""+eventId, Toast.LENGTH_SHORT).show();


        floatingActionButtonMemory.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
//                  EventIdClass eventIdClass = new EventIdClass();
//                  eventIdClass.setEventId(eventId);

                  bottomSheet_addMemory = new BottomSheet_AddMemory();
                  bottomSheet_addMemory.setcID(eventId);
                  bottomSheet_addMemory.show(getSupportFragmentManager(), "bottomSheetImageDialog");
            }
        });


        database = FirebaseDatabase.getInstance().getReference().child("UserList").child(currentuser).child("Events").child(eventId);
        database.child("Memories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    memorylist.clear();
                    for (DataSnapshot data: dataSnapshot.getChildren()) {
                        MemoryClass memoryClass = data.getValue(MemoryClass.class);
                        memorylist.add(memoryClass);

                    }
                    Toast.makeText(MemoryActivity.this, ""+memorylist.size(), Toast.LENGTH_SHORT).show();
                    momentAdapter = new MomentAdapter(memorylist,MemoryActivity.this);
                    memoryRecycler.setAdapter(momentAdapter);
                    momentAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(MemoryActivity.this, "Empty database", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MemoryActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });





    }
}
