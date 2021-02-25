package ensa.application01.usermanagementprojectv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    Button loginRegister, loginbtn, loginForgotPassword;
    EditText loginEmail, loginPassword;
    FirebaseAuth firebaseAuth;
    AlertDialog.Builder reset_password;
    LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();
        reset_password=new AlertDialog.Builder(this);
        inflater=this.getLayoutInflater();
        //On récupère les bouton Register
        loginRegister = (Button) findViewById(R.id.loginRegister);
        loginbtn=(Button)findViewById(R.id.loginbtn);
        loginForgotPassword=(Button)findViewById(R.id.loginForgotPassword);
        loginEmail = (EditText)findViewById(R.id.loginEmail);
        loginPassword = (EditText)findViewById(R.id.loginPassword);
        //On y ajoute un évènement onclick
        loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validation
                String email=loginEmail.getText().toString();
                String password=loginPassword.getText().toString();
                int nbError=0;
                if(email.isEmpty()){
                    loginEmail.setError("Email must not be empty ");
                    nbError++;
                }
                if(password.isEmpty()){
                    loginPassword.setError("Password must not be empty");
                    nbError++;
                }else if(password.length()<6){
                    loginPassword.setError("Password must be at least 6 characters");
                    nbError++;
                }
                if(nbError>0){
                    return;
                }
                //Tout est correct
                Toast.makeText(getApplicationContext(), "Fields validated", Toast.LENGTH_SHORT).show();
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        loginForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view=inflater.inflate(R.layout.reset_popup,null);
                reset_password.setTitle("Reset Password?")
                        .setMessage("Enter your email, to reset your password")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Validate email address
                                EditText popEmail= (EditText) view.findViewById(R.id.popupEmail);
                                String email=popEmail.getText().toString();
                                //Send email
                                if(email.isEmpty()){
                                    popEmail.setError("Email must not be empty");
                                    return;
                                }
                                //Tout est correct
                                firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Login.this,"Reset Password email sent",Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        }).setNegativeButton("Cancel",null)
                        .setView(view)
                        .create()
                        .show();
            }
        });
    }
    // Si l'utilisateur s'est connecté au paravant
    // On lui redirige directement
    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}