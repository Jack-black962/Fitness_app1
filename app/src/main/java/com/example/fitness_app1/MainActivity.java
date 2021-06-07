package com.example.fitness_app1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.fitness_app1.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnRegister;
    FirebaseAuth auth;          //авторизация
    FirebaseDatabase db;        //подключение к бд
    DatabaseReference users;    //работа с таблицей внутри бд
    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //toLogIn();

        btnSignIn = findViewById(R.id.logInButton);
        btnRegister = findViewById(R.id.registerButton);

        root = findViewById(R.id.root_element);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        //Обработчик события регистрации
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //регистрация
                showRegisterWindow();
            }
        });

        //вход
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //вход
                //showSignInWindow();
                startActivity(new Intent(MainActivity.this, UserActivity.class));
            }
        });

        // Write a message to the database (for check)
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");*/
    }

    //функция для входа в аккаунт
    private void showSignInWindow(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Войти");
        dialog.setMessage("Введите учётные данные");

        //Вызвать шаблон для входа
        LayoutInflater inflater = LayoutInflater.from(this);        //шаблон
        View signInWindow = inflater.inflate(R.layout.activity_sign_in, null);       //поместить переменную (окно входа)
        dialog.setView(signInWindow);     //вызвать

        //final - значит пер-я константа
        final MaterialEditText email = signInWindow.findViewById(R.id.emailField);
        final MaterialEditText pass = signInWindow.findViewById(R.id.passField);

        //отмена
        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        //отправить
        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //если не введёт пароль и нажмёт добавить
                if (pass.getText().toString().length() < 5) {
                    Snackbar.make(root, "Введите пароль более 5 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //вход в аккаунт
                auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnSuccessListener(
                                new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        startActivity(new Intent(MainActivity.this, UserActivity.class));
                                    }
                                }).addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                                Snackbar.make(root, "Ошибка авторизации. " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                );

            }
        });

        dialog.show();
    }

    //функция регистрации
    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Регистрация");
        dialog.setMessage("Введите данные для регистрации");

        //Вызвать шаблон регистрации
        LayoutInflater inflater = LayoutInflater.from(this);        //шаблон
        View registerWindow = inflater.inflate(R.layout.activity_register, null);       //поместить переменную (окно регистрации)
        dialog.setView(registerWindow);     //вызвать

        //final - значит пер-я константа
        final MaterialEditText email = registerWindow.findViewById(R.id.emailField);
        final MaterialEditText pass = registerWindow.findViewById(R.id.passField);
        final MaterialEditText name = registerWindow.findViewById(R.id.nameField);
        final MaterialEditText phone = registerWindow.findViewById(R.id.phoneField);

        //отмена
        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        });

        //отправить (регистрация в бд)
        dialog.setPositiveButton("Зарегистрироваться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                //если не введёт email и нажмёт добавить
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //если не введёт ФИО и нажмёт добавить
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(root, "Введите ваше ФИО", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //если не введёт телефон и нажмёт добавить
                if (TextUtils.isEmpty(phone.getText().toString())) {
                    Snackbar.make(root, "Введите ваш телефон", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //если не введёт пароль и нажмёт добавить
                if (pass.getText().toString().length() < 5) {
                    Snackbar.make(root, "Введите пароль более 5 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                //регистрация пользователя
                auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();

                                user.setEmail(email.getText().toString());
                                user.setName(name.getText().toString());
                                user.setPass(pass.getText().toString());
                                user.setPhone(phone.getText().toString());

                                //передаём данные в бд. Ключ каждого пользователя - его email
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(
                                                new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Snackbar.make(root, "Поьзователь добавлен!", Snackbar.LENGTH_SHORT).show();
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
                );

            }
        });

        dialog.show();
    }

    /*public void toLogIn () {
        btn1 = (Button)findViewById(R.id.logInButton);
        btn2 = (Button)findViewById(R.id.registerButton);

        btn1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(".LogInActivity");
                        startActivity(intent);
                    }
                }
        );

        btn2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(".RegActivity");
                        startActivity(intent);
                    }
                }
        );
    }*/
}