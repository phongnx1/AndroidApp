package com.phongnx.firebaserealtimedatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    Button buttonAddArtist;
    Spinner spinnerGenres;

    DatabaseReference databaseArtists;

    ListView listViewArtists;

    List<Artist> artistList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");

        editTextName = (EditText) findViewById(R.id.editTextName);
        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenres);
        buttonAddArtist = (Button) findViewById(R.id.buttonAddArtist);

        listViewArtists = (ListView) findViewById(R.id.listViewArtists);

        artistList = new ArrayList<>();
        buttonAddArtist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addArtist();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                artistList.clear();

                for(DataSnapshot artistSnapshot : dataSnapshot.getChildren()){
                    Artist artist = artistSnapshot.getValue(Artist.class);
                    artistList.add(artist);
                }

                ArtistList adapter = new ArtistList(MainActivity.this, artistList);

                // attaching adapter to the listview
                listViewArtists.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addArtist(){

        // get values to save
        String name = editTextName.getText().toString().trim();
        String genre = spinnerGenres.getSelectedItem().toString();

        // check if value is empty
        if(!TextUtils.isEmpty(name)){

            // get a unique id using for push().getKey() method
            // It will create a unique id as Artist ID
            String id = databaseArtists.push().getKey();

            // Create Artist Object
            Artist artist = new Artist(id, name, genre);

            // Save the Artist
            databaseArtists.child(id).setValue(artist);

            // display success toast
            Toast.makeText(this, "Artist added", Toast.LENGTH_LONG).show();
        } else {
            // display case value is not inputed
            Toast.makeText(this, "you should enter a name", Toast.LENGTH_LONG).show();
        }
    }
}
