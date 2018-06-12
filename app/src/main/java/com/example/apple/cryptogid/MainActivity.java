package com.example.apple.cryptogid;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

    RecyclerView icoRecycler;
    String[] tags = {"btc_usd","eth_usd","ltc_usd","dash_usd"};
    String[] names = {"Bicoin","Ethereum","Litecoin","Dash"};
    int[] ids = {R.drawable.bitcoin,R.drawable.ethereum,R.drawable.litecoin,R.drawable.dash};
    Integer[] prices = new Integer[4];
    IcoAdapter adapter = new IcoAdapter();
    public static final String YOBIT = "https://yobit.net/api/3/ticker/btc_usd-eth_usd-ltc_usd-dash_usd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        icoRecycler = findViewById(R.id.ico_recycler);
        icoRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        icoRecycler.setAdapter(adapter);
    }

    private class GetIco extends AsyncTask<Void,Void,List<Coin>>{
        @Override
        protected void onPostExecute(List<Coin> coins) {
            super.onPostExecute(coins);
            for (Coin coin : coins)
                adapter.add(coin);
        }

        @Override
        protected List<Coin> doInBackground(Void... voids) {
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
