<<<<<<< HEAD
11111111
=======
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
import com.mykeyboard.KeyBoardUtils;

public class MainActivity extends AppCompatActivity {
//    private KeyBoardEditText text;
//    private KeyboardView keyboardView;
//    private LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMykeyboard();
//        initMycustomkeyboard();

    }

    //mykeyboard 用法
    private void initMykeyboard(){
        KeyBoardUtils.getInstance().initView(this,3,"#30ff00ff",KeyBoardUtils.DEVICES_TYPE_ANDROID);
        KeyBoardUtils.getInstance().setOnKeyPressListenerListener(new KeyBoardUtils.OnKeyPressListener() {
            @Override
            public void onkeyPress(int primaryCode, String text) {
                Log.i("primaryCode","onPress--"+primaryCode);
                System.out.println("您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")");
                if(primaryCode == Keyboard.KEYCODE_DONE){
                    Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(MainActivity.this,"您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")",Toast.LENGTH_SHORT).show();
                }
            }
        });
        KeyBoardUtils.getInstance().show();
    }

    //mycustomkeyboard 用法
    private void initMycustomkeyboard(){
//        //方式一 可获取到edittext对象进行操作
////        text = KeyBoardUtil.initView(this,1,"#10000000");
////        text.setOnKeyBoardStateChangeListener(new KeyBoardEditText.OnKeyboardStateChangeListener(){
////            @Override
////            public void onkeyPress(int primaryCode) {
////                Log.i("primaryCode","onPress--"+primaryCode);
////                System.out.println("您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")");
////                Toast.makeText(Main2Activity.this,"您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")",Toast.LENGTH_SHORT).show();
////            }
////        });
////        text.show();
//        //方式一 直接用工具类一步到位 显示 隐藏   show hide
//        KeyBoardUtil.initView(this,2,"#30000000",new KeyBoardEditText.OnKeyboardStateChangeListener(){
//            @Override
//            public void onkeyPress(int primaryCode) {
//                Log.i("primaryCode","onPress--"+primaryCode);
//                System.out.println("您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")");
//                if(primaryCode == Keyboard.KEYCODE_DONE){
//                    Toast.makeText(MainActivity.this,KeyBoardUtil.getKeyBoardEditText().getText().toString(),Toast.LENGTH_SHORT).show();
//                }else{
////                    Toast.makeText(MainActivity.this,"您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")",Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//
//        //方式二  四种键盘模式，可多个deittext 不同样式
//        final KeyBoardEditText text1 = (KeyBoardEditText) findViewById(R.id.edText1);
//        KeyBoardUtilNoEdittext.initView(this,1,text1,"#00ff00",new KeyBoardEditText.OnKeyboardStateChangeListener(){
//            @Override
//            public void onkeyPress(int primaryCode) {
//                Log.i("primaryCode","onPress--"+primaryCode);
//                System.out.println("您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")");
//                if(primaryCode == Keyboard.KEYCODE_DONE){
//                    Toast.makeText(MainActivity.this,text1.getText().toString(),Toast.LENGTH_SHORT).show();
//                }else{
////                    Toast.makeText(MainActivity.this,"您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")",Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//
//
//        KeyBoardEditText text2 = (KeyBoardEditText) findViewById(R.id.edText2);
//        KeyBoardUtilNoEdittext.initView(this,2,text2,"#0000ff");
//
//        KeyBoardEditText text3 = (KeyBoardEditText) findViewById(R.id.edText3);
//        KeyBoardUtilNoEdittext.initView(this,3,text3,"#ff0000");
//
//        KeyBoardEditText text4 = (KeyBoardEditText) findViewById(R.id.edText4);
//        KeyBoardUtilNoEdittext.initView(this,4,text4,"#ff00ff");
//
//
//        Button bt_soft=findViewById(R.id.bt_soft);
//        bt_soft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                KeyBoardUtil.show();
//                KeyBoardUtils.getInstance().show();
//            }
//        });
//        Button bt_soft1=findViewById(R.id.bt_soft1);
//        bt_soft1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                KeyBoardUtil.hide();
//                KeyBoardUtils.getInstance().hide();
//            }
//        });
//
////        text1.addTextChangedListener(new TextWatcher() {
////            @Override
////            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
////                System.out.println("beforeTextChanged="+charSequence.toString());
////            }
////
////            @Override
////            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
////                System.out.println("onTextChanged="+charSequence.toString());
////            }
////
////            @Override
////            public void afterTextChanged(Editable editable) {
////                System.out.println("afterTextChanged="+editable.toString());
////            }
////        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("您按下了onKeyDown："+event.getKeyCode());
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        System.out.println("您按下了dispatchKeyEvent："+event.getKeyCode());
        if(KeyBoardUtils.getInstance().dispatchKeyEvent(event)){
        }
        return super.dispatchKeyEvent(event);
    }


}
>>>>>>> d87019f... 3333
