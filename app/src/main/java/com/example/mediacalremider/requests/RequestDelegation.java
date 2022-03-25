package com.example.mediacalremider.requests;

import com.example.mediacalremider.users.UserModel;

import java.util.List;

public interface RequestDelegation {
    void getListOfRequests(List<UserModel> users);

    void cancelRequest(String id,int position);
    void acceptRequest(String id,int position);

}
