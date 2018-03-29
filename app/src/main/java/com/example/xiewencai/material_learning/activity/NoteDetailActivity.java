package com.example.xiewencai.material_learning.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xiewencai.material_learning.R;
import com.example.xiewencai.material_learning.db.Note;
import com.example.xiewencai.material_learning.util.NoteUtils;

import java.util.List;

public class NoteDetailActivity extends BaseActivity {

    private Note note;
    private EditText title;
    private EditText content;
    private TextView date;
    private boolean isBlank;
    private boolean isEditStateChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        title =  findViewById(R.id.note_detail_title);
        content=findViewById(R.id.note_detail_content);
        date=findViewById(R.id.note_detail_date);


        //监听EditText的状态，如果呗编辑，则标志位置1
        setEditChangeListener(title);
        setEditChangeListener(content);

        int position = getIntent().getIntExtra("position", -1);
        isBlank=(position ==-1);
        if(!isBlank){
            List<Note> noteList= NoteUtils.getNoteDataFromDB();
            note=noteList.get(position);
            title.setText(note.getTitle());
            content.setText(note.getContent());
            date.setText(new StringBuilder().append("创建时间:").append(note.getDate()));
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
            note.setDate(NoteUtils.getCurrentDate());
            note.setTitle(title.getText().toString());
            note.setContent(content.getText().toString());
            note.save();
        }else if(isEditStateChanged){

          //  note.setDate(TimeParse.getCurrentDate());
            note.setTitle(title.getText().toString());
            note.setContent(content.getText().toString());
            note.update(note.getId());
            NoteUtils.queryNoteByDate(note,this);
        }
    }


    private void setEditChangeListener(EditText editText){

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start!=0)
               isEditStateChanged=true;
              // Log.w("EditText监听，是否输入内容或状态改变",s+"///"+start+"///"+before+"当前回调已执行"+count);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
             //   Log.w("监听EditText","改变后的字符串S："+s);
            }
        });
    }

}
