package com.example.xiewencai.material_learning.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xiewencai.material_learning.R;
import com.example.xiewencai.material_learning.db.Note;
import com.example.xiewencai.material_learning.util.NoteUtils;
import com.example.xiewencai.material_learning.util.TimeParse;

import java.util.List;

public class NoteDetailActivity extends BaseActivity {

    private Note note;
    private EditText title;
    private EditText content;
    private TextView date;
    private boolean isBlank;
    private  int position;

    //@SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        title =  findViewById(R.id.note_detail_title);
        content=findViewById(R.id.note_detail_content);
        date=findViewById(R.id.note_detail_date);

        position= getIntent().getIntExtra("position",-1);
        isBlank=(position==-1);
        if(!isBlank){
            List<Note> noteList= NoteUtils.getNoteDataFromDB();
            note=noteList.get(position);
            title.setText(note.getTitle());
            content.setText(note.getContent());
            date.setText("创建时间:"+TimeParse.getTimeByDate(note.getDate()));
        }else {
            note=new Note();
        }

    }


    protected void onPause(){
        super.onPause();

        if(isBlank){
            if(title.getText().length()==0&&content.getText().length()==0){//当标题和内容都为空时,不存入数据库
                return;
            }
            note.setUpload(false);
            note.setDate(TimeParse.getCurrentDate());
            note.setTitle(title.getText().toString());
            note.setContent(content.getText().toString());
            note.save();
        }else{
          //  note.setDate(TimeParse.getCurrentDate());
            note.setTitle(title.getText().toString());
            note.setContent(content.getText().toString());
            note.update(note.getId());
        }
    }


}
