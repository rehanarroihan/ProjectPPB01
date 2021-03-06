package id.sch.smktelkom_mlg.project.xirpl207162534.digitalbulletin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    EditText email,pass, name;
    FirebaseAuth mAuth;
    Button sup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.supEmail);
        pass = (EditText) findViewById(R.id.supPassword);
        name = (EditText) findViewById(R.id.etNama);
        sup = (Button) findViewById(R.id.sup);
        sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sup.setEnabled(false);
                sup.setText("Signing up...");
                signup();
            }
        });
    }

    private boolean valid() {
        if (name.getText().toString().equals("")) {
            name.setError("The Name is Empty");
            return false;
        } else if (email.getText().toString().equals("")) {
            email.setError("Email Is Empty");
            return false;
        } else if (pass.getText().toString().equals("")) {
            pass.setError("Password Is Empty");
            return false;
        } else if (pass.getText().toString().length() < 6) {
            pass.setError("Password Must Consist More Then 6 Characters");
            return false;
        } else {
            return true;
        }
    }
    public void signup(){
        if(valid()){
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                // Email Set
                                DatabaseReference ref = db.getReference("users").child(mAuth.getCurrentUser().getUid()).child("email");
                                ref.setValue(mAuth.getCurrentUser().getEmail());
                                // Name Set
                                DatabaseReference refname = db.getReference("users").child(mAuth.getCurrentUser().getUid()).child("name");
                                refname.setValue(name.getText().toString());
                                Toast.makeText(getApplicationContext(), "Welcome, " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to Signup :(", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    }
