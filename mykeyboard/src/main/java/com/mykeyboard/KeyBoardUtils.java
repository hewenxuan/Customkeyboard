package com.mykeyboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.UiModeManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 软键盘工具类带edittext
 *
 * @version V1.0 <软键盘工具类>
 * @FileName: com.example.mycustomkeyboard.KeyBoardUtils.java
 * @author: helong
 * @date: 2020-1-6 09:57
 */
public class KeyBoardUtils {
    private KeyBoardUtils() {
    }
    public static String DEVICES_TYPE_ANDROID ="android";
    public static String DEVICES_TYPE_TV ="tv";
    private String devices_type = DEVICES_TYPE_ANDROID;
    private int COED_HIDE = -800;//隐藏回调code
    private String text_con = "";
    private Activity mActivity;
    private OnKeyPressListener mListener;
    private static KeyBoardUtils mKeyBoardUtils;
    private int keyboard_Type = -1;//  1 字母（默认小写字母）  2 字符  3 数字
    private boolean isXiaoxie = true;//是否是小写字母
    private boolean isShow = false;
    private View rl_keyboard;//键盘父布局
    private View layout_con;//键盘布局

    private LinearLayout layout_zifu_com;//字母字符键盘布局
    private LinearLayout layout_num;//数字键盘布局

    //公用的29个button
    private Button[] bt_coms = new Button[29];
    private Integer[] bt_coms_ids = {R.id.bt1, R.id.bt2, R.id.bt3, R.id.bt4, R.id.bt5, R.id.bt6, R.id.bt7, R.id.bt8, R.id.bt9,
            R.id.bt10, R.id.bt11, R.id.bt12, R.id.bt13, R.id.bt14, R.id.bt15, R.id.bt16, R.id.bt17, R.id.bt18, R.id.bt19, R.id.bt20,
            R.id.bt21, R.id.bt22, R.id.bt23, R.id.bt24, R.id.bt25, R.id.bt26, R.id.bt27, R.id.bt28, R.id.bt29};
    private String[] zimus_com_X = {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "符", "大写", "z", "x", "c", "v", "b", "n", "m", "删"};
    private String[] zimus_com_D = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", "L", "符", "小写", "Z", "X", "C", "V", "B", "N", "M", "删"};
    private String[] zifus = {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "'", "\"", "=", "_", ":", ";", "?", "~", "|", "·", "+", "-", "\\", "/", "[", "]", "{", "}", "删"};
    //Tag 小写code大写code字符  小写code比大写大32
    private String[] com_codes_tag = {"113code81code33", "119code87code64", "101code69code35", "114code82code36", "116code84code37",
            "121code89code94", "117code85code38", "105code73code42", "111code79code40", "112code80code41",
            "97code65code39", "115code83code34", "100code68code61", "102code70code95", "103code71code58",
            "104code72code59", "106code74code63", "107code75code126", "108code76code124", "-203code-203code183",
            "-1code-1code43", "122code90code45", "120code88code92", "99code67code47", "118code86code91",
            "98code66code93", "110code78code123", "109code77code125", "-5"};

//    private String[] zifus_bttom={"123","@","#","$","%","^","&","*","(",")","'","\"","=","_",":",";","?","~","|","·","+","-","\\","/","[","]","{","}"};

    //字符下面的
    private LinearLayout layout_zifuBttom;
    private Button[] bt_layout_zifuBttoms = new Button[9];
    private Integer[] bt_layout_zifuBttoms_ids = {R.id.bt_zifuBttom1, R.id.bt_zifuBttom2, R.id.bt_zifuBttom3, R.id.bt_zifuBttom4, R.id.bt_zifuBttom5, R.id.bt_zifuBttom6, R.id.bt_zifuBttom7, R.id.bt_zifuBttom8, R.id.bt_zifuBttom9};

    //字母下面的
    private LinearLayout layout_zimuBttom;
    private Button[] bt_layout_zimuBttoms = new Button[4];
    private Integer[] bt_layout_zimuBttoms_ids = {R.id.bt_zimuBttom1, R.id.bt_zimuBttom2, R.id.bt_zimuBttom3, R.id.bt_zimuBttom4};

