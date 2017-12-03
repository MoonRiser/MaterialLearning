package com.example.xiewencai.material_learning.activity;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.xiewencai.material_learning.R;

public class SettingActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        android.support.v7.widget.Toolbar toolbar=(android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_setting);
       setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


     //   getFragmentManager().beginTransaction().replace(R.id.frame_setting,new PrefFragment()).commit();

    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }


}
