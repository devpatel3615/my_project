package com.example.finalchocohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

public class PDF_download extends AppCompatActivity {
    private static final int WRITE_PERMISSION = 1001;
    String url="https://valentinachoco.000webhostapp.com/PDF/Presentation.pdf";
    Button button;
    String keyword="PDF";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_downloder);
        PDFView pdfView=(PDFView)findViewById(R.id.pdfView);
        pdfView.fromAsset("Valintina choco word file123.pdf").load();
        setTitle("Brochure");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        button=findViewById(R.id.Download);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(PDF_download.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                        String filename=url.substring(url.indexOf(keyword)+keyword.length());
                        downloadFile(filename,url);
                    }
                    else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
                    }
                }
                else {
                    String filename=url.substring(url.indexOf(keyword)+keyword.length());
                    downloadFile(filename,url);
                }

            }
        });
    }

    private void downloadFile(String filename, String url) {
        //   url.substring(url.lastIndexOf('/') + 1);
        Uri downloadURI = Uri.parse(url);

        DownloadManager manager=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        try {
            if (manager!=null){
                DownloadManager.Request request= new DownloadManager.Request(downloadURI);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setTitle(filename = url.substring(url.lastIndexOf('/') + 1));
                request.setDescription("Downloading" + filename);
                request.setAllowedOverMetered(true);
                request.setAllowedOverRoaming(true);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                request.setMimeType(getMimeType(downloadURI));
                final DownloadManager.Request request1 = request;
                manager.enqueue(request);
                Toast.makeText(this, "Start Downloading...", Toast.LENGTH_SHORT).show();

            }

        }
        catch (Exception e){
            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            Log.e("ERROR:MAIN","E:"+e.getMessage());
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==WRITE_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }
            else
            {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String getMimeType(Uri uri)
    {
        ContentResolver resolver=getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri));
    }
}