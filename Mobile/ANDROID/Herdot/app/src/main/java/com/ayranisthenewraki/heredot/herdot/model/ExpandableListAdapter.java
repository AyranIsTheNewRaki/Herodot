package com.ayranisthenewraki.heredot.herdot.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayranisthenewraki.heredot.herdot.HeritageItemHomepageActivity;
import com.ayranisthenewraki.heredot.herdot.MapsViewAllActivity;
import com.ayranisthenewraki.heredot.herdot.R;
import com.ayranisthenewraki.heredot.herdot.addAnnotationActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by idilgun on 11/05/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, CulturalHeritageObject> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, CulturalHeritageObject> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final CulturalHeritageObject cho = (CulturalHeritageObject) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_body, null);
        }

        TextView listItemCategory = (TextView) convertView
                .findViewById(R.id.listItemCategory);

        TextView listItemDescription = (TextView) convertView
                .findViewById(R.id.listItemDescription);


        if(cho.getCategory()!=null){
            listItemCategory.setText(cho.getCategory());
        }
        if(cho.getDescription() != null){
            listItemDescription.setText(cho.getDescription());
        }
        if(cho.getImageUrl() != null){
            new DownloadImageTask((ImageView) convertView.findViewById(R.id.itemImageView))
                    .execute(cho.getImageUrl());
        }else{
            ImageView imageView = (ImageView) convertView.findViewById(R.id.itemImageView);
            imageView.setImageDrawable(null);
        }

        Button viewOnMapButton = (Button) convertView.findViewById(R.id.itemViewOnMap);
        viewOnMapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(_context, MapsViewAllActivity.class);
                Bundle bundle = new Bundle();
                TimeLocationListWrapper tlcListWrapper = new TimeLocationListWrapper();
                tlcListWrapper.setTlcList(cho.getActualTimeLocations());
                bundle.putSerializable("timeLocationList", tlcListWrapper);
                intent.putExtras(bundle);
                _context.startActivity(intent);
            }
        });

        Button addAnnotationButton = (Button) convertView.findViewById(R.id.itemAddAnnotationButton);
        addAnnotationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(_context, addAnnotationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("culturalHeritageObject", cho);
                intent.putExtras(bundle);
                _context.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_header, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
