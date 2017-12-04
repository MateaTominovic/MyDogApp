package com.sbgmail.mateatominovic.mydogapp.activities.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sbgmail.mateatominovic.mydogapp.R;
import com.sbgmail.mateatominovic.mydogapp.activities.dataModels.Confing;
import com.sbgmail.mateatominovic.mydogapp.activities.dataModels.DogData;
import com.sbgmail.mateatominovic.mydogapp.activities.dataModels.DogInfo;
import com.sbgmail.mateatominovic.mydogapp.activities.dataModels.DogMetaData;
import com.sbgmail.mateatominovic.mydogapp.activities.dataModels.LocationData;
import com.sbgmail.mateatominovic.mydogapp.activities.dataModels.Notification;
import com.sbgmail.mateatominovic.mydogapp.activities.fragments.BasicFragment;
import com.sbgmail.mateatominovic.mydogapp.activities.fragments.DogInfoFragment;
import com.sbgmail.mateatominovic.mydogapp.activities.fragments.GoogleMapFragment;
import com.sbgmail.mateatominovic.mydogapp.activities.rest.RestProvider;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FrameLayout frameLayout;
    ProgressBar pbLoader;
    LinearLayout loaderContainer;
    public static final int TYPE_MY_DOG = 0;
    public static final int TYPE_MY_HEALTH = 1;
    public static final int TYPE_MY_FEED = 2;
    public static final int TYPE_MY_WALK = 3;
    public static final int TYPE_MY_GOOGLE_MAP = 4;
    public static final int TYPE_MY_NOTIFICATION = 5;
    private static final int REQUEST_CODE_GPS = 1001;
    Call<Confing> logoutCall;
    Call<DogData> dogDataCall;
    Call<DogInfo> dogInfoCall;
    Call<LocationData> locationCall;
    Call<Notification> notificationCall;
    DogData dogData;
    DogInfo dogInfo;
    LocationData locationData;
    Notification notificationData;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //verzija androida
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.metalic_sunburst)); //postavljanje boje status bara
        }

        token = getIntent().getExtras().getString("token");   // dohvacanje podataka iz prethodnog activity-a

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        frameLayout = (FrameLayout) findViewById(R.id.fl_fragment_container);
        pbLoader = (ProgressBar) findViewById(R.id.pb_loader);
        loaderContainer = (LinearLayout) findViewById(R.id.ll_loader_container);
        pbLoader.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.metalic_sunburst), PorterDuff.Mode.SRC_ATOP); // bojanje loadera
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // doda i poveze hamburger menu iz toolbara s drawerom
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this); // osluskuje jesu li itemi kliknuti
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) { // ako je drawer otvoren zatvori ga
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed(); // proslijedi akciju activityu
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // trazi koju metodu tj akciju treba izvrsiti za vraceni item
        int id = item.getItemId();

        frameLayout.bringToFront(); // postavlja framelayout na vrh hijerarhije view-a
        if (id == R.id.nav_app_icon) {
            dogInfo();
        } else if (id == R.id.nav_health) {
            cijepljenje();
        } else if (id == R.id.nav_feed) {
            hranjenje();
        } else if (id == R.id.nav_walk) {
            setnja();
        } else if (id == R.id.nav_notification) {
            notification();
        } else if (id == R.id.google_map){
            locationData();
        } else if (id == R.id.nav_logout){
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START); // zatvori drawer
        return true;
    }

    private void addFragment(int type){
        if(type == TYPE_MY_GOOGLE_MAP){
            getSupportFragmentManager().beginTransaction() // ocekuje neku promjenu tipa dodaj, replace ili brisi
                    .replace(R.id.fl_fragment_container, new GoogleMapFragment()).commit(); // zamjenjuje fragmente u kontenjeru
        } else if (type == TYPE_MY_DOG){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_fragment_container, new DogInfoFragment()).commit();
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt("type", type);
            BasicFragment basicFragment = new BasicFragment();
            basicFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_fragment_container, basicFragment).commit();
        }
    }

    // ukoliko je permission granted tj odobren tada ucitaj google mapu i promjeni toolbar title
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //callbaci od riquestanja permissiona
        //provjerava o kojem se request kodu radi
        if(requestCode == REQUEST_CODE_GPS){
            // gledamo jel osoba prihvatila permission
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // ako je odobreno otvaramo novi fragment s google mapom
                addFragment(TYPE_MY_GOOGLE_MAP);
                // postavaljamo toolbar title
                changeToolBarTitle(TYPE_MY_GOOGLE_MAP);
            }
        }
    }

    private void showLoader(){
        loaderContainer.bringToFront();
        loaderContainer.setVisibility(View.VISIBLE);
    }

    private void hideLoader(){
        loaderContainer.setVisibility(View.GONE);
    }

    private void showErrorMessage(){
        Toast.makeText(HomeActivity.this, "Dogodila se greška :(", Toast.LENGTH_SHORT).show();
    }

    private void changeToolBarTitle(int type){
        switch (type){
            case TYPE_MY_DOG:
                getSupportActionBar().setTitle("Moj pas");
                break;

            case TYPE_MY_FEED:
                getSupportActionBar().setTitle("Hranjenje");
                break;

            case TYPE_MY_GOOGLE_MAP:
                getSupportActionBar().setTitle("Lokacije");
                break;

            case TYPE_MY_HEALTH:
                getSupportActionBar().setTitle("Zdravlje");
                break;

            case TYPE_MY_WALK:
                getSupportActionBar().setTitle("Šetnja");
                break;

            case TYPE_MY_NOTIFICATION:
                getSupportActionBar().setTitle("Notifikacije");
                break;
        }
    }

    private void logout(){
        //prikazi loader
        showLoader();
        //radimo call na server da se odlogiramo
        logoutCall = RestProvider.getInterface(this).logout(token);
        //  pokrecemo sekvencu logouta koja se poziva asinkrono tj. rezultat calla se ocekuje asinkrono
        logoutCall.enqueue(new Callback<Confing>() { // new Callback<Config> je listener
            @Override
            public void onResponse(Call<Confing> call, Response<Confing> response) {
                // provjeravmo jel request bio uspjesan
                if(response.body().isStatus()){
                    hideLoader();
                    // ubi aktivity
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Confing> call, Throwable t) { // poziva se ako je puknila konekcija ili ako se nesto dogodi
                hideLoader();
                showErrorMessage();
            }
        });
    }

    private void cijepljenje() {
        showLoader(); // prikazi laoder
        dogDataCall = RestProvider.getInterface(this).getCijepljenje(token); // napravi call
        dogDataCall.enqueue(new Callback<DogData>() { // neka bude asikron
            @Override
            public void onResponse(Call<DogData> call, Response<DogData> response) {
                hideLoader(); // kada dode response sa servera sakrij loader
                if(response.isSuccessful()){ // ako je bio uspjesan
                    dogData = response.body(); // uzmi objekt koji nosi u sebi
                    dogData.setType(TYPE_MY_HEALTH);
                    Collections.sort(dogData.getDogMetaData(), new DogMetaData()); // sortiranje podataka
                    addFragment(TYPE_MY_HEALTH); // otvori fragment
                    changeToolBarTitle(TYPE_MY_HEALTH); // postavi ime toolbara
                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<DogData> call, Throwable t) {
                hideLoader();
                showErrorMessage();
            }
        });
    }

    private void hranjenje() {
        showLoader();
        dogDataCall = RestProvider.getInterface(this).getHranjenje(token);
        dogDataCall.enqueue(new Callback<DogData>() {
            @Override
            public void onResponse(Call<DogData> call, Response<DogData> response) {
                hideLoader();
                if(response.isSuccessful()){
                    response.body().setType(TYPE_MY_FEED);
                    dogData = response.body();
                    Collections.sort(dogData.getDogMetaData(), new DogMetaData());
                    addFragment(TYPE_MY_FEED);
                    changeToolBarTitle(TYPE_MY_FEED);
                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<DogData> call, Throwable t) {
                hideLoader();
                showErrorMessage();
            }
        });
    }

    private void setnja() {
        showLoader();
        dogDataCall = RestProvider.getInterface(this).getSetnja(token);
        dogDataCall.enqueue(new Callback<DogData>() {
            @Override
            public void onResponse(Call<DogData> call, Response<DogData> response) {
                hideLoader();
                if(response.isSuccessful()){
                    response.body().setType(TYPE_MY_WALK);
                    dogData = response.body();
                    Collections.sort(dogData.getDogMetaData(), new DogMetaData());
                    addFragment(TYPE_MY_WALK);
                    changeToolBarTitle(TYPE_MY_WALK);
                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<DogData> call, Throwable t) {
                hideLoader();
                showErrorMessage();
            }
        });
    }


    private void dogInfo() {
        showLoader();
        dogInfoCall = RestProvider.getInterface(this).getDogInfoData(token);
        dogInfoCall.enqueue(new Callback<DogInfo>() {
            @Override
            public void onResponse(Call<DogInfo> call, Response<DogInfo> response) {
                hideLoader();
                if(response.isSuccessful()){
                    dogInfo = response.body();
                    addFragment(TYPE_MY_DOG);
                    changeToolBarTitle(TYPE_MY_DOG);
                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<DogInfo> call, Throwable t) {
                hideLoader();
                showErrorMessage();
            }
        });
    }

    private void locationData() {
        showLoader();
        locationCall = RestProvider.getInterface(this).getLocationData(token);
        locationCall.enqueue(new Callback<LocationData>() {
            @Override
            public void onResponse(Call<LocationData> call, Response<LocationData> response) {
                hideLoader();
                if(response.isSuccessful()){
                    locationData = response.body();
                    // provjerava je li permission za Fine location prihvacen, ako nije requesta permission za njega
                    if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // request permissiona
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_GPS);
                    } else {
                        addFragment(TYPE_MY_GOOGLE_MAP);
                        changeToolBarTitle(TYPE_MY_GOOGLE_MAP);
                    }
                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<LocationData> call, Throwable t) {
                hideLoader();
                showErrorMessage();
            }
        });
    }

    private void notification() {
        showLoader();
        notificationCall = RestProvider.getInterface(this).getNotifications(token);
        notificationCall.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if(response.isSuccessful()){
                    hideLoader();
                    notificationData = response.body();
                    notificationData.setType(TYPE_MY_NOTIFICATION);
                    dogData = new DogData();
                    dogData.setType(TYPE_MY_NOTIFICATION);
                    dogData.setDogMetaData(notificationData.getMetaData().getAllList());
                    Collections.sort(dogData.getDogMetaData(), new DogMetaData());
                    addFragment(TYPE_MY_NOTIFICATION);
                    changeToolBarTitle(TYPE_MY_NOTIFICATION);
                } else {
                    hideLoader();
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                showErrorMessage();
            }
        });
    }
    public DogData getDogData() {
        return dogData;
    }

    public void setDogData(DogData dogData) {
        this.dogData = dogData;
    }

    public DogInfo getDogInfo() {
        // vraca podoatke o psu
        return dogInfo;
    }

    public void setDogInfo(DogInfo dogInfo) {
        this.dogInfo = dogInfo;
    }

    public LocationData getLocationData() {
        return locationData;
    }

    public void setLocationData(LocationData locationData) {
        this.locationData = locationData;
    }

    @Override
    protected void onStop() { // okida se kada se izade iz skrina
        // ubija callove
        if(dogDataCall != null)
        dogDataCall.cancel();
        if(dogInfoCall != null)
        dogInfoCall.cancel();
        if(logoutCall != null)
        logoutCall.cancel();
        if(locationCall != null)
            locationCall.cancel();
        super.onStop();
    }
}
