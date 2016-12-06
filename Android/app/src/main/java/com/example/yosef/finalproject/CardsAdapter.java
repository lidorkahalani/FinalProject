package com.example.yosef.finalproject;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.MyViewHolder> {

    private List<Card> cards;
    private Context conext;

    //final String imageRelativePat = "http://10.0.2.2/final_project/images/";
    //final String imageRelativePat = "http://mysite.lidordigital.co.il/Quertets/php/images/";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView category;
        public ImageView image;
        public TextView card1;
        public TextView card2;
        public TextView card3;
        public TextView card4;
        private boolean isClicked = false;

        public MyViewHolder(View view, int width) {
            super(view);

            view.setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.setOnCreateContextMenuListener(this);
            category = (TextView) view.findViewById(R.id.series);
            image = (ImageView) view.findViewById(R.id.card_image);
            card1 = (TextView) view.findViewById(R.id.card_name1);
            card2 = (TextView) view.findViewById(R.id.card_name2);
            card3 = (TextView) view.findViewById(R.id.card_name3);
            card4 = (TextView) view.findViewById(R.id.card_name4);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }

    int width;

    public CardsAdapter(List<Card> cards, int width) {
        this.cards = cards;
        this.width = width;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        conext = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singelcard, parent, false);
        return new MyViewHolder(itemView, width);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Card card = getItem(position);
        String fullPath;

        holder.category.setText(card.getCategoryName());
        //holder.image.setImageDrawable(card.getItemPicture());
       // holder.image.setImageURI(card.getImageName()););
        if (!holder.isClicked) {
            if (card.getCardName().equals(card.getItemsArray()[0])) {
                holder.card1.setTypeface(Typeface.DEFAULT_BOLD);
            } else if (card.getCardName().equals(card.getItemsArray()[1])) {
                holder.card2.setTypeface(Typeface.DEFAULT_BOLD);
            } else if (card.getCardName().equals(card.getItemsArray()[2])) {
                holder.card3.setTypeface(Typeface.DEFAULT_BOLD);
            } else if (card.getCardName().equals(card.getItemsArray()[3])) {
                holder.card4.setTypeface(Typeface.DEFAULT_BOLD);
            }
        }

        holder.card1.setText(card.getItemsArray()[0]);
        holder.card2.setText(card.getItemsArray()[1]);
        holder.card3.setText(card.getItemsArray()[2]);
        holder.card4.setText(card.getItemsArray()[3]);
        fullPath = ServerUtils.imageRelativePat + card.getImageName();
        ImageLoader imageLoader = new ImageLoader(conext);
        imageLoader.DisplayImage(fullPath, R.mipmap.ic_launcher, holder.image);


    }

    public Card getItem(int position) {
        return cards.get(position);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
