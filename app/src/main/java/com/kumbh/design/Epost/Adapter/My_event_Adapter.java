package com.kumbh.design.Epost.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kumbh.design.Epost.Activity.Set_Images;
import com.kumbh.design.Epost.Activity.Update;
import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.data.Constant_events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class My_event_Adapter extends RecyclerView.Adapter<My_event_Adapter.MyViewHolder> {
    private Context context;
    private List<Constant_events> my_events;
    Intent i;
    RequestQueue requestQueue;
    String mac_id,reminder_category;
    String category;

    public My_event_Adapter(Context context, List<Constant_events> my_events) {
        this.context = context;
        this.my_events = my_events;
        requestQueue = Volley.newRequestQueue(context);
    }

    public My_event_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_my_events, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Constant_events festival = my_events.get(position);
        holder.festival_name.setText(festival.getFestival_name());
        holder.date.setText(festival.getDate());

        String date = parseDateToddMMyyyy(festival.getDate());

        holder.date.setText(date.substring(0, date.indexOf("-")).toString());
        holder.Month.setText(date.substring(date.indexOf("-") + 1, date.length()));

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(context,Update.class);
                i.putExtra("reminder_id",festival.getId());
                i.putExtra("reminder_category",festival.getCategory());
                Log.d("reminder_cat_adapter",festival.getCategory());
                i.putExtra("reminder_title",festival.getFestival_name());
                i.putExtra("reminder_date",festival.getDate());
                i.putExtra("reminder_time",festival.getTime());
                context.startActivity(i);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mac_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                System.out.println("android_id=" + mac_id);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setMessage(context.getResources().getString(R.string.delete));
                alertDialog.setPositiveButton(context.getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(position, festival.getId());
                    }
                });
                alertDialog.setNegativeButton(context.getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        holder.event_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (festival.getCategory().equalsIgnoreCase("Birthday")){
                    i = new Intent(context, Set_Images.class);
                    i.putExtra("festival","birthday");
                    Log.d("birthday",festival.getCategory());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);

                }else if (festival.getCategory().equalsIgnoreCase("Love")){
                    i = new Intent(context, Set_Images.class);
                    Log.d("loveu",festival.getCategory());
                    i.putExtra("festival","loveu");
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);

                }else if (festival.getCategory().equalsIgnoreCase("Anniversary")){
                    i = new Intent(context, Set_Images.class);
                    i.putExtra("festival","anniversary");
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);

                }else if (festival.getCategory().equalsIgnoreCase("Good Morning")){
                    i = new Intent(context, Set_Images.class);
                    i.putExtra("festival","goodmorning");
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);

                }else if (festival.getCategory().equalsIgnoreCase("Good Night")){
                    i = new Intent(context, Set_Images.class);
                    i.putExtra("festival","goodnight");
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);

                }else if (festival.getCategory().equalsIgnoreCase("Congratulations")){
                    i = new Intent(context, Set_Images.class);
                    i.putExtra("festival","congratulation");
                    Log.d("congratulation",festival.getCategory());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("my_events", String.valueOf(my_events.size()));
        return my_events.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView festival_name,date,Month;
        ImageView update,delete;
        RelativeLayout event_id;
        Typeface tf;


        public MyViewHolder(View itemView) {
            super(itemView);

            festival_name = (TextView) itemView.findViewById(R.id.main_name);
            date = (TextView) itemView.findViewById(R.id.date);
            Month = (TextView) itemView.findViewById(R.id.date1);
            update = (ImageView)itemView.findViewById(R.id.update);
            delete = (ImageView)itemView.findViewById(R.id.delete);
            event_id = (RelativeLayout)itemView.findViewById(R.id.event_id);

            tf = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/swift_lt.ttf");
            festival_name.setTypeface(tf,Typeface.BOLD);

            tf = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/swift_rm.ttf");
            date.setTypeface(tf,Typeface.BOLD);
            Month.setTypeface(tf,Typeface.BOLD);

        }
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "dd-MM-yyyy";
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

    public void delete(int position, String id){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        StringRequest request = new StringRequest(Request.Method.GET,
                "https://www.kumbhdesign.in/mobile-app/depost/api/reminder/delete/" + mac_id + "/" + id ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("response");
                            JSONObject object1 = array.getJSONObject(0);
                            String error = object1.getString("error");

                            if (error.equals("0")){
                                my_events.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, my_events.size());
                                Toast.makeText(context,object1.getString("message"),Toast.LENGTH_LONG).show();
                            }else if (error.equals("1")){
                                Toast.makeText(context,object1.getString("message"),Toast.LENGTH_LONG).show();
                            }else if (error.equals("2")){
                                Toast.makeText(context,object1.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Toast.makeText(My_event_Adapter.this.context,String.valueOf(error),Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
    }
}
