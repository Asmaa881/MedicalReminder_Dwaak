package com.example.mediacalremider.users;


import java.util.List;

public interface Delegation {
    void getUserSuccessListener();
    void getDependencySuccessListener(List<UserModel>dependencies);

}
