package com.nitin.loginregister.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nitin.loginregister.R;
import com.nitin.loginregister.sql.DatabaseHelper;


/**
 * Created by Sona on 24-03-2018.
 */


public class Description extends AppCompatActivity {


    TextView name;
    TextView desc;
    TextView date;
    Button applYButton;
    TextView appliedBoolean;
    String name1;
    String email;
    String from;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(from.equals("2")){
        Intent intent=new Intent(this,UsersListActivity.class);
        intent.putExtra("EMAIL", email);
        startActivity(intent);}
        else{
            Intent intent=new Intent(this,AppliedActivity.class);
            intent.putExtra("EMAIL", email);
            startActivity(intent);
        }
    }

    public void onButtonClick(View view){
        if(email==null){
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        else{
            new DatabaseHelper(Description.this).apply(name1,email);
            Intent intent=new Intent(this,Description.class);
            intent.putExtra("name",name1);
            intent.putExtra("email",email);
            intent.putExtra("from", getIntent().getStringExtra("from"));
            startActivity(intent);
        }


    }
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);
        getSupportActionBar().setTitle("Details of the workshop");
        name=(TextView)findViewById(R.id.headingTextView);
        desc=(TextView)findViewById(R.id.dTextView);
        date=(TextView) findViewById(R.id.descTextView);
        applYButton=(Button) findViewById(R.id.applyButton);
        appliedBoolean=(TextView) findViewById(R.id.Applied);
        Intent i=getIntent();
        name1=i.getStringExtra("name");
        String[] description=new DatabaseHelper(Description.this).getDesc(name1);
        name.setText(description[0].toString());
        desc.setText(description[1]);
        date.setText(description[2]);
        email=i.getStringExtra("email");
        if(email!=null){
            if(new DatabaseHelper(Description.this).getAppliedStatus(name1,email)){
                appliedBoolean.setVisibility(View.VISIBLE);
                applYButton.setVisibility(View.INVISIBLE);
                applYButton.setEnabled(false);
            }
            else {
                appliedBoolean.setVisibility(View.INVISIBLE);
                applYButton.setVisibility(View.VISIBLE);
                 applYButton.setEnabled(true);
                 }
        }
        else {
            appliedBoolean.setVisibility(View.INVISIBLE);
            applYButton.setVisibility(View.VISIBLE);
            applYButton.setEnabled(true);
        }
        from=i.getStringExtra("from");
    }
}
