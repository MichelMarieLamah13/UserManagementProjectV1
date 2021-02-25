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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPassword extends AppCompatActivity {
    EditText resetPassword, resetPasswordRepeat;
    Button reset;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        //On récupère les données
        reset=(Button)findViewById(R.id.reset);
        resetPassword=(EditText)findViewById(R.id.resetPassword);
        resetPasswordRepeat=(EditText)findViewById(R.id.resetPasswordRepeat);
        // Action listener sur le bouton
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validations
                String password=resetPassword.getText().toString();
                String passwordRepeat=resetPasswordRepeat.getText().toString();
                int nbError=0;
                if(password.isEmpty()){
                    resetPassword.setError("Password must not be empty");
                    nbError++;
                }else if(password.length()<6){
                    resetPassword.setError("Password must be at least 6 characters");
                    nbError++;
                }
                if(passwordRepeat.isEmpty()){
                    resetPasswordRepeat.setError("Password Repeat must not be empty");
                    nbError++;
                }else{
                    if(passwordRepeat.length()<6){
                        resetPasswordRepeat.setError("Password Repeat must be at least 6 characters");
                        nbError++;
                    }else if (!passwordRepeat.equals(password)){
                        resetPasswordRepeat.setError("Password Repeat must be equals to Password");
                        nbError++;
                    }
                }
                if(nbError>0){
                    return;
                }
                //Tout est correct
                Toast.makeText(getApplicationContext(),"Fields validated", Toast.LENGTH_LONG).show();
                firebaseUser.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Password updated successfully",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}