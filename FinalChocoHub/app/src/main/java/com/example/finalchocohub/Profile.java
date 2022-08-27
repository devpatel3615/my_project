package com.example.finalchocohub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.shadowfax.proswipebutton.ProSwipeButton;

public class Profile<Databasehelper> extends AppCompatActivity {
    TabLayout tb;
    String employee_id,employee_name,employee_address,employee_phone,employee_email,employee_password;
    public static String Tag_Array = "customer";
    public static String Tag_emp_id = "cid";
    public static String Tag_emp_name = "cname";
    public static String Tag_emp_address = "caddress";
    public static String Tag_emp_email = "cemail";
    public static String Tag_emp_phone = "cphone";
    public static String Tag_emp_password = "cpassword";
    String c_img=null;
    String fileName="";
    int serverResponseCode = 0;
    private CircleImageView ProfileImage;
    ImageView iprofile;
    String  cid=null;
    JSONArray array=null;
    AlertDialog.Builder alert = null;

    TextView tvname,tvaddress,tvphone,tvemail,tvpassword,changPassWord,txt,txt1,txt2;
    ImageView text_man;

    Button update;
    Databasehelper db = null;
    SharedPreferences ses = null;
    //    String update_profile_url="https://valentinachoco.000webhostapp.com/valentina/Update_Employee.php";
    String url = "https://valentinachoco.000webhostapp.com/valentina/view_Employee.php";
    String finalUploadURL="https://valentinachoco.000webhostapp.com/valentina/finalfileupload.php";
    JSONParser jsonp = new JSONParser();
    ArrayList<HashMap<String, String>> elist = null;
    int cnt = 0;
    ProgressDialog pDialog;
    String empid=null;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ses = getSharedPreferences(login.sessionLogin, MODE_PRIVATE);
        empid = ses.getString("empid", null);
        //text_man=(ImageView) findViewById(R.id.text_man);
        tvname=(TextView) findViewById(R.id.tvename);
        tvaddress=(TextView) findViewById(R.id.tvaddress);
        tvphone=(TextView) findViewById(R.id.tvphone);
        tvemail=(TextView) findViewById(R.id.tvemail);
        changPassWord=(TextView) findViewById(R.id.changPassWord);
        //tvpassword=(TextView) findViewById(R.id.tvpassword);
        txt=(TextView)findViewById(R.id.txt);
        //txt1=(TextView)findViewById(R.id.txt1);
        //txt2=(TextView)findViewById(R.id.txt2);
        cid = ses.getString("cid", null);
        update=(Button) findViewById(R.id.btnupdate);
        ProfileImage =(CircleImageView) findViewById(R.id.profile_image);
        iprofile=findViewById(R.id.imagebutton);
        iprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent gallary=new Intent();
                gallary.setType("image/*");
                gallary.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallary,"Select Image"),PICK_IMAGE);*/
                selectImage();
            }
        });
        new getProfile().execute();
    }
    private void selectImage()
    {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Log.e("Result Code",requestCode+"");
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles())
                {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    // bitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, true);
                    ProfileImage.setImageBitmap(bitmap);

                    File path = android.os.Environment
                            .getExternalStorageDirectory();
                    f.delete();
                    OutputStream outFile = null;
                    final File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);

                        runOnUiThread(new Runnable() {
                            public void run() {
//                                 tv.setText("File Upload Completed.");
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                uploadFile(file.getAbsolutePath());
                                Toast.makeText(Profile.this, "File Upload Start.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                final String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.e("path of image", picturePath+"");

                ProfileImage.setImageBitmap(thumbnail);

                runOnUiThread(new Runnable() {
                    public void run() {
//                         tv.setText("File Upload Completed.");
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        uploadFile(picturePath);

                    }
                });

            }
        }
    }
    public int uploadFile(String sourceFileUri)
    {
        pDialog = ProgressDialog.show(Profile.this, "", "Uploading file...", true);
        Log.e("Reach Here","Reach");
        String upLoadServerUri = "https://unshunned-varactor.000webhostapp.com/safety/uploadprofileimage.php";
        fileName = sourceFileUri;

        Log.e("File Name is:-",fileName);
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File Does not exist");
            return 0;
        }
        try { // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upLoadServerUri);
            conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("enctype", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.e("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
            if(serverResponseCode == 200)
            {
                runOnUiThread(new Runnable() {
                    public void run()
                    {
//                           tv.setText("File Upload Completed.");
                        new FinalUploadClass().execute();
                    }
                    class FinalUploadClass extends AsyncTask<String,String,String>
                    {
                        @Override
                        protected String doInBackground(String... strings) {

                            List<NameValuePair> params = new ArrayList<>();
                            String tempfilename=fileName.substring(fileName.lastIndexOf("/")+1,fileName.length());
                            params.add(new BasicNameValuePair("cid", cid));
                            params.add(new BasicNameValuePair("filename",tempfilename));


                            JSONObject obj = jsonp.makeHttpRequest(finalUploadURL, "POST", params);
                            try {
                                if (obj.getInt("success") > 0) {
                                    cnt = 1;
                                } else {
                                    cnt = 0;
                                }
                            } catch (Exception e) {
                                Log.e("Ex is:-", e + "");
                            }
                            return null;

                        }


                        protected void onPostExecute(String file_url) {

                            if (cnt == 1) {
                                Toast.makeText(Profile.this, "Image Upload Sucessfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Profile.this, "Sorry Image is not Uploaded", Toast.LENGTH_SHORT).show();
                            }
                            pDialog.dismiss();
                        }
                    }
                });
            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex)
        {
            pDialog.dismiss();
            Log.e("Ex is:-",ex+"");
            ex.printStackTrace();
            Toast.makeText(Profile.this, "MalformedURLException", Toast.LENGTH_LONG).show();
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        }
        catch (Exception e1)
        {
            pDialog.dismiss();
            Log.e("Ex is:-",e1+"");
            Toast.makeText(Profile.this, "Exception : " + e1.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Upload Exception", "Exception : " + e1.getMessage(), e1);
        }
        pDialog.dismiss();
        return serverResponseCode;
    }

    class getProfile extends AsyncTask<String,String,String>
    {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Profile.this);
            pDialog.setMessage("Please wait Loading Profile....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings)
        {
            elist=new ArrayList<HashMap<String, String>>();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("cid",cid));
            JSONObject obj = jsonp.makeHttpRequest(url, "GET", params);
            try {
                Log.e("--->",url);
                if (obj.getInt("success")>0) {
                    cnt = 1;
                    array = obj.getJSONArray(Tag_Array);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        employee_id = o.getString(Tag_emp_id);
                        employee_name = o.getString(Tag_emp_name);
                        employee_address = o.getString(Tag_emp_address);
                        employee_phone = o.getString(Tag_emp_phone);
                        employee_email = o.getString(Tag_emp_email);
                        employee_password = o.getString(Tag_emp_password);
                        c_img=o.getString("cimage");


                    }
                } else {
                    cnt = 0;
                }
            }
            catch (Exception e)
            {
                Log.e("Ex-",e+"");
            }
            return null;
        }
        protected void onPostExecute(String file_url)
        {
            pDialog.dismiss();

            if(cnt==0)
            {
                Toast.makeText(Profile.this, "No Profile Found", Toast.LENGTH_SHORT).show();

            }

            else
            {
                tvname.setText(employee_name);
                tvaddress.setText(employee_address);
                tvphone.setText(employee_phone);
                tvemail.setText(employee_email);
//                tvpassword.setText(employee_password);
                if(c_img!=null && !c_img.equals("null"))
                {
                    Picasso.with(Profile.this).load("https://valentinachoco.000webhostapp.com/valentina/profileimage/"+c_img).into(ProfileImage);
                }
                else
                {
                    Picasso.with(Profile.this).load("https://valentinachoco.000webhostapp.com/valentina/profileimage/man.png").into(ProfileImage);
                }

            }
//            text_man.setTranslationY(300);
//            tvname.setTranslationY(300);
//            tvaddress.setTranslationY(300);
//            tvphone.setTranslationY(300);
//            tvemail.setTranslationY(300);
//            tvpassword.setTranslationY(300);
//            update.setTranslationY(300);
//            changPassWord.setTranslationY(300);
//            txt.setTranslationY(300);
//            txt1.setTranslationY(300);
//            txt2.setTranslationY(300);

//            text_man.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
//            tvname.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
//            tvaddress.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
//            tvphone.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
//            tvemail.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
//            tvpassword.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
//            update.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
//            changPassWord.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
//            txt.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
//            txt1.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
//            txt2.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(Profile.this,Update_profile.class);
                    startActivity(i);
                    finish();
                }
            });
//            update.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
//                @Override
//                public void onSwipeConfirm() {
//                    Intent i=new Intent(Profile.this,Update_profile.class);
//                    startActivity(i);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            update.showResultIcon(true);
//                        }
//                    },200);
//
//                }
//            });


            changPassWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(Profile.this,change_password.class);
                    startActivity(i);
                }

            });

        }
    }
}