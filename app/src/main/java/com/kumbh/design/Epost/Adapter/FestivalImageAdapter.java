package com.kumbh.design.Epost.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.droidninja.imageeditengine.ImageEditor;
import com.droidninja.imageeditengine.model.UserData;
import com.droidninja.imageeditengine.views.PhotoEditorView;
import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.model.ListPostItemFestival;
import com.kumbh.design.Epost.model.ListTemplateItem;
import com.kumbh.design.Epost.util.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.LAYOUT_DIRECTION_RTL;
import static android.view.View.VISIBLE;
import static com.droidninja.imageeditengine.views.PhotoEditorView.progressBar;

public class FestivalImageAdapter extends RecyclerView.Adapter<FestivalImageAdapter.MyViewHolder> {

    private Context context;
    private List<ListTemplateItem> images;
    private LayoutInflater inflater;
    private String festival, id;
    private SessionManager pref_manager;
    private UserData user;

    public FestivalImageAdapter(Context context, List<ListTemplateItem> images, String Fest, String id) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
        festival = Fest;
        id = id;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        pref_manager = new SessionManager(context);
        View v = inflater.inflate(R.layout.fesrival_grid, null, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.animationView.setVisibility(View.VISIBLE);

        Picasso.get().load(images.get(position).getTemplateDemoImagePath()).into(holder.imageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                holder.animationView.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
            }
        });


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap returnedBitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(returnedBitmap);

                v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                v.draw(canvas);
                new ImageEditor.Builder(((Activity) context), images.get(position).getTemplateImagePath(), festival, pref_manager.getName()).setStickerAssets("stickers").festivalCall(images.get(position).getTemplateImagePath(), images.get(position).getPostId(), images.get(position).getTemplateId());

            }
        });
        final View rootViewImage = inflater.inflate(com.droidninja.imageeditengine.R.layout.img_layout, null);
        ImageView logoImage = rootViewImage.findViewById(com.droidninja.imageeditengine.R.id.imgPhotoEditorImage);
        FrameLayout.LayoutParams layoutParamsImage = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
       layoutParamsImage.gravity=Gravity.START;
       layoutParamsImage.setMargins(10,8,0,0);
        final View rootViewText = inflater.inflate(com.droidninja.imageeditengine.R.layout.view_text_layout, null);
        TextView txtEmail = rootViewText.findViewById(com.droidninja.imageeditengine.R.id.tvPhotoEditorText);
        txtEmail.setTextSize(5);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        String userId = pref_manager.getUserId();

        //  Log.d("DATA_URL","https://api.qwant.com/api/search/images?count=50&q="+festival+"+backgrounds&t=images&safesearch=1&locale=en_US&uiv=4");
//        pb_setimage.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, "https://www.kumbhdesign.in/mobile-app/depost/api/profile-update/" + userId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
//                   pb_setimage.setVisibility(View.GONE);

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("user_data");
                    int error = jsonObject2.getInt("error");
                    if (error == 0) {

                        JSONObject userData = jsonObject2.getJSONObject("user");
                        user = new UserData(userData.getString("facebook_url"), userData.getString("company_email"), userData.getString("company_logo_path"), userData.getString("website_url"), userData.getString("instagram_url"), userData.getString("linkedin_url"), userData.getString("mobile_number"), userData.getString("company_address"), userData.getString("twitter_url"));
                        Log.v("userData", user.getCompanyEmail());
                        layoutParams.gravity = Gravity.END;
                        layoutParams.setMargins(0, 20, 0, 0);
                        txtEmail.setText(user.getCompanyEmail());
                        Picasso.get().load(user.getCompany_logo_path()).into(logoImage);

                    }

//                    photoEditorView.addImage(null,user.getCompany_logo_path());


                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

//                        pb_setimage.setVisibility(View.GONE);
                        Log.d("Error", error.toString());

                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
        holder.relativeLayout.addView(rootViewText, layoutParams);
        holder.relativeLayout.addView(rootViewImage, layoutParamsImage);

//       holder.img_test.setImageDrawable(context.getResources().getDrawable(R.drawable.logo_cicle));


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, img_test;
        FrameLayout relativeLayout;
        LottieAnimationView animationView;


        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image1);
            relativeLayout = itemView.findViewById(R.id.grid_image);
            animationView = itemView.findViewById(R.id.animation_view);
            img_test = itemView.findViewById(R.id.img_set);

        }
    }


}
