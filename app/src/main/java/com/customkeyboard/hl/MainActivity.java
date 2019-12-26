package com.customkeyboard.hl;

import androidx.appcompat.app.AppCompatActivity;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycustomkeyboard.KeyBoardEditText;
import com.example.mycustomkeyboard.KeyBoardUtil;
import com.example.mycustomkeyboard.KeyBoardUtilNoEdittext;

public class MainActivity extends AppCompatActivity {
    private KeyBoardEditText text;
    private KeyboardView keyboardView;
    private LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        text = KeyBoardUtil.initView(this,1,"#10000000");
//        text.setOnKeyBoardStateChangeListener(new KeyBoardEditText.OnKeyboardStateChangeListener(){
//            @Override
//            public void onkeyPress(int primaryCode) {
//                Log.i("primaryCode","onPress--"+primaryCode);
//                System.out.println("您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")");
//                Toast.makeText(Main2Activity.this,"您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")",Toast.LENGTH_SHORT).show();
//            }
//        });
//        text.show();

        KeyBoardUtil.initView(this,2,"#10000000",new KeyBoardEditText.OnKeyboardStateChangeListener(){
            @Override
            public void onkeyPress(int primaryCode) {
                Log.i("primaryCode","onPress--"+primaryCode);
                System.out.println("您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")");
                if(primaryCode == Keyboard.KEYCODE_DONE){
                    Toast.makeText(MainActivity.this,KeyBoardUtil.getKeyBoardEditText().getText().toString(),Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(MainActivity.this,"您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")",Toast.LENGTH_SHORT).show();
                }

            }
        });


        text = (KeyBoardEditText) findViewById(R.id.edText);
        KeyBoardUtilNoEdittext.initView(this,2,text,"#3000ff00",new KeyBoardEditText.OnKeyboardStateChangeListener(){
            @Override
            public void onkeyPress(int primaryCode) {
                Log.i("primaryCode","onPress--"+primaryCode);
                System.out.println("您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")");
                if(primaryCode == Keyboard.KEYCODE_DONE){
                    Toast.makeText(MainActivity.this,text.getText().toString(),Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(MainActivity.this,"您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")",Toast.LENGTH_SHORT).show();
                }

            }
        });


        KeyBoardEditText text1 = (KeyBoardEditText) findViewById(R.id.edText1);
        KeyBoardUtilNoEdittext.initView(this,1,text1,"#300000ff");


        Button bt_soft=findViewById(R.id.bt_soft);
        bt_soft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyBoardUtil.show();
            }
        });
        Button bt_soft1=findViewById(R.id.bt_soft1);
        bt_soft1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyBoardUtil.hide();
            }
        });

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("beforeTextChanged="+charSequence.toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("onTextChanged="+charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println("afterTextChanged="+editable.toString());
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("您按下了onKeyDown："+event.getKeyCode());
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        System.out.println("您按下了dispatchKeyEvent："+event.getKeyCode());
        return super.dispatchKeyEvent(event);
    }


}
