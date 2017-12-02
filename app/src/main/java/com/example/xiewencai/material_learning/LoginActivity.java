package com.example.xiewencai.material_learning;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public  class LoginActivity extends  BaseActivity{

    private EditText accountNum;
    private EditText psw;
    private Button login;
    private Button signIn;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox checkBox;

    protected  void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_login);

        LayoutInflater factory = LayoutInflater.from(LoginActivity.this);
              final View dialogSignIn = factory.inflate(R.layout.dialog_signin, null,false);

        //获取sharePreference对象
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        accountNum=(EditText) findViewById(R.id.accountEditText);
        psw=(EditText) findViewById(R.id.pswEditText);
        login=(Button)findViewById(R.id.Loginbutton);
        signIn=(Button)findViewById(R.id.RegisterButton) ;
        checkBox=(CheckBox)findViewById(R.id.checkBox2) ;

        boolean isRember=pref.getBoolean("remember_password",false);
        //通过从sharedPreference对象写入的标记isRemenber来判断是否记住密码
        if(isRember){
            String account = pref.getString("account","");
            String password= pref.getString("password","");
            accountNum.setText(account);
            psw.setText(password);
            checkBox.setChecked(true);
        }



        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                //运行时权限获取
                if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
                    requestPermiassionDialog();
                }else {
                    loginBmob();
                }
            }
        });

        //注册点击事件使用对话框进行注册
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeParentsView(dialogSignIn);

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                        alertDialog.setTitle("用户注册");
                        alertDialog.setView(dialogSignIn);
                        alertDialog.setPositiveButton("注册", new DialogInterface.OnClickListener() {
                   @Override
                  public void onClick(DialogInterface dialog, int which) {

                       EditText user = (EditText)dialogSignIn.findViewById(R.id.username);
                       EditText passwd = (EditText)dialogSignIn.findViewById(R.id.password);
                       final         String userStr = user.getText().toString();
                       final        String passwdStr = passwd.getText().toString();
                                        if(passwdStr.length()<8){
                                            Toast.makeText(LoginActivity.this, "密码至少8位", Toast.LENGTH_LONG).show();
                                    //      removeParentsView(dialogSignIn);
                                          return;
                                        }
                       registerBmob(userStr,passwdStr);//在后台注册账户信息


                   }
                        });
                        alertDialog.show();
            }
        });

    }



    private void requestPermiassionDialog(){
        AlertDialog.Builder  dialog = new AlertDialog.Builder(LoginActivity.this);
        dialog.setTitle("说明");
        dialog.setCancelable(false);
        dialog.setMessage("完成正常功能需要获取 读取电话状态权限，不会用于其他用途，请放心");
        dialog.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.READ_PHONE_STATE},1);//申请权限
            }
        });
        dialog.show();

    }

    public void onRequestPermissionsResult(int requestCode ,String permissions[],int[] grantResults){

        switch (requestCode){
            case  1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    loginBmob();
                else
                    Toast.makeText(LoginActivity.this,"you denied the permission",Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void registerBmob(String userName,String passWord){
        BmobUser bu = new BmobUser();
        bu.setUsername(userName);
        bu.setPassword(passWord);
        bu.signUp(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser s, BmobException e) {
                if(e==null){
                    Toast.makeText(LoginActivity.this, "注册成功:" +s.toString(), Toast.LENGTH_LONG).show();
                }else{
                    Log.i("bmob","注册失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    //负责登陆账号密码的判断
    private void  loginBmob(){
      final   String account=accountNum.getText().toString();
     final   String psword= psw.getText().toString();
     if(account.length()==0  || psword.length()==0){//当账号或密码的输入为空时提示
         Toast.makeText(this, "请输入账号密码", Toast.LENGTH_SHORT).show();
         return;
     }
        BmobUser bu = new BmobUser();
        bu.setUsername(account);
        bu.setPassword(psword);
        bu.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser s, BmobException e) {
                if(e==null){
                    Toast.makeText(LoginActivity.this, "登陆成功" , Toast.LENGTH_LONG).show();
                    editor=pref.edit();
                    if(checkBox.isChecked()){
                        editor.putBoolean("remember_password",true);
                        editor.putString("account",account);
                        editor.putString("password",psword);
                    }else {
                        editor.clear();}

                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    if(e.getErrorCode()==9016){
                        Toast.makeText(LoginActivity.this, "网络不可用，请检查你的网络连接", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(LoginActivity.this,"账号或密码错误（password or account is not valid）",Toast.LENGTH_LONG).show();
                    }
                    Log.i("bmob","登陆失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    //去除视图的所有父视图
    public void removeParentsView(View view){
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeAllViews();
        }
    }


}