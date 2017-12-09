package com.example.xiewencai.material_learning.activity;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.xiewencai.material_learning.bean.Msg;
import com.example.xiewencai.material_learning.R;
import com.example.xiewencai.material_learning.adapter.WeTalkAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeTalkActivity extends BaseActivity {

    private WeTalkAdapter adapter;
    private List<Msg> msgList= new ArrayList<>();
    private EditText editText;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_talk);

        initMsgs();
        recyclerView= findViewById(R.id.recycler_view_weTalk);
        Toolbar toolbar=  findViewById(R.id.toolbar_weTalk);
        Button button= findViewById(R.id.send);
          editText=  findViewById(R.id.editText);

        setSupportActionBar(toolbar);
        ActionBar actionBar= getSupportActionBar();
        if(actionBar!=null)
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("你好，小娜(Fake Cortana)");

        //设置recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new WeTalkAdapter(msgList);
        recyclerView.setAdapter(adapter);

        //监听send按钮，将editText中的文字取出来放入消息列表中
        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          String content = editText.getText().toString();
                                          Msg msg = new Msg(content,Msg.TYPE_SEND);
                                          msgList.add(msg);
                                          adapter.notifyItemInserted(msgList.size()-1);
                                          recyclerView.scrollToPosition(msgList.size()-1);
                                          editText.setText("");
                                          //加入随机消息，模拟小娜的回复
                                          msgList.add(randomMsg());
                                          adapter.notifyItemInserted(msgList.size()-1);
                                          recyclerView.scrollToPosition(msgList.size()-1);
                                      }
                                  }
        );


    }

    //初始化消息列表
    private  void  initMsgs(){
        Msg msg00=new Msg("我是小娜，我能帮你做点什么呢？",Msg.TYPE_RECEIVED);
        Msg msg01=new Msg("今天天气怎么样？",Msg.TYPE_SEND);
        Msg msg02=new Msg("天气很好，就像你的心情一样；你有什么要分享的吗？",Msg.TYPE_RECEIVED);
        msgList.add(msg00);msgList.add(msg01);msgList.add(msg02);
    }

    //点击工具栏返回键销毁当前活动，
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case  android.R.id.home:
                msgList.clear();
                finish();
                return  true;
        }
        return  super.onOptionsItemSelected(item);
    }

    //随机产生一个消息
    private  Msg randomMsg(){
        Random random=new Random();
        int index= random.nextInt(5);
        List<Msg> rmsgList = new ArrayList<>();
        Msg msg10= new Msg("嗯？然后呢？",Msg.TYPE_RECEIVED);Msg msg11= new Msg("这真是有趣呢？23333",Msg.TYPE_RECEIVED);
        Msg msg12= new Msg("我们来背诗吧，我先，苟利国家生死以？ ",Msg.TYPE_RECEIVED);
        Msg msg13= new Msg("其实，我有个秘密，藏在我的心底，无人洞悉",Msg.TYPE_RECEIVED);
        Msg msg14= new Msg("我想到个问题，你看 小羊肖恩 吗？",Msg.TYPE_RECEIVED);
        rmsgList.add(msg10); rmsgList.add(msg11); rmsgList.add(msg12); rmsgList.add(msg13); rmsgList.add(msg14);
        return rmsgList.get(index);
    }

}