    //数字按键
    private Button[] bt_layout_num = new Button[14];
    private Integer[] bt_layout_num_ids = {R.id.bt_num1, R.id.bt_num2, R.id.bt_num3, R.id.bt_num4, R.id.bt_num5, R.id.bt_num6, R.id.bt_num7, R.id.bt_num8, R.id.bt_num9, R.id.bt_num10
            , R.id.bt_num11, R.id.bt_num12, R.id.bt_num13, R.id.bt_num14};
    private String[] num_codes_tag = {"49", "50", "51", "-5", "52", "53", "54", "48", "55", "56", "57", "-202", "-11", "-10"};

    //缩放比例
    private float scale_X = 1f;
    private ImageView tv_scale,tv_scale_del;

    private String FouceBg= "#00ff00";//按键获取焦点背景色
    private String bgColor ="#00fffffff";//键盘背景色
    private List<View> views ;

    private boolean isTipClickFocus = false;//0 没有选择中  1 选择中
    private boolean init(final Activity mContext, int keyboard_Type,String devices_type) {
        if (keyboard_Type > 4) {//键盘模式只能到4
            return false;
        }
        this.devices_type = devices_type;
        views =new ArrayList<>();
        views.clear();
        mActivity = mContext;
        this.keyboard_Type = keyboard_Type;
        LayoutInflater factory = LayoutInflater.from(mContext);
        rl_keyboard = factory.inflate(R.layout.content_keyboard_bt, null);
        ViewGroup vg = (ViewGroup) mContext.getWindow().getDecorView();
        vg.addView(rl_keyboard);
        text_con = "";
        isShow = false;
        layout_zimuBttom = rl_keyboard.findViewById(R.id.layout_zimuBttom);
        layout_zifuBttom = rl_keyboard.findViewById(R.id.layout_zifuBttom);
        layout_num = rl_keyboard.findViewById(R.id.layout_num);
        layout_zifu_com = rl_keyboard.findViewById(R.id.layout_zifu_com);
        layout_con = rl_keyboard.findViewById(R.id.layout_con);
        tv_scale = rl_keyboard.findViewById(R.id.tv_scale);

        tv_scale_del =rl_keyboard.findViewById(R.id.tv_scale_del);
        tv_scale_del.setTag("scaleX_del");
        tv_scale_del.setOnClickListener(btnOnClickListener);
        tv_scale_del.setFocusable(true);
        views.add(tv_scale_del);
        tv_scale_del.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b) {
                View parent= (View) view.getParent();
                if(b){
                    parent.setBackgroundColor(Color.parseColor(FouceBg));
                }else{
                    parent.setBackgroundColor(Color.parseColor("#00ffffff"));
                }
            }
        });

        tv_scale.setTag("scaleX_add");
        views.add(tv_scale);
        tv_scale.setOnClickListener(btnOnClickListener);
        tv_scale.setFocusable(true);
        tv_scale.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b) {
                View parent= (View) view.getParent();
                if(b){
                    parent.setBackgroundColor(Color.parseColor(FouceBg));
                }else{
                    parent.setBackgroundColor(Color.parseColor("#00ffffff"));
                }
            }
        });
        Configuration mConfiguration = mActivity.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            rl_keyboard.findViewById(R.id.tv_tip).setVisibility(View.GONE);
            rl_keyboard.findViewById(R.id.tv_line).setVisibility(View.GONE);
            tv_scale.setVisibility(View.GONE);
            tv_scale_del.setVisibility(View.GONE);
        } else {
            rl_keyboard.findViewById(R.id.tv_tip).setVisibility(View.VISIBLE);
            rl_keyboard.findViewById(R.id.tv_line).setVisibility(View.VISIBLE);
            tv_scale.setVisibility(View.VISIBLE);
            tv_scale_del.setVisibility(View.VISIBLE);
            init_touthMove(layout_con);
            setKeyboardWidth(1f);
            if(this.devices_type == DEVICES_TYPE_TV){//TV的话需要处理焦点问题
                rl_keyboard.findViewById(R.id.tv_tip).setFocusable(true);
                rl_keyboard.findViewById(R.id.tv_tip).setClickable(true);
                rl_keyboard.findViewById(R.id.tv_tip).setTag("tv_tip");
                rl_keyboard.findViewById(R.id.tv_tip).setOnFocusChangeListener(new View.OnFocusChangeListener(){
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        View parent= (View) view.getParent();
                        if(b){
                            parent.setBackgroundColor(Color.parseColor(FouceBg));
                        }else{
                            parent.setBackgroundColor(Color.parseColor("#00ffffff"));
                        }
                    }
                });
                rl_keyboard.findViewById(R.id.tv_tip).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        isTipClickFocus=!isTipClickFocus;
                        if(isTipClickFocus ){
                            layout_con.setBackgroundColor(Color.parseColor("#80000000"));
                        }else{
                            layout_con.setBackgroundColor(Color.parseColor(bgColor));
                        }
                        setViewFocus(!isTipClickFocus);
                    }
                });
            }else{//安卓端需要点击其他地方进行隐藏
                rl_keyboard.setClickable(true);
                rl_keyboard.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        hide();
                    }
                });
            }
        }
        init_coms(rl_keyboard);
        init_zimu(rl_keyboard);//初始化字母
        init_zifu(rl_keyboard);//初始化字符
        init_shuzi(rl_keyboard);//初始化数字
        set_type(keyboard_Type);//根据类型选择显示的键盘
        hide();
        set_scaleX(true);
        return true;
    }

    //设置焦点
    private void setViewFocus(boolean b){
        if(views ==null ||views.size()<=0){
            return;
        }
        for(int i=0;i<views.size();i++){
            views.get(i).setFocusable(b);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        //判断当前 tip是否有焦点  没有返回false
        if(isTipClickFocus&& event.getAction() == KeyEvent.ACTION_DOWN){
//          System.out.println("您按下了dispatchKeyEvent："+event.getKeyCode());
            screenHeight = ScreenUtils.getScreenHeight(mActivity);//获取屏幕宽度
            screenWidth = ScreenUtils.getScreenWidth(mActivity);//屏幕高度-状态栏
//            System.out.println("===_状态栏=" +ScreenUtils.getStatusHeight(mActivity) );
//            System.out.println("===_应用宽=" + screenWidth + "应用高：" + screenHeight);
//            System.out.println("===_键盘宽=" + layout_con.getWidth() + "键盘高：" + layout_con.getHeight());
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layout_con.getLayoutParams();
            float top = layout_con.getX();
            float left = layout_con.getY();
//            System.out.println("====X=="+layout_con.getX()+ "====Y="+layout_con.getY());
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    layout_con.setY(layout_con.getY()+10f);
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    layout_con.setY(layout_con.getY()-10f);
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    layout_con.setX(layout_con.getX()+10f);
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    layout_con.setX(layout_con.getX()-10f);
                    break;
            }
            if(layout_con.getX()<0){
                layout_con.setX(0f);
            }
            if(layout_con.getY()<0){
                layout_con.setY(0f);
            }
            if(layout_con.getX() > screenWidth-layout_con.getWidth()){
                layout_con.setX(screenWidth-layout_con.getWidth());
            }
            if(layout_con.getY() > screenHeight-layout_con.getHeight()){
                layout_con.setY(screenHeight-layout_con.getHeight());
            }
            return true;
        }
        return false;
    }

    /**
     * 设置缩放
     *
     * @param
     */
//    private void set_scaleX() {
//        if (layout_con != null) {
//            this.scale_X = this.scale_X - 0.1f;
//            if (this.scale_X < 0.4f) {
//                this.scale_X = 1.0f;
//            }
//            setKeyboardWidth(this.scale_X);
//        }
//    }
    private void set_scaleX() {
        if (layout_con != null) {
            if(isSaleAdd){//正在放大
                this.scale_X = this.scale_X + 0.1f;
                if (this.scale_X >= 1f) {
                    this.scale_X =1f;
                }
            }else{//正在缩小
                this.scale_X = this.scale_X - 0.1f;
                if (this.scale_X <= 0.5f) {
                    this.scale_X =0.5f;
                }
            }
            setKeyboardWidth(this.scale_X);
        }
    }
    private void set_scaleX(boolean b) {
        if (layout_con != null) {
            if(b){//正在放大
                this.scale_X = this.scale_X + 0.1f;
                if (this.scale_X >= 1f) {
                    this.scale_X =1f;
                    //变大图标不可点击
                    tv_scale.setClickable(false);
                    tv_scale.setBackgroundResource(R.drawable.img_scale_add_disable);
                }else{
                    //变大图标可点击
                    tv_scale.setClickable(true);
                    tv_scale.setBackgroundResource(R.drawable.img_scale_add);
                }
                tv_scale_del.setClickable(true);
                tv_scale_del.setBackgroundResource(R.drawable.img_scale_del);
            }else{//正在缩小
                this.scale_X = this.scale_X - 0.1f;
                if (this.scale_X <= 0.5f) {
                    this.scale_X =0.5f;
                    //变小图标不可点击
                    tv_scale_del.setClickable(false);
                    tv_scale_del.setBackgroundResource(R.drawable.img_scale_del_disable);
                }else{
                    //变小图标可点击
                    tv_scale_del.setClickable(true);
                    tv_scale_del.setBackgroundResource(R.drawable.img_scale_del);
                }
                tv_scale.setClickable(true);
                tv_scale.setBackgroundResource(R.drawable.img_scale_add);
            }
            setKeyboardWidth(this.scale_X);
        }
    }

    private void init_coms(View view) {
        //findViewById  tag
        for (int i = 0; i < bt_coms_ids.length; i++) {
            bt_coms[i] = view.findViewById(bt_coms_ids[i]);
            bt_coms[i].setOnClickListener(btnOnClickListener);
            bt_coms[i].setOnFocusChangeListener(mOnFocusChangeListener);
            views.add(bt_coms[i]);
            bt_coms[i].setTag(com_codes_tag[i]);
        }
    }

    private View.OnFocusChangeListener mOnFocusChangeListener =new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
              if(b){
                  view.setBackgroundColor(Color.parseColor(FouceBg));
              }else{
                  view.setBackgroundResource(R.drawable.selector_keyboard_keys);
              }
        }
    };

    private void init_zimu(View view) {
        //字母最后一行
        for (int i = 0; i < bt_layout_zimuBttoms_ids.length; i++) {
            bt_layout_zimuBttoms[i] = view.findViewById(bt_layout_zimuBttoms_ids[i]);
            bt_layout_zimuBttoms[i].setOnClickListener(btnOnClickListener);
            bt_layout_zimuBttoms[i].setOnFocusChangeListener(mOnFocusChangeListener);
            views.add(bt_layout_zimuBttoms[i]);
        }
    }

    private void init_zifu(View view) {
        //字符最后一行
        for (int i = 0; i < bt_layout_zifuBttoms_ids.length; i++) {
            bt_layout_zifuBttoms[i] = view.findViewById(bt_layout_zifuBttoms_ids[i]);
            bt_layout_zifuBttoms[i].setOnClickListener(btnOnClickListener);
            bt_layout_zifuBttoms[i].setOnFocusChangeListener(mOnFocusChangeListener);
            views.add(bt_layout_zifuBttoms[i]);
        }
    }

    private void init_shuzi(View view) {
        //数字键盘初始化
        for (int i = 0; i < bt_layout_num_ids.length; i++) {
            bt_layout_num[i] = view.findViewById(bt_layout_num_ids[i]);
            bt_layout_num[i].setOnClickListener(btnOnClickListener);
            bt_layout_num[i].setOnFocusChangeListener(mOnFocusChangeListener);
            views.add(bt_layout_num[i]);
            bt_layout_num[i].setTag(num_codes_tag[i]);
        }
    }

    private void set_type(int keyboard_Type) {
        this.keyboard_Type = keyboard_Type;
        layout_zifu_com.setVisibility(View.VISIBLE);
        layout_num.setVisibility(View.GONE);
        switch (keyboard_Type) {
            case 1://字母
                isXiaoxie = true;
                for (int i = 0; i < zimus_com_X.length; i++) {
                    bt_coms[i].setText(zimus_com_X[i]);
                }
                layout_zifuBttom.setVisibility(View.GONE);
                layout_zimuBttom.setVisibility(View.VISIBLE);
                bt_coms[0].requestFocus();
                break;
            case 2://字符
                for (int i = 0; i < zifus.length; i++) {
                    bt_coms[i].setText(zifus[i]);
                }
                layout_zifuBttom.setVisibility(View.VISIBLE);
                layout_zimuBttom.setVisibility(View.GONE);
                bt_coms[0].requestFocus();
                break;
            case 3://数字
                layout_zifu_com.setVisibility(View.GONE);
                layout_num.setVisibility(View.VISIBLE);
                bt_layout_num[0].requestFocus();
                break;
            default:
                break;
        }

        setNextFouce();
    }
    //如果是tv 需要优化焦点问题  ，左右两边焦点到头处理
    private void setNextFouce() {
        if(this.devices_type == DEVICES_TYPE_ANDROID){
            return;
        }
        rl_keyboard.findViewById(R.id.tv_tip).setNextFocusRightId(R.id.tv_scale);
        tv_scale.setNextFocusLeftId(R.id.tv_tip);

        bt_coms[0].setNextFocusLeftId(R.id.bt10);//q 左边是 p
        bt_coms[9].setNextFocusRightId(R.id.bt1);//p 右边是 q

        bt_coms[10].setNextFocusLeftId(R.id.bt20);//a 左边是 符
        bt_coms[19].setNextFocusRightId(R.id.bt11);//符 右边是 a

        bt_coms[20].setNextFocusLeftId(R.id.bt29);//大写 左边是 删除
        bt_coms[28].setNextFocusRightId(R.id.bt21);//删除 右边是 大写

//        if(this.keyboard_Type == 4){//这个分支暂时不支持中文
//            bt_coms[21].setNextFocusLeftId(R.id.bt29);//z 左边是 删除
//            bt_coms[28].setNextFocusRightId(R.id.bt22);//删除 右边是 z
//        }

        bt_layout_zifuBttoms[0].setNextFocusLeftId(R.id.bt_zifuBttom9);//123 左边是 abc
        bt_layout_zifuBttoms[8].setNextFocusRightId(R.id.bt_zifuBttom1);//abc 右边是 123

        bt_layout_zimuBttoms[0].setNextFocusLeftId(R.id.bt_zimuBttom4);//123 左边是 回车
        bt_layout_zimuBttoms[3].setNextFocusRightId(R.id.bt_zimuBttom1);//回车 右边是 123


        bt_layout_num[0].setNextFocusLeftId(R.id.bt_num4);//1 左边是 删除
        bt_layout_num[3].setNextFocusRightId(R.id.bt_num1);//删除 右边是 1

        bt_layout_num[4].setNextFocusLeftId(R.id.bt_num8);//4 左边是 0
        bt_layout_num[7].setNextFocusRightId(R.id.bt_num5);//0 右边是 4

        bt_layout_num[8].setNextFocusLeftId(R.id.bt_num12);//7 左边是 abc
        bt_layout_num[11].setNextFocusRightId(R.id.bt_num9);//abc 右边是 7

        bt_layout_num[12].setNextFocusLeftId(R.id.bt_num14);//space 左边是 回车
        bt_layout_num[13].setNextFocusRightId(R.id.bt_num13);//回车 右边是 space


    }

    public static KeyBoardUtils getInstance() {
        if (mKeyBoardUtils == null) {
            mKeyBoardUtils = new KeyBoardUtils();
        }
        return mKeyBoardUtils;
    }

    private OnClickListener btnOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
