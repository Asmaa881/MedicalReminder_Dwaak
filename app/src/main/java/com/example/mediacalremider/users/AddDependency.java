package com.example.mediacalremider.users;

import static com.example.mediacalremider.MainActivity.appCurrentUser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mediacalremider.R;
import com.example.mediacalremider.ValidationClass;

public class AddDependency extends AppCompatActivity {
EditText dependentEmail;
Button saveBtn;
static ProgressDialog dependencyProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dependency);
        dependencyProgressBar=new ProgressDialog(this);
        dependentEmail= findViewById(R.id.dependencyEmailTxt);
        saveBtn=findViewById(R.id.addDependentBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dependentEmail.getText().toString().isEmpty()){
                    if(ValidationClass.isValidEmail(dependentEmail.getText().toString().trim())){
                        if(!(dependentEmail.getText().toString().equals(appCurrentUser.getEmail()))){
                            UserController.addDependent(dependentEmail.getText().toString(),AddDependency.this);
                        }
                        else{
                            Toast.makeText(AddDependency.this,"You cant send request to your self",Toast.LENGTH_LONG).show();

                        }
                    }else{
                        Toast.makeText(AddDependency.this,"Invalid email",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(AddDependency.this,"Please enter user 's email at first",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}