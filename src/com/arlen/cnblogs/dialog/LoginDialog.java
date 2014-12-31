package com.arlen.cnblogs.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class LoginDialog {
	//
	private Context context;
	// �ȴ��Ի���ı���
	private String title;
	// �ȴ�����
	private String message;
	// show
	private final int SHOW = 1;
	// dismiss
	private final int DISMISS = 0;
	// ���ȶԻ���
	private ProgressDialog progressDialog = null;

	public LoginDialog(Context context) {
		this.context = context;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW:
				progressDialog = ProgressDialog.show(context, title, message);
				break;
			case DISMISS:
				progressDialog.dismiss();
				break;
			}
		}
	};

	public void showProgressDialog(String title, String message,
			final ProgressCallBack callBack) {
		this.title = title;
		this.message = message;
		handler.sendEmptyMessage(SHOW);// �����Ի���
		new Thread() {
			public void run() {
				callBack.action();// ִ�в���
				handler.sendEmptyMessage(DISMISS);// ִ����ϣ��رնԻ���
			}
		}.start();
	}

	public interface ProgressCallBack {
		public void action();
	}
}
