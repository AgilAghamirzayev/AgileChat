package com.aqil.agilechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ChatRooms extends AppCompatActivity {

    private Button add_room_button;
    private EditText room_name;
    private ListView listView_of_chattRoomNames;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        add_room_button = (Button) findViewById(R.id.add_room_button);
        room_name = (EditText) findViewById(R.id.add_room);
        listView_of_chattRoomNames = (ListView) findViewById(R.id.add_rooms_list_view);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_rooms);
        listView_of_chattRoomNames.setAdapter(arrayAdapter);

        Request_UserName();

        add_room_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mReference = database.getReference(room_name.getText().toString());
                mReference.setValue("");

                room_name.setText("");
                room_name.requestFocus();

            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mReference = database.getReference(room_name.getText().toString());
        mReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new  HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }

                list_of_rooms.clear();
                list_of_rooms.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView_of_chattRoomNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent messages = new Intent(ChatRooms.this, MessageActivity.class);
                messages.putExtra("user_name", name);
                messages.putExtra("room_name", ((TextView)view).getText().toString());
                startActivity(messages);
            }
        });

    }



    private void Request_UserName(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatRooms.this);
        builder.setTitle("Enter UserName");
        final EditText inputField = new EditText(ChatRooms.this);
        builder.setView(inputField);


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = inputField.getText().toString();


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Request_UserName();

            }
        });

        builder.show();

    }
}