//            System.out.println("您按下了："+view.getTag());
            int code = -999;
            if (view.getTag() != null) {
                if (view.getTag().toString().trim().equals("scaleX_add")) {
                    set_scaleX(true);
                    return;
                }
                if (view.getTag().toString().trim().equals("scaleX_del")) {
                    set_scaleX(false);
                    return;
                }
                if (view.getTag().toString().contains("code")) {
                    String[] strs = view.getTag().toString().split("code");
                    if (keyboard_Type == 1) {//字母
                        if (isXiaoxie) {
                            code = Integer.parseInt(strs[0]);
                        } else {
                            code = Integer.parseInt(strs[1]);
                        }
                    } else if (keyboard_Type == 2) {//字符
                        code = Integer.parseInt(strs[2]);
                    }
                } else {
                    code = Integer.parseInt(view.getTag().toString());
                }
            }
//            System.out.println("您按下了："+Character.toString((char) code)+"("+code+")");
            if (code > 0) {
//                text_con = text_con + Character.toString((char) code);
                text_con = Character.toString((char) code);
            }
            switch (code) {
                case Keyboard.KEYCODE_DELETE://删除
                    if (text_con.length() >= 1) {
                        text_con = text_con.substring(0, text_con.length() - 1);
                    }
                    break;
                case -201://切换数字键盘
                    set_type(3);
                    break;
                case -202://切换字母键盘
                    set_type(1);
                    break;
                case -203://切换字符键盘
                    set_type(2);
                    break;
                case -204://切换系统默认键盘
                    break;
                case -10://回车
//                    hide();
                    break;
                case Keyboard.KEYCODE_SHIFT://大小写切换
                    changeCapital(!isXiaoxie);
                    break;
                default:
                    break;
            }
            System.out.println("您按下了=text_con=：" + text_con);
            if (mListener != null && code > -200) {
                mListener.onkeyPress(code, text_con);
            }
        }
    };

    //大小写切换
    private void changeCapital(boolean isXiaoxie) {
        this.isXiaoxie = isXiaoxie;
        String[] strs;

        if (isXiaoxie) {
            strs = zimus_com_X;
        } else {
            strs = zimus_com_D;
        }
        for (int i = 0; i < bt_coms.length; i++) {
            bt_coms[i].setText(strs[i]);
        }
    }

    private int screenWidth, screenHeight;
    private int lastX, lastY;

    private void init_touthMove(final View view) {
        screenHeight = ScreenUtils.getScreenHeight(mActivity);//获取屏幕宽度
        screenWidth = ScreenUtils.getScreenWidth(mActivity);//屏幕高度-状态栏
        System.out.println("helong_应用宽=" + screenWidth + "应用高：" + screenHeight);
        view.setOnTouchListener(null);
        view.setOnTouchListener(new View.OnTouchListener() {
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
                        if (top >= screenHeight - view.getHeight()) {
                            top = screenHeight - view.getHeight();
                        }
                        if (left >= screenWidth - view.getWidth()) {
                            left = screenWidth - view.getWidth();
                        }
                        if (left < 0) {
                            left = 0;
                        }
                        if (top < ScreenUtils.getStatusHeight(mActivity)) {
                            top = ScreenUtils.getStatusHeight(mActivity);
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
    }

    public interface OnKeyPressListener {
        void onkeyPress(int primaryCode, String text);
    }

    /**
     * 设置键盘背景色
     *
     * @param bgColor 键盘背景色 #10000000
     */
    public void setKeyboardBgColor(String bgColor) {
        if (layout_con != null) {
            this.bgColor=bgColor;
            layout_con.setBackgroundColor(Color.parseColor(bgColor));
        }
    }

    /**
     * 初始化键盘view
     *
     * @param mContext     Activity
     * @param keyboard_num 显示键盘类型 1数字 2字母 3特殊字符 4 随机数字
     * @param devices_type  设备类型 安卓orTV  android  tv
     *
     */
    public void initView(Activity mContext, int keyboard_num,String devices_type) {
        if (!init(mContext, keyboard_num,devices_type)) {
            return;
        }
    }

    /**
     * 初始化键盘view
     *
     * @param mContext     Activity
     * @param keyboard_num 显示键盘类型 1数字 2字母 3特殊字符 4 随机数字
     * @param devices_type  设备类型 安卓orTV  android  tv
     * @param color        键盘背景色 #10000000
     *
     */
    public void initView(Activity mContext, int keyboard_num ,String color,String devices_type) {
        if (!init(mContext, keyboard_num, devices_type)) {
            return;
        }
        setKeyboardBgColor(color);
    }

    /**
     * 设置软键盘刚弹出的时候显示字母键盘还是数字键盘
     *
     * @param keyboard_num 显示键盘类型 1数字 2字母 3特殊字符 4 随机数字
     */
    public void setKeyboardType(int keyboard_num) {
        if (rl_keyboard == null) {
            return;
        }
        set_type(keyboard_num);
    }

    /**
     * 显示软键盘
     */
    public void show(final String color) {
        if (rl_keyboard == null|| rl_keyboard.getVisibility()==View.VISIBLE) {
            return;
        }
        text_con = "";
        //设置动画，从自身位置的最下端向上滑动了自身的高度，持续时间为500ms
        final TranslateAnimation ctrlAnimation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 1, TranslateAnimation.RELATIVE_TO_SELF, 0);
        ctrlAnimation.setDuration(400l);     //设置动画的过渡时间
        rl_keyboard.postDelayed(new Runnable() {
            @Override
            public void run() {
                rl_keyboard.setVisibility(View.VISIBLE);
                setKeyboardBgColor(color);
                set_type(keyboard_Type);//根据类型选择显示的键盘
                rl_keyboard.startAnimation(ctrlAnimation);
            }
        }, 100);
    }

    /**
     * 显示软键盘
     */
    public void show() {
        if (rl_keyboard == null|| rl_keyboard.getVisibility()==View.VISIBLE) {
            return;
        }
        text_con = "";
        //设置动画，从自身位置的最下端向上滑动了自身的高度，持续时间为500ms
        final TranslateAnimation ctrlAnimation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 1, TranslateAnimation.RELATIVE_TO_SELF, 0);
        ctrlAnimation.setDuration(400l);     //设置动画的过渡时间
        rl_keyboard.postDelayed(new Runnable() {
            @Override
            public void run() {
                rl_keyboard.setVisibility(View.VISIBLE);
                set_type(keyboard_Type);//根据类型选择显示的键盘
                rl_keyboard.startAnimation(ctrlAnimation);
            }
        }, 100);
    }

    /**
     * 隐藏软键盘
     */
    public void hide() {
        if (rl_keyboard == null || rl_keyboard.getVisibility()!=View.VISIBLE) {
            return;
        }
        //设置动画，从自身位置的最下端向上滑动了自身的高度，持续时间为500ms
        final TranslateAnimation ctrlAnimation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 1);
        ctrlAnimation.setDuration(400l);     //设置动画的过渡时间
        rl_keyboard.postDelayed(new Runnable() {
            @Override
            public void run() {
                rl_keyboard.setVisibility(View.GONE);
                isShow = false;
                rl_keyboard.startAnimation(ctrlAnimation);
                if(mListener!=null){
                    mListener.onkeyPress(COED_HIDE,"");
                }
            }
        }, 100);
    }

    /**
     * 按键监听
     *
     * @param listener
     */
    public void setOnKeyPressListenerListener(OnKeyPressListener listener) {
        this.mListener = listener;
    }
    private boolean isSaleAdd=false;
    /**
     * 横屏设置键盘宽度
     *
     * @param num float类型   0f - 1f
     */
    public void setKeyboardWidth(float num) {
        if (layout_con == null) {
            return;
        }
        this.scale_X = num;
        Configuration mConfiguration = mActivity.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
            return;
        }
        if (num >= 1) {//最大等于宽
            num = 1;
//            tv_scale.setBackgroundResource(R.drawable.img_scale_del);
            isSaleAdd =false;
        }
        if (num <= 0.5) {//最小0.5倍    0.45也可以
            num = 0.5f;
//            tv_scale.setBackgroundResource(R.drawable.img_scale_add);
            isSaleAdd =true;
        }
        int width = ScreenUtils.getScreenWidth(mActivity);
        int height = ScreenUtils.getScreenHeight(mActivity) - ScreenUtils.getStatusHeight(mActivity);//屏幕高度-状态栏
        RelativeLayout.LayoutParams Params = (RelativeLayout.LayoutParams) layout_con.getLayoutParams();
        Params.width = (int) (width * num);
        System.out.println("屏幕宽= "+width+" Params.width= "+ Params.width +"  缩放倍数= "+num);
        Params.height= height /2;
//        Params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        Params.addRule(RelativeLayout.CENTER_VERTICAL);
//        Params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        if(num == 1){
            Params.leftMargin=0;
            Params.rightMargin=0;
            if(devices_type == DEVICES_TYPE_TV){
                layout_con.setX(0);//安卓掉用这个会显示不正常
            }
        }
        layout_con.setLayoutParams(Params);
        //把得到的值保留1位小数四舍五入
        BigDecimal bg3 = new BigDecimal(num);
        double f3 = bg3.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
//        tv_scale.setText("X" + f3);
        layout_con.invalidate();
    }

    /**
     * 设置按键获取焦点颜色
     * @param bgColor  默认  "#00ff00"
     */
    public void setFouceBg(String bgColor){
        this.FouceBg=bgColor;
    }

}
