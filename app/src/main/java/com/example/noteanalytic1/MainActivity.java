package com.example.noteanalytic1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements CatAdapter.ItemClickListener {
    private FirebaseFirestore db;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ArrayList<Category> items;
    private CatAdapter adapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView rycCat;
    private CardView card;
    private Calendar calendar;
    private int hour;
    private int minute;
    private int second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        card = findViewById(R.id.card);
        screenTrack("Categories Screen");

        rycCat = findViewById(R.id.rycCat);
        items = new ArrayList<>();
        adapter = new CatAdapter(this, items, this);
        layoutManager = new LinearLayoutManager(this);
        rycCat.setLayoutManager(layoutManager);
        rycCat.setHasFixedSize(true);
        rycCat.setAdapter(adapter);

        getAllCat();
    }

    private void getAllCat() {
        db.collection("Categories")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            Log.d("tag", "onSuccess: LIST EMPTY");
                            return;
                        } else {
                            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                if (documentSnapshot.exists()) {
                                    String id = documentSnapshot.getId();
                                    String name = documentSnapshot.getString("Category");
                                    Category cat = new Category(id, name);
                                    items.add(cat);
                                    adapter.notifyDataSetChanged();
                                    Log.e("LogDATA", items.toString());
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("LogDATA", "get failed with ", e);
                    }
                });
    }

    public void cardEvent(String id, String name, String content) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, content);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public void screenTrack(String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Main Activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

    @Override
    protected void onPause() {
        super.onPause();

        calendar = Calendar.getInstance();
        int hour2 = calendar.get(Calendar.HOUR);
        int minute2 = calendar.get(Calendar.MINUTE);
        int second2 = calendar.get(Calendar.SECOND);

        int h = hour2 - hour;
        int m = minute2 - minute;
        int s = second2 - second;

        HashMap<String, Object> screens = new HashMap<>();
        screens.put("name", "Categories Screen");
        screens.put("hours", h);
        screens.put("minute", m);
        screens.put("seconds", s);

        db.collection("Categories Screen")
                .add(screens)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e("Hours", String.valueOf(h));
                        Log.e("Minutes", String.valueOf(m));
                        Log.e("Seconds", String.valueOf(s));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("LogDATA", "Error adding document", e);
                    }
                });
    }

    @Override
    public void onItemClick(int position, String id) {
        Category cat = items.get(position);
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putExtra("Category", cat);
        cardEvent(id, "Category Button", cat.getName());
        startActivity(intent);
    }
}
