package fr.wildcodeschool.metro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private static final String TAG_GOOGLE = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        Button btNotRegister = findViewById(R.id.btNotRegister);
        btNotRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRegisterActivity = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(goToRegisterActivity);
            }
        });

        Button btLogin = findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etLogin = findViewById(R.id.etLogin);
                EditText etPassword = findViewById(R.id.etPassword);
                signIn(etLogin.getText().toString(), etPassword.getText().toString());
            }
        });

        Button btLoginGoogle = findViewById(R.id.btLoginGoogle);
        btLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn(String email, String password) {
        final ConstraintLayout mainLayout = findViewById(R.id.main_layout);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent goToMapsActivity = new Intent(MainActivity.this, MapsActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG_GOOGLE, getString(R.string.Google_sign_in_failed), e);
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        final ConstraintLayout mainLayout = findViewById(R.id.main_layout);
        Log.d(TAG_GOOGLE, "firebaseAuthWithGoogle: " + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Snackbar.make(mainLayout, String.format(getString(R.string.welcome_name), acct.getDisplayName()), Snackbar.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG_GOOGLE, getString(R.string.signIn_With_Credential_failure), task.getException());
                            Snackbar.make(mainLayout, R.string.Authentication_failed, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menulauncher, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.btMapView:
                Intent goToMapView = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(goToMapView);
                return true;
            case R.id.btListView:
                Intent goToListView = new Intent(MainActivity.this, RecycleViewStation.class);
                startActivity(goToListView);
                return true;
            case R.id.itemMenuRegister:
                Intent goToRegisterView = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(goToRegisterView);
                return true;
            case R.id.itemMenuLogin:
                Intent goToMainActivity = new Intent(MainActivity.this, MainActivity.class);
                startActivity(goToMainActivity);
                return true;
            case R.id.itemMenuFav:
                Intent goToFavorites = new Intent(MainActivity.this, Favorites.class);
                startActivity(goToFavorites);
                return true;
            case R.id.itemMenuLogout:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}