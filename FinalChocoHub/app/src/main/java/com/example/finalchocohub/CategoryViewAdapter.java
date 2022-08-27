package com.example.finalchocohub;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryViewAdapter extends BaseAdapter implements Filterable {

    Context context;
    String catid = null;
    ProgressDialog pDialog = null;
    datahelper db = null;
    String id = null;
    JSONParser jsonp = new JSONParser();
    int cnt = 0;
    ArrayList<HashMap<String, String>> clist;
    ArrayList<HashMap<String, String>> data;
    AssociationFilter filter;
    CategoryViewAdapter(Context context, ArrayList<HashMap<String, String>> clist) {
        this.context = context;
        this.clist = clist;
        data = clist;
    }

    @Override
    public int getCount() {
        return clist.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final TextView tvcatname;
        final TextView tvcateid;
        final ImageView iv;
        //final RelativeLayout rl1;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View custome;
        if (view == null) ;


        custome = inflater.inflate(R.layout.row_view_category, viewGroup, false);
        HashMap<String, String> map = clist.get(i);
        tvcateid = (TextView) custome.findViewById(R.id.tv_cat_id);
        tvcateid.setText(map.get(Category.Tag_cate_id));
        tvcatname = (TextView) custome.findViewById(R.id.tv_cat_name);
        tvcatname.setText(map.get(Category.Tag_cate_name));
        //iv = (ImageView) custome.findViewById(R.id.ivcat);
        //Picasso.with(context).load(map.get(Category.Tag_cate_img)).into(iv);
        //rl1=custome.findViewById(R.id.rl1);
        db = new datahelper(context);
        //Animation animation= AnimationUtils.loadAnimation(context, R.anim.bottom_animation);
        //tvcatname.startAnimation(animation);
        //iv.startAnimation(animation);
        //rl1.startAnimation(animation);

        return custome;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new AssociationFilter();
        }
        return filter;
    }
    private class AssociationFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = data;
                results.count = data.size();
            } else {
                ArrayList<HashMap<String, String>> preferenceAssociationList = new ArrayList<HashMap<String, String>>();
                for (HashMap<String, String> assocation : data) {


                    if (assocation.get("cate_name").toUpperCase()
                            .contains(constraint.toString().toUpperCase())) {
                        preferenceAssociationList.add(assocation);
                    }
                }
                results.values = preferenceAssociationList;
                results.count = preferenceAssociationList.size();
            }
            return results;
        }

        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            System.out.println("count "+results.count);
            if (results.count == 0)
            {
                notifyDataSetInvalidated();
            }
            else
            {
                clist = (ArrayList<HashMap<String, String>>) results.values;
                System.out.println("size "+clist.size());
                notifyDataSetChanged();
            }
        }
    }

}
