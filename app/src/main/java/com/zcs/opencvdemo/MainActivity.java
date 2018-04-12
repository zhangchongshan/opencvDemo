package com.zcs.opencvdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;


public class MainActivity extends AppCompatActivity {

    Button btnProcess;
    Button btnEnhance;
    Bitmap srcBitmap;
    Bitmap grayBitmap;
    Bitmap enhanceBitmap;
    ImageView imgHuaishi;
    Button btnEnhanceAdd;
    Button btnEnhanceNext;
    Button btnEnhanceClear;
    TextView enhanceLabel;

    private float mSigma_S=0.0f;
    private float mSigma_R=0.0f;
    private static boolean flag = true;
    //private static boolean isFirst = true;
    private static final String TAG = "MainActivity";

    //OpenCV库加载并初始化成功后的回调函数
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status) {
            // TODO Auto-generated method stub
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    Log.i(TAG, "成功加载");
                    procSrc2Gray();
                    break;
                default:
                    super.onManagerConnected(status);
                    Log.i(TAG, "加载失败");
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        ProcessClickListener processClickListener=new ProcessClickListener();
        btnProcess.setOnClickListener(processClickListener);
        btnEnhance.setOnClickListener(processClickListener);
        btnEnhanceAdd.setOnClickListener(processClickListener);
        btnEnhanceNext.setOnClickListener(processClickListener);
        btnEnhanceClear.setOnClickListener(processClickListener);
    }
    public void initUI(){
        btnProcess = (Button)findViewById(R.id.btn_gray_process);
        btnEnhance=findViewById(R.id.btn_enhance_process);
        btnEnhanceAdd=findViewById(R.id.btn_enhance_add);
        btnEnhanceNext=findViewById(R.id.btn_enhance_next);
        btnEnhanceClear=findViewById(R.id.btn_enhance_clear);
        enhanceLabel=findViewById(R.id.enhance_label);
        imgHuaishi = (ImageView)findViewById(R.id.img_huaishi);
        Log.i(TAG, "initUI sucess...");

    }

    public void procSrc2Gray(){
        Mat rgbMat = new Mat();
        Mat grayMat = new Mat();
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        grayBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.RGB_565);
        Utils.bitmapToMat(srcBitmap, rgbMat);//convert original bitmap to Mat, R G B.
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);//rgbMat to gray grayMat
        Utils.matToBitmap(grayMat, grayBitmap); //convert mat to bitmap
        Log.i(TAG, "procSrc2Gray sucess...");
    }

    private class ProcessClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
//            if(isFirst)
//            {
//            procSrc2Gray();
//            imageEnhance();
//                isFirst = false;
//            }
            switch (v.getId()) {
                case R.id.btn_gray_process: {
                    if (flag) {
                        imgHuaishi.setImageBitmap(grayBitmap);
                        btnProcess.setText("查看原图");
                        flag = false;
                    } else {
                        imgHuaishi.setImageBitmap(srcBitmap);
                        btnProcess.setText("灰度化");
                        flag = true;
                    }
                }
                break;
                case R.id.btn_enhance_process:{
                    imageEnhance(mSigma_S,mSigma_R);
                    enhanceLabel.setText("S: "+mSigma_S+"--R:"+mSigma_R);
                    imgHuaishi.setImageBitmap(enhanceBitmap);
                }
                break;
                case R.id.btn_enhance_add:{
                    mSigma_S+=10.0f;
                }
                break;
                case R.id.btn_enhance_next:{
                    mSigma_R+=0.1f;
                }
                break;
            }
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    private void imageEnhance(float sigma_s,float sigma_r){
        Mat rgbMat = new Mat();
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        enhanceBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.RGB_565);
        Utils.bitmapToMat(srcBitmap, rgbMat);//convert original bitmap to Mat, R G B.
        Mat enhanceMat=new Mat();
//        Photo.detailEnhance(rgbMat,enhanceMat);
        Photo.detailEnhance(rgbMat,enhanceMat,100.0f,0.2f);
        Utils.matToBitmap(enhanceMat, enhanceBitmap); //convert mat to bitmap
    }
}
