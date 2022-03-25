package com.example.mediacalremider.requests;

import static com.example.mediacalremider.MainActivity.appCurrentUser;
import static com.example.mediacalremider.requests.RequestListActivity.myAdapter;
import static com.example.mediacalremider.requests.RequestListActivity.myRequestedUsers;
import static com.example.mediacalremider.users.UserController.RequestDocRef;
import static com.example.mediacalremider.users.UserController.userDocRef;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mediacalremider.users.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestController {
    static FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
    static String uid = current_user.getUid();
    static List<String>myRequestedUserIds=new ArrayList<>();
    static List<UserModel> requestedUsers=new ArrayList<>();


    public static void getRequests(RequestDelegation delegation) {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        RequestDocRef.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        List<String> userIds = (ArrayList<String>) document.getData().get("all Requests");
                        for(String s:userIds){
                            userDocRef.document(s).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    requestedUsers.add(documentSnapshot.toObject(UserModel.class));
                                    for(int i=0; i<requestedUsers.size();i++){
                                        Log.i("index",requestedUsers.get(i).getName()+i);
                                    }
                                    delegation.getListOfRequests(requestedUsers);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("error", "get Requested User");
                                }
                            });
                            Log.d("TAG", s);
                        }
                        //
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    public static void acceptRequest(String id, Context context,List<UserModel>requstedUsers) {

        int position = indexForAccountWithNum(id,requstedUsers);
        Log.i("index",String.valueOf(position)+id);
        if (appCurrentUser.getDependency().size() < 2) {
            if (requstedUsers.get(position).getMyHealthTacker().size() < 2)
            {
                if (myRequestedUserIds.size() == 0) {
                    RequestDocRef.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ///////////////get list of my request to update it //////////////
                            myRequestedUserIds = (ArrayList<String>) documentSnapshot.getData().get("all Requests");
                            myRequestedUserIds.remove(id);
                            updatingMethod(id,requstedUsers);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("error", "get lists of ids");

                        }
                    });
                } else {
                    myRequestedUserIds.remove(id);
                    updatingMethod(id,requstedUsers);

                }
            }else{
                myRequestedUsers.remove(position);
                myAdapter.notifyDataSetChanged();
                Toast.makeText(context, "Sorry You can't be a Health Tacker for this User Any more because he reached the limited number of health tacker", Toast.LENGTH_LONG).show();
                updatingWithLimitedSize(id);
            }
        } else {
            myRequestedUsers.remove(position);
            myAdapter.notifyDataSetChanged();
            Toast.makeText(context, "You reached The Limited Size", Toast.LENGTH_LONG).show();
            updatingWithLimitedSize(id);


        }



    }


    public static void cancelRequest(String id){
        //////we did not assign values to the list yet//////////
        if(myRequestedUserIds.size()==0){
            RequestDocRef.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    myRequestedUserIds = (ArrayList<String>) documentSnapshot.getData().get("all Requests");
                    myRequestedUserIds.remove(id);
                    //////we removed the last request////////
                    if(myRequestedUserIds.size()==0){
                        RequestDocRef.document(uid).delete();
                        appCurrentUser.setRequestState("Empty");
                        userDocRef.document(uid).set(appCurrentUser, SetOptions.merge());
                    }else{
                        HashMap<String,Object> update=new HashMap<>();
                        update.put("all Requests",myRequestedUserIds);
                        RequestDocRef.document(uid).set(update);
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                Log.i("error","from request cancellation");
                }
            });
        }
        else{
            myRequestedUserIds.remove(id);
            if(myRequestedUserIds.size()==0){
                RequestDocRef.document(uid).delete();
                appCurrentUser.setRequestState("Empty");
                userDocRef.document(uid).set(appCurrentUser, SetOptions.merge());

            }else{
                HashMap<String,Object> update=new HashMap<>();
                update.put("all Requests",myRequestedUserIds);
                RequestDocRef.document(uid).set(update);
            }

        }

    }

    static int indexForAccountWithNum(String searchActNum, List<UserModel> users) {
        for (int i = 0; i < users.size(); i++)
            if (users.get(i).getId() == searchActNum)
                return i;
        return -1;
    }

    public static void updatingMethod(String id, List<UserModel> requstedUsers){
        int position = indexForAccountWithNum(id,requstedUsers);

        if (myRequestedUserIds.size() == 0) {
            RequestDocRef.document(uid).delete();
            ///////////////update health tacker and dependency here to////////////////
            appCurrentUser.setRequestState("Empty");
            appCurrentUser.getDependency().add(id);
            requstedUsers.get(position).getMyHealthTacker().add(uid);
            userDocRef.document(uid).set(appCurrentUser, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    userDocRef.document(id).set( requstedUsers.get(position), SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.i("depen","success 2");
                            myRequestedUsers.remove(position);
                            myAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        } else {
            HashMap<String, Object> update = new HashMap<>();
            update.put("all Requests", myRequestedUserIds);
            //////update list of my request/////
            RequestDocRef.document(uid).set(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    appCurrentUser.getDependency().add(id);
                    requstedUsers.get(position).getMyHealthTacker().add(uid);
                    ///////add user to my dependency///////
                    userDocRef.document(uid).set(appCurrentUser, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            ////////////add me to requested user 's health tacker///////
                            Log.i("depen","success");
                            userDocRef.document(id).set( requstedUsers.get(position), SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.i("depen","success 2");
                                    myRequestedUsers.remove(position);
                                    myAdapter.notifyDataSetChanged();

                                }
                            });
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

    }
    public static void updatingWithLimitedSize(String id){
        if (myRequestedUserIds.size() == 0) {
            RequestDocRef.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    myRequestedUserIds = (ArrayList<String>) documentSnapshot.getData().get("all Requests");
                    myRequestedUserIds.remove(id);
                    if (myRequestedUserIds.size() == 0) {
                        RequestDocRef.document(uid).delete();
                        appCurrentUser.setRequestState("Empty");
                        userDocRef.document(uid).set(appCurrentUser, SetOptions.merge());

                    }else{
                        HashMap<String, Object> update = new HashMap<>();
                        update.put("all Requests", myRequestedUserIds);
                        //////update list of my request/////
                        RequestDocRef.document(uid).set(update);
                    }

                }
            });
        }else{
            myRequestedUserIds.remove(id);
            if (myRequestedUserIds.size() == 0) {
                RequestDocRef.document(uid).delete();
                appCurrentUser.setRequestState("Empty");
                userDocRef.document(uid).set(appCurrentUser, SetOptions.merge());

            }else{
                HashMap<String, Object> update = new HashMap<>();
                update.put("all Requests", myRequestedUserIds);
                //////update list of my request/////
                RequestDocRef.document(uid).set(update);
            }

        }
    }

}
