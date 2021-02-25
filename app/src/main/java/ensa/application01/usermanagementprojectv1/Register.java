package ensa.application01.usermanagementprojectv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    Button registerLoginbtn, registerbtn, registerLogout;
    EditText registerFullname, registerEmail, registerPassword, registerPasswordRepeat;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth=FirebaseAuth.getInstance();
        //On récupère les éléments
        registerLoginbtn = (Button) findViewById(R.id.registerLoginbtn);
        registerbtn=(Button)findViewById(R.id.registerbtn);
        registerFullname=(EditText) findViewById(R.id.registerFullname);
        registerEmail=(EditText) findViewById(R.id.registerEmail);
        registerPassword=(EditText) findViewById(R.id.registerPassword);
        registerPasswordRepeat=(EditText) findViewById(R.id.registerRepeatPassword);
        //On applique un évènement  onclick aux boutons
        registerLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validation
                String fullname=registerFullname.getText().toString();
                String email=registerEmail.getText().toString();
                String password=registerPassword.getText().toString();
                String passwordRepeat=registerPasswordRepeat.getText().toString();
                int nbError=0;
                if(fullname.isEmpty()){
                    registerFullname.setError("Fullname must not be empty");
                    nbError++;
                }else if(fullname.length()<6){
                    registerFullname.setError("Fullname must be at least 6 characters");
                    nbError++;
                }

                if(email.isEmpty()){
                    registerEmail.setError("Email must not be empty");
                    nbError++;
                }

                if(password.isEmpty()){
                    registerPassword.setError("Password must not be empty");
                    nbError++;
                }else if(password.length()<6){
                    registerPassword.setError("Password must be at least 6 characters");
                    nbError++;
                }

                if(passwordRepeat.isEmpty()){
                    registerPasswordRepeat.setError("Password Repeat must not be empty");
                    nbError++;
                }else if(passwordRepeat.length()<6){
                    registerPasswordRepeat.setError("Password Repeat must be at least 6 characters");
                    nbError++;
                }
                if(!password.equals(passwordRepeat)){
                    registerPasswordRepeat.setError("Password Repeat must be equal to password");
                    nbError++;
                }
                if(nbError>0){
                    return;
                }
                // Tout est correct
                Toast.makeText(getApplicationContext(), "Fields validated", Toast.LENGTH_SHORT).show();
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(),Login.class));
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
    }

}