package com.example.test.myrecorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private EditText et_name;
    private EditText et_password;
    private Button mLoginBtn;
    private CheckBox checkBox_password;
    private CheckBox checkBox_login;
    private ImageView iv_see_password;

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginBtn = (Button) findViewById(R.id.btn_login);
        et_name = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        checkBox_password = (CheckBox) findViewById(R.id.checkBox_password);
        checkBox_login = (CheckBox) findViewById(R.id.checkBox_login);
        iv_see_password = (ImageView) findViewById(R.id.iv_see_password);
        setupEvents();
        initData();
    }

    private void initData() {

        //判断用户第一次登陆
        if (firstLogin()) {
            checkBox_password.setChecked(false);//取消复选框
            checkBox_login.setChecked(false);
        }
        //判断是否记住密码
        if (remenberPassword()) {
            checkBox_password.setChecked(true);//勾选记住密码
            setTextNameAndPassword();//把密码和账号输入到输入框中
        } else {
            setTextName();//把用户账号放到输入账号的输入框中
        }
        //判断是否自动登录
        if (autoLogin()) {
            checkBox_login.setChecked(true);
            login();
        }
    }

    /**
     * 把本地保存的数据设置数据到输入框中
     */
    public void setTextNameAndPassword() {
        et_name.setText("" + getLocalName());
        et_password.setText("" + getLocalPassword());
    }

    //设置数据到输入框中
    public void setTextName() {
        et_name.setText("" + getLocalName());
    }


    // 获得保存在本地的密码

    // 获得保存在本地的用户名
    public String getLocalName() {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        String name = helper.getString("name");
        return name;
    }

    public String getLocalPassword() {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        String password = helper.getString("password");
        return password;

    }

    /**
     * 判断是否自动登录
     */
    private boolean autoLogin() {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        boolean autoLogin = helper.getBoolean("autoLogin", false);
        return autoLogin;
    }

    /**
     * 判断是否记住密码
     */
    private boolean remenberPassword() {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        boolean remenberPassword = helper.getBoolean("remenberPassword", false);
        return remenberPassword;
    }

    private void setupEvents() {
        mLoginBtn.setOnClickListener(this);
        checkBox_password.setOnCheckedChangeListener(this);
        checkBox_login.setOnCheckedChangeListener(this);
        iv_see_password.setOnClickListener(this);

    }

    /**
     * 判断是否是第一次登陆
     */
    private boolean firstLogin() {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        boolean first = helper.getBoolean("first", true);
        if (first) {
            //创建一个ContentVa对象（自定义的）设置不是第一次登录
            helper.putValues(new SharedPreferencesUtils.ContentValue("first", false),
                    new SharedPreferencesUtils.ContentValue("remenberPassword", false),
                    new SharedPreferencesUtils.ContentValue("autoLogin", false),
                    new SharedPreferencesUtils.ContentValue("name", ""),
                    new SharedPreferencesUtils.ContentValue("password", ""));
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loadUserName();    //保存用户名
                login();
                break;
            case R.id.iv_see_password:
                setPasswordVisibility();    //设置输入框的文本可见或不可见
                break;
        }
    }

    /**
     * 模拟登录情况
     */
    private void login() {

        if (getAccount().isEmpty()) {
            Toast.makeText(this, "你输入的账号为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (getPassword().isEmpty()) {
            Toast.makeText(this, "你输入的密码为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        //登录一般都是请求服务器来判断密码是否正确，要请求网络，要子线程
        // showLoading();//显示加载框
        Thread loginRunnable = new Thread() {

            @Override
            public void run() {
                super.run();
                setLoginBtnClickable(false);//点击登录后，设置登录按钮不可点击状态
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //判断账号和密码
                Ion.with(LoginActivity.this)
                        .load("https://smrtyan.cn/login.php")
                        .setBodyParameter("uname", et_name.getText().toString())
                        .setBodyParameter("pwd", et_password.getText().toString())
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                runOnUiThread(new Runnable() {
                                    public void run() {


                                        try {
                                            JSONObject jsonObject = new JSONObject(result);
                                            if (jsonObject.getString("auth").equals("ok")){
                                                MainMenuActivity.isLoginIn = true;
                                                Toast.makeText(LoginActivity.this, jsonObject.getString("token"), Toast.LENGTH_SHORT).show();
                                                loadCheckBoxState();
                                                SharedPreferencesUtils helper = new SharedPreferencesUtils(LoginActivity.this, "setting");
                                                helper.putValues(new SharedPreferencesUtils.ContentValue("token", jsonObject.getString("token")));
                                                if (MainMenuActivity.isLoginIn) {
                                                    Intent intent = new Intent(LoginActivity.this, CloudFileActivity.class);
                                                    startActivity(intent);
                                                }
                                                finish();
                                            }else {

                                                Toast.makeText(LoginActivity.this, "账号或密码不正确", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
//                if (getAccount().equals("") && getPassword().equals("")) {
//                    //showToast("登录成功");
//                    // loadCheckBoxState();//记录下当前用户记住密码和自动登录的状态;
////                    Intent intent=new Intent();//创建Intent对象
////                    intent.setAction(Intent.ACTION_VIEW);//为Intent设置动作
////                    intent.setData(Uri.parse("https://smarty.cn"));//为Intent设置数据
////                    startActivity(intent);
//                    finish();//关闭页面
//
//
//                } else {
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//
//                            Toast.makeText(LoginActivity.this, "输入的登录账号或密码不正确", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                }
                setLoginBtnClickable(true);  //这里解放登录按钮，设置为可以点击

            }
        };
          loginRunnable.start();
    }

    /**
     * 保存用户账号
     */
    public void loadUserName() {
        if (!getAccount().equals("") || !getAccount().equals("请输入登录账号")) {
            SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
            helper.putValues(new SharedPreferencesUtils.ContentValue("name", getAccount()));
        }
    }

    /**
     * 设置密码可见和不可见的相互转换
     */
    private void setPasswordVisibility() {
        if (iv_see_password.isSelected()) {
            iv_see_password.setSelected(false);
            //密码不可见
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            iv_see_password.setSelected(true);
            //密码可见
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    /**
     * 获取账号
     */
    public String getAccount() {
        return et_name.getText().toString().trim();//去掉空格
    }

    /**
     * 获取密码
     */
    public String getPassword() {
        return et_password.getText().toString().trim();//去掉空格
    }

    /**
     * 保存用户选择“记住密码”和“自动登陆”的状态
     */
    private void loadCheckBoxState() {
        loadCheckBoxState(checkBox_password, checkBox_login);
    }

    /**
     * 保存按钮的状态值
     */
    public void loadCheckBoxState(CheckBox checkBox_password, CheckBox checkBox_login) {

        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");

        //如果设置自动登录
        if (checkBox_login.isChecked()) {
            //创建记住密码和自动登录是都选择,保存密码数据
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("remenberPassword", true),
                    new SharedPreferencesUtils.ContentValue("autoLogin", true),
                    new SharedPreferencesUtils.ContentValue("password", md5(getPassword())));
            //Base64Utils.encryptBASE64(getPassword())
        } else if (!checkBox_password.isChecked()) { //如果没有保存密码，那么自动登录也是不选的
            //创建记住密码和自动登录是默认不选,密码为空
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("remenberPassword", false),
                    new SharedPreferencesUtils.ContentValue("autoLogin", false),
                    new SharedPreferencesUtils.ContentValue("password", ""));
        } else if (checkBox_password.isChecked()) {   //如果保存密码，没有自动登录
            //创建记住密码为选中和自动登录是默认不选,保存密码数据
            helper.putValues(
                    new SharedPreferencesUtils.ContentValue("remenberPassword", true),
                    new SharedPreferencesUtils.ContentValue("autoLogin", false),
                    new SharedPreferencesUtils.ContentValue("password", md5(getPassword())));
        }
    }

    /**
     * 是否可以点击登录按钮
     *
     * @param clickable
     */
    public void setLoginBtnClickable(boolean clickable) {
        mLoginBtn.setClickable(clickable);
    }

    /**
     * CheckBox点击时的回调方法 ,不管是勾选还是取消勾选都会得到回调
     *
     * @param buttonView 按钮对象
     * @param isChecked  按钮的状态
     */
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == checkBox_password) {  //记住密码选框发生改变时
            if (!isChecked) {   //如果取消“记住密码”，那么同样取消自动登陆
                checkBox_login.setChecked(false);
            }
        } else if (buttonView == checkBox_login) {   //自动登陆选框发生改变时
            if (isChecked) {   //如果选择“自动登录”，那么同样选中“记住密码”
                checkBox_password.setChecked(true);
            }
        }
    }

}
