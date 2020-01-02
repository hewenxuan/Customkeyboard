package com.example.mycustomkeyboard;

import android.app.Activity;
import android.graphics.Color;
import android.inputmethodservice.KeyboardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 软键盘工具类不带edittext，需要传进来
 *
 * @version V1.0 <软键盘工具类不带edittext，需要传进来>
 * @FileName: com.example.mycustomkeyboard.KeyBoardUtil.java
 * @author: helong
 * @date: 2019-12-26 09:57
 */
public class KeyBoardUtilNoEdittext {
    private static KeyBoardEditText text;
    private static KeyboardView keyboardView;
    private static LinearLayout sLinearLayout;

    public static List<KeyBoardEditText> texts=new ArrayList<KeyBoardEditText>();
    /**
     * 初始化键盘view
     * @param mContext Activity
     * @param keyboard_num 显示键盘类型 1数字 2字母 3特殊字符 4 随机数字
     * @param edit KeyBoardEditText对象
     * @return KeyBoardEditText
     */
    public static void initView(Activity mContext,int keyboard_num,KeyBoardEditText edit){
        if(!init(mContext,keyboard_num,edit)){
            return ;
        }
    }

    /**
     * 初始化键盘view
     * @param mContext Activity
     * @param keyboard_num 显示键盘类型 1数字 2字母 3特殊字符 4 随机数字
     * @param edit KeyBoardEditText对象
     * @param color 键盘背景色 #10000000
     * @return KeyBoardEditText
     */
    public static void initView(Activity mContext,int keyboard_num,KeyBoardEditText edit,String color){
        if(!init(mContext,keyboard_num,edit)){
            return ;
        }
        keyboardView.setBackgroundColor(Color.parseColor(color));
        sLinearLayout.findViewById(R.id.tv_tip).setBackgroundColor(Color.parseColor(color));
    }

    /**
     * 初始化键盘view
     * @param mContext Activity
     * @param keyboard_num 显示键盘类型 1数字 2字母 3特殊字符 4 随机数字
     * @param edit KeyBoardEditText对象
     * @param color 键盘背景色 #10000000
     * @param listener  监听器 KeyBoardEditText.OnKeyboardStateChangeListener
     */
    public static void initView(Activity mContext,int keyboard_num,KeyBoardEditText edit,String color,KeyBoardEditText.OnKeyboardStateChangeListener listener){
        if(!init(mContext,keyboard_num,edit)){
            return ;
        }
        text.setOnKeyBoardStateChangeListener(listener);
        keyboardView.setBackgroundColor(Color.parseColor(color));
        sLinearLayout.findViewById(R.id.tv_tip).setBackgroundColor(Color.parseColor(color));
    }

    private static boolean init(Activity mContext,int keyboard_num,KeyBoardEditText edit){
        if(keyboard_num > 4){//键盘模式只能到4
            return false;
        }
        if(!texts.contains(edit)){
            texts.add(edit);
        }
        LayoutInflater factory = LayoutInflater.from(mContext);
        View layout = factory.inflate(R.layout.keyboard_noedit, null);
        ViewGroup vg= (ViewGroup) mContext .getWindow().getDecorView();
        vg.addView(layout);
        text = edit;
//        if(keyboardView == null){
        keyboardView = layout.findViewById(R.id.view_keyboard);
//        }
//        if(viewGroup == null){
        sLinearLayout =layout.findViewById(R.id.layout_main);
//        }
        edit.setKeyboardType(mContext,sLinearLayout,keyboardView,keyboard_num);

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
            sLinearLayout.findViewById(R.id.tv_tip).setBackgroundColor(Color.parseColor(color));
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
            if(KeyBoardUtil.getKeyBoardEditText()!=null){
                KeyBoardUtil.getKeyBoardEditText().hide();
            }
            text.show();
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hide (){
        if(text!=null){
            if(KeyBoardUtil.getKeyBoardEditText()!=null){
                KeyBoardUtil.getKeyBoardEditText().hide();
            }
            text.hide();
        }
    }

}
