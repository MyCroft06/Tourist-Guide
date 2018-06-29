package com.test.ertugrulemre.htmlparsing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MuseumActivity extends AppCompatActivity {

    private ListView lv;
    final ArrayList<Gezilecek> liste = new ArrayList<>(); // liste tanımlama
    private YerAdapter adapter;

    private ProgressDialog progressDialog;
    private int i=0;
    private String konum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum);

        lv = (ListView) findViewById(R.id.list);
        adapter = new YerAdapter(MuseumActivity.this, android.R.layout.simple_list_item_1, liste);

        new tasktask().execute();

    }

    public class tasktask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(MuseumActivity.this);
            progressDialog.setTitle("Veriler Yükleniyor");
            progressDialog.setMessage("Lütfen bekleyiniz..");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            while (1==1) {
                try {

                    java.net.URL url = new URL("http://gezelin.xyz/gez/gezilecek.json");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.connect();
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
                        JSONObject array_rows = root.getJSONObject("Ankara");
                        //Log.d("JSON","array_rows:"+array_rows);
                        i++;
                        JSONObject object_rows = array_rows.getJSONObject(String.valueOf(i));
                        //Log.d("JSON", "object_rows:" + object_rows);
                        String location = object_rows.getString("location");
                        String name = object_rows.getString("name");
                        String statement = object_rows.getString("statement");
                        String address = object_rows.getString("address");
                        //Log.d("JSON","name:"+name);

                        liste.add(new Gezilecek(location,name,statement,address));//listeye ekliyor
                    }
                } catch (MalformedURLException e) {
                    Log.d("error", "error1");
                } catch (IOException e) {
                    Log.d("error", "error2");
                } catch (JSONException e) {
                    //Log.d("error", "error3");
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            lv.setAdapter(adapter); // adapter ile gezilecek list i ve Iv listview ı bağladık
            progressDialog.dismiss();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//Burada listeye eklediğimiz elemanların click metodu

                    konum= liste.get(position).isLocation().toString();//listede basılan elemanın positionı ve o elemanın location bilgileri
                    String[] kard = konum.split("=");

                    try {
                        /** Bundle olusturup verileri bundle'a ekleyelim */
                        Bundle extras = new Bundle();
                        extras.putString("name", liste.get(position).getIsim().toString());
                        extras.putString("address", liste.get(position).isAdres().toString());
                        extras.putString("statement", liste.get(position).isAciklama().toString());
                        extras.putString("position",kard[1]);

                        /** Intent olusturalim */
                        Intent intent = new Intent();

                        /** Bundle'i intente ekleyelim */
                        intent.putExtras(extras);

                        /** Yeni sayfayi cagiralim */
                        intent.setClass(MuseumActivity.this, DetayActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}