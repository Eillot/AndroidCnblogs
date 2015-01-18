package com.arlen.cnblogs.utils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class AppUtils {

  /**
   * ���ַ���ת��ΪURL����
   * 
   * @param string
   * @return URL
   */
  public static URL parseStringToUrl(String string) {
    URL url = null;
    try {
      Log.i("parseStringToUrl", "ת��:" + string);
      url = new URL(string);
      Log.i("parseStringToUrl", "ת���ɹ�");
    } catch (MalformedURLException e) {
      Log.e("parseStringToUrl", "ת��ʧ��");
      e.printStackTrace();
    }
    return url;
  }

  /**
   * ��Stringת��ΪDate����
   * 
   * @param string
   * @return Date
   */
  @SuppressLint("SimpleDateFormat")
  public static Date parseStringToDate(String string) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(AppMacros.SIMPLE_DATA_FORMATE);
    Date date = null;
    try {
      date = dateFormat.parse(string);
    } catch (ParseException e) {
      Log.e("parseStringToDate", "ת��ʧ��");
      e.printStackTrace();
    }
    return date;
  }

  /**
   * ��Dateת��ΪString����
   * 
   * @param date
   * @return String
   */
  @SuppressLint("SimpleDateFormat")
  public static String parseDateToString(Date date) {
    try {
      SimpleDateFormat dateFormat = new SimpleDateFormat(AppMacros.SIMPLE_DATA_FORMATE);
      return dateFormat.format(date);
    } catch (Exception e) {
      Log.e("parseDateToString", "ת��ʧ��");
      e.printStackTrace();
    }
    return null;
  }

  /**
   * ��ʱ��ת��Ϊ����
   * 
   * @param datetime
   * @return
   */
  public static String parseDateToChinese(Date datetime) {
    Date today = new Date();
    long seconds = (today.getTime() - datetime.getTime()) / 1000;

    long year = seconds / (24 * 60 * 60 * 30 * 12);// �������
    long month = seconds / (24 * 60 * 60 * 30);// �������
    long date = seconds / (24 * 60 * 60); // ��������
    long hour = (seconds - date * 24 * 60 * 60) / (60 * 60);// ����Сʱ��
    long minute = (seconds - date * 24 * 60 * 60 - hour * 60 * 60) / (60);// ���ķ�����
    long second = (seconds - date * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);// ��������

    if (year > 0) {
      return year + "��ǰ";
    }
    if (month > 0) {
      return month + "��ǰ";
    }
    if (date > 0) {
      return date + "��ǰ";
    }
    if (hour > 0) {
      return hour + "Сʱǰ";
    }
    if (minute > 0) {
      return minute + "����ǰ";
    }
    if (second > 0) {
      return second + "��ǰ";
    }
    return parseDateToString(datetime);
  }

  /**
   * ȥ�����͡����š������б���ظ�����
   * 
   * @param list
   */
  public static <T> void removeDuplicate(List<T> list) {
    for (int i = 0; i < list.size(); i++) {
      for (int j = i + 1; j < list.size(); j++) {
        if (list.get(i).equals(list.get(j))) {
          list.remove(j);
        }
      }
    }
  }

  /**
   * ת��ΪԲ�Ǿ���
   * 
   * @param bitmap
   * @param corner
   * @return
   */
  public static Bitmap roundCorner(Bitmap bitmap, int corner) {
    Bitmap bitmapTemp =
        Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmapTemp);
    Paint paint = new Paint();
    Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    RectF rectF = new RectF(rect);
    float f = corner;
    paint.setAntiAlias(true);
    canvas.drawARGB(0, 0, 0, 0);
    paint.setColor(-12434878);
    canvas.drawRoundRect(rectF, f, f, paint);
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    canvas.drawBitmap(bitmap, rect, rect, paint);
    return bitmapTemp;
  }

  public static String InputStream2String(InputStream inputStream) {
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
    StringBuilder stringBuilder = new StringBuilder();
    String line = null;

    try {
      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        inputStream.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return stringBuilder.toString();
  }

  public static void SaveBitmap2File(Bitmap bitmap, String fileName) {
    CompressFormat format = Bitmap.CompressFormat.JPEG;
    int quality = 100;
    OutputStream outputStream = null;

    try {
      outputStream = new FileOutputStream("/sdcard/Cnblogs/image/avatar/" + fileName);
    } catch (Exception e) {
      e.printStackTrace();
    }

    bitmap.compress(format, quality, outputStream);
  }

  public static void ShareText(Context context, String content) {
    Intent intent = new Intent(Intent.ACTION_SEND);
    // intent.setType("image/*");
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_SUBJECT, "Cnblogs");
    intent.putExtra(Intent.EXTRA_TEXT, content);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(Intent.createChooser(intent, ((Activity) context).getTitle()));

  }

  public static void SharePicture(Context context, String content, String picture) {
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("image/png");
    intent.putExtra(Intent.EXTRA_STREAM, picture);
    intent.putExtra(Intent.EXTRA_SUBJECT, "Cnblogs");
    intent.putExtra(Intent.EXTRA_TEXT, content);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(Intent.createChooser(intent, ((Activity) context).getTitle()));
  }

  // http://www.cnblogs.com/zhangtingkuo/admin/EditPosts.aspx?opt=1

  public static String LoadModule(Context context, String fileName) {
    String content = "";
    try {

      InputStream inputStream = context.getResources().getAssets().open(fileName);
      InputStreamReader inputReader = new InputStreamReader(inputStream);
      BufferedReader bufferedReader = new BufferedReader(inputReader);
      String line = "";
      while ((line = bufferedReader.readLine()) != null) {
        content += line;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return content;
  }

}
