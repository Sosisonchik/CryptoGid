package com.example.apple.cryptogid;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class IcoAdapter extends RecyclerView.Adapter<IcoAdapter.IcoViewHolder> {
    ArrayList<Coin> coins = new ArrayList<>();

    public void add(Coin coin){
        coins.add(coin);
    }


    @NonNull
    @Override
    public IcoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,null);
        return new IcoViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull IcoViewHolder icoViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return coins.size();
    }

    class IcoViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;
        ImageView icon;
        ImageView arrow;

        private void bind(ViewGroup viewGroup, int index){
            Coin coin = coins.get(index);

            name.setText(coin.getName());
            price.setText(coin.getPrice());
            icon.setImageResource(coin.getImgNumber());

            if (coin.isUp()) {
                arrow.setImageResource(R.drawable.arrow_up);
            }else{
                arrow.setImageResource(R.drawable.arrow_down);
            }
        }

        public IcoViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.ico_name);
            price = itemView.findViewById(R.id.ico_price);
            icon = itemView.findViewById(R.id.ico_image);
            arrow = itemView.findViewById(R.id.ico_status);
        }
    }
}
