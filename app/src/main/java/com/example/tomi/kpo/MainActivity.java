package com.example.tomi.kpo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private Button gombOllo, gombKo, gombPapir, reset;
    private TextView TVcounterJatekos, TVcounterEllenfel;
    private ImageView kepJatekos, kepEllenfel, fullImage;
    private Switch kapcsoloExpert;
    private RelativeLayout layerCsatater, bal;
    private Toast toast = null;
    private boolean foglalt = false;
    Random rnd = new Random();
    int kepek[]={R.drawable.fekete,R.drawable.rock_bal,R.drawable.paper_bal,R.drawable.scissors_bal};
    //kő    1   <   papír   2
    //papír 2   <   olló    3
    //olló  3   <   kő      1
    int valasztas;
    int gepValasztas;
    int eredmenyJatekos;
    int eredmenyGep;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();

        gombOllo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!foglalt) animacio(gombOllo.getX(), gombOllo.getY(), R.drawable.scissors_jobb, valasztas = 3);
            }
        });

        gombPapir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!foglalt) animacio(gombPapir.getX(), gombPapir.getY(), R.drawable.paper_jobb, valasztas = 2);
            }
        });

        gombKo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!foglalt) animacio(gombKo.getX(), gombKo.getY(), R.drawable.rock_jobb, valasztas = 1);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });
    }

    private void animacio(float xa, float ya, final int kep, final int valasztas){

        foglalt = true;
        float xb = kepJatekos.getX();
        float yb = kepJatekos.getY();
        float magassag = layerCsatater.getHeight();
        float oldaltav = bal.getWidth();

        int hossz = 400;

        AnimatorSet animatorSetPre = new AnimatorSet();
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(fullImage, "alpha",0f);
        fadeOut.setDuration(100);

        final AnimatorSet animatorSet = new AnimatorSet();
        final ObjectAnimator moverx = ObjectAnimator.ofFloat(fullImage, "TranslationX", xa-65f, xb+oldaltav);
        moverx.setDuration(hossz);
        final ObjectAnimator movery = ObjectAnimator.ofFloat(fullImage, "TranslationY", ya+magassag-65f, yb);
        movery.setDuration(hossz);
        final ObjectAnimator fadeIn = ObjectAnimator.ofFloat(fullImage, "alpha",0f, 1f);
        fadeIn.setDuration(hossz);
        final ObjectAnimator XScale = ObjectAnimator.ofFloat(fullImage, "ScaleX",.65f, 1f);
        XScale.setDuration(hossz);
        final ObjectAnimator YScale = ObjectAnimator.ofFloat(fullImage, "ScaleY",.65f, 1f);
        YScale.setDuration(hossz);

        jatek(0);

        animatorSetPre.play(fadeOut);
        animatorSet.play(moverx).with(movery).with(fadeIn).with(XScale).with(YScale);
        animatorSetPre.start();

        animatorSetPre.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                fullImage.setBackgroundResource(kep);
                animatorSet.start();
                if (!kapcsoloExpert.isChecked()){
                    gepValasztas = rnd.nextInt(3)+1;
                    jatek(gepValasztas);
                }else {
                    switch (valasztas){
                        case 1: //kő
                            gepValasztas = 2;
                            jatek(gepValasztas);
                            break;
                        case 2: //papír
                            gepValasztas = 3;
                            jatek(gepValasztas);
                            break;
                        case 3: //olló
                            gepValasztas = 1;
                            jatek(gepValasztas);
                            break;
                        default:
                            break;
                    }

                }
            }
        });
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                foglalt = false;

            }
        });

    }

    private void jatek(final int kep){

        final int kepId = kepek[kep];
        final AnimatorSet animatorSetAfter = new AnimatorSet();
        AnimatorSet animatorSetAfterPre = new AnimatorSet();

        final ObjectAnimator fadeIn = ObjectAnimator.ofFloat(kepEllenfel, "alpha",0f, 1f);
        fadeIn.setDuration(100);

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(kepEllenfel, "alpha",0f);
        fadeOut.setDuration(100);

        animatorSetAfterPre.play(fadeOut);
        animatorSetAfter.play(fadeIn);
        animatorSetAfterPre.start();

        animatorSetAfterPre.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animatorSetAfter.start();
                kepEllenfel.setBackgroundResource(kepId);
                sorsolas(kep);
            }
        });

    }

    private void sorsolas(int kep){
        //kő    1   <   papír   2   <   olló    3
        //papír 2   <   olló    3   <   kő      1
        //olló  3   <   kő      1   <   papír   2
        switch (kep){
            case 1: //gép választása kő
                switch (valasztas){
                    case 1: //játékos válaszása kő
                        Dontetlen();
                        break;
                    case 2:
                        JatekosNyert();
                        break;
                    case 3:
                        GepNyert();
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                switch (valasztas){
                    case 1:
                        GepNyert();
                        break;
                    case 2:
                        Dontetlen();
                        break;
                    case 3:
                        JatekosNyert();
                        break;
                    default:
                        break;
                }
                break;
            case 3:
                switch (valasztas){
                    case 1:
                        JatekosNyert();
                        break;
                    case 2:
                        GepNyert();
                        break;
                    case 3:
                        Dontetlen();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    private void JatekosNyert(){
        toast = Toast.makeText(getApplicationContext(), "Játékos nyert!", Toast.LENGTH_SHORT);
        toast.show();
        eredmenyJatekos++;
        TVcounterJatekos.setText("Játékos: "+eredmenyJatekos);
    }

    private void GepNyert(){
        toast = Toast.makeText(getApplicationContext(), "Gép nyert!", Toast.LENGTH_SHORT);
        toast.show();
        eredmenyGep++;
        TVcounterEllenfel.setText("Gép: "+eredmenyGep);
    }

    private void Dontetlen(){
        toast = Toast.makeText(getApplicationContext(), "Döntetlen!", Toast.LENGTH_SHORT);
        toast.show();

    }

    private void init(){
        gombOllo = (Button) findViewById(R.id.gombOllo);
        gombKo = (Button) findViewById(R.id.gombKo);
        gombPapir = (Button) findViewById(R.id.gombPapir);
        reset = (Button) findViewById(R.id.reset);
        TVcounterJatekos = (TextView) findViewById(R.id.TVcounterJatekos);
        TVcounterEllenfel = (TextView) findViewById(R.id.TVcounterEllenfel);
        fullImage = (ImageView) findViewById(R.id.fullImage);
        kepJatekos = (ImageView) findViewById(R.id.kepJatekos);
        kepEllenfel = (ImageView) findViewById(R.id.kepEllenfel);
        kapcsoloExpert = (Switch) findViewById(R.id.kapcsoloExpert);
        layerCsatater = (RelativeLayout) findViewById(R.id.layerCsatater);
        bal = (RelativeLayout) findViewById(R.id.bal);

        eredmenyGep = 0;
        eredmenyJatekos = 0;
        foglalt = false;
        TVcounterJatekos.setText("Játékos: "+eredmenyJatekos);
        TVcounterEllenfel.setText("Gép: "+eredmenyGep);
        kepEllenfel.setBackgroundResource(0);
        fullImage.setBackgroundResource(0);
    }
}
