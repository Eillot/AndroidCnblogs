package com.arlen.cnblogs.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json {

  // ������һ��json��װ,�������ַ�������
  /**
   * ����˵��: 1.webContent ��ȡ����ҳ��װ��json��ʽ���� 2.key ��������ʽ��ɵ�json�ļ�����
   * */
  public String[] getJSON(String webContent, String[] key) {
    int size = key.length;
    String[] s = new String[size];
    try {
      JSONObject jsonObject = new JSONObject(webContent);
      for (int j = 0; j < size; j++) {
        s[j] = jsonObject.getString(key[j]);
        System.out.println(key[j] + "===string===" + jsonObject.getString(key[j]));
      }
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      s = null;
    }
    return s;
  }

  // ��ȡ�����͵Ľṹ,����ArrayList<HashMap<String, Object>>,����listview���������
  /**
   * ����˵��: 1.webContent ��ȡ����ҳ��װ��json��ʽ���� 2.key ��������ʽ��ɵ�json�ļ����� 3.jsonName ��װjson�������ݵ�json����
   * */

  public ArrayList<HashMap<String, Object>> getJSONArray(String webContent, String[] key,
      String jsonName) {
    ArrayList<HashMap<String, Object>> list;
    JSONArray jsonObject;
    try {
      jsonObject = new JSONObject(webContent).getJSONArray(jsonName);
      list = new ArrayList<HashMap<String, Object>>();
      for (int i = 0; i < jsonObject.length(); i++) {
        JSONObject jsonObject2 = (JSONObject) jsonObject.opt(i);
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (int j = 0; j < key.length; j++) {
          map.put(key[j], jsonObject2.getString(key[j]));
          System.out.println(key[j] + "===" + jsonObject2.getString(key[j]));
        }
        list.add(map);
      }

    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      list = null;
    }
    return list;
  }
}
