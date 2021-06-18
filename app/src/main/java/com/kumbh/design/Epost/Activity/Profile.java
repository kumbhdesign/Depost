package com.kumbh.design.Epost.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.droidninja.imageeditengine.model.UserData;
import com.kumbh.design.Epost.FileUploadService;
import com.kumbh.design.Epost.R;
import com.kumbh.design.Epost.ServiceGenerator;
import com.kumbh.design.Epost.util.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Profile extends AppCompatActivity {

    private boolean valid;
    EditText et_email, et_mob, et_website, et_fb, et_insta, et_twitter, et_address,et_linkedin;
    ImageView img_edit, account_back;
    Button bt_submit;
    Bitmap bitmap;
    Uri imageUri;
    private Uri tempUri;
    CircleImageView profile;
    private String path;
    static SessionManager shared_pr;
    TextView text, txt_name, pr_mail_id, company_email, com_mob, com_address, com_website, con_facebook, com_twitter, com_insta, txt_logo,com_linkedin;
    ImageView back, img_edit_logo;
    LinearLayout li_website, li_facebook, li_twitter, li_insta, li_email, li_address, li_mobno, li_profile, li_text,li_linkedin;
    Typeface tf;
    private UserData user;
    ImageView img1, img2, img3, img4, img5, img6;
    String websiteUrl = "";
    ProgressDialog pb_festival;
    String instagram = "",twitterText;
    String facebook = "";
    String linkedin = "";
    String comapanyAddress = "";
    private String filename;
    private MultipartBody.Part body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pb_festival = new ProgressDialog(this);

        et_email = findViewById(R.id.et_email);
        li_profile = findViewById(R.id.profile_edit);
        li_text = findViewById(R.id.li_text);
        et_fb = findViewById(R.id.et_fb_url);
        txt_logo = findViewById(R.id.txt_logo);
        et_address = findViewById(R.id.et_address);
        et_website = findViewById(R.id.et_website_url);
        et_linkedin=findViewById(R.id.et_linkedin_link);
        et_mob = findViewById(R.id.et_mobno);
        img_edit_logo = findViewById(R.id.img_edit_logo);
        et_insta = findViewById(R.id.et_insta_link);
        et_twitter = findViewById(R.id.et_twitter_link);
        bt_submit = findViewById(R.id.bt_submit);
        img_edit = findViewById(R.id.img_edit);
//        my_logo = findViewById(R.id.my_logo);
        account_back = findViewById(R.id.account_back);
        shared_pr = new SessionManager(getApplicationContext());
        profile = (CircleImageView) findViewById(R.id.my_profilee);
        txt_name = (TextView) findViewById(R.id.txt_name);
        text = (TextView) findViewById(R.id.text);
        pr_mail_id = (TextView) findViewById(R.id.pr_mail_id);
        back = (ImageView) findViewById(R.id.back);
        company_email = findViewById(R.id.profile_com_value);
        com_address = findViewById(R.id.profile_com_adress_value);
        com_mob = findViewById(R.id.profile_com_mobile_value);
        com_website = findViewById(R.id.profile_com_website_value);
        con_facebook = findViewById(R.id.profile_com_facebook_value);
        com_twitter = findViewById(R.id.profile_com_twitter_value);
        com_insta = findViewById(R.id.profile_com_insta_value);
        com_linkedin=findViewById(R.id.profile_com_linkedin_value);
        img1 = findViewById(R.id.img_edit);
        img2 = findViewById(R.id.img_edit_address);
        img3 = findViewById(R.id.img_edit_facebook);
        img4 = findViewById(R.id.img_edit_mobile);
        img5 = findViewById(R.id.img_edit_insta);
        img6 = findViewById(R.id.img_edit_website);
        li_website = findViewById(R.id.rel_comapny_website);
        li_facebook = findViewById(R.id.rel_comapny_facebook);
        li_twitter = findViewById(R.id.rel_comapny_twitter);
        li_insta = findViewById(R.id.rel_comapny_insta);
        li_address = findViewById(R.id.rel_company_address);
        li_mobno = findViewById(R.id.rel_comapny_mobile);
        li_email = findViewById(R.id.li_comapany_name);
        li_linkedin=findViewById(R.id.rel_comapny_linkedin);

        tf = Typeface.createFromAsset(getAssets(), "fonts/swift_lt.ttf");
        text.setTypeface(tf, Typeface.BOLD);

        shared_pr = new SessionManager(getApplicationContext());


        Log.d("Email", shared_pr.getEmail());
        Log.d("Name", shared_pr.getName());

        txt_name.setText(shared_pr.getName());
        pr_mail_id.setText(shared_pr.getEmail());
        li_profile.setVisibility(View.VISIBLE);
        getData();
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user != null && user.getCompany_logo_path() != null && user.getCompany_logo_path().compareTo("") != 0) {
                    Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
                    startActivity(intent);
                } else {
                    selectImage();
                }

            }
        });
        img_edit_logo.setVisibility(View.VISIBLE);
        img_edit_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null && user.getCompany_logo_path() != null && user.getCompany_logo_path().compareTo("") != 0) {
                    Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
                    startActivity(intent);
                } else {
                    selectImage();
                }
            }
        });
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
                startActivity(intent);
            }
        });
        txt_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (user != null && user.getCompany_logo_path() != null && user.getCompany_logo_path().compareTo("") != 0) {
//                    Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
//                    startActivity(intent);
//                } else {
//                    selectImage();
//                }
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
                startActivity(intent);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
                startActivity(intent);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
                startActivity(intent);
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
                startActivity(intent);
            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileDetails.class);
                startActivity(intent);
            }
        });


        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInput();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void validateInput() {
        websiteUrl = et_website.getText().toString();
        instagram = et_insta.getText().toString();
        twitterText=et_twitter.getText().toString();

        facebook = et_fb.getText().toString();
        linkedin = et_twitter.getText().toString();
        comapanyAddress = et_address.getText().toString();

        if (et_email.getText().toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches()) {
            et_email.setError("enter a valid email address");
            valid = false;
        } else if (et_mob.getText().toString().isEmpty() || et_mob.getText().toString().length() != 10) {
            et_mob.setError("Please enter a valid mobile number");
            valid = false;
        } else if (comapanyAddress == null || comapanyAddress.length() == 0) {
            et_address.setError("Please enter address");
        } else if (path == null || path.length() == 0) {
            if (user.getCompany_logo_path() != null && user.getCompany_logo_path().trim().length() > 0 && user.getCompany_logo_path().compareTo("null") != 0) {
//                String path= shared_pr.getLogoPhoto();
//
//                filename=path.substring(path.lastIndexOf("/")+1);
                uploadFile(null);
            } else {
                Toast.makeText(Profile.this, "Please select logo image", Toast.LENGTH_LONG).show();


            }
        }
//        else if (et_website.getText().toString()!=null && et_website.getText().toString().length() > 0) {
//            websiteUrl = "";
//            valid = false;
//        } else if (et_fb.getText().toString().isEmpty()) {
//            et_fb.setError("Please enter a Facebook link");
//            valid = false;
//        } else if (et_insta.getText().toString().isEmpty()) {
//            et_insta.setError("Please enter a Instagram link");
//            valid = false;
//        } else if (et_link.getText().toString().isEmpty()) {
//            et_link.setError("Please enter a Linkedin link");
//            valid = false;
//        }

        else {
            uploadFile(path);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                try {
                    try {
                        if (Build.VERSION.SDK_INT <= 17) {
                            bitmap = BitmapFactory.decodeFile(imageUri.getPath());
                        } else {
                            bitmap = MediaStore.Images.Media.getBitmap(Profile.this.getContentResolver(), imageUri);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    tempUri = getImageUri(Profile.this, bitmap);
                    Log.d("imageUri", String.valueOf(tempUri));
                    path = getRealPathFromURI(tempUri);
                    shared_pr.setLogoPhoto(path);
                    if (user.getCompany_logo_path() != null && user.getCompany_logo_path().trim().length() > 0 && user.getCompany_logo_path().compareTo("null") != 0) {
                        filename = user.getCompany_logo_path().substring(user.getCompany_logo_path().lastIndexOf("/") + 1);
                    } else {
                        filename = path.substring(path.lastIndexOf("/") + 1);
                    }
                    Bitmap thumbnail = (BitmapFactory.decodeFile(path));
                    Log.w("image", path + "");
                    profile.setImageBitmap(thumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                path = c.getString(columnIndex);
                shared_pr.setLogoPhoto(path);
                if (user.getCompany_logo_path() != null && user.getCompany_logo_path().trim().length() > 0 && user.getCompany_logo_path().compareTo("null") != 0) {
                    filename = user.getCompany_logo_path().substring(user.getCompany_logo_path().lastIndexOf("/") + 1);
                } else {
                    filename = path.substring(path.lastIndexOf("/") + 1);
                }
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(path));
                Log.w("image", path + "");
                profile.setImageBitmap(thumbnail);
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

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    if (ActivityCompat.checkSelfPermission(Profile.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(Profile.this, new String[]{Manifest.permission.CAMERA}, 0);
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
                            imageUri = FileProvider.getUriForFile(Profile.this,
                                    getPackageName() + ".provider",
                                    file1);
                            //outPutfileUri = Uri.fromFile(file1);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(cameraIntent, 0);

                        }
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

    public void show_dialog(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    pb_festival.setMessage(msg);
                    pb_festival.show();
                }
            }
        });
    }

    public void hide_dialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    if (pb_festival != null) {
                        pb_festival.dismiss();
                    }
                }
            }
        });
    }

    private void uploadFile(String fileUri) {
        show_dialog("Please Wait");
        // create upload service client
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        if (fileUri != null) {


            // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
            // use the FileUtils to get the actual file by uri
            File file = new File(fileUri);

            // create RequestBody instance from file
            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"),
                            file
                    );

            // MultipartBody.Part is used to send also the actual file name
            body =
                    MultipartBody.Part.createFormData("company_logo", file.getName(), requestFile);

            // add another part within the multipart request
            String descriptionString = "hello, this is description speaking";
            RequestBody description =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, descriptionString);
        } else {
            if (user.getCompany_logo_path() != null && user.getCompany_logo_path().trim().length() > 0 && user.getCompany_logo_path().compareTo("null") != 0) {
                filename = user.getCompany_logo_path().substring(user.getCompany_logo_path().lastIndexOf("/") + 1);
            }
        }

        RequestBody emailRequest = RequestBody.create(MediaType.parse("text/plain"), et_email.getText().toString());
        RequestBody mobileRequest = RequestBody.create(MediaType.parse("text/plain"), et_mob.getText().toString());
        RequestBody websiteRequest = RequestBody.create(MediaType.parse("text/plain"), websiteUrl);
        RequestBody facebookdRequest = RequestBody.create(MediaType.parse("text/plain"), facebook);
        RequestBody linkedinRequest = RequestBody.create(MediaType.parse("text/plain"), linkedin);
        RequestBody instadRequest = RequestBody.create(MediaType.parse("text/plain"), instagram);
        RequestBody twitterRequest = RequestBody.create(MediaType.parse("text/plain"), twitterText);
        RequestBody logohidden = RequestBody.create(MediaType.parse("text/plain"), filename);
        RequestBody companyAddress = RequestBody.create(MediaType.parse("text/plain"), comapanyAddress);

        String userId = shared_pr.getUserId();


        // finally, execute the request
        Call<ResponseBody> call = service.upload(userId, body, shared_pr.getUserId(), emailRequest, mobileRequest, websiteRequest, facebookdRequest, instadRequest, linkedinRequest, twitterRequest,logohidden, companyAddress);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hide_dialog();
                JSONObject object = null;
                String message = "";
