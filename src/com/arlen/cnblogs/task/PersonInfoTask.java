package com.arlen.cnblogs.task;

import com.arlen.cnblogs.bean.PersonInfo;
import com.arlen.cnblogs.utils.AppMacros;
import com.arlen.cnblogs.utils.HtmlUtils;

import android.os.AsyncTask;
import android.widget.TextView;

public class PersonInfoTask extends AsyncTask<Void, Void, Void> {
  private PersonInfo personInfo;
  private TextView textViewNickName;
  private TextView textViewCnblogsAge;
  private TextView textViewFollowers;
  private TextView textViewFollowees;

  public PersonInfoTask(PersonInfo personInfo) {
    super();
    this.personInfo = personInfo;
  }

  @Override
  protected Void doInBackground(Void... params) {
    try {
      personInfo = HtmlUtils.getPersonInfo(AppMacros.BLOG_APP);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  protected void onPostExecute(Void result) {
    super.onPostExecute(result);
    textViewNickName.setText("�ǳƣ�" + personInfo.getNickName());
    textViewCnblogsAge.setText("԰�䣺" + personInfo.getCnblogsAge());
    textViewFollowers.setText("��˿��" + personInfo.getFollowers());
    textViewFollowees.setText("��ע��" + personInfo.getFollowees());
  }

  public void setTextViewNickName(TextView textViewNickName) {
    this.textViewNickName = textViewNickName;
  }

  public void setTextViewCnblogsAge(TextView textViewCnblogsAge) {
    this.textViewCnblogsAge = textViewCnblogsAge;
  }

  public void setTextViewFollowers(TextView textViewFollowers) {
    this.textViewFollowers = textViewFollowers;
  }

  public void setTextViewFollowees(TextView textViewFollowees) {
    this.textViewFollowees = textViewFollowees;
  }

}
