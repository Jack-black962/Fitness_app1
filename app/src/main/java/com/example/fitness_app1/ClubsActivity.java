package com.example.fitness_app1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClubsActivity extends AppCompatActivity {

    FirebaseAuth auth;          //авторизация
    FirebaseDatabase dbase;        //подключение к бд
    DatabaseReference clubs;    //работа с таблицей внутри бд

    String nameC1, emailC1, adressC1, phoneC1, scheduleC1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs);

        auth = FirebaseAuth.getInstance();
        dbase = FirebaseDatabase.getInstance();
        clubs = dbase.getReference("Clubs");


    }
}