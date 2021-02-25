package ensa.application01.usermanagementprojectv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    Button mainLogout, mainVerify;
    TextView mainEmailMessage;
    FirebaseAuth firebaseAuth;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        reset_alert=new AlertDialog.Builder(this);
        inflater=this.getLayoutInflater();
        //On recupère les éléménts
        mainEmailMessage = (TextView) findViewById(R.id.mainEmailMessage);
        mainVerify = (Button) findViewById(R.id.mainVerify);
        //Si l'email n'est pas vérifié
        if (!firebaseAuth.getCurrentUser().isEmailVerified()) {
            mainEmailMessage.setVisibility(View.VISIBLE);
            mainVerify.setVisibility(View.VISIBLE);
        }
        mainLogout = (Button) findViewById(R.id.mainLogout);
        //On ajoute aux buttons des évènements
        mainLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                ;
            }
        });
        mainVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Verification email send",Toast.LENGTH_LONG).show();
                        mainEmailMessage.setVisibility(View.GONE);
                        mainVerify.setVisibility(View.GONE);
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
    //On crée un menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.mainResetPassword){
            startActivity(new Intent(this, ResetPassword.class));
        }
        if(item.getItemId()==R.id.mainUpdateEmail){
            View view=inflater.inflate(R.layout.reset_popup,null);
            reset_alert.setTitle("Update Email?")
                    .setMessage("Enter your new email address")
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
                            FirebaseUser usr=firebaseAuth.getCurrentUser();
                            usr.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this,"Email updated successfully", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).setNegativeButton("Cancel",null)
                    .setView(view)
                    .create()
                    .show();
        }
        if(item.getItemId()==R.id.mainDeleteAccount){
            reset_alert.setTitle("Delete Account?")
                    .setMessage("Do you really want to delete your account")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //Tout est correct
                            FirebaseUser usr=firebaseAuth.getCurrentUser();
                            usr.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this,"Account deleted successfully", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(),Login.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).setNegativeButton("Cancel",null)
                    .create()
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }
}