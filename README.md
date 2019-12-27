# Customkeyboard
自定义软键盘 四种类型（支持唤出系统默认键盘，可多个edittext 对应不同类型不同颜色的键盘）
1数字 2字符  3字母  4随机数字

两种方式，（简单用法在demo的mainactivity有体现，可参考，具体用法看下面）
1.一种自带edittext（主要用法看 KeyBoardUtil.java类 可自行扩展）
  	KeyBoardUtil.initView(this,2,"#30000000",new KeyBoardEditText.OnKeyboardStateChangeListener(){
            @Override
            public void onkeyPress(int primaryCode) {
                Log.i("primaryCode","onPress--"+primaryCode);
                System.out.println("您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")");
                if(primaryCode == Keyboard.KEYCODE_DONE){
                    Toast.makeText(MainActivity.this,KeyBoardUtil.getKeyBoardEditText().getText().toString(),Toast.LENGTH_SHORT).show();
                }else{
					//Toast.makeText(MainActivity.this,"您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")",Toast.LENGTH_SHORT).show();
                }
            }
    });
  

2.需要自己用的时候布局加自定义edittext（主要用法看 KeyBoardUtilNoEdittext.java类 可自行扩展）
	 //方式二  四种键盘模式，可多个deittext 不同样式
    final KeyBoardEditText text1 = (KeyBoardEditText) findViewById(R.id.edText1);
    KeyBoardUtilNoEdittext.initView(this,1,text1,"#3000ff00",new KeyBoardEditText.OnKeyboardStateChangeListener(){
        @Override
        public void onkeyPress(int primaryCode) {
            Log.i("primaryCode","onPress--"+primaryCode);
            System.out.println("您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")");
            if(primaryCode == Keyboard.KEYCODE_DONE){
                Toast.makeText(MainActivity.this,text1.getText().toString(),Toast.LENGTH_SHORT).show();
            }else{
				//Toast.makeText(MainActivity.this,"您按下了："+Character.toString((char) primaryCode)+"("+primaryCode+")",Toast.LENGTH_SHORT).show();
            }
        }
    });
    KeyBoardEditText text2 = (KeyBoardEditText) findViewById(R.id.edText2);
    KeyBoardUtilNoEdittext.initView(this,2,text2,"#300000ff");
    KeyBoardEditText text3 = (KeyBoardEditText) findViewById(R.id.edText3);
    KeyBoardUtilNoEdittext.initView(this,3,text3,"#30ff0000");
    KeyBoardEditText text4 = (KeyBoardEditText) findViewById(R.id.edText4);
    KeyBoardUtilNoEdittext.initView(this,4,text4,"#30ff00ff");


*用法  
1.导入moudelmycustomkeyboard
2.App的 build.gradle dependencies 里面添加 implementation project(':mycustomkeyboard')    点击 Sync Now
3.打完收工 
