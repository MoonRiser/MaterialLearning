package com.example.xiewencai.material_learning;


import android.os.AsyncTask;
import android.os.Environment;

import junit.framework.TestSuite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

interface DownloadListener {
    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPause();

    void onCanceled();
}


public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSE = 2;
    public static final int TYPE_CANCELED = 3;

    private DownloadListener downloadListener;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;

    public DownloadTask(DownloadListener listener) {
        this.downloadListener = listener;
    }


    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile saveFile = null;
        File file = null;
        try {
            long downloadLength = 0;
            String downloadUrl = params[0];
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);
            if (file.exists()) {
                downloadLength = file.length();
            }
            long contentLength = getContentLength(downloadUrl);
            if (contentLength == 0) {
                return TYPE_FAILED;
            } else if (contentLength == downloadLength) {
                return TYPE_SUCCESS;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().//断点下载，指定从哪个字节开始下载
                    addHeader("RANGE", "bytes=" + downloadLength + '-').url(downloadUrl).build();
            Response response = client.newCall(request).execute();
            if (response != null) {
                is = response.body().byteStream();
                saveFile = new RandomAccessFile(file, "rw");
                saveFile.seek(downloadLength);//跳过已下载的字节
                byte[] b = new byte[1024];
                int total = 0;
                int len;

                while ((len = is.read(b)) != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        return TYPE_PAUSE;
                    } else {
                        total += len;
                        saveFile.write(b, 0, len);
                        //计算已下载的百分比
                        int progress = (int) ((total + downloadLength) * 100 / contentLength);
                        publishProgress(progress);//传入参数，切换到ui线程
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(is!=null){is.close();}
                if(saveFile!=null){saveFile.close();}
                if(isCanceled&&file!=null){file.delete();}
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    protected void onProgressUpdate(Integer...values){
        int progress=values[0];
        downloadListener.onProgress(progress);
        lastProgress=progress;
    }

    protected void onPostExecute(Integer status){
        switch(status){
            case TYPE_CANCELED:
                downloadListener.onCanceled();
                break;
            case TYPE_FAILED:
                downloadListener.onFailed();
                break;
            case TYPE_PAUSE:
                downloadListener.onPause();
                break;
            case TYPE_SUCCESS:
                downloadListener.onSuccess();
                break;
            default:
                break;
        }
    }

    public void pauseDownload(){
        isPaused=true;
    }

    public void cancelDownload(){
        isCanceled=true;
    }

    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(downloadUrl).build();
        Response response=client.newCall(request).execute();
        if(response!=null&&response.isSuccessful()){
            long contentLength=response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0;
    }

}


