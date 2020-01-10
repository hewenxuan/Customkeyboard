package com.mykeyboard;

/**
 * A部分按键code
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.mykeyboard.KeyCodeUtils.java
 * @author: helong
 * @date: 2020-01-09 14:18
 */
public class KeyCodeUtils {
    /**
     * 键盘类型
     */
    public final static int KEYBOARD_TYPE_ZIMU=1;
    public final static int KEYBOARD_TYPE_ZIFU=2;
    public final static int KEYBOARD_TYPE_NUM=3;
    public final static int KEYBOARD_TYPE_ZH=4;

    /**
     * code
     */
    public final static int KEY_COED_FUHAO = -203;// 符号 切换 按键
    public final static int KEY_COED_ABC = -202;// 字母 切换 按键
    public final static int KEY_COED_ZHOREG = -204;//中英文切换 按键
    public final static int KEY_COED_NUM = -201;//数字 切换 按键
    public final static int KEY_COED_HIDE= -800;//软键盘 隐藏code
    public final static int KEY_COED_DAXIAO = -1;//字母大小写切换 按键

    public final static int KEY_COED_ZH = 999;//中文 回调999代表当前是中文输入，取回调第二个参数为 中文

    public final static int KEY_COED_DEL = -5;//删除
    public final static int KEY_COED_ENTER = -205;//回车换行  10
    public final static int KEY_COED_SPACE = 32;// space 切换 按键



}
