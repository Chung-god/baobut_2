package com.company.baobut2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    EditText etText;
    Button btnSend;
    String stEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        stEmail = getIntent().getStringExtra("email");
        Button btnFinish = (Button)findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { finish(); }
        });
        btnSend=(Button)findViewById(R.id.btnSend);
        etText=(EditText)findViewById(R.id.etText);


            recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            recyclerView.setHasFixedSize(true);

            // use a linear layout manager
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);


            // specify an adapter (see also next example)
            String[] myDataset = {"test1","test2","test3","test4"};
            mAdapter = new MyAdapter(myDataset);
            recyclerView.setAdapter(mAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stText = etText.getText().toString();
                Toast.makeText(ChatActivity.this,"Msg: "+stText,Toast.LENGTH_LONG).show();
                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                //Child 생성
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String datetime = dateformat.format(c.getTime());
                System.out.println(datetime);

                DatabaseReference myRef = database.getReference("message").child(datetime);

                Hashtable<String,String> numbers = new Hashtable<String,String>();
                numbers.put("email",stEmail);
                numbers.put("text",stText);

                myRef.setValue(numbers);
                }
            });
        }
    }

