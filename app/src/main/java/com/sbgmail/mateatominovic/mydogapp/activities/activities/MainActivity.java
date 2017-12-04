package com.sbgmail.mateatominovic.mydogapp.activities.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.sbgmail.mateatominovic.mydogapp.R;

public class MainActivity extends AppCompatActivity {

    // UI globalne varijable
    LinearLayout llMainContainer;
    ImageView ivBannerIcon;
    TextView tvAppName;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // prva metoda koja se poziva kada se pokrece activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // povezuje activity s xml layoutom

        // stvaranje objekata od view-a iz xml-a
        llMainContainer = (LinearLayout) findViewById(R.id.ll_main_container);
        ivBannerIcon = (ImageView) findViewById(R.id.iv_icon_banner);
        tvAppName = (TextView) findViewById(R.id.tv_app_name);

        Window window = getWindow();
        // provjerava je li android OS lolipop ili verzija nakon lolipopa
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //postavlja boju status bara
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.metalic_sunburst));
        }

        // instanciranje objekta
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                // translacija po y osi slike i texta
                ObjectAnimator.ofFloat(ivBannerIcon,"translationY", -2000,0),
                ObjectAnimator.ofFloat(tvAppName,"translationY", 2000,0)
        );

        animatorSet.addListener(new AnimatorListenerAdapter() {
            //listener za prvu animaciju- okida se kada zavrsi
            @Override
            public void onAnimationEnd(Animator animation) {
                //druga animacija
                ViewAnimator.animate(llMainContainer).tada().duration(1000).onStop(new AnimationListener.Stop() {
                    //listener druge animacije
                    @Override
                    public void onStop() {
                        // pokreni login activity
                        startLoginActivity();
                    }
                }).start();
            }
        });
        // postavljanje trajanja prve animacije
        animatorSet.setDuration(3000);
        // pokretanje animacije
        animatorSet.start();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
