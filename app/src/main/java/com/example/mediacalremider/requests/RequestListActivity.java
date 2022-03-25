package com.example.mediacalremider.requests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.mediacalremider.R;
import com.example.mediacalremider.users.UserModel;

import java.util.List;

public class RequestListActivity extends AppCompatActivity implements RequestDelegation {
    RecyclerView requestRecyclerview;
   public static RequestRecyclerAdapter myAdapter;
    static List<UserModel>myRequestedUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        requestRecyclerview=findViewById(R.id.requestRecyclerView);
        LinearLayoutManager layoutManager= new LinearLayoutManager(RequestListActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        requestRecyclerview.setLayoutManager(layoutManager);
        RequestController.getRequests(this);
    }




    @Override
    public void getListOfRequests(List<UserModel>users) {
        myAdapter= new RequestRecyclerAdapter(RequestListActivity.this, RequestListActivity.this);
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
        RequestController.acceptRequest(id, RequestListActivity.this,myRequestedUsers);

    }
}