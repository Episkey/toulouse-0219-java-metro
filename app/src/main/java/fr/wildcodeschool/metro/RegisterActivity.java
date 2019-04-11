package fr.wildcodeschool.metro;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        Button btRegister = findViewById(R.id.btRegister);
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etPseudo = findViewById(R.id.etPseudo);
                EditText etEmail = findViewById(R.id.etEmail);
                EditText etPassword = findViewById(R.id.etpassword);
                EditText etPassword2 = findViewById(R.id.etpassword2);
                if (etPassword.getText().toString().equals(etPassword2.getText().toString())) {
                    createAccount(etEmail.getText().toString(), etPassword.getText().toString());
                } else {
                    Toast.makeText(RegisterActivity.this, "Passwords don't match, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent goToMapsActivity = new Intent(RegisterActivity.this, MapsActivity.class);
                            goToMapsActivity.putExtra("user",user);
                            startActivity(goToMapsActivity);

                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, getString(R.string.authenticationFailed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
