package com.androidtutorialshub.loginregister.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.androidtutorialshub.loginregister.R;
import com.androidtutorialshub.loginregister.adapters.UsersRecyclerAdapter;
import com.androidtutorialshub.loginregister.model.User;
import com.androidtutorialshub.loginregister.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity {

    private AppCompatActivity activity = UsersListActivity.this;
    private AppCompatTextView textViewName;
    private List<String> listUsers;
    private ListView listviewUsers;
    private ArrayAdapter<String> listAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().setTitle("All Workshops");

        initViews();
        initObjects();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.choose, menu);
        SpannableString s=new SpannableString(menu.getItem(0).getTitle().toString());
        s.setSpan(new ForegroundColorSpan(Color.GREEN),0,s.length(),0);
        menu.getItem(0).setTitle(s);
        SpannableString s1=new SpannableString(menu.getItem(1).getTitle().toString());
        s1.setSpan(new ForegroundColorSpan(Color.GRAY),0,s1.length(),0);
        menu.getItem(1).setTitle(s1);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.AppliedWorkshop) {
            String emailFromIntent = getIntent().getStringExtra("EMAIL");

            Intent intent = new Intent(this,AppliedActivity.class );
            intent.putExtra("EMAIL",emailFromIntent);
            startActivity(intent);

        }
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem item1=menu.findItem(R.id.allWorkshop);
        MenuItem item2=menu.findItem(R.id.AppliedWorkshop);
        if(getIntent().getStringExtra("EMAIL")==null){
            item1.setVisible(false);
            item2.setVisible(false);
        }
        else{
            item1.setVisible(true);
            item2.setVisible(true);
        }
        return true;
    }



    /**
     * This method is to initialize views
     */
    private void initViews() {
        listviewUsers = (ListView) findViewById(R.id.displayListView);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        listUsers = new ArrayList<>();
        listAdapter = new ArrayAdapter<String>(UsersListActivity.this,android.R.layout.simple_list_item_1,listUsers);

        listviewUsers.setAdapter(listAdapter);
        listviewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(UsersListActivity.this,Description.class);
                intent.putExtra("name",listUsers.get(i));
                intent.putExtra("email",getIntent().getStringExtra("EMAIL"));
                intent.putExtra("from", "2");
                startActivity(intent);
            }
        });
        databaseHelper = new DatabaseHelper(activity);
        databaseHelper.addWorkshop();
        getDataFromSQLite();

    }

    /**
     * This method is to fetch all user records from SQLite
     */
    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                listUsers.clear();
                String emailFromIntent = getIntent().getStringExtra("EMAIL");
               listUsers.addAll(databaseHelper.getAllWorkshops(emailFromIntent));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                listAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
