package com.example.apple.cryptogid;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sp;

    FloatingActionButton fb;

    RecyclerView icoRecycler;
    String[] tags = {"btc_usd","eth_usd","ltc_usd","dash_usd"};
    String[] tagsB = {"btc","eth","ltc","dash"};
    String[] names = {"Bitcoin","Ethereum","Litecoin","Dash"};
    int[] ids = {R.drawable.bitcoin,R.drawable.ethereum,R.drawable.litecoin,R.drawable.dash};
    IcoAdapter adapter = new IcoAdapter();


    public static final String YOBIT = "https://yobit.net/api/3/ticker/btc_usd-eth_usd-ltc_usd-dash_usd";
    ImageView load;

    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = DBHelper.getInstance(MainActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_NAME,null,null,null,null,null,null);

        if (cursor.moveToFirst()){
            Toast.makeText(MainActivity.this,"Hi",Toast.LENGTH_SHORT).show();
            int nameI = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int priceI = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            int imgI = cursor.getColumnIndex(DBHelper.KEY_IMG);
            int statusI = cursor.getColumnIndex(DBHelper.KEY_STATUS);

            while (cursor.moveToNext()){
                int price;
                String name;
                int id;
                boolean status;

                if (cursor.getInt(statusI)==0)
                    status = true;
                else
                    status = false;
                price = cursor.getInt(priceI);
                id = cursor.getInt(imgI);
                name = cursor.getString(nameI);
                adapter.add(new Coin(name,price,status,id));
            }

        }

        initList();


        if (adapter.getItemCount()==0){
            new GetIco().execute();

        }
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetIco().execute();
            }
        });

    }

    private void initList() {
        icoRecycler = findViewById(R.id.ico_recycler);
        icoRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        icoRecycler.setAdapter(adapter);
        icoRecycler.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        fb = findViewById(R.id.refresh);
        fb.attachToRecyclerView(icoRecycler);
    }




    private class GetIco extends AsyncTask<Void,Void,List<Coin>>{
        @Override
        protected void onPostExecute(List<Coin> coins) {
            dbHelper = DBHelper.getInstance(MainActivity.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(DBHelper.TABLE_NAME,null,null);
            ContentValues cv = new ContentValues();
            super.onPostExecute(coins);
            if (adapter.getItemCount()!=0 && adapter.getItemCount()==coins.size()){
                for (int i=0;i<adapter.getItemCount();i++){
                    Coin old = adapter.getCoin(i);
                    Coin current = coins.get(i);
                    Log.d("PRICE",current.getName() + " - " + current.getPrice());
                    if (current.getPrice() >= old.getPrice())
                        current.setUp(true);
                    else
                        current.setUp(false);
                    adapter.update(i,current);
                }
            }else{
                for (Coin coin : coins)
                    adapter.add(coin);
            }

            for (Coin coin : coins){
                cv.put(DBHelper.KEY_PRICE,coin.getPrice());
                cv.put(DBHelper.KEY_NAME,coin.getName());
                cv.put(DBHelper.KEY_IMG,coin.getImgNumber());
                if (coin.isUp())
                    cv.put(DBHelper.KEY_STATUS,0);
                else
                    cv.put(DBHelper.KEY_STATUS,1);

                db.insert(DBHelper.TABLE_NAME,null,cv);
            }




        }

        @Override
        protected List<Coin> doInBackground(Void... voids) {
            onProgressUpdate();
            List<Coin> coins = new ArrayList<>();

            try {
                URL url = new URL(YOBIT);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(250);

                String line;
                StringBuilder sb = new StringBuilder();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                while ((line=in.readLine())!=null){
                    sb.append(line);
                }

                String json = sb.toString();
                JSONParser parser = new JSONParser();
                JSONObject main = (JSONObject)parser.parse(json);

                for (int i=0;i<tags.length;i++){
                    JSONObject coinJ = (JSONObject) main.get(tags[i]);
                    Coin coin = new Coin(names[i],getPrice(coinJ.get("avg").toString()),true,ids[i]);
                    coins.add(coin);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return coins;
        }

    }

    private Integer getPrice(String str){
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();

        for (char ch : chars){
            if(ch!='.')
                sb.append(ch);
            else break;
        }

        return  Integer.valueOf(sb.toString());
    }

}
