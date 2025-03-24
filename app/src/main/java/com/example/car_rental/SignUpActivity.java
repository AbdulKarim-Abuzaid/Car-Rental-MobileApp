package com.example.car_rental;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class SignUpActivity extends AppCompatActivity {

    private RequestQueue queue;
    EditText FiarstName;
    EditText LastName;
    EditText emails;
    EditText pass;
    EditText phones;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initalze() ;
        btnRegister() ;
    }


    // initalize the Elemant
    public void initalze() {

         queue = Volley.newRequestQueue(this);
         FiarstName = findViewById(R.id.edtFName) ;
         LastName  = findViewById(R.id.edtLName) ;
         emails = findViewById(R.id.edtEmail) ;
         pass = findViewById(R.id.edtPass) ;
         phones = findViewById(R.id.edtPhone) ;
         register = findViewById(R.id.btnRegister) ;
    }


//define the action of the button

public void btnRegister(){

        register.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            registerUser();
        }
    });
}


public  void registerUser(){

    String firstName = FiarstName.getText().toString().trim();
    String lastName = LastName.getText().toString().trim();
    String email = emails.getText().toString().trim();
    String password = pass.getText().toString().trim();
    String phone = phones.getText().toString().trim();

// check the input
    // Check for empty fields
    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
        Toast.makeText(SignUpActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        return;
    }



    // Create JSON object for parameters
    JSONObject params = new JSONObject();
    try {
        params.put("first_name", firstName);
        params.put("last_name", lastName);
        params.put("email", email);
        params.put("password", password);
        params.put("phone", phone);
    } catch (JSONException e) {
        e.printStackTrace();
    }

    String REGISTER_URL = "http://10.0.2.2:80/rest/register.php";
    // Create a new JsonObjectRequest
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST, REGISTER_URL, params,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("RegisterResponse", response.toString());
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            // Registration successful
                            Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Registration failed
                            String message = response.getString("message");
                            Toast.makeText(SignUpActivity.this, "Registration failed: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(SignUpActivity.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle error
                    Log.e("volley_error", error.toString());
                    Toast.makeText(SignUpActivity.this, "Volley error: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
    );

    // Add the request to the RequestQueue
    queue.add(jsonObjectRequest);
}










}









