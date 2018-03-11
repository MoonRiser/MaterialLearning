package com.example.xiewencai.material_learning.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.xiewencai.material_learning.db.Note;
import com.example.xiewencai.material_learning.db.NoteRemote;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;

/**
 * Created by Xie Wencai on 2018/3/2.
 */

public class NoteUtils {


    public static List<Note> getNoteDataFromDB(){//从本地数据库获取note数据
        List<Note> notesLink=new LinkedList<>();//返回链表便于从头节点插入新数据
        List<Note> notes= DataSupport.findAll(Note.class);
        if(notes.size()==0){
            Note note=new Note();
            note.setContent("写下你的第一个笔记吧(●ˇ∀ˇ●)");
            note.setDate(TimeParse.getCurrentDate());
            note.setId(0);
            notes.add(note);
            //  note.save();
        }
        for (Note note: notes) {
            notesLink.add(0,note);
        }
        return notesLink;
    }

    public static void uploadNoteData(final Context context){

        List<BmobObject> noteRemotes=new ArrayList<>();
        final List<Note> notes= DataSupport.findAll(Note.class);
        if(notes.size()==0){
            Toast.makeText(context, "当前并没有可用于上传的笔记", Toast.LENGTH_SHORT).show();
        }else {

            for (Note note: notes) {
                if (!note.getUpload()){//当前note对象未被上传过
                    NoteRemote noteRemote=new NoteRemote();
                    noteRemote.setContent(note.getContent());
                    noteRemote.setDate(note.getDate());
                    noteRemote.setTitle(note.getTitle());
                    noteRemote.setIdentify(BmobUser.getCurrentUser().toString());
                    noteRemotes.add(noteRemote);
                }

            }
            new BmobBatch().insertBatch(noteRemotes).doBatch(new QueryListListener<BatchResult>() {
                @Override
                public void done(List<BatchResult> list, BmobException e) {
                    for(int i=0;i<list.size();i++){
                        BatchResult result = list.get(i);
                        BmobException ex =result.getError();
                        if(ex==null){
                            notes.get(i).setUpload(true);
                            Log.e("上传Bmob","第"+i+"个数据批量添加成功："+result.getCreatedAt()+","+result.getObjectId()+","+result.getUpdatedAt());
                        }else{
                            Log.e("上传Bmob","第"+i+"个数据批量添加失败："+ex.getMessage()+","+ex.getErrorCode());
                            Toast.makeText(context, "第"+i+"个数据批量添加失败：", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });



        }

    }

    public  static Note getFirstItemFromDB(){
        Note note= DataSupport.findFirst(Note.class);
        return note;

    }

    public static Note getLastItemFromDB(){
        Note note= DataSupport.findLast(Note.class);
        return note;
    }

    public static Note get(int position){
        List<Note> notes=getNoteDataFromDB();
        return  notes.get(position);
    }

}
