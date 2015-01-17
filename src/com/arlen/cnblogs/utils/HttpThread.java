package com.arlen.cnblogs.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class HttpThread extends Thread {

  private Context context;
  private ProgressDialog proDialog;
  private String url;
  private String[] key;
  private String[] value;
  private String[] jsonKey;
  private String jsonName;


  private String[] array;
  ArrayList<HashMap<String, Object>> list;

  public HttpThread(Context context, ProgressDialog proDialog) {
    this.context = context;
    this.proDialog = proDialog;
  }

  @Override
  public void run() {
    Message msg = handler.obtainMessage();
    HttpPostRequest post = new HttpPostRequest();
    int res = post.requestHttp(url, key, value);
    String webContent = post.getWebContext();
    msg.what = res;
    if (res == 1) {
      // ����json
      Json json = new Json();
      if (jsonName != null)
        // ���������͵�json
        list = json.getJSONArray(webContent, jsonKey, jsonName);
      else
        // ��������jsonֵ
        array = json.getJSON(webContent, jsonKey);
    }
    handler.sendMessage(msg);
  }

  private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      int what = msg.what;
      HttpStatus status = new HttpStatus();
      // ���سɹ�����ʱ
      if (what == 1) {
        // ����������json
        if (list != null) {
          int size = list.size();
          String result = "";
          for (int i = 0; i < size; i++) {
            int s = jsonKey.length;
            // ����jsonKey����ֵ
            for (int j = 0; j < s; j++) {
              result += jsonKey[j] + ":" + list.get(i).get(jsonKey[j]) + "\n";
            }
            result += "\n";
          }
          Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        }
        // �������ַ�json
        if (array != null) {
          int arraySize = array.length;
          String rs = "";
          for (int k = 0; k < arraySize; k++) {
            rs += jsonKey[k] + ":" + array[k] + "\n";
          }
          Toast.makeText(context, rs, Toast.LENGTH_LONG).show();
        }
      }
      // ���ݷ������˷�������,�Զ�����ʾ
      else if (what == 2) {
        status.setTips("�Զ�����ʾ2");
      }// ���ݷ������˷�������,�Զ�����ʾ
      else if (what == 3) {
        status.setTips("�Զ�����ʾ3");
      }
      status.ShowHttpStatusTips(what, context, proDialog);
    }
  };

  // activity���洫�ݵĲ���
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String[] getKey() {
    return key;
  }

  public void setKey(String[] key) {
    this.key = key;
  }

  public String[] getValue() {
    return value;
  }

  public void setValue(String[] value) {
    this.value = value;
  }

  public String[] getJsonKey() {
    return jsonKey;
  }

  public void setJsonKey(String[] jsonKey) {
    this.jsonKey = jsonKey;
  }

  public String getJsonName() {
    return jsonName;
  }

  public void setJsonName(String jsonName) {
    this.jsonName = jsonName;
  }
}
