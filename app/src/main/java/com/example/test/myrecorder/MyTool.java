package com.example.test.myrecorder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

public class MyTool {
    private static  String APP_ID = "wx3de2a3ff86d8a045";
    private static String RequestURL =  "http://139.199.200.239/upload/issues.mp3";

    public static  void shareMusic(Context context){
        IWXAPI api = WXAPIFactory.createWXAPI(context,APP_ID,true);
        api.registerApp(APP_ID);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.filebox);

        WXMusicObject musicObject = new WXMusicObject();
        musicObject.musicUrl = RequestURL;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = musicObject;
        msg.title = "issues";
        msg.description = "this is a test";

        //设置缩略图
        Bitmap bmp = Bitmap.createScaledBitmap(bitmap,80,80,true);
        bitmap.recycle();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);

        msg.thumbData = baos.toByteArray();

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");// 唯一标识一个请求
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;

        api.sendReq(req);


    }

    public static void shareText(Context context,String text){
        IWXAPI api = WXAPIFactory.createWXAPI(context,APP_ID,true);
        api.registerApp(APP_ID);

        WXTextObject textObject = new WXTextObject();
        textObject.text = text;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObject;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction(text);
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    private static String buildTransaction(String text) {
        return text+String.valueOf(System.currentTimeMillis());
    }
}
