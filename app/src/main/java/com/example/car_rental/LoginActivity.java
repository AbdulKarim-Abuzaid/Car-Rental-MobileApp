package com.example.car_rental;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    //define shardPreferance
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    Button login ;
    private RequestQueue queue  ;
    TextView messge ;
    TextView signUp ;
    EditText email ;
    EditText password ;
    CheckBox box ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
//

//        initalize elemant
        initilaize();
//        check if the user make shared
        isAccountInLocal();
//      make textView click to open Sigh up
        ListnToSignUp();

//   login() ;

        openSignUp();
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    public  void openSignUp(){

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Define email and password

                String ema = email.getText().toString().trim();
                String passw = password.getText().toString().trim();
                // Create a new request queue


                // Create JSON object for parameters
                JSONObject params = new JSONObject();
                try {
                    params.put("email", ema);
                    params.put("password", passw);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String LOGIN_URL = "http://10.0.2.2:80/rest/login.php";
                // Create a new JsonObjectRequest
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST, LOGIN_URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("LoginResponse", response.toString());
                                try {
                                    boolean success = response.getBoolean("success");
                                    if (success) {
                                        // Login successful
                                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, HomeNormalCustomerActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // Login failed
                                        String message = response.getString("message");
                                        Toast.makeText(LoginActivity.this, "Login failed: " + message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(LoginActivity.this, "JSON parsing error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle error
                                Log.e("volley_error", error.toString());
                                Toast.makeText(LoginActivity.this, "Volley error: " + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );

                // Add the request to the RequestQueue
                queue.add(jsonObjectRequest);





//                // Create an Intent to start LoginActivity
                boolean ck = box.isChecked();

                if(ck){

                    boolean check ;

                    check = prefs.contains("email");

                    if(!check ){

                        String ema2 = email.getText().toString().trim();
                        String passw2 = password.getText().toString().trim();
                        editor.putString("email" ,ema2) ;
                        editor.putString("passw" ,passw2) ;
                        editor.apply();
                    }
                }else{
                    editor.remove("email");
                    editor.remove("passw");
                    editor.apply(); // Commit the changes
                }

            }
        });


    }

//    initilaize all elemant

     public void initilaize(){
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
         editor = prefs.edit();
         login = findViewById(R.id.button_signIn) ;
         queue =   Volley.newRequestQueue(this);
         signUp = findViewById(R.id.txtSup) ;
         messge = findViewById(R.id.txtMess) ;
         box = findViewById(R.id.ck_box) ;
         email = findViewById(R.id.editText_email) ;
         password = findViewById(R.id.editText_password) ;
     }

//    define the signupText
     public void ListnToSignUp(){
         signUp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 // Open the new activity
                 Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                 startActivity(intent);
             }
         });
     }

   // check if the shard is have the account

    public void isAccountInLocal(){

        boolean check  ;

        check = prefs.contains("email");
        Log.d("CheckValue", "Email key exists in SharedPreferences: " + check);
        if( check ){

             box.setChecked(true);

//             get data and set it
             String em = prefs.getString("email" ,"") ;
             String pass = prefs.getString("passw" ,"") ;
             password.setText(pass);
             email.setText(em);
        }
    }


}