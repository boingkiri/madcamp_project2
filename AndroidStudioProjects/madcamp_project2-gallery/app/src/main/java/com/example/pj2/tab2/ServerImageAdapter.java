package com.example.pj2.tab2;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.pj2.R;
import com.example.pj2.helper.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ServerImageAdapter extends BaseAdapter {
    int CustomGalleryItemBg; // 앞서 정의해 둔 attrs.xml의 resource를 background로 받아올 변수 선언
//    String mBasePath; // CustomGalleryAdapter를 선언할 때 지정 경로를 받아오기 위한 변수
    Context mContext; // CustomGalleryAdapter를 선언할 때 해당 activity의 context를 받아오기 위한 context 변수
    ArrayList<String> mImgs = new ArrayList<>(); // 위 mBasePath내의 file list를 String 배열로 저장받을 변수
    Bitmap bm; // 지정 경로의 사진을 Bitmap으로 받아오기 위한 변수
    DataSetObservable mDataSetObservable = new DataSetObservable();
    tab2_download_ServerConnection connectionUtil;

    public String TAG = "Gallery Adapter Example :: ";

    public ServerImageAdapter(Context context){ // CustomGalleryAdapter의 생성자
        this.mContext = context;
        String[] tmp_mimgs;
        Utils utils = new Utils(context);
        connectionUtil = new tab2_download_ServerConnection(mContext,this);
        connectionUtil.downloadToServer();
//        mImgs.add("552da67ae2d6f4597fbc2982da7ec7ca");
    }

    @Override
    public int getCount() { // Gallery array의 객체 갯수를 앞서 세어 둔 file.list()를 받은 String[]의 원소 갯수와 동일하다는 가정 하에 반환
        Log.d("ServerImage",Integer.toString(mImgs.size()));
        return mImgs.size();
    }

    @Override
    public Object getItem(int position) { // Gallery array의 해당 position을 반환
        return mImgs.get(position); // String value
    }

    @Override
    public long getItemId(int position) { // Gallery array의 해당 position을 long 값으로 반환
        return position;
    }

    public ArrayList<String> getmImgs(){
        return mImgs;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }
        ///////
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        int reqWidth = 256;
        int reqHeight = 192;
        if((width > reqWidth) || (height > reqHeight)){
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        //////

        Log.d("position","http://192.249.19.254:7280/download/show?filename="+mImgs.get(position));

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.fragment_tab2_select, parent, false);

        Thread mThread = new Thread(){
        @Override
        public void run() {
           try{
               URL url = new URL("http://192.249.19.254:7280/download/show?filename="+mImgs.get(position));
               HttpURLConnection con = (HttpURLConnection) url.openConnection();
               con.connect();
               InputStream is = con.getInputStream();
               bm = BitmapFactory.decodeStream(is);
           }catch (IOException e){

           }
        }
        };
        mThread.start();
        try{
            mThread.join();
        }catch (InterruptedException e){e.printStackTrace();}

//        connectionUtil.getPhotoFromServer(mImgs.get(position), imageView);

        Bitmap mThumbnail = ThumbnailUtils.extractThumbnail(bm, 250, 250);
        imageView.setPadding(8, 8, 8, 8);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
        imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT, GridView.LayoutParams.WRAP_CONTENT));
        imageView.setImageBitmap(mThumbnail);
        return imageView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer){ // DataSetObserver의 등록(연결)
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer){ // DataSetObserver의 해제
        mDataSetObservable.unregisterObserver(observer);
    }

    @Override
    public void notifyDataSetChanged(){ // 위에서 연결된 DataSetObserver를 통한 변경 확인
        mDataSetObservable.notifyChanged();
    }

    public void updateData(ArrayList<String> filename){
        mImgs = filename;
        Log.d("serverimage",mImgs.get(1));
//        mImgs.add(filename);
        notifyDataSetChanged();
    }

}
