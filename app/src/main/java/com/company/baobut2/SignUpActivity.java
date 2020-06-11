package com.company.baobut2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG  ="SignUpActivity";
    EditText etId,etPassword;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();//데이터 베이스 초기화
        etId = (EditText)findViewById(R.id.etId);
        etPassword=(EditText)findViewById(R.id.etPassword);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        Button registerBtn = (Button)findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stEmail = etId.getText().toString();//입력된 이메일 받아오기
                String stPassword = etPassword.getText().toString();//입력된 문자열 받아오기

                if(stEmail.isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Please insert Email",Toast.LENGTH_LONG).show();
                    return;
                }
                if(stPassword.isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Please insert Password",Toast.LENGTH_LONG).show();
                    return;
                }


                //Toast.makeText(MainActivity.this,"Email : "+stEmail+"Password : "+stPassword,Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(stEmail, stPassword)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");//실제로 성공적 실행이 될 때 로그실행,위치 확인위해
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //updateUI(user);
                                    Intent in = new Intent(SignUpActivity.this,MainActivity.class);//화면전환
                                    startActivity(in);


                                    DatabaseReference myRef = database.getReference("users").child(user.getUid());

                                    Hashtable<String,String> numbers = new Hashtable<String,String>();
                                    numbers.put("email",user.getEmail());

                                    myRef.setValue(numbers);
                                    Toast.makeText(SignUpActivity.this,"Register Success",Toast.LENGTH_LONG).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });
    }
    //실행했을 때 자동으로 체크
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
}
