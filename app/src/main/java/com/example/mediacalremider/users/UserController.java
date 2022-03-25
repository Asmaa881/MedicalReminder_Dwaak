package com.example.mediacalremider.users;

import static com.example.mediacalremider.MainActivity.appCurrentUser;
import static com.example.mediacalremider.MainActivity.current_user;
import static com.example.mediacalremider.MainActivity.userDependencies;
import static com.example.mediacalremider.users.AddDependency.dependencyProgressBar;
import static com.example.mediacalremider.users.LoginActivity.loadingBar;
import static com.example.mediacalremider.users.SignUp.prg_bar;
//import static com.example.mediacalremider.Users.UserProfile.currentUser;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mediacalremider.MainActivity;
import com.example.mediacalremider.dependency.getDrugsDelegation;
import com.example.mediacalremider.drugs.add_drugs.model.DrugsModel;
import com.example.mediacalremider.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserController {

    static FirebaseFirestore db= FirebaseFirestore.getInstance();
    public static CollectionReference userDocRef =db.collection("UsersInfo");
    public static CollectionReference RequestDocRef =db.collection("Request Information");
    static FirebaseAuth mAuth= FirebaseAuth.getInstance();
    static List<String> userIds=new ArrayList<>();
    static List<UserModel>users;

    public static void userRegistration(Context context,UserModel user){
        prg_bar.setTitle("Registering");
        prg_bar.setMessage("Please Wait Until It Finish");
        prg_bar.setCanceledOnTouchOutside(false);
        prg_bar.show();
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = current_user.getUid();
                user.setId(uid);
                userDocRef.document(uid).set(user);
                Intent inte = new Intent(context, MainActivity.class);
                inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(inte);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Try again later", Toast.LENGTH_LONG).show();
                prg_bar.dismiss();
                Log.i("Error","user registration");


            }
        });

    }

    public static void userLogin(String email,String password, Context context,userDelegation delegation){
        loadingBar.setTitle("Login");
        loadingBar.setMessage("Please Wait Until It Finish");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Toast.makeText(context, "user login successfully", Toast.LENGTH_SHORT).show();
                    delegation.successfullyLogin();
                    loadingBar.dismiss();
                } else {
                    Toast.makeText(context, "user login error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    //  loadingBar.dismiss();

                }
            }
        });
    }

    public static UserModel getUser(Delegation delegation){
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        userDocRef.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                appCurrentUser=documentSnapshot.toObject(UserModel.class);
                delegation.getUserSuccessListener();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Error","get user");
            }
        });
        return appCurrentUser;
    }

    public static  void addDependent(String email,Context context){
        dependencyProgressBar.setTitle("Sending Request");
        dependencyProgressBar.setMessage("Please Wait Until It Finish");
        dependencyProgressBar.setCanceledOnTouchOutside(false);
        dependencyProgressBar.show();
        String uid = current_user.getUid();
        ///////////////////get all Users////////////////////////
        userDocRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                users=queryDocumentSnapshots.toObjects(UserModel.class);
                ////////////////////get user id of the corresponding email//////////////
                boolean exist=false;
                for(int i=0;i<users.size();i++){
                    if(users.get(i).getEmail().equals(email)){
                        exist=true;
                        HashMap<String,List<String>> receiver=new HashMap<>();
                        int finalI1 = i;
                        int finalI2 = i;
                        if(!(appCurrentUser.getMyHealthTacker().contains(users.get(i).getId()))){
                            ///////////////// get request list of that received user/////////////////
                            RequestDocRef.document(users.get(i).getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                                            userIds = (ArrayList<String>) document.getData().get("all Requests");
                                            userIds.add(uid);
                                            receiver.put("all Requests",userIds);
                                            ///////////////// update request list of that received user/////////////////

                                            RequestDocRef.document(users.get(finalI2).getId()).set(receiver,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(context, "Request Sent Successfully", Toast.LENGTH_SHORT).show();
                                                    dependencyProgressBar.dismiss();
                                                    HashMap<String,Object> update=new HashMap<>();
                                                    update.put("requestState","Exist");
                                                    userDocRef.document(users.get(finalI1).getId()).set(update,SetOptions.merge());
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Error While sending", Toast.LENGTH_SHORT).show();
                                                    dependencyProgressBar.dismiss();
                                                }
                                            });

                                        } else {
                                            Log.d("TAG", "No such document");
                                            List<String> userIds = new ArrayList<>();
                                            userIds.add(uid);
                                            receiver.put("all Requests",userIds);
                                            RequestDocRef.document(users.get(finalI2).getId()).set(receiver,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(context, "Request Sent Successfully", Toast.LENGTH_SHORT).show();
                                                    dependencyProgressBar.dismiss();
                                                    HashMap<String,Object> update=new HashMap<>();
                                                    update.put("requestState","Exist");
                                                    userDocRef.document(users.get(finalI1).getId()).set(update,SetOptions.merge());
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Error While sending", Toast.LENGTH_SHORT).show();
                                                    dependencyProgressBar.dismiss();
                                                }
                                            });

                                        }
                                    } else {
                                        Log.d("TAG", "get failed with ", task.getException());
                                        dependencyProgressBar.dismiss();

                                    }
                                }
                            });
                        }else {
                            Toast.makeText(context, "He is already from your health keepers ", Toast.LENGTH_SHORT).show();
                            dependencyProgressBar.dismiss();
                        }
                        break;
                    }

                }
                if(exist==false){
                    Toast.makeText(context, "No User with this email ", Toast.LENGTH_SHORT).show();
                    dependencyProgressBar.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Error","Getting List of users");

            }
        });
    }

    public static void getDependent(UserModel user,Delegation delegation){
        if(user.getDependency().size()!=0){
            List<String>ids=user.getDependency();
            for(String s:ids){
                userDocRef.document(s).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            UserModel userModel=documentSnapshot.toObject(UserModel.class);
                            userDependencies.add(userModel);
                            delegation.getDependencySuccessListener(userDependencies);
                        }

                    }
                });
            }
        }
    }

    public static List<DrugsModel>getDrugs(String id, getDrugsDelegation delegation)
    {   List<DrugsModel>drugs = new ArrayList<>();
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        userDocRef.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserModel userModel= documentSnapshot.toObject(UserModel.class);

                List <String>drugsIds=userModel.getDrugsIds();
                for(String s:drugsIds){
                    FirebaseFirestore.getInstance().collection("Drugs").document(s).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){
                                DrugsModel drugsModel= documentSnapshot.toObject(DrugsModel.class);
                                drugs.add(drugsModel);
                                delegation.getDrugsSuccessfully(drugs);
                                Log.i("documentSnapshot",drugs.toString());
                            }
                        }
                    });
                }
            }
        });
        return  drugs;
    }

}
