package com.example.car_rental;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeNormalCustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private RequestQueue queue  ;
    public LinearLayout secondLinearLayout ;
    final Handler handler = new Handler();
    private CarAdapter adapter;//

//    private DataBaseHelper dataBaseHelper;
    public static List<Car> allCars = new ArrayList<>();
    public static List<Car> favCars = new ArrayList<>();
    public static List<Car> reserveCars = new ArrayList<>();
    public static List<Car> carSpecialOffers = new ArrayList<>();
    public static List<Car> chevroletCars = new ArrayList<>();
    public static List<Car> jeepCars = new ArrayList<>();
    public static List<Car> fordCars = new ArrayList<>();
    public static List<Car> dodgeCars = new ArrayList<>();
    public static List<Car> lamborghiniCars = new ArrayList<>();
    public static List<Car> teslaCars = new ArrayList<>();
    public static List<Car> hondaCars = new ArrayList<>();
    public static List<Car> toyotaCars = new ArrayList<>();
    public static List<Car> koenigseggCars = new ArrayList<>();
    public Toolbar toolbar;
    public static NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_normal_customer);

//        dataBaseHelper = new DataBaseHelper(this);



        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set nav header based on information of user
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername= (TextView) headerView.findViewById(R.id.view_name);
        TextView navEmail= (TextView) headerView.findViewById(R.id.view_email);
//        navUsername.setText(User.currentUser.getString(0) +" " +User.currentUser.getString(1));
//        navEmail.setText(User.currentUser.getString(3));

        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("HOME");
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeCustomerFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_home);
        queue =  Volley.newRequestQueue(this);
        getAllCarNotSpecialAndNotReserved();

    }
    @Override
    public void onBackPressed(){
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.nav_profile){
            toolbar.setTitle("PROFILE");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileCustomerFragment()).commit();
        }
        if (item.getItemId()==R.id.nav_home){
            toolbar.setTitle("HOME");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeCustomerFragment()).commit();
        }
        if (item.getItemId()==R.id.nav_logout){
            StringBuilder details = new StringBuilder();
            details.append("Are you sure you want to logout?");

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeNormalCustomerActivity.this);
            builder.setTitle("Confirm Logout")
                    .setMessage(details.toString())
                    .setNegativeButton("cancel",null)
                    .setPositiveButton("sure", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(HomeNormalCustomerActivity.this, LoginActivity.class);
                            HomeNormalCustomerActivity.this.startActivity(intent);
                            finish();
                        }
                    })
                    .create()
                    .show();
        }
//        if (item.getItemId()==R.id.nav_call_find_us){
//            toolbar.setTitle("CALL US OR FIND US");
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CallOrFindUsFragment()).commit();
//        }
        if (item.getItemId()==R.id.nav_carMenu){
            toolbar.setTitle("CAR MENU");
            drawer.closeDrawer(GravityCompat.START);

            // thread to not make the app freeze
            new Thread(() -> runOnUiThread(()->{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CarMenuFragment()).commit();
            })).start();
        }
//        if (item.getItemId()==R.id.nav_yourFavorites){
//            toolbar.setTitle("FAVORITE CARS");
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FavoriteCarsFragment()).commit();
//        }
//        if (item.getItemId()==R.id.nav_yourReservations){
//            toolbar.setTitle("RESERVED CARS");
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ReservedCarsFragment()).commit();
//        }
//        if (item.getItemId()==R.id.nav_specialOffers){
//            toolbar.setTitle("SPECIAL OFFERS");
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SpecialOffersFragment()).commit();
//        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getAllCarNotSpecialAndNotReserved(){

        HomeNormalCustomerActivity.allCars.clear();
        HomeNormalCustomerActivity.chevroletCars.clear();
        HomeNormalCustomerActivity.fordCars.clear();
        HomeNormalCustomerActivity.dodgeCars.clear();
        HomeNormalCustomerActivity.hondaCars.clear();
        HomeNormalCustomerActivity.jeepCars.clear();
        HomeNormalCustomerActivity.lamborghiniCars.clear();
        HomeNormalCustomerActivity.koenigseggCars.clear();
        HomeNormalCustomerActivity.teslaCars.clear();
        HomeNormalCustomerActivity.toyotaCars.clear();


        String url = "http://10.0.2.2:80/rest/info_json.php?";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        Log.d("volley_response", response.toString()); // Log the response
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                // Example: Log each car's data

                                Car car3 = new Car();
                                car3.setID(obj.getInt("id"));
                                car3.setFactoryName(obj.getString("factory_name"));
                                car3.setType(obj.getString("type"));
                                car3.setPrice(obj.getString("price"));
                                car3.setFuelType(obj.getString("fule_type"));
                                car3.setTransmission(obj.getString("transmission"));
                                car3.setMileage(obj.getString("mileage"));
                                car3.setImage(obj.getString("image"));
                                car3.setImgFavButton(R.drawable.ic_favorite_border);
                                car3.setDealerID(obj.getInt("dealerId"));
                                car3.setRating(obj.getDouble("rating"));
                                car3.setRatingCount(obj.getInt("rating_count"));
                                car3.setDealerName("Abdalkareem");
                                HomeNormalCustomerActivity.allCars.add(car3);

                                if (car3.getFactoryName().equals("Chevrolet")){
                                    HomeNormalCustomerActivity.chevroletCars.add(car3);
                                } else if (car3.getFactoryName().equals("Ford")){
                                    HomeNormalCustomerActivity.fordCars.add(car3);
                                } else if (car3.getFactoryName().equals("Dodge")){
                                    HomeNormalCustomerActivity.dodgeCars.add(car3);
                                } else if (car3.getFactoryName().equals("Honda")){
                                    HomeNormalCustomerActivity.hondaCars.add(car3);
                                } else if (car3.getFactoryName().equals("Jeep")){
                                    HomeNormalCustomerActivity.jeepCars.add(car3);
                                } else if (car3.getFactoryName().equals("Lamborghini")){
                                    HomeNormalCustomerActivity.lamborghiniCars.add(car3);
                                } else if (car3.getFactoryName().equals("Koenigsegg")){
                                    HomeNormalCustomerActivity.koenigseggCars.add(car3);
                                } else if (car3.getFactoryName().equals("Tesla")){
                                    HomeNormalCustomerActivity.teslaCars.add(car3);
                                } else if (car3.getFactoryName().equals("Toyota")){
                                    HomeNormalCustomerActivity.toyotaCars.add(car3);
                                }

                            } catch (JSONException e) {
                                Log.e("volley_error", "JSON parsing error", e);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.d("volley_error", error.toString());
                    }
                });


        queue.add(request);
    }

}