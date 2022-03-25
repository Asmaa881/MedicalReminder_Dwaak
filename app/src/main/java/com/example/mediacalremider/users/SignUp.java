package com.example.mediacalremider.users;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mediacalremider.R;
import com.example.mediacalremider.ValidationClass;

import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity {
    static ProgressDialog prg_bar;
    Button registerBtn;
    EditText email;
    EditText name;
    EditText password;
    EditText cPassword;
    UserModel userModel;
    ImageView fatherImg;
    ImageView motherImg;
    ImageView sonImg;
    ImageView grandFatherImg;
    ImageView grandMotherImg;
    ImageView daughterImg;
    ImageView profileImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userModel=new UserModel();
        fatherImg=findViewById(R.id.father);
        motherImg=findViewById(R.id.mother);
        sonImg=findViewById(R.id.son);
        daughterImg=findViewById(R.id.daughter);
        grandFatherImg=findViewById(R.id.grand_father);
        grandMotherImg=findViewById(R.id.grand_mother);
        profileImg=findViewById(R.id.profile_image);
        email =findViewById(R.id.emailTxt);
        name =findViewById(R.id.nameTxt);
        password =findViewById(R.id.dependencyEmailTxt);
        cPassword=findViewById(R.id.passwordConfirmTxt);
        prg_bar = new ProgressDialog(this);
        registerBtn=findViewById(R.id.registerBtn);

        grandFatherImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userModel.setIcon(R.drawable.elder);
                profileImg.setImageResource(R.drawable.elder);
            }
        });
        grandMotherImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userModel.setIcon(R.drawable.grandmother);
                profileImg.setImageResource(R.drawable.grandmother);
            }
        });
        fatherImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userModel.setIcon(R.drawable.father);
                profileImg.setImageResource(R.drawable.father);
            }
        });
        motherImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userModel.setIcon(R.drawable.mother);
                profileImg.setImageResource(R.drawable.mother);
            }
        });
        sonImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userModel.setIcon(R.drawable.son);
                profileImg.setImageResource(R.drawable.son);
            }
        });
        daughterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userModel.setIcon(R.drawable.daughter);
                profileImg.setImageResource(R.drawable.daughter);
            }
        });




        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals(cPassword.getText().toString())) {
                    if (!TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString()) || TextUtils.isEmpty(name.getText().toString()) ) {
                        if(ValidationClass.isValidEmail(email.getText().toString().trim())){

                            userModel.setEmail(email.getText().toString());
                            userModel.setPassword(password.getText().toString());
                            userModel.setName(name.getText().toString());
                            userModel.setRequestState("Empty");
                            if(userModel.getIcon()==0){
                                Toast.makeText(SignUp.this, "Please select your icon at first", Toast.LENGTH_LONG).show();

                            }else{
                                UserController.userRegistration(SignUp.this,userModel);

                            }
                        }else{
                            Toast.makeText(SignUp.this, "Invalid Email", Toast.LENGTH_LONG).show();

                        }
                    }else{
                        Toast.makeText(SignUp.this, "Complete all required Fields at first", Toast.LENGTH_LONG).show();

                    }
                    }else{
                    Toast.makeText(SignUp.this, "Confirm Your Password correctly", Toast.LENGTH_LONG).show();

                }

            }
        });
    }
}