package com.kumbh.design.Epost.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kumbh.design.Epost.Activity.SetFestivalImagesActivity;
import com.kumbh.design.Epost.Activity.Set_Images;
import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.model.ListPostItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Festival_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private List<ListPostItem> festivallist;
    String id;
    Intent i;
    RequestQueue requestQueue;
    String mac_id;
    String country;

    public Festival_Adapter(Context context, List<ListPostItem> festivallist, String id) {
        this.context = context;
        this.festivallist = festivallist;
        this.id = id;
        this.country = country;
        requestQueue = Volley.newRequestQueue(context);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        RecyclerView.ViewHolder viewHolder;
        if(viewType == 1) {
            view = inflater.inflate(R.layout.custom_festival_page, null);
            viewHolder = new MyViewHolder(view);
        }else{
            view = inflater.inflate(R.layout.custom_icon, null);
            viewHolder = new MyViewHolder2(view);
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == festivallist.size()){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder.getItemViewType() == 1) {

            MyViewHolder vh1 = (MyViewHolder) holder;
            final ListPostItem festival = festivallist.get(position);
            vh1.Title.setText(festival.getPostName());
            String date = parseDateToddMMyyyy(festival.getDate());
            Log.d("Date_new", date);
            vh1.Date.setText(date.substring(0, date.indexOf("-")).toString());
            vh1.Month.setText(date.substring(date.indexOf("-") + 1, date.length()));

            if (Integer.parseInt(id) == 1){
                vh1.bday_nt.setVisibility(View.GONE);
            }else {
                vh1.bday_nt.setVisibility(View.VISIBLE);
            }

            vh1.festival_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.parseInt(id) == 1) {
                        Intent intent = new Intent(context, SetFestivalImagesActivity.class);
                        intent.putExtra("festival", festival.getPostName().replace(" ", "+").replace("'", "%27"));
                        intent.putExtra("id", festival.getPostId());
                        context.startActivity(intent);
                    }else{
                        Intent intent = new Intent(context, SetFestivalImagesActivity.class);
                        intent.putExtra("festival", "birthday");
                        intent.putExtra("id", festival.getPostId());
                        context.startActivity(intent);
                    }
                }
            });

            vh1.bday_nt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mac_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    System.out.println("android_id=" + mac_id);

                    Add_Birthday(mac_id,"birthday","REMINDER",festival.getPostName(),parseDateToddMMyyyy1(festival.getDate()),"00:01");

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        Log.d("festivallist.size()", String.valueOf(festivallist.size()));
        return festivallist.size()+1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Title,Date,Month;
        RelativeLayout festival_id;
        Typeface tf;
        ImageView bday_nt;

        public MyViewHolder(View itemView) {
            super(itemView);

            Title = (TextView) itemView.findViewById(R.id.main_name);
            Date = (TextView) itemView.findViewById(R.id.date);
            Month = (TextView) itemView.findViewById(R.id.date1);
            festival_id = (RelativeLayout) itemView.findViewById(R.id.festival_id);
            bday_nt = (ImageView) itemView.findViewById(R.id.bday_nt);

            tf = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/swift_lt.ttf");
            Title.setTypeface(tf,Typeface.BOLD);

            tf = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/swift_rm.ttf");
            Date.setTypeface(tf,Typeface.BOLD);
            Month.setTypeface(tf,Typeface.BOLD);
        }
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date).toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str.trim();
    }

    public String parseDateToddMMyyyy1(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date).toUpperCase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str.trim();
    }

    private class MyViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout custom_icon;
        ImageView loveu_1,anniversary_1,birthday_1,gm_1,gn_1,congratulation_1;

        public MyViewHolder2(View view) {
            super(view);

            loveu_1 = (ImageView)view.findViewById(R.id.loveu_1);
            anniversary_1 = (ImageView)view.findViewById(R.id.anniversary_1);
            birthday_1 = (ImageView) view.findViewById(R.id.birthday_1);
            gm_1 = (ImageView) view.findViewById(R.id.gm_1);
            gn_1 = (ImageView) view.findViewById(R.id.gn_1);
            congratulation_1 = (ImageView) view.findViewById(R.id.congratulation_1);
            custom_icon = (RelativeLayout) itemView.findViewById(R.id.custom_icon);

            loveu_1.setOnClickListener(this);
            anniversary_1.setOnClickListener(this);
            birthday_1.setOnClickListener(this);
            gm_1.setOnClickListener(this);
            gn_1.setOnClickListener(this);
            congratulation_1.setOnClickListener(this);

        }



        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.loveu_1:
                    i = new Intent(context,Set_Images.class);
                    i.putExtra("festival","loveu");
                    context.startActivity(i);
                    break;
                case R.id.anniversary_1:
                    i = new Intent(context,Set_Images.class);
                    i.putExtra("festival","anniversary");
                    context.startActivity(i);
                    break;
                case R.id.birthday_1:
                    i = new Intent(context,Set_Images.class);
                    i.putExtra("festival","birthday");
                    context.startActivity(i);
                    break;
                case R.id.gm_1:
                    i = new Intent(context,Set_Images.class);
                    i.putExtra("festival","goodmorning");
                    context.startActivity(i);
                    break;
                case R.id.gn_1:
                    i = new Intent(context,Set_Images.class);
                    i.putExtra("festival","goodnight");
                    context.startActivity(i);
                    break;
                case R.id.congratulation_1:
                    i = new Intent(context,Set_Images.class);
                    i.putExtra("festival","congratulation");
                    context.startActivity(i);
                    break;
            }
        }
    }

    public void Add_Birthday(String android_id, String reminder_category,String reminder_type, String reminder_title, String reminder_date, String reminder_time) {
        StringRequest request = new StringRequest(Request.Method.POST,
                " http://www.kumbhdesign.in/mobile-app/depost/api/reminder_new/add",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("response");
                            JSONObject object1 = array.getJSONObject(0);
                            String error = object1.getString("error");

                            if (error.equals("0")){
                                Toast.makeText(context,object1.getString("message"),Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(context, My_Events.class);
//                                context.startActivity(intent);

                            }else if (error.equals("1")){
                                Toast.makeText(context,object1.getString("message"),Toast.LENGTH_LONG).show();
                            }else if (error.equals("2")){
                                Toast.makeText(context,object1.getString("message"),Toast.LENGTH_LONG).show();
                            }else if (error.equals("3")){
                                Toast.makeText(context,object1.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,String.valueOf(error),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("mac_id",android_id);
                Log.d("mac_id",android_id);
                param.put("reminder_category", reminder_category);
                Log.d("reminder_category", reminder_category);
                param.put("reminder_title", reminder_title);
                Log.d("reminder_title", reminder_title);
                param.put("reminder_type", reminder_type);
                Log.d("reminder_type", reminder_type);
                param.put("reminder_date", reminder_date);
                Log.d("reminder_date", reminder_date);
                param.put("reminder_time", reminder_time);
                Log.d("reminder_time", reminder_time);
                return param;
            }
        };
        requestQueue.add(request);
    }

}
