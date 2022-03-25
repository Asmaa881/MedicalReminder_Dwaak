package com.example.mediacalremider.requests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.mediacalremider.R;
import com.example.mediacalremider.users.UserModel;

import java.util.List;

public class RequestList extends AppCompatActivity implements RequestDelegation {
    RecyclerView requestRecyclerview;
    RequestRecyclerAdapter myAdapter;
    static List<UserModel>myRequestedUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        requestRecyclerview=findViewById(R.id.requestRecyclerView);
        LinearLayoutManager layoutManager= new LinearLayoutManager(RequestList.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        requestRecyclerview.setLayoutManager(layoutManager);
        RequestController.getRequests(this);
    }




    @Override
    public void getListOfRequests(List<UserModel>users) {
        myAdapter= new RequestRecyclerAdapter(RequestList.this,RequestList.this);
        myRequestedUsers=users;
        if(users!=null){

            Log.d("ergdror",users.toString());
        }
        myAdapter.setUsers(users);
        requestRecyclerview.setAdapter(myAdapter);

    }

    @Override
    public void cancelRequest(String id,int position) {
        RequestController.cancelRequest(id);
        myRequestedUsers.remove(position);

        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void acceptRequest(String id,int position) {
        RequestController.acceptRequest(id,RequestList.this,myRequestedUsers);
        myRequestedUsers.remove(position);
        myAdapter.notifyDataSetChanged();
    }
}