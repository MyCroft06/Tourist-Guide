package com.test.ertugrulemre.htmlparsing;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DetayActivity extends AppCompatActivity{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private DetayActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    Dialog myDialog;
    private TextView museum_txt;
    private TextView txtclose;
    private EditText mFeedback;
    private RatingBar mRatingBar;
    private TextView mRatingScale;
    private Button btn;

    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    private String names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detay);

        Button comment_btn = (Button)findViewById(R.id.comment_btn);//Yorum penceresini açıyor
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment();
            }
        });

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new DetayActivity.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        //Bu alt kısımda listeden seçilen müze hakkında gönderilen bilgileri alıyoruz
        Bundle extras = getIntent().getExtras();
        final String name = extras.getString("name");
        String statement = extras.getString("statement");
        String address = extras.getString("address");
        final String posi = extras.getString("position");//konum bilgisi

        names=name;

        TextView txt_name=(TextView)findViewById(R.id.textView);
        txt_name.setText(name);

        GlobalBus.getBus().postSticky(new DataEvent(name,address,statement));//İntent'de veri gönderiyoruz ama fragöente veri gönderirken -GlobalBus- kullandık.

        Button konum_btn = (Button) findViewById(R.id.konum_btn);//Bu buton müzenin konumunu Map'te haritada göstermesi için kullanılıyor.
        konum_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetayActivity.this,MapsActivity.class);

                i.putExtra("local",posi);//Haritada gösterebilmesi için location gönderdik
                i.putExtra("name",name);//Haritada icon un üzeride isim yazsındiye name ini gönderdik

                startActivity(i);
            }
        });
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DetayActivity.PlaceholderFragment newInstance(int sectionNumber) {
            DetayActivity.PlaceholderFragment fragment = new DetayActivity.PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_detay, container, false);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new StatementFragment();
                    Bundle args1 = new Bundle();
                    args1.putString("p_name",names);
                    fragment.setArguments(args1);
                    break;
                case 1:
                    fragment = new CommentFragment();
                    Bundle args2 = new Bundle();
                    args2.putString("p_name",names);
                    fragment.setArguments(args2);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }

    private void commentSend(final String feedback, final float rating) {// Yorumun Gönderildiği kısım

        Thread t = new Thread(){

        public void run(){
            Looper.prepare();
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 5000);
            HttpResponse response;
            try {
                HttpPost post = new HttpPost("http://gezelin.xyz/gez/kaydet.php");//Gönderilen verileri bu php dosyası alacak ve veri tabanın yazacak
                List<NameValuePair> eklenecekler = new ArrayList<NameValuePair>(2);

                //Gönderilecek olan veriler bunlar
                eklenecekler.add(new BasicNameValuePair("userid",mFirebaseAuth.getCurrentUser().getEmail()));
                eklenecekler.add(new BasicNameValuePair("name",names));
                eklenecekler.add(new BasicNameValuePair("comment",feedback));
                eklenecekler.add(new BasicNameValuePair("rating",String.valueOf(rating)));

                post.setEntity(new UrlEncodedFormEntity(eklenecekler, "UTF-8"));
                String html = "";
                response = client.execute(post);

                if (response != null) {
                    InputStream in = response.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String satirlar = null;
                    try {
                        while ((satirlar = reader.readLine()) != null) {
                            sb.append(satirlar + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    html = sb.toString();
                    JSONArray parcala = new JSONArray(html);//Gönderilen veriler veritabanına kayıt edildimi sorgusu if ile yapıldı burada.
                    if (parcala.length() > 0) {
                        JSONObject obj = parcala.getJSONObject(0);
                        if (obj.getBoolean("islem") != true) {
                            Toast.makeText(DetayActivity.this, "Bir sorun oluştu ve form gönderilemedi", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetayActivity.this, "Form başarıyla ulaştı", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            } catch (Exception e) {
                Log.e("Try-catch hatasi", "Hata" + e);
            }
            Looper.loop();
        }
    };
        t.start();
    }

    private void comment(){ //Yorum penceresi Buradan alınan yorum ve rating -commentSend- e gönderiliyor

        myDialog = new Dialog(this);

        myDialog.setContentView(R.layout.popup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

        mFirebaseAuth=FirebaseAuth.getInstance();
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                }
            }
        };

        museum_txt = (TextView) myDialog.findViewById(R.id.textView11);
        mRatingBar = (RatingBar) myDialog.findViewById(R.id.ratingBar);
        mRatingScale = (TextView) myDialog.findViewById(R.id.textView10);
        mFeedback = (EditText) myDialog.findViewById(R.id.editText2);
        btn = (Button)myDialog.findViewById(R.id.send_btn);

        museum_txt.setText(names);

       mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {// Rading Bar'ın kaç puan ise ona göre
           @Override
           public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
               mRatingScale.setText(String.valueOf(v));
               switch ((int) ratingBar.getRating()) {
                   case 1:
                       mRatingScale.setText("Very bad");
                       break;
                   case 2:
                       mRatingScale.setText("Need some improvement");
                       break;
                   case 3:
                       mRatingScale.setText("Good");
                       break;
                   case 4:
                       mRatingScale.setText("Great");
                       break;
                   case 5:
                       mRatingScale.setText("Awesome. I love it");
                       break;
                   default:
                       mRatingScale.setText("");
               }
           }
       });

        btn.setOnClickListener(new View.OnClickListener() { // Bu buton yorum textbox ını boş bıraktı ise uyarmak için
            @Override
            public void onClick(View view) {
                if (mFeedback.getText().toString().isEmpty()) {
                    Toast.makeText(DetayActivity.this, "Please fill in feedback text box", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DetayActivity.this, "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();

                    commentSend(mFeedback.getText().toString().trim(),mRatingBar.getRating());//Eğer herşey yolunda ise -commentSend- methoduna gönderiyoruz.
                    myDialog.dismiss();
                }
            }
        });
    }
}