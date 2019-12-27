package com.example.mycustomkeyboard;

import android.app.Activity;
import android.graphics.Color;
import android.inputmethodservice.KeyboardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 软键盘工具类带edittext
 *
 * @version V1.0 <软键盘工具类带edittext>
 * @FileName: com.example.mycustomkeyboard.KeyBoardUtil.java
 * @author: helong
 * @date: 2019-12-26 09:57
 */
public class KeyBoardUtil {
    private static KeyBoardEditText text;
    private static KeyboardView keyboardView;
    private static LinearLayout viewGroup;
    /**
     * 初始化键盘view
     * @param mContext Activity
     * @param keyboard_num 显示键盘类型 1数字 2字母 3特殊字符 4 随机数字
     * @return KeyBoardEditText
     */
    public static KeyBoardEditText initView(Activity mContext,int keyboard_num){
        if(!init(mContext,keyboard_num)){
            return null;
        }
        return text;
    }

    /**
     * 初始化键盘view
     * @param mContext Activity
     * @param keyboard_num 显示键盘类型 1数字 2字母 3特殊字符 4 随机数字
     * @param color 键盘背景色 #10000000
     * @return KeyBoardEditText
     */
    public static KeyBoardEditText initView(Activity mContext,int keyboard_num,String color){
        if(!init(mContext,keyboard_num)){
            return null;
        }
        keyboardView.setBackgroundColor(Color.parseColor(color));
        return text;
    }

    /**
     * 初始化键盘view
     * @param mContext Activity
     * @param keyboard_num 显示键盘类型 1数字 2字母 3特殊字符 4 随机数字
     * @param color 键盘背景色 #10000000
     * @param listener  监听器 KeyBoardEditText.OnKeyboardStateChangeListener
     * @return KeyBoardEditText
     */
    public static KeyBoardEditText initView(Activity mContext,int keyboard_num,String color,KeyBoardEditText.OnKeyboardStateChangeListener listener){
        if(!init(mContext,keyboard_num)){
            return null;
        }
        text.setOnKeyBoardStateChangeListener(listener);
        keyboardView.setBackgroundColor(Color.parseColor(color));
        return text;
    }

    private static boolean init(Activity mContext,int keyboard_num){
        if(keyboard_num > 4){//键盘模式只能到4
            return false;
        }
        LayoutInflater factory = LayoutInflater.from(mContext);
        View layout = factory.inflate(R.layout.keyboard, null);
        ViewGroup vg= (ViewGroup) mContext .getWindow().getDecorView();
        vg.addView(layout);
        text = layout.findViewById(com.example.mycustomkeyboard.R.id.ed_main);
        keyboardView = layout.findViewById(com.example.mycustomkeyboard.R.id.view_keyboard);
        viewGroup =layout.findViewById(com.example.mycustomkeyboard.R.id.layout_main);
        text.setKeyboardType(mContext,viewGroup,keyboardView,keyboard_num);
        return true;
    }

    /**
     * 设置软键盘刚弹出的时候显示字母键盘还是数字键盘
     * @param keyboard_num 显示键盘类型 1数字 2字母 3特殊字符 4 随机数字
     */
    public static void setKeyboardType (int keyboard_num) {
        if(text!=null){
            text.setKeyboardType(keyboard_num);
        }
    }

    /**
     * 设置键盘背景色
     * @param color 键盘背景色 #10000000
     */
    public static void setBackgroundColor(String color){
        if(keyboardView!=null){
            keyboardView.setBackgroundColor(Color.parseColor(color));
        }
    }

    /**
     * 设置键盘按下监听
     * @param listener 监听器 KeyBoardEditText.OnKeyboardStateChangeListener
     */
    public static void setOnKeyBoardStateChangeListener(KeyBoardEditText.OnKeyboardStateChangeListener listener){
        if(text!=null){
            text.setOnKeyBoardStateChangeListener(listener);
        }
    }

    /**
     * 获取KeyBoardEditText对象 可单独对editview其他属性设置
     * @return
     */
    public static KeyBoardEditText getKeyBoardEditText(){
        if(text == null){
            return null;
        }
        return text;
    }

    /**
     * 显示软键盘
     */
    public static void show (){
        if(text!=null){
            if(KeyBoardUtilNoEdittext.getKeyBoardEditText()!=null){
                KeyBoardUtilNoEdittext.getKeyBoardEditText().hide();
            }
            text.show();
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hide (){
        if(text!=null){
            if(KeyBoardUtilNoEdittext.texts.size()>0){
                for(KeyBoardEditText t:KeyBoardUtilNoEdittext.texts){
                    t.hide();
                }
            }
            text.hide();
        }
    }

}
