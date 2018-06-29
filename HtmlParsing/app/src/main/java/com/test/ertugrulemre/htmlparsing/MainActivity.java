package com.test.ertugrulemre.htmlparsing;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;

    private TextView txtclose, mail, nam;

    Dialog myDialog;

    FirebaseDatabase db;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        baslangic();//Bu firebase de kişi signIn mi SingUp mı kontrolu yapıyor

        myDialog = new Dialog(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener!=null)
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.museum_image:
                Intent museum = new Intent(MainActivity.this, MuseumActivity.class);
                startActivity(museum);
                break;
            case R.id.near_image:
                Intent near = new Intent(MainActivity.this, NearActivity.class);
                startActivity(near);
                break;
            case R.id.recent_image:
                Intent recent = new Intent(MainActivity.this, RecentActivity.class);
                startActivity(recent);
                break;
            case R.id.profile_image:
                myDialog.setContentView(R.layout.profile);
                txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
                nam =(TextView) myDialog.findViewById(R.id.mail_txt);
                mail =(TextView) myDialog.findViewById(R.id.nam_txt);
                mail.setText(mFirebaseAuth.getCurrentUser().getEmail());
                nam.setText(mFirebaseAuth.getCurrentUser().getDisplayName());
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
                break;
        } // switch end
    } // onClick end

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//eğer başlangıc yani firebasede kayıt var ve griş yapıldı ise giriş yaptın yazıyor
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
            if (resultCode == RESULT_OK)
                Toast.makeText(MainActivity.this,"Giris Yaptın",Toast.LENGTH_SHORT).show();
            else if (requestCode == RESULT_CANCELED)
                finish();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void baslangic() {

        db = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        //mFirebaseAuth.getCurrentUser().getUid(); // to capture user information

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null)
                    Toast.makeText(MainActivity.this,"Giris Yaptın",Toast.LENGTH_SHORT).show();
                else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    //.setIsSmartLockEnabled(false) for auto login
                                    //.setTheme() theme
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }// end else
            }// end onAuthStateChanged
        }; // .AuthStateListener
    } // end method
}