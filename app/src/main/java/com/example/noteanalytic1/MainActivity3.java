package com.example.noteanalytic1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

public class MainActivity3 extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAnalytics mFirebaseAnalytics;
    TextView textView;
    ImageView imageView;
    TextView c;
    TextView textView4;
    String id;
    String cat;
    String cid;
    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        screenTrack("Details Notes Screen");
        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
        Note note = (Note) intent.getSerializableExtra("Note");
        id = note.getId();
        textView4 = findViewById(R.id.textView4);
        cat = note.getCategory();
        textView = findViewById(R.id.textView2);
        c = findViewById(R.id.textView3);
        imageView = findViewById(R.id.imageView);
        GetAllNotes();
    }

    private void GetAllNotes() {
        DocumentReference docRef = db.collection("Categories").document(cid).collection("Notes").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("Note");
                        String content = documentSnapshot.getString("Content");
                        String image = documentSnapshot.getString("Image");
                        String category = documentSnapshot.getString("Category");
                        Picasso.get().load(image).into(imageView);
                        c.setText(content);
                        textView.setText(name);
                        textView4.setText(category);
                        Log.e("log data", id + " " + name);
                    } else {
                        Log.d("tag", "onSuccess: good");
                        return;
                    }
                } else {
                    Log.e("log data", "failed ");
                }
            }
        });
    }

    public void screenTrack(String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "Main Activity3");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

    @Override
    protected void onPause() {

        Calendar calendar = Calendar.getInstance();
        int hour2 = calendar.get(Calendar.HOUR);
        int minute2 = calendar.get(Calendar.MINUTE);
        int second2 = calendar.get(Calendar.SECOND);

        int h = hour2 - hour;
        int m = minute2 - minute;
        int s = second2 - second;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> screens = new HashMap<>();
        screens.put("name", "Details Notes Screen");
        screens.put("hours", h);
        screens.put("minute", m);
        screens.put("seconds", s);
        db.collection("Details Notes Screen").add(screens)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        Log.e("Hours", String.valueOf(h));
        Log.e("Minutes", String.valueOf(m));
        Log.e("Seconds", String.valueOf(s));
        super.onPause();
    }
}
