package com.example.mycustomkeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.input.InputManager;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;

import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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

    public  int KeyBoard_NUM =1;
    public  int KeyBoard_LETTER =2;
    public  int KeyBoard_SMBOL =3;
    public  int KeyBoard_Random_NUM =4;

    public  int NowKeyBoardType = 1;
    private Activity mContext;

    private  LinearLayout viewGroup;
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
        keyboardRandomNumber = new Keyboard(getContext(), R.xml.keyboard_num);
        for (int i=0; i<arrays.length; i++) {
            noLists.add(arrays[i]);
        }
    }

    private void randomKeyboardNumber() {
        List<Keyboard.Key> keyList = keyboardRandomNumber.getKeys();
        // 查找出0-9的数字键
        List<Keyboard.Key> newkeyList = new ArrayList<Keyboard.Key>();
        for (int i = 0; i < keyList.size(); i++) {
            if (keyList.get(i).label != null
                    && isNumber(keyList.get(i).label.toString())) {
                newkeyList.add(keyList.get(i));
            }
        }
        // 数组长度
        int count = newkeyList.size();
        // 结果集
        List<KeyModel> resultList = new ArrayList<KeyModel>();
        // 用一个LinkedList作为中介
        LinkedList<KeyModel> temp = new LinkedList<KeyModel>();
        // 初始化temp
        for (int i = 0; i < count; i++) {
            temp.add(new KeyModel(48 + i, i + ""));
        }
        // 取数
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int num = rand.nextInt(count - i);
            resultList.add(new KeyModel(temp.get(num).getCode(),
                    temp.get(num).getLable()));
            temp.remove(num);
        }
        for (int i = 0; i < newkeyList.size(); i++) {
            newkeyList.get(i).label = resultList.get(i).getLable();
            newkeyList.get(i).codes[0] = resultList.get(i)
                    .getCode();
        }
        //   hideKeyBoard();
        keyboardView.setKeyboard(keyboardRandomNumber);
    }

    private boolean isNumber(String str) {
        String wordstr = "0123456789";
        return wordstr.contains(str);
    }

    private int screenWidth,screenHeight;
    private int lastX,lastY;

    /**
     * 设置软键盘刚弹出的时候显示字母键盘还是数字键盘
     * @param vg 包裹KeyboardView的ViewGroup
     * @param kv KeyboardView
     * @param keyboard_num 键盘模式
     */
    public void setKeyboardType (Activity mContext,LinearLayout vg, KeyboardView kv, int keyboard_num) {
        this.NowKeyBoardType=keyboard_num;
        this.mContext=mContext;
        viewGroup = vg;


        screenWidth = ScreenUtils.getScreenWidth(getContext());//获取屏幕宽度
        screenHeight = ScreenUtils.getScreenHeight(getContext()) - ScreenUtils.getStatusHeight(getContext());//屏幕高度-状态栏
         
        viewGroup.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        int top = v.getTop() + dy;
                        int left = v.getLeft() + dx;
                        if (top <= 0) {
                            top = 0;
                        }
                        if (top >= screenHeight - viewGroup.getHeight() ) {
                            top = screenHeight - viewGroup.getHeight();
                        }
                        if (left >= screenWidth - viewGroup.getWidth()) {
                            left = screenWidth - viewGroup.getWidth();
                        }
                        if (left <= 0) {
                            left = 0;
                        }
                        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(v.getWidth(), v.getHeight());
                        param.leftMargin = left;
                        param.topMargin = top;
                        v.setLayoutParams(param);
//                        v.layout(left, top, left+v.getWidth(), top+v.getHeight());
                        v.postInvalidate();
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });

        setPos();
        keyboardView = kv;
        if (keyboard_num ==KeyBoard_NUM) {
            keyboardView.setKeyboard(keyboardNumber);
        } else if (keyboard_num ==KeyBoard_LETTER) {
            keyboardView.setKeyboard(keyboardLetter);
        }else if (keyboard_num ==KeyBoard_SMBOL){
            keyboardView.setKeyboard(keyboardSymbol);
        }else if (keyboard_num ==KeyBoard_Random_NUM){
            keyboardView.setKeyboard(keyboardRandomNumber);
            randomKeyboardNumber();
        }

        //显示预览
        keyboardView.setPreviewEnabled(true);
        //为KeyboardView设置按键监听
        keyboardView.setOnKeyboardActionListener(this);
    }

    private void setPos(){
        //System.out.println("-------------2"+viewGroup.getLayoutParams());

        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向

        int width =ScreenUtils.getScreenWidth(getContext());
        int height =ScreenUtils.getScreenHeight(getContext()) - ScreenUtils.getStatusHeight(getContext());//屏幕高度-状态栏
        RelativeLayout.LayoutParams Params = (RelativeLayout.LayoutParams) viewGroup.getLayoutParams();
        if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            Params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            viewGroup.setOnTouchListener(null);
            viewGroup.findViewById(R.id.tv_tip).setVisibility(View.GONE);//竖屏不让拖动
            viewGroup.findViewById(R.id.line).setVisibility(View.GONE);
        }else{
            Params.width =height;
            Params.rightMargin=30;
//            Params.topMargin=100;
//            Params.bottomMargin=(height-Params.width)/2;
//            Params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            Params.addRule(RelativeLayout.CENTER_VERTICAL);
            Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        }
        viewGroup.setLayoutParams(Params);
    }

    /**
     * 设置软键盘刚弹出的时候显示字母键盘还是数字键盘
     * @param keyboard_num 键盘模式
     */
    public void setKeyboardType (int keyboard_num) {
        this.NowKeyBoardType=keyboard_num;
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
                if (listener != null) {
                    listener.onkeyPress(primaryCode);
                }
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
        if(KeyBoardUtilNoEdittext.texts.size()>0){
            for(KeyBoardEditText t:KeyBoardUtilNoEdittext.texts){
                t.hide();
            }
        }
        if(KeyBoardUtil.getKeyBoardEditText()!=null){
            KeyBoardUtil.getKeyBoardEditText().hide();
        }
        if(keyboardView ==null||viewGroup ==null){
            return;
        }
        if(this.NowKeyBoardType == KeyBoard_Random_NUM){
            randomKeyboardNumber();
        }
        System.out.println("keyboardView.getKeyboard()="+keyboardView.getKeyboard());
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
//        return super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            show();
        }else{
            return super.onTouchEvent(event);
        }
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
//        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
//        View v = mContext.getWindow().peekDecorView();
//        if (null != v) {
//            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//        }
    }
}
