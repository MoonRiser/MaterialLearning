package com.example.xiewencai.material_learning;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class BaiduMapActivity extends BaseActivity {

    public LocationClient locationClient;
    private TextView positionText;
    private MapView mapView;
    private BaiduMap baiduMap;
    private Boolean isFirstLocate=true;
    static int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationClient=new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                flag++;
                StringBuilder currentPosition=new StringBuilder();
                currentPosition.append("当前的位置为：\n");
                currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
                currentPosition.append("经度：").append(bdLocation.getLongitude()).append("\n");
                currentPosition.append("国家：").append(bdLocation.getCountry()).append("\n");
                currentPosition.append("省：").append(bdLocation.getProvince()).append("\n");
                currentPosition.append("市：").append(bdLocation.getCity()).append("\n");
                currentPosition.append("县/区：").append(bdLocation.getDistrict()).append("\n");
                currentPosition.append("村/街道：").append(bdLocation.getStreet()).append("\n");
                currentPosition.append("当前定位方式为：");
                if(bdLocation.getLocType()==BDLocation.TypeGpsLocation){
                    currentPosition.append("GPS");
                    navigateTo(bdLocation);
                }else if(bdLocation.getLocType()==BDLocation.TypeNetWorkLocation){
                    currentPosition.append("网络");
                    navigateTo(bdLocation);
                }


                showToast(BaiduMapActivity.this,"这是第"+flag+"次运行");
                positionText.setText(currentPosition);

            }
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission( this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissionDialog();
        }else {
            requestLocation();
        }
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map);
        positionText=(TextView)findViewById(R.id.textView_currlocation);
        mapView=(MapView)findViewById(R.id.mapView);
        baiduMap=mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
    }


    private void initLocation(){
        LocationClientOption option=new LocationClientOption();
        option.setScanSpan(5000);
       // option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
    }

    private void requestLocation(){
        initLocation();
        locationClient.start();
    }

    //获取权限时弹出警示对话框，提示用户需要申请权限 并申请权限
    private void requestPermissionDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("说明");
        dialog.setCancelable(false);
        dialog.setMessage("定位需要获取 位置权限，不会用于其他用途，请放心");
        dialog.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ActivityCompat.requestPermissions(BaiduMapActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);//申请权限
           showToast(BaiduMapActivity.this,"申请成功执行");
            }
        });
        dialog.show();


    }
    //用户对权限申请做出选择后，回调该方法
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    requestLocation();
                    showToast(this,"权限授权成功");
                }
                else{
                    Toast.makeText(BaiduMapActivity.this, "you denied the permission“程序即将奔溃” ", Toast.LENGTH_LONG).show();
                    ActivityCollector.finishAll();
                }
                break;
                default:
        }

    }

    //将地图移动到设备所在位置
    public void navigateTo(BDLocation location){
        if(isFirstLocate){
            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
            showToast(this,"定位到当前位置已执行");

            MapStatusUpdate    update=MapStatusUpdateFactory.zoomTo(8f);
            baiduMap.animateMapStatus(update);
            update= MapStatusUpdateFactory.newLatLng(latLng);
            baiduMap.animateMapStatus(update);

           isFirstLocate=false;
        }
        MyLocationData.Builder locationBuilder=new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        baiduMap.setMyLocationData(locationBuilder.build());

    }


    protected void onDestroy(){
        super.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        locationClient.stop();
        showToast(this,"定位已关闭");
    }

    protected  void onResume(){
        super.onResume();
        mapView.onResume();
    }

    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }


}
