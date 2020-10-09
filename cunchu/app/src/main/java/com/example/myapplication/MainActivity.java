package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private final static String SharedPreferencesFileName="config";
    private final static String Key_Number = "num";
    private final static String Key_Name = "name";

    private static final String TAG = "Contact Names";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    EditText numInput;
    EditText nameInput;
    TextView tvOutput;
    Button btnSave;
    Button btnLoad;

    Button btnCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numInput=findViewById(R.id.num_input);
        nameInput=findViewById(R.id.name_input);
        tvOutput=findViewById(R.id.tv_output);
        btnSave=(Button)findViewById(R.id.btn_save);
        btnLoad=(Button)findViewById(R.id.btn_load);

        btnCon=(Button)findViewById(R.id.contact);

        preferences=getSharedPreferences(SharedPreferencesFileName,MODE_PRIVATE);
        editor=preferences.edit();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!numInput.getText().toString().equals("")&&!nameInput.getText().toString().equals("")){
                    editor.putString(Key_Number,numInput.getText().toString());
                    editor.putString(Key_Name,nameInput.getText().toString());
                    editor.apply();
//                    save();
                    Toast.makeText(MainActivity.this,"已写入文件",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"未填写数据",Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strNum = preferences.getString(Key_Number,null);
                String strName = preferences.getString(Key_Name,null);
                if(strNum!=null&&strName!=null){
                    String str = ""+strNum+","+strName;
                    tvOutput.setText(str);
                    Toast.makeText(MainActivity.this,"已读取文件",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"未找到数据",Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cursor= getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
                while(cursor.moveToNext())
                {
                    String msg="User";
                    String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    msg=msg+" name:"+name;
                    Log.v(TAG,msg);}
                Toast.makeText(MainActivity.this,"已读取联系人",Toast.LENGTH_LONG).show();
            }
        });
//----------------------------------//
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!etInput.getText().toString().equals("")){
//                    save(etInput.getText().toString());
//                    Toast.makeText(MainActivity.this,"已写入文件",Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(MainActivity.this,"未填写数据",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        btnLoad.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String dataFromFile=load();
//                if(!dataFromFile.equals("")){
//                    tvOutput.setText(dataFromFile);
//                    Toast.makeText(MainActivity.this,"已读取文件",Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(MainActivity.this,"未找到数据",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    public void save(String inputText){
        FileOutputStream out=null;
        BufferedWriter writer=null;
        try{
            out=openFileOutput("data", Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(writer!=null) writer.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String load(){
        FileInputStream in=null;
        BufferedReader reader=null;
        StringBuilder content=new StringBuilder();
        try{
            in=openFileInput("data");
            reader=new BufferedReader(new InputStreamReader(in));
            String line="";
            while ((line=reader.readLine())!=null){
                content.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return content.toString();
    }
}
