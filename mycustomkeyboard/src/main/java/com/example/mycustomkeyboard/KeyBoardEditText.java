package com.example.mycustomkeyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.input.InputManager;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;

import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by zd on 2018/4/2.
 */

public class KeyBoardEditText extends EditText implements KeyboardView.OnKeyboardActionListener {

    /**数字键盘*/
    private Keyboard keyboardNumber;
    /**字母键盘*/
    private Keyboard keyboardLetter;
    /**特殊字符键盘*/
    private Keyboard keyboardSymbol;
    /**随机数字键盘*/
    private Keyboard keyboardRandomNumber;

    public static int KeyBoard_NUM =1;
    public static int KeyBoard_LETTER =2;
    public static int KeyBoard_SMBOL =3;
    public static int KeyBoard_Random_NUM =4;
    private Activity mContext;

    private  ViewGroup viewGroup;
    private  KeyboardView keyboardView;

    /**是否为大写*/
    public static boolean isCapital = false;
    private int[] arrays = new int[]{Keyboard.KEYCODE_SHIFT, Keyboard.KEYCODE_MODE_CHANGE,
            Keyboard.KEYCODE_CANCEL, Keyboard.KEYCODE_DONE, Keyboard.KEYCODE_DELETE,
            Keyboard.KEYCODE_ALT, 32};
    private List<Integer> noLists = new ArrayList<>();
    private OnKeyboardStateChangeListener listener;

    public KeyBoardEditText(Context context) {
        super(context);
        initEditView();
    }

    public KeyBoardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initEditView();
    }

    public KeyBoardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initEditView();
    }

    /**初始化数字和字母键盘和特殊字符键盘*/
    private void initEditView() {
        keyboardNumber = new Keyboard(getContext(), R.xml.keyboard_num);
        keyboardLetter = new Keyboard(getContext(), R.xml.keyboard_letter);
        keyboardSymbol = new Keyboard(getContext(), R.xml.keyboard_symbol);
        keyboardRandomNumber = new Keyboard(getContext(), R.xml.keyboard_random_num);


        for (int i=0; i<arrays.length; i++) {
            noLists.add(arrays[i]);
        }
    }


    /**
     * 设置软键盘刚弹出的时候显示字母键盘还是数字键盘
     * @param vg 包裹KeyboardView的ViewGroup
     * @param kv KeyboardView
     * @param keyboard_num 键盘模式
     */
    public void setKeyboardType (Activity mContext,ViewGroup vg, KeyboardView kv, int keyboard_num) {
        this.mContext=mContext;
        viewGroup = vg;
        keyboardView = kv;
        if (keyboard_num ==KeyBoard_NUM) {
            keyboardView.setKeyboard(keyboardNumber);
        } else if (keyboard_num ==KeyBoard_LETTER) {
            keyboardView.setKeyboard(keyboardLetter);
        }else if (keyboard_num ==KeyBoard_SMBOL){
            keyboardView.setKeyboard(keyboardSymbol);
        }else if (keyboard_num ==KeyBoard_Random_NUM){
            keyboardView.setKeyboard(keyboardRandomNumber);
        }

        //显示预览
        keyboardView.setPreviewEnabled(true);
        //为KeyboardView设置按键监听
        keyboardView.setOnKeyboardActionListener(this);
    }
    /**
     * 设置软键盘刚弹出的时候显示字母键盘还是数字键盘
     * @param keyboard_num 键盘模式
     */
    public void setKeyboardType (int keyboard_num) {
        if (keyboard_num ==KeyBoard_NUM) {
            keyboardView.setKeyboard(keyboardNumber);
        } else if (keyboard_num ==KeyBoard_LETTER) {
            keyboardView.setKeyboard(keyboardLetter);
        }else if (keyboard_num ==KeyBoard_SMBOL){
            keyboardView.setKeyboard(keyboardSymbol);
        }else if (keyboard_num ==KeyBoard_Random_NUM){
            keyboardView.setKeyboard(keyboardRandomNumber);
        }

        //显示预览
        keyboardView.setPreviewEnabled(true);
        //为KeyboardView设置按键监听
        keyboardView.setOnKeyboardActionListener(null);
        keyboardView.setOnKeyboardActionListener(this);
    }

    public void setOnKeyBoardStateChangeListener(OnKeyboardStateChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onPress(int primaryCode) {
        if (listener != null && primaryCode> -200) {
            listener.onkeyPress(primaryCode);
        }
        canShowPreview(primaryCode);
    }

    /**
     * 判断是否需要预览Key
     *
     * @param primaryCode keyCode
     */
    private void canShowPreview(int primaryCode) {

        if (noLists.contains(primaryCode)) {
            keyboardView.setPreviewEnabled(false);
        } else {
            keyboardView.setPreviewEnabled(true);
        }
    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
//        if (listener != null && primaryCode> -200) {
//            listener.onkeyPress(primaryCode);
//        }
        final Editable editable = getText();
        int start = getSelectionStart();
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE://删除
                if (editable != null && editable.length() > 0 && start > 0) {
                    editable.delete(start-1, start);
                }
                break;
            case -201://切换数字键盘
                keyboardView.setKeyboard(keyboardNumber);
                break;
            case -202://切换字母键盘
                keyboardView.setKeyboard(keyboardLetter);
                break;
            case -203://切换字符键盘
                keyboardView.setKeyboard(keyboardSymbol);
                break;
            case -204://切换系统默认键盘
                showInput(this);
                break;
            case Keyboard.KEYCODE_DONE://完成
                keyboardView.setVisibility(View.GONE);
                viewGroup.setVisibility(GONE);

                break;
            case Keyboard.KEYCODE_SHIFT://大小写切换
                changeCapital(!isCapital);
                keyboardView.setKeyboard(keyboardLetter);
                break;
            default:
                editable.insert(start, Character.toString((char)primaryCode));
                break;
        }
    }

    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    public void showInput(final EditText et) {
        et.setVisibility(View.VISIBLE);
        et.requestFocus();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
//                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
//                imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);

//                InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.showSoftInput(et, 0);

                InputMethodManager imm=(InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 100);
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput(final EditText et) {
        et.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        View v = mContext.getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**切换键盘大小写*/
    private void changeCapital(boolean b) {

        isCapital = b;
        List<Keyboard.Key> lists = keyboardLetter.getKeys();
        for (Keyboard.Key key: lists) {
            if (key.label != null && isKey(key.label.toString())) {
                if (isCapital) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0] - 32;
                } else  {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0] + 32;
                }
            } else if (key.label != null && key.label.toString().equals("小写")) {
                key.label = "大写";
            } else if (key.label != null && key.label.toString().equals("大写")) {
                key.label = "小写";
            }
        }
    }

    /** * 判断此key是否正确，且存在 * * @param key * @return */
    private boolean isKey(String key) {
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        if (lowercase.indexOf(key.toLowerCase()) > -1) {
            return true;
        }
        return false;
    }



    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    public interface OnKeyboardStateChangeListener {
        void onkeyPress(int primaryCode);
    }

    public void show(){
        if(keyboardView ==null||viewGroup ==null){
            return;
        }
        this.requestFocus();
        keyboardView.setVisibility(VISIBLE);
        viewGroup.setVisibility(VISIBLE);

    }

    public void hide(){
        keyboardView.setVisibility(View.GONE);
        viewGroup.setVisibility(GONE);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideSystemSoftInput();
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            if (keyboardView.getVisibility() != VISIBLE) {
//                keyboardView.setVisibility(VISIBLE);
//                viewGroup.setVisibility(VISIBLE);
//                if (listener != null)
//                listener.show();
//            }
//        }
        if(KeyBoardUtilNoEdittext.getKeyBoardEditText()!=null){
            KeyBoardUtilNoEdittext.getKeyBoardEditText().hide();
        }
        if(KeyBoardUtil.getKeyBoardEditText()!=null){
            KeyBoardUtil.getKeyBoardEditText().hide();
        }
        show();
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            if (keyboardView.getVisibility() == VISIBLE) {
//                hide();
//            } else {
//                show();
//            }
//        }
//        hide();
        return true;
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && (viewGroup.getVisibility() != GONE
        || keyboardView.getVisibility() != GONE)) {
            viewGroup.setVisibility(GONE);
            keyboardView.setVisibility(GONE);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        hideSystemSoftInput();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        hideSystemSoftInput();
    }

    /**隐藏系统软键盘*/
    private void hideSystemSoftInput() {
//        InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        manager.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
