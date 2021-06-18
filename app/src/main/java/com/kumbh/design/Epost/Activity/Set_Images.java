package com.kumbh.design.Epost.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.droidninja.imageeditengine.ImageEditor;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.util.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kumbh.design.Epost.Activity.HomrPage.signOut;

public class Set_Images extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    ImageView choose_color;
    Animation animation, animation1;
    HorizontalScrollView set_color;
    public String festival, image_list, id;
    ProgressBar pb_setimage;
    int flag = 0;
    SessionManager shared_pr;
    CircleImageView profile;
    CircleImageView black, blue, brown, gray, green, orange, pink, purple, red, teal, yellow;
    View v;
    String country;
    private RecyclerView gridView;
    private ArrayList<String> images;
    FloatingActionButton camera, back_color1, floating_camera;
    Uri tempUri;
    Bitmap b;
    Uri imageUri;
    InterstitialAd mInterstitialAd;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__images);

        mInterstitialAd = new InterstitialAd(Set_Images.this);

        checkConnection();

//        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6008474329722648~3510787218");
//
//        // set the ad unit ID
//        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//
//        // Load ads into Interstitial Ads
//        mInterstitialAd.loadAd(adRequest);
//
//        mInterstitialAd.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                showInterstitial();
//            }
//        });

        pb_setimage = (ProgressBar) findViewById(R.id.pb_setimage);

        black = (CircleImageView) findViewById(R.id.black);
        blue = (CircleImageView) findViewById(R.id.blue);
        brown = (CircleImageView) findViewById(R.id.brown);
        gray = (CircleImageView) findViewById(R.id.gray);
        green = (CircleImageView) findViewById(R.id.green);
        orange = (CircleImageView) findViewById(R.id.orange);
        pink = (CircleImageView) findViewById(R.id.pink);
        purple = (CircleImageView) findViewById(R.id.purple);
        red = (CircleImageView) findViewById(R.id.red);
        teal = (CircleImageView) findViewById(R.id.teal);
        yellow = (CircleImageView) findViewById(R.id.yellow);

        camera = (FloatingActionButton) findViewById(R.id.camera);
        back_color1 = (FloatingActionButton) findViewById(R.id.back_color1);
        floating_camera = (FloatingActionButton) findViewById(R.id.floating_camera);
        camera.setOnClickListener(this);

        black.setOnClickListener(this);
        blue.setOnClickListener(this);
        brown.setOnClickListener(this);
        gray.setOnClickListener(this);
        green.setOnClickListener(this);
        orange.setOnClickListener(this);
        pink.setOnClickListener(this);
        purple.setOnClickListener(this);
        red.setOnClickListener(this);
        teal.setOnClickListener(this);
        yellow.setOnClickListener(this);

        choose_color = (ImageView) findViewById(R.id.choose_color);
        set_color = (HorizontalScrollView) findViewById(R.id.horizontal_scr);

        gridView = (RecyclerView) findViewById(R.id.gridView);
        LinearLayoutManager manager = new LinearLayoutManager(Set_Images.this);
        gridView.setLayoutManager(manager);
        final float density = getResources().getDisplayMetrics().density;


        Intent intent = getIntent();
        festival = intent.getStringExtra("festival");
        image_list = intent.getStringExtra("image_list");
        id = intent.getStringExtra("id");
        country = intent.getStringExtra("country");

        images = new ArrayList<>();

        shared_pr = new SessionManager(getApplicationContext());
        profile = (CircleImageView) findViewById(R.id.profile);
//        Log.d("photo", shared_pr.getPhoto());
     /*   if(!shared_pr.getPhoto().equalsIgnoreCase("null"))
            Picasso.get().load(shared_pr.getPhoto()).into(profile);
        else
            Picasso.get().load(R.drawable.avatar).into(profile);*/


        if (!"null".equalsIgnoreCase(shared_pr.getPhoto())) {
            Picasso.get().load(shared_pr.getPhoto()).into(profile);
        } else {
            Picasso.get().load(R.drawable.avatar).into(profile);
        }

        profile.setOnClickListener(this);
        floating_camera.setOnClickListener(this);

