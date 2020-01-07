package com.example.pj2.tab2;


import android.app.Activity;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.pj2.R;
import com.example.pj2.helper.AppConstant;
import com.example.pj2.helper.Utils;
import com.facebook.AccessToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab2 extends Fragment {
    private ImageAdapter imgAdapter;
    private GridView gridView;
    String basePath = null;
    final int TAKE_CAMERA = 2;
    final int TAKE_GALLERY = 3;
    private Button camera;
    String currentPhotoPath = null;


    public tab2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab2, container, false);
        gridView = v.findViewById(R.id.mygalleryid);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        Log.d("Facebook_User", accessToken.getUserId());

//        File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File directory = new File(android.os.Environment.getExternalStorageDirectory() + File.separator + AppConstant.PHOTO_ALBUM);
//        File a = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        Log.d("aaa",mediaStorageDir.getPath());

        if (!directory.exists()){
            if(!directory.mkdirs()){
                Log.e("tab2","failed to create directory");
            }
        }
        basePath = directory.getPath();

        camera = v.findViewById(R.id.takepicbtn);
        imgAdapter = new ImageAdapter(getContext().getApplicationContext());
        gridView.setAdapter(imgAdapter);
        final Utils utils = new Utils(getActivity());

        //Set activity to send parameter to event listener
        final Activity activity = getActivity();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = new Intent(_activity, FullScreenViewActivity.class);
//                i.putExtra("position", _postion);
//                _activity.startActivity(i);
                Intent i = new Intent(activity, FullScreenViewActivity.class);
                i.putExtra("position", position);
                activity.startActivity(i);
            }
        });

        camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                "com.example.tt.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, TAKE_CAMERA);
                    }
                }
            }
        });


        Button button = v.findViewById(R.id.gallery);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 갤러리 들어감
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), TAKE_GALLERY);
            }
        });

        Button Uploadbtn = v.findViewById(R.id.uploadserverbtn);
        Uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                //Casting getitem value to integer -> can be vulnerable.
                Fragment newFragment = new tab2_upload();

                fragmentTransaction.replace(R.id.outerfragment, newFragment);
//                FragmentStatePagerAdapter swipe = DemoCollectionPagerAdapter(fm);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Button downloadbtn = v.findViewById(R.id.downloadserverbtn);
        downloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                uploadToServer((String)imgAdapter.getItem(1));
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                //Casting getitem value to integer -> can be vulnerable.
                Fragment newFragment = new tab2_download();

                fragmentTransaction.replace(R.id.outerfragment, newFragment);
//                FragmentStatePagerAdapter swipe = DemoCollectionPagerAdapter(fm);
//                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                imgAdapter.notifyDataSetChanged();

            }
        });
//        return inflater.inflate(R.layout.fragment_tab2, container, false);
        return v;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(basePath);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Log.d("AA",currentPhotoPath);
        return image;
    }

    public void onActivityResult( int requestCode, int resultCode, Intent data)
    {
        if( resultCode == RESULT_OK )
        {
// 카메라로 찍었을 떄
            if( requestCode == TAKE_CAMERA ) //1
            {
                if( data == null) {
                    Log.e("BB","AAAABBBAA");
                    imgAdapter.updateData();
                    imgAdapter.notifyDataSetChanged();
                    return ;
                }

// 찍은 사진을 이미지뷰에 보여준다.
                Log.e("AAAAA","AAAAAA");
                imgAdapter.updateData();
                imgAdapter.notifyDataSetChanged();
                gridView.setAdapter(imgAdapter);
            }

// 앨범에서 가져올 때
            else if( requestCode == TAKE_GALLERY ) //2
            {
                ClipData clipData = data.getClipData();
                List<Uri> imageListUri = new ArrayList<>();

                if(clipData == null){
                    Toast.makeText(getActivity(), "다중선택이 불가능한 기기입니다.", Toast.LENGTH_LONG).show();
                }

                else if(clipData != null){
                    for(int i = 0; i < clipData.getItemCount(); i++){
                        Log.i("3. single choice", String.valueOf(clipData.getItemAt(i).getUri()));
                        if (clipData.getItemAt(i).getUri().toString().contains(basePath)) continue;
                        else{
                            String src_path = getPath(getActivity(),clipData.getItemAt(i).getUri());
                            String[] parsing = src_path.split("/");
                            String dst_path = basePath + "/" + parsing[parsing.length - 1];
                            Log.i("Filepath", dst_path);
                            try{
                                copy(src_path, dst_path);
                            }catch (IOException e){
                                e.printStackTrace();
                            }
//                            imgAdapter.addList(clipData.getItemAt(i).getUri().toString());
//                            imageListUri.add(clipData.getItemAt(i).getUri());
                        }
                        imgAdapter.updateData();
                    }

                }
            }
        }
            else
            {
                System.out.println( "camera return error" ) ;
                return ;
            }
        }

    public static void copy(String src, String dst) throws IOException {
        File src_file = new File(src);
        InputStream in = new FileInputStream(src_file);
        try {
            File dst_file = new File(dst);
            OutputStream out = new FileOutputStream(dst_file);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


}




