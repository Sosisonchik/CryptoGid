package com.example.apple.cryptogid;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class IcoAdapter extends RecyclerView.Adapter<IcoAdapter.IcoViewHolder> {
    ArrayList<Coin> coins = new ArrayList<>();

    public void add(Coin coin){
        coins.add(coin);
        notifyDataSetChanged();
    }

    public Coin getCoin(int position){
        return coins.get(position);
    }

    public void update(int position,Coin coin){
        coins.set(position,coin);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public IcoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,null);
        return new IcoViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull IcoViewHolder icoViewHolder, int i) {
        icoViewHolder.bind(i);
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

        private void bind( int index){
            Coin coin = coins.get(index);


            name.setText(coin.getName());
            price.setText(String.valueOf(coin.getPrice()) + " $");
            icon.setImageResource(coin.getImgNumber());

            if (coin.isUp()) {
                arrow.setImageResource(R.drawable.arrow_up);
            }else{
                arrow.setImageResource(R.drawable.arrow_down);
            }

            itemView.setAnimation(AnimationUtils.loadAnimation(itemView.getContext(),R.anim.status_arrive));
            arrow.setAnimation(AnimationUtils.loadAnimation(itemView.getContext(),R.anim.rotate));
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
