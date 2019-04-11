package fr.wildcodeschool.metro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        Button btLogin = findViewById(R.id.btLogin);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etLogin = findViewById(R.id.etLogin);
                EditText etPassword = findViewById(R.id.etPassword);
                signIn(etLogin.getText().toString(), etPassword.getText().toString());
            }
        });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, getString(R.string.success));
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent goToMapsActivity = new Intent(MainActivity.this, MapsActivity.class);
                            goToMapsActivity.putExtra("user", user);
                            startActivity(goToMapsActivity);
                        } else {
                            Log.w(TAG, getString(R.string.failure), task.getException());
                            Toast.makeText(MainActivity.this, getString(R.string.failure_toast), Toast.LENGTH_SHORT).show();
                        }
                    }
        });
    }

    private void signOut() {
        mAuth.signOut();
    }
}