//        Picasso.get().load(image_list).placeholder(R.drawable.preview).into(back_image);

        animation = AnimationUtils.loadAnimation(this, R.anim.move);
        animation1 = AnimationUtils.loadAnimation(this, R.anim.rotate);

        choose_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    choose_color.animate().rotation(180).start();
                    set_color.setVisibility(View.VISIBLE);
                    flag = 1;
                } else {
                    choose_color.animate().rotation(360).start();
                    set_color.setVisibility(View.GONE);
                    flag = 0;
                }
            }
        });
        getData();
        back_color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Set_Images.this, back_color.class);
                i.putExtra("festival", festival);
                i.putExtra("image_list", image_list);
                i.putExtra("id", id);
                startActivity(i);
                finish();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                new ImageEditor.Builder(this, picturePath, festival, shared_pr.getName()).setStickerAssets("stickers").open();
                cursor.close();
            } else if (requestCode == 0) {
                try {
                    try {
                        if (Build.VERSION.SDK_INT <= 17) {
                            b = BitmapFactory.decodeFile(imageUri.getPath());
                        } else {
                            b = MediaStore.Images.Media.getBitmap(Set_Images.this.getContentResolver(), imageUri);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //b = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    Uri tempUri = getImageUri(Set_Images.this, b);
                    Log.d("imageUri", String.valueOf(tempUri));
                    String path = getRealPathFromURI(tempUri);
                    new ImageEditor.Builder(this, path, festival, shared_pr.getName()).setStickerAssets("stickers").open();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void getData() {
        //  Log.d("DATA_URL","https://api.qwant.com/api/search/images?count=50&q="+festival+"+backgrounds&t=images&safesearch=1&locale=en_US&uiv=4");
        pb_setimage.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, "https://api.qwant.com/api/search/images?count=50&q=" + festival + "+backgrounds&t=images&safesearch=1&locale=en_US&uiv=4", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    pb_setimage.setVisibility(View.GONE);

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("result");

                    JSONArray jsonArray = jsonObject2.getJSONArray("items");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                        String link = jsonObject3.getString("media");
                        Log.d("media", link);
                        images.add(link);
                    }
                    image_list = jsonArray.getJSONObject(0).getString("media");
//                    Picasso.get().load(jsonArray.getJSONObject(0).getString("link")).placeholder(R.drawable.preview).into(back_image);
                    LinearLayoutManager llm = new LinearLayoutManager(Set_Images.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    gridView.setLayoutManager(llm);
                    GridViewAdapter gridViewAdapter = new GridViewAdapter(Set_Images.this, images, festival, id);
                    gridView.setAdapter(gridViewAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pb_setimage.setVisibility(View.GONE);
                        Log.d("Error", error.toString());
                        // Toast.makeText(Set_Images.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Set_Images.this, HomrPage.class);
        i.putExtra("id", id);
        startActivity(i);
        finish();
    }

    private void get_images(String color_name) {
        Log.d("DATA_URL", "https://api.qwant.com/api/search/images?count=50&q=" + festival + "%20backgrounds&t==images&safesearch=1&locale=en_US&uiv=4&color=" + color_name);
        pb_setimage.setVisibility(View.VISIBLE);
        images.clear();
        StringRequest request = new StringRequest(Request.Method.GET, "https://api.qwant.com/api/search/images?count=50&q=" + festival + "+backgrounds&t==images&safesearch=1&locale=en_US&uiv=4&color=" + color_name, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    pb_setimage.setVisibility(View.GONE);

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("result");

                    JSONArray jsonArray = jsonObject2.getJSONArray("items");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                        String link = jsonObject3.getString("media");
                        Log.d("media", link);
                        images.add(link);
                    }
                    image_list = jsonArray.getJSONObject(0).getString("media");
//                    Picasso.get().load(jsonArray.getJSONObject(0).getString("link")).placeholder(R.drawable.preview).into(back_image);

                    GridViewAdapter gridViewAdapter = new GridViewAdapter(Set_Images.this, images, festival, id);
                    if (gridView != null) {
                        gridView.setAdapter(gridViewAdapter);
                    } else {
                        //Toast.makeText(Set_Images.this,"gridView is null",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pb_setimage.setVisibility(View.GONE);
                        // Toast.makeText(Set_Images.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.camera:
                startGallery();
                break;
            case R.id.floating_camera:
                startCamera();
                break;
            case R.id.black:
                choose_color.animate().rotation(360).start();
                flag = 0;
                get_images("black");
                set_color.setVisibility(View.GONE);
                break;
            case R.id.blue:
                choose_color.animate().rotation(360).start();
                flag = 0;
                get_images("blue");
                set_color.setVisibility(View.GONE);
                break;
            case R.id.brown:
                choose_color.animate().rotation(360).start();
                flag = 0;
                set_color.setVisibility(View.GONE);
                get_images("brown");
                break;
            case R.id.gray:
                choose_color.animate().rotation(360).start();
                flag = 0;
                get_images("gray");
                set_color.setVisibility(View.GONE);
                break;
            case R.id.green:
                choose_color.animate().rotation(360).start();
                set_color.setVisibility(View.GONE);
                flag = 0;
                get_images("green");
                break;
            case R.id.orange:
                choose_color.animate().rotation(360).start();
                flag = 0;
                set_color.setVisibility(View.GONE);
                get_images("orange");
                break;
            case R.id.pink:
                choose_color.animate().rotation(360).start();
                flag = 0;
                get_images("pink");
                set_color.setVisibility(View.GONE);
                break;
            case R.id.purple:
                choose_color.animate().rotation(360).start();
                flag = 0;
                get_images("purple");
                set_color.setVisibility(View.GONE);
                break;
            case R.id.red:
                choose_color.animate().rotation(360).start();
                flag = 0;
                get_images("red");
                set_color.setVisibility(View.GONE);
                break;
            case R.id.teal:
                choose_color.animate().rotation(360).start();
                flag = 0;
                get_images("teal");
                set_color.setVisibility(View.GONE);
                break;
            case R.id.yellow:
                choose_color.animate().rotation(360).start();
                flag = 0;
                get_images("yellow");
                set_color.setVisibility(View.GONE);
                break;
            case R.id.profile:
                showFilterPopup(v);
                break;
        }
    }

    private void showFilterPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuLogout:
                        signOut(Set_Images.this);
                        break;

                    case R.id.profile:
                        Intent i = new Intent(getApplicationContext(), Profile.class);
                        startActivity(i);
                        break;
                    case R.id.menycurrentplan:
                        Log.v("print","sdhg");
                        Intent intent = new Intent(getApplicationContext(), currentPlanActivity.class);
                        startActivity(intent);
                        break;
//                    case R.id.details:
//                        Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
//                        startActivity(intent);
//                        break;

                    default:
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public void startCamera() {
        if (ActivityCompat.checkSelfPermission(Set_Images.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Set_Images.this, new String[]{Manifest.permission.CAMERA}, 0);
        } else {
            if (Build.VERSION.SDK_INT <= 17) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //outPutfileUri = Uri.fromFile(file1);
                imageUri = getOutputMediaFileUri(1);

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, 0);
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file1 = new File(Environment.getExternalStorageDirectory(),
                        "MyPhoto.jpg");
                imageUri = FileProvider.getUriForFile(Set_Images.this,
                        getPackageName() + ".provider",
                        file1);
                //outPutfileUri = Uri.fromFile(file1);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, 0);

            }
        }

    }


    public void startGallery() {
        if (ActivityCompat.checkSelfPermission(Set_Images.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Set_Images.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "camera");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("camera", "Oops! Failed create "
                        + "camera" + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");


        return mediaFile;
    }

    public boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected(this);
        showSnack(isConnected);
        return isConnected;
    }

    public void showSnack(boolean isConnected) {
        String message;
        if (isConnected) {

        } else {
            message = "Check your Internet connection.";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppController.getInstance().setConnectivityListener(this);

    }

    @Override
    public void onTokenRefresh() {

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
