package com.test.ertugrulemre.htmlparsing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class NearActivity extends AppCompatActivity {

    private ListView konum_list;
    ArrayList<Near> list = new ArrayList<Near>();

    //private TextView konum_txt;

    private int i = 0;
    private int x = 0;
    private String array_elements;
    private String array_elements_name;

    //private ProgressDialog progressDialog; Var ama kullanmıyorum

    private LocationManager locationManager;
    private LocationListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near);

        konum_list = (ListView) findViewById(R.id.listview);

        //new BackgroundTask().execute("0,0");
        locations();

    }

    public void locations(){

        //konum_txt = (TextView)findViewById(R.id.konumtxt);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); //tanımladık

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //konum_txt.setText("\n " + location.getLatitude() + " " + location.getLongitude());

                Log.d("giden", String.valueOf(location.getLatitude() + "," + location.getLongitude()));
                new BackgroundTask().execute(String.valueOf(location.getLatitude() + "," + location.getLongitude()));
                list.clear();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", 1000, 0, listener); // 5000 ms de bi kontrol et 5 saniye

    }

    public class BackgroundTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Durmadan dönüyor başı sonu yok
//            progressDialog= new ProgressDialog(NearActivity.this);
//            progressDialog.setTitle("Veriler Yükleniyor");
//            progressDialog.setMessage("Lütfen bekleyiniz..");
//            progressDialog.setIndeterminate(false);
//            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            i=0;
            while (i<=5){
                try {

                    URL url=new URL("http://gezelin.xyz/gez/gezilecek.json");

                    HttpURLConnection con= (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.connect();
                    int statuscode=con.getResponseCode();
                    if(statuscode== HttpURLConnection.HTTP_OK)
                    {
                        BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                        StringBuilder sb=new StringBuilder();
                        String line=br.readLine();
                        while(line!=null)
                        {
                            sb.append(line);
                            line=br.readLine();
                        }
                        String json=sb.toString();
                        //Log.d("JSON",json);
                        JSONObject root=new JSONObject(json);
                        JSONObject array_rows=root.getJSONObject("Ankara");
                        //Log.d("JSON","array_rows:"+array_rows);

                        i++;
                        JSONObject object_rows=array_rows.getJSONObject(String.valueOf(i));

                        //Log.d("JSON","object_rows:"+object_rows);
                        array_elements=object_rows.getString("location");
                        array_elements_name = object_rows.getString("name");
                        //Log.d("JSON","array_elements:"+array_elements);

                        String[] loca = array_elements.split("=");
                        String[] location1 = loca[1].split(", ");
                        //Log.d("JSON","locatio:"+location1[0]+location1[1]);

                        String dist_url = "https://maps.googleapis.com/maps/api/distancematrix/" +
                                "json?origins=%22" + params[0] + "%22&destinations=%22" + location1[0] + "," + location1[1] + "%22&mode=driving&language=tr-TR&avoid=tolls&key=AIzaSyAH0TfBbUkq9ePwblr5c9Q3zT1nS9EC-ZQ";

                        distanc(dist_url,array_elements_name);

                    }
                } catch (MalformedURLException e) {
                    Log.d("error", "error1");
                } catch (IOException e) {
                    Log.d("error", "error2");
                } catch (JSONException e) {
                    Log.d("error","error3"+e);

                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            final NearAdapter adaptor = new NearAdapter(NearActivity.this, android.R.layout.simple_list_item_1,list);

            Collections.sort(list, new Comparator<Near>() {
                @Override
                public int compare(Near near, Near t1) {
                    String id1 = near.getDistance();
                    String id2 = t1.getDistance();

                    // ascending order
                    return id1.compareTo(id2);

                    // descending order
                    //return id2.compareTo(id1);
                }
            });
            adaptor.notifyDataSetChanged();

            konum_list.setAdapter(adaptor);
            adaptor.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public void distanc(String atl, String names) {

            try {

                URL url = new URL(atl);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                //Log.d("Link bu",atl);
                int statuscode = con.getResponseCode();
                if (statuscode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();
                    while (line != null) {
                        sb.append(line);
                        line = br.readLine();
                    }
                    String json = sb.toString();
                    //Log.d("JSON",json);
                    JSONObject root = new JSONObject(json);
                    JSONArray array_rows = root.getJSONArray("rows");
                    //Log.d("JSON","array_rows:"+array_rows);
                    JSONObject object_rows = array_rows.getJSONObject(0);
                    //Log.d("JSON","object_rows:"+object_rows);
                    JSONArray array_elements = object_rows.getJSONArray("elements");
                    //Log.d("JSON","array_elements:"+array_elements);
                    JSONObject object_elements = array_elements.getJSONObject(0);
                    //Log.d("JSON","object_elements:"+object_elements);
                    JSONObject object_duration = object_elements.getJSONObject("duration");
                    String duration = object_duration.getString("text");
                    JSONObject object_distance = object_elements.getJSONObject("distance");
                    String distance = object_distance.getString("text");

                    Log.d("JSON", "distance:" + distance);
                    Log.d("JSON", "duration:" + duration);
                    //return object_duration.getString("value")+","+object_distance.getString("value");

                    //x++;
                    //list.add( x + "distance:" + distance + "duration:" + duration);
                    list.add(new Near(names, distance, duration));

                }
            } catch (MalformedURLException e) {
                Log.d("error", "error1");
            } catch (IOException e) {
                Log.d("error", "error2");
            } catch (JSONException e) {
                Log.d("error", "error34");
            }
        }
    }