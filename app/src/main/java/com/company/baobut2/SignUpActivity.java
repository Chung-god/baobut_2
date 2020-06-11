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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG  ="SignUpActivity";
    EditText etId,etPassword;
    String region;
    Integer movie = 0;
    Integer computer = 0;
    Integer song = 0;
    Integer social = 0;
    Integer work = 0;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseDatabase database;
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] items = new String[]{"부산", "서울", "others"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                region = (String) parent.getItemAtPosition(position);
                System.out.println("Region : "+region);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

         CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkMovie) ;
         checkBox1.setOnClickListener(new CheckBox.OnClickListener() {
             @Override
             public void onClick(View v) {
                movie = 1;
             }
         });

         CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkComputer) ;
         checkBox2.setOnClickListener(new CheckBox.OnClickListener() {
             @Override
             public void onClick(View v) {
                 computer = 1;
             }
         });

         CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkSong) ;
         checkBox3.setOnClickListener(new CheckBox.OnClickListener() {
             @Override
             public void onClick(View v) {
                 song = 1;
             }
         });

         CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkSocial) ;
         checkBox4.setOnClickListener(new CheckBox.OnClickListener() {
             @Override
             public void onClick(View v) {
                 social = 1;
             }
         });
         CheckBox checkBox5 = (CheckBox) findViewById(R.id.checkWork) ;
         checkBox5.setOnClickListener(new CheckBox.OnClickListener() {
             @Override
             public void onClick(View v) {
                 work = 1;
             }
         });



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
                String stPassword =
                        etPassword.getText().toString();//입력된 문자열 받아오기

                if(stEmail.isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Please insert Email",Toast.LENGTH_LONG).show();
                    return;
                }
                if(stPassword.isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Please insert Password",Toast.LENGTH_LONG).show();
                    return;
                }



                //Toast.makText(MainActivity.this,"Email : "+stEmail+"Password : "+stPassword,Toast.LENGTH_LONG).show();
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

                                    Hashtable<String,Object> numbers = new Hashtable<>();
                                    numbers.put("email",user.getEmail());
                                    numbers.put("region",region);
                                    numbers.put("movie",movie);
                                    numbers.put("computer",computer);
                                    numbers.put("song",song);
                                    numbers.put("social",social);
                                    numbers.put("work",work);

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
