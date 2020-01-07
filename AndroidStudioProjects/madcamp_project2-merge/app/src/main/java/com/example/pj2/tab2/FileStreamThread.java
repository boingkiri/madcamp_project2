package com.example.pj2.tab2;

import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileStreamThread extends Thread {
    String filename;
    String fileBase;
    public FileStreamThread(String server_filename, String filebase){
        filename = server_filename;
        fileBase = filebase;
    }

    @Override
    public void run() {
        try{
            URL url = new URL("http://192.249.19.254:7280/download/show?filename="+filename);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            InputStream is = con.getInputStream();

            File dst_file = new File(fileBase + "/" + filename + ".jpg");
            FileOutputStream os = new FileOutputStream(dst_file);
            Log.d("DownloadFilename",fileBase + "/" + filename + ".jpg");

            byte[] readBuffer = new byte[1024];
//            while (is.read(readBuffer, 0, readBuffer.length) != -1)
//            {
//                //버퍼 크기만큼 읽을 때마다 출력 스트림에 써준다.
//                os.write(readBuffer);
//            }
            int read;
            for (;;) {
                read = is.read(readBuffer);
                if (read <= 0) {
                    break;
                }
                os.write(readBuffer, 0, read); //file 생성
            }
            os.flush();

            try
            {
                // 파일 닫기. 여기에도 try/catch가 필요하다.
                os.close();
                is.close();
                con.disconnect();
            }
            catch (Exception e)
            {
                System.out.println("닫기 실패" + e);
            }

        }catch (IOException e){

        }
    }
}