//                try {
//                    object = new JSONObject("response");
//                    JSONObject userData = object.getJSONObject("user_data");
//                     message = userData.getString("message");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                Toast.makeText(Profile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Profile.this, HomrPage.class);
                startActivity(intent);
                finish();


                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hide_dialog();
                Toast.makeText(Profile.this, "Upload error....", Toast.LENGTH_LONG).show();
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    private void getData() {
        String userId = shared_pr.getUserId();

        //  Log.d("DATA_URL","https://api.qwant.com/api/search/images?count=50&q="+festival+"+backgrounds&t=images&safesearch=1&locale=en_US&uiv=4");
        show_dialog("Please Wait");
        StringRequest request = new StringRequest(Request.Method.GET, "https://www.kumbhdesign.in/mobile-app/depost/api/profile-update/" + userId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
//                    pb_setimage.setVisibility(View.GONE);

                    hide_dialog();
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


                    }

                    if(user!=null)
                    {
                        if (user.getCompany_logo_path() != null && user.getCompany_logo_path().trim().length() > 0 && user.getCompany_logo_path().compareTo("null") != 0) {
                            Picasso.get().load(user.getCompany_logo_path()).placeholder(R.drawable.logo_cicle).error(R.drawable.logo_cicle).into(profile);
                        }
                        if (user.getCompanyEmail() != null && user.getCompanyEmail().trim().length() > 0 && user.getCompanyEmail().compareTo("null") != 0) {
                            li_text.setVisibility(View.VISIBLE);
                            li_email.setVisibility(View.VISIBLE);
                            li_profile.setVisibility(View.GONE);


                            company_email.setText(user.getCompanyEmail());
                        }
                        if (user.getAddress() != null && user.getAddress().trim().length() > 0 && user.getAddress().compareTo("null") != 0) {
                            li_address.setVisibility(View.VISIBLE);
                            com_address.setText(user.getAddress());
                        }
                        if (user.getMobileNumber() != null && user.getMobileNumber().trim().length() > 0 && user.getMobileNumber().compareTo("null") != 0) {
                            li_mobno.setVisibility(View.VISIBLE);
                            com_mob.setText(user.getMobileNumber());
                        }


                        if (user.getWebsiteUrl() != null && user.getWebsiteUrl().trim().length() > 0 && user.getWebsiteUrl().compareTo("null") != 0) {
                            li_website.setVisibility(View.VISIBLE);
                            com_website.setText(user.getWebsiteUrl());
                        }
                        if (user.getFacebookUrl() != null && user.getFacebookUrl().trim().length() > 0 && user.getFacebookUrl().compareTo("null") != 0) {
                            li_facebook.setVisibility(View.VISIBLE);
                            con_facebook.setText(user.getFacebookUrl());
                        }
                        if (user.getTwitter_url() != null && user.getTwitter_url().trim().length() > 0 && user.getTwitter_url().compareTo("null") != 0) {
                            li_twitter.setVisibility(View.VISIBLE);
                            com_twitter.setText(user.getTwitter_url());
                        }
                        if (user.getInstagramUrl() != null && user.getInstagramUrl().trim().length() > 0 && user.getInstagramUrl().compareTo("null") != 0) {
                            li_insta.setVisibility(View.VISIBLE);
                            com_insta.setText(user.getInstagramUrl());
                        }


                        if (user.getLinkedinUrl() != null && user.getLinkedinUrl().trim().length() > 0 && user.getLinkedinUrl().compareTo("null") != 0) {
                            li_linkedin.setVisibility(View.VISIBLE);
                            com_linkedin.setText(user.getLinkedinUrl());
                        }
                    }



                } catch (JSONException e) {
                    hide_dialog();
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pb_setimage.setVisibility(View.GONE);
                        Log.d("Error", error.toString());
                        Toast.makeText(Profile.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
        requestQueue.add(request);
    }
}
