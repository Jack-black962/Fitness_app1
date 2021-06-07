package com.example.fitness_app1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitness_app1.Models.Club;
import com.example.fitness_app1.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    FirebaseAuth auth;          //авторизация
    FirebaseDatabase dbase;        //подключение к бд
    DatabaseReference clubs;    //работа с таблицей внутри бд
    RelativeLayout root;
    String message;
    Button clubs_btn, btnpr, btn_reg_club;
    ArrayList <Club> clubArrayList;
    List<Club> clubList;
    Club club;
    int i = 0, n = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        clubs_btn = findViewById(R.id.clubs_btn);
        btnpr = findViewById(R.id.profile_btn);
        btn_reg_club = findViewById(R.id.reg_club);

        root = findViewById(R.id.root_element2);

        auth = FirebaseAuth.getInstance();
        dbase = FirebaseDatabase.getInstance();
        clubs = dbase.getReference("Clubs");

        //Обработчик события регистрации
        btn_reg_club.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //регистрация
                showRegisterWindow();
            }
        });


        String listOfClubs[] = null;



        //чтение клубов из бд
        clubs_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(UserActivity.this, ClubsActivity.class));
                        /*ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String club_name = ds.child("name").getValue(String.class);
                                    String club_email = ds.child("email").getValue(String.class);
                                    String club_adress = ds.child("adress").getValue(String.class);
                                    String club_phone = ds.child("phone").getValue(String.class);
                                    String club_schedule = ds.child("schedule").getValue(String.class);

                                    listOfClubs[i] = club_name + '\n' + club_email + '\n' + club_adress + '\n' + club_phone + '\n' + club_schedule;
                                    //i++;


                                    /*String id = ds.child("id").getValue(String.class);
                                    String note = ds.child("note").getValue(String.class);
                                    String title = ds.child("title").getValue(String.class);
                                    String user = ds.child("user").getValue(String.class);
                                    Log.d("TAG", id + " / " + note + " / " + title + " / " + users);
                                }
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        };
                        clubs.addListenerForSingleValueEvent(eventListener);

                        /*n = i;
                        for (i = 0; i < n; i++) {
                            Toast.makeText(UserActivity.this, listOfClubs[i], Toast.LENGTH_SHORT).show();
                        }*/

                        /*clubs.child("Clubs").addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();

                                while (dataSnapshots.hasNext()) {
                                    club = dataSnapshot.getValue(Club.class);
                                    clubArrayList.add(club);
                                }
                            }



                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        for (Club item:clubArrayList
                             ) {
                            Toast.makeText(UserActivity.this, item.getName().toString(), Toast.LENGTH_SHORT).show();
                            /*Toast.makeText(UserActivity.this, item.getAdress().toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(UserActivity.this, item.getEmail().toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(UserActivity.this, item.getPhone().toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(UserActivity.this, item.getSchedule().toString(), Toast.LENGTH_SHORT).show();*/
                        //}

                        //String showclubs = clubList.toString();
                        //Toast.makeText(UserActivity.this, "showclubs", Toast.LENGTH_LONG).show();


                        //clubs.child("Clubs").child()

                        /*auth = FirebaseAuth.getInstance();
                        db = FirebaseDatabase.getInstance();
                        clubs = db.getReference("Clubs");
                        clubs.setValue("Hello, World!");*/
                    }
                }
        );

        /*btnpr.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clubs.addValueEventListener(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        //Retrieve latest value
                                        message = snapshot.getValue().toString();
                                        Toast.makeText(UserActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                        //Error handling
                                    }
                                }
                        );
                    }
                }
        );*/
    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Регистрация клуба");
        dialog.setMessage("Введите данные для регистрации");

        //Вызвать шаблон регистрации
        LayoutInflater inflater = LayoutInflater.from(this);        //шаблон
        View registerWindow = inflater.inflate(R.layout.activity_reg_club, null);       //поместить переменную (окно регистрации)
        dialog.setView(registerWindow);     //вызвать

        //final - значит пер-я константа
        final MaterialEditText name = registerWindow.findViewById(R.id.nameField);
        final MaterialEditText email = registerWindow.findViewById(R.id.emailclubField);
        final MaterialEditText adress = registerWindow.findViewById(R.id.adressField);
        final MaterialEditText phone = registerWindow.findViewById(R.id.phoneField);
        final MaterialEditText schedule = registerWindow.findViewById(R.id.scheduleField);
        final MaterialEditText id = registerWindow.findViewById(R.id.idclubField);

        //отмена
        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        //отправить (регистрация в бд)
        dialog.setPositiveButton("Зарегистрировать", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                //если не введёт email и нажмёт добавить
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(root, "Введите название", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //если не введёт email и нажмёт добавить
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите название", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(adress.getText().toString())) {
                    Snackbar.make(root, "Введите адрес", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //если не введёт телефон и нажмёт добавить
                if (TextUtils.isEmpty(phone.getText().toString())) {
                    Snackbar.make(root, "Введите телефон", Snackbar.LENGTH_SHORT).show();
                    return;
                }




                //передаём данные в бд. Ключ каждого пользователя - его email
                auth.createUserWithEmailAndPassword(email.getText().toString(), phone.getText().toString())
                        .addOnSuccessListener(
                                new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        //регистрация
                                        Club club = new Club();

                                        club.setName(name.getText().toString());
                                        club.setEmail(email.getText().toString());
                                        club.setAdress(adress.getText().toString());
                                        club.setPhone(phone.getText().toString());
                                        club.setSchedule(schedule.getText().toString());

                                        clubs.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(club)
                                                .addOnSuccessListener(
                                                        new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Snackbar.make(root, "Клуб добавлен!", Snackbar.LENGTH_SHORT).show();
                                                            }
                                                        });
                                    }
                                }
                        ).addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Snackbar.make(root, "Ошибка регистрации. " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                );




                /*auth.createUserWithEmailAndPassword(name.getText().toString(), adress.getText().toString()).addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Club club = new Club();

                                club.setName(name.getText().toString());
                                club.setAdress(adress.getText().toString());
                                club.setPhone(phone.getText().toString());

                                //передаём данные в бд. Ключ каждого пользователя - его email
                                clubs.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(club)
                                        .addOnSuccessListener(
                                                new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Snackbar.make(root, "Клуб добавлен!", Snackbar.LENGTH_SHORT).show();
                                                    }
                                                });

                            }
                        }).addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Snackbar.make(root, "Ошибка регистрации. " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                );*/

            }
        });

        dialog.show();
    }

}