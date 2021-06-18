package com.kumbh.design.Epost.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.text.HtmlCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.kumbh.design.Epost.Activity.Festival_list_page_premium;
import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.model.PlansListItem;

import java.util.List;


public class premiumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(PlansListItem item);
    }

    Context context;
    private List<PlansListItem> festivallist;
    private final OnItemClickListener listener;

    RequestQueue requestQueue;

    public premiumAdapter(Context context, List<PlansListItem> festivallist, OnItemClickListener listener) {
        this.context = context;
        this.festivallist = festivallist;

        requestQueue = Volley.newRequestQueue(context);
        this.listener = listener;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder viewHolder;

        view = inflater.inflate(R.layout.premium_item_row, null);
        viewHolder = new MyViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {


        MyViewHolder vh1 = (MyViewHolder) holder;
        final PlansListItem festival = festivallist.get(position);
        if(festival.getPlanPrice()!=null && festival.getPlanPrice().compareTo("0.00")!=0)
        {
            vh1.Title.setText(festival.getPlanTitle()+":"+" "+festival.getPlanPrice());
        }
        else{
            vh1.Title.setText(festival.getPlanTitle());
        }

        if(position==0)
        {
            vh1.card_premium.setCardBackgroundColor(Color.parseColor("#B4272C"));
        }
       else if(position==1)
        {
            vh1.card_premium.setCardBackgroundColor(Color.parseColor("#f77056"));
        }
       else if(position==2)
        {
            vh1.card_premium.setCardBackgroundColor(Color.parseColor("#fd994f"));
        }
       else{
            vh1.card_premium.setCardBackgroundColor(Color.parseColor("#fbc751"));
        }




        vh1.card_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Festival_list_page_premium.class);
                intent.putExtra("id", "1");
                context.startActivity(intent);

            }
        });
        vh1.bt_subsribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                    listener.onItemClick(festival);
            }
        });


    }

    @Override
    public int getItemCount() {

        return festivallist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Title;
        CardView card_premium;
        Button bt_subsribe;


        public MyViewHolder(View itemView) {
            super(itemView);

            Title = (TextView) itemView.findViewById(R.id.tv_plan_name);

            card_premium = itemView.findViewById(R.id.card_premium);
            bt_subsribe=itemView.findViewById(R.id.save);


        }
    }


}
