package com.example.a1.tastyapp.Request;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.a1.tastyapp.ReviewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kwanwoo on 2017. 11. 3..
 */

public class UploadReview extends AsyncTask<String, String, String> {

    /**
     * *******  Change to your server address
     */
    final String serverURL = "http://13.114.103.74:3000";


    final String upLoadServerUri = serverURL + "/api/photo";
    Activity activity;

    public UploadReview(Activity activity) {
        this.activity = activity;
    }


    @Override
    protected String doInBackground(String... objects) {

        String fileName = objects[0];

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(fileName);

        try {

            // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upLoadServerUri);

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                    + fileName + "\"" + lineEnd);

            dos.writeBytes(lineEnd);

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

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
            int serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);

            StringBuilder response;
            if (serverResponseCode == 200) {
                BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()), 8192);
                response = new StringBuilder();
                String strLine = null;
                while ((strLine = input.readLine()) != null)
                    response.append(strLine);

                input.close();

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
                return response.toString();
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            Log.e("uploadFile", "error: " + ex.getMessage(), ex);
            return "MalformedURLException Exception : check script url.";
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("uploadFile", "Exception : " + e.getMessage(), e);
            return "Got Exception : see logcat ";
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (activity instanceof ReviewActivity) {
            ReviewActivity reviewActivity = (ReviewActivity) activity;

            JSONObject postDataParam = new JSONObject();
            try {
                postDataParam.put("user_id", reviewActivity.getUser_id());
                postDataParam.put("resname", reviewActivity.getName());
                postDataParam.put("point", reviewActivity.getRatingBar().getRating());
                postDataParam.put("memo", reviewActivity.getEdittext().getText().toString());
                postDataParam.put("picture", result);

            } catch (JSONException e) {
                Log.e("", "JSONEXception");
            }
            if (isNetworkAvailable())
                new InsertData(activity, "insert-review").execute(postDataParam);
            else
                Toast.makeText(activity, "인터넷 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
