package com.example.finalchocohub;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.example.finalchocohub.R;

public class LoadingDilog extends Dialog {
    public LoadingDilog(@NonNull Context context)
    {
        super(context);

        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity= Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(params);
        setTitle(null);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tost,null);
        setContentView(view);
    }

//    private Activity activity;
//    private AlertDialog dialog;
//    LoadingDilog(Activity myActivity){
//        activity=myActivity;
//    }
//    void startLoadingAnimation()
//    {
//
//        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
//        LayoutInflater inflater=activity.getLayoutInflater();
//        builder.setView(inflater.inflate(R.layout.custom_tost,null));
//        builder.setCancelable(false);
////        dialog=builder.create();
//        dialog.show();
//    }
//    void dismissdialog(){
//        dialog.dismiss();
//    }
}
