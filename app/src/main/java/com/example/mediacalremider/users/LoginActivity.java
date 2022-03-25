package com.example.mediacalremider.users;

import static com.example.mediacalremider.users.UserController.userDocRef;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediacalremider.R;
import com.example.mediacalremider.home.HomeActivity;
import com.google.android.gms.auth.api.credentials.IdToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

public class LoginActivity extends AppCompatActivity implements userDelegation{

    EditText email_et;
    EditText password_et;
    TextView forgetpaa_tv;
    Button login_btn;
    Button signup_btn;
    ImageView facebooklogin_lmg;
    ImageView googlelogin_img;


    //GoogleApiClient.Builder mGoogleSignInClient;
    public  static  final int RC_SIGN_IN= 1;
    private  static  final  String TAG = "LOGIN ACTIVITY";
    private GoogleApiClient mgoogleSignIn;
    private GoogleSignInClient mgoogleSignInClient;
    static ProgressDialog loadingBar;

    public  static  final String USEREMAIL= "email";
    public  static  final String PASSWORD= "password";

    String Password ="";
    String email ="";


    GoogleSignInClient mGoogleSignInClient;
    IdToken idToken;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signup_btn=findViewById(R.id.sign_up);
        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.passwrd_et);
        forgetpaa_tv = findViewById(R.id.forgetpas_tv);
        login_btn = findViewById(R.id.login_btn);
        googlelogin_img = findViewById(R.id.login_google);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                String email = email_et.getText().toString();
                String password = password_et.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    email_et.setError("Email can't empty");
                    email_et.requestFocus();
                    Toast.makeText(getBaseContext(), "Please enter right email", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter right password", Toast.LENGTH_SHORT).show();
                    password_et.setError("password can't empty");
                    password_et.requestFocus();
                } else {
                    UserController.userLogin(email,password,LoginActivity.this,LoginActivity.this);

                }
            }

        });
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);
            }
        });


        googlelogin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("473858188722-ec1ht06uv6bd6umhmut1khef3chvfgig.apps.googleusercontent.com")
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                signIn();
            }
        });
        if (PrefUtilities.with(this).isUserLogedin()) {

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra(USEREMAIL, email);
            startActivity(intent);
        }
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                //   loadingbar.dismiss();
                finish();
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String password = getCurrentUser().getDisplayName();
                            String email = getCurrentUser().getEmail();
                           // UserModel userModel = new UserModel(email, password);

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            //   addUserInDB(userModel);
                           // UserController.userLogin(email, password, LoginActivity.this, LoginActivity.this);

                            //   loadingbar.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            UserController.userLogin(email, password, LoginActivity.this, LoginActivity.this);

                        } else
                            //   loadingbar.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });

    }

    private FirebaseUser getCurrentUser() {

        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
        return mAuth.getCurrentUser();
    }

    private void updateUI(FirebaseUser user) {
        GoogleSignInAccount googleUser= GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        UserModel s= new UserModel();
        String email=googleUser.getEmail();
        String name=googleUser.getDisplayName();
        s.setEmail(email);
        s.setPassword("gmail");
        s.setName(name);
        s.setRequestState("Empty");
        s.setId(user.getUid());
        s.setIcon(R.drawable.father);
        userDocRef.document(user.getUid()).set(s, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });





    }

    private void passCheck(@NonNull DocumentSnapshot snapshot) {
        final String uPass = password_et.getText().toString();
        final String storedPass = snapshot.getString("password");
        if (storedPass != null && storedPass.equals(uPass)) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra(USEREMAIL, email);
            startActivity(intent);
        } else {
            Toast.makeText(LoginActivity.this, "Invalid Password!", Toast.LENGTH_LONG).show();
            PrefUtilities.with(this).setUserLogin(false);
        }
    }




    private String handleFireBaseException(Task task) {
        String errorMessage = "";

        try {
            throw task.getException();
        } catch (FirebaseAuthWeakPasswordException e) {
            errorMessage = "weak Password";
        } catch (FirebaseAuthUserCollisionException e) {
            errorMessage = "user exists already";
        } catch (FirebaseAuthInvalidUserException e) {
            errorMessage = "problem in e-mail or password";
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        return errorMessage;
    }

    static void onSuccessReturn(String userName) {

    }







    @Override
    public void successfullyLogin() {
        Intent intent= new Intent(LoginActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}