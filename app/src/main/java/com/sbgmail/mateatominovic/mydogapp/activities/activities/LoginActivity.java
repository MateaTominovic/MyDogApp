package com.sbgmail.mateatominovic.mydogapp.activities.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.sbgmail.mateatominovic.mydogapp.R;
import com.sbgmail.mateatominovic.mydogapp.activities.activities.HomeActivity;
import com.sbgmail.mateatominovic.mydogapp.activities.dataModels.Confing;
import com.sbgmail.mateatominovic.mydogapp.activities.rest.RestProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<Confing> {

    // UI globalne variable
    EditText etUsername;
    EditText etPassword;
    TextView tvLoginButton;
    ProgressBar pbLoader;
    LinearLayout loaderContainer;
    // retrofit call globalna variabla
    Call<Confing> confingCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // prva metoda koja se pozove kada se pokrece Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // poveže activity s xml layoutom

        // bojanje status bara
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.metalic_sunburst));
        }

        // stvaranje objekata od view-a iz xml-a
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        tvLoginButton = (TextView) findViewById(R.id.tv_login);
        pbLoader = (ProgressBar) findViewById(R.id.pb_loader);
        loaderContainer = (LinearLayout) findViewById(R.id.ll_loader_container);

        // popunjava username i password ukoliko postoje podaci spremljeni u shared preference
        preFilLoginData();
        // postavlja button na onClick event
        tvLoginButton.setOnClickListener(this);

        //bojanje loadera u odgovarajucu boju
        pbLoader.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.metalic_sunburst), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void onClick(View v) {
        //////////////////////////////////////////VALIDACIJA ////////////////////////
        boolean usernameEmpty = false;
        boolean passwordEmpty = false;
        // provjerava je li u edit text prazan - username
        if(etUsername.getText().toString().isEmpty()){
            usernameEmpty = true; // postavlja flag u true ako je prazan
        }
        // provjerava je li u edit text prazan - password
        if(etPassword.getText().toString().isEmpty()){
            passwordEmpty = true; // postavlja flag u true ako je prazan
        }

        if(usernameEmpty && passwordEmpty){ // ako su oba dva prazna ispisi poruku
            Toast.makeText(this, "Unesite korisničko ime i lozinku", Toast.LENGTH_LONG).show(); // ispisuje poruku
        } else if(usernameEmpty){ // samo je prazan username
            Toast.makeText(this, "Unesite korisničko ime", Toast.LENGTH_LONG).show();
        } else if(passwordEmpty){// samo je prazan password
            Toast.makeText(this, "Unesite lozinku ime", Toast.LENGTH_LONG).show();
        }
        //////////////////////////////////////////KRAJ VALIDACIJE ////////////////////////
        if(!usernameEmpty && !passwordEmpty){ // oba  unesena
            confingCall = RestProvider.getInterface(this).login(etUsername.getText().toString(), etPassword.getText().toString()); // radi call za podatke
            confingCall.enqueue(this); // asinkroni poziv za podatke okinit ce listener
            loaderContainer.setVisibility(View.VISIBLE); // prikazuje loader tj. postavlja ga u vidljivo stanje
        }
    }

    // kad dodu podaci s neta okine se ova metoda
    @Override
    public void onResponse(Call<Confing> call, Response<Confing> response) {
        if(response.isSuccessful()){ // ako je request prosao ok
            if(response.body().isStatus()){
                // spremanje username i passa u shared preferences
                saveLoginDataToSharedPreferences(etUsername.getText().toString(), etPassword.getText().toString());
                // pokretanje novog activitja- ako je uspjesan login prebaci na next screen
                Intent intent = new Intent(this, HomeActivity.class);
                // sluzi za prenos podataka u sljedeci skrin
                intent.putExtra("token", response.body().getToken()); // salje token u obliku stringa  //ako je login valjan dobije se token koji saljem na svaki request
                startActivity(intent);
            } else {
                loaderContainer.setVisibility(View.GONE);// sakrije loader
                Toast.makeText(LoginActivity.this, "Krivo korisničko ime ili lozinka", Toast.LENGTH_SHORT).show(); // ako je fail ispisi poruku
            }
        }
    }

    // callback se okida ako je nesto poslo po zlu
    @Override
    public void onFailure(Call<Confing> call, Throwable t) {
        loaderContainer.setVisibility(View.GONE); // sakrije laoder
        Toast.makeText(LoginActivity.this, "Dogodila se greška :(", Toast.LENGTH_SHORT).show(); // ispise toast za gresku
    }

    private void saveLoginDataToSharedPreferences(String username, String password){
        SharedPreferences sharedPreferences = getSharedPreferences("MyDogAPP", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username); // spremam  username
        editor.putString("password", password); // spremam password
        editor.apply();
    }

    // dohvaca spremljene podatke iz shared preference
    private ArrayList<String> getLoginDataFromSharedPreferences(){
        ArrayList<String> loginData = new ArrayList<>();// podaci iz shared prefa se stavljaju u array listu stringova
        SharedPreferences sharedPreferences = getSharedPreferences("MyDogAPP", MODE_PRIVATE);
        loginData.add(sharedPreferences.getString("username", "")); // "" - ako nemam podatka vrati prazan string
        loginData.add(sharedPreferences.getString("password", ""));
        return loginData;
    }

    private void preFilLoginData(){
        // uzima podatke iz sahred prefa
        ArrayList<String> preFilData = getLoginDataFromSharedPreferences();
        etUsername.setText(preFilData.get(0)); // postavlja podatke
        etPassword.setText(preFilData.get(1)); // posatvlja podatke

        if(!etUsername.getText().toString().equals("") && !etPassword.getText().toString().equals("")){ // ako postoje podaci pokreni sekvencu za logiranje
            onClick(null);
        }
    }

    @Override
    protected void onStop() {
        // ubijamo call
        confingCall.cancel();
        super.onStop();
    }
}

