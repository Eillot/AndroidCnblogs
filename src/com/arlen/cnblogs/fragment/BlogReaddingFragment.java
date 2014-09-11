package com.arlen.cnblogs.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;

import com.arlen.cnblogs.BlogActivity;
import com.arlen.cnblogs.R;
import com.arlen.cnblogs.adapter.BlogListAdapter;
import com.arlen.cnblogs.entity.Blog;
import com.arlen.cnblogs.utils.AppUtils;
import com.arlen.cnblogs.utils.Config;

public class BlogReaddingFragment extends ListFragment {

	private List<Blog> blogList;
	private String path;
	private int pageSize;

	private BlogListAdapter adapter;
	private Handler handler = null;

	private Intent intent;

	public BlogReaddingFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_blog_readding,
				container, false);
		return rootView;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		showBlogItem(blogList.get(position));
	}

	private void showBlogItem(Blog blogEntry) {
		intent = new Intent(this.getActivity(), BlogActivity.class);

		if (blogEntry.getAuthorAvatar() != null) {
			intent.putExtra("avatar", blogEntry.getAuthorAvatar().toString());
		} else {
			intent.putExtra("avatar",
					"http://pic.cnitblog.com/avatar/413207/20131211125235.png");
		}
		intent.putExtra("title", blogEntry.getBlogTitle());
		intent.putExtra("author", blogEntry.getAuthorName());
		intent.putExtra("published",
				AppUtils.parseDateToString(blogEntry.getPublishedDateDate()));
		intent.putExtra("id", blogEntry.getBlogId());
		intent.putExtra("link", blogEntry.getBlogTitle());

		startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		Log.i("onCreateContextMenu", "000000000");
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = this.getActivity().getMenuInflater();
		inflater.inflate(R.menu.blog_list_context_menu, menu);
		menu.setHeaderTitle(R.string.app_name);
		Log.i("onCreateContextMenu", "1111111111");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("ReaddingFragment", "onCreate");

		blogList = new ArrayList<Blog>();
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2 * 1000);
					initData();
					handler.sendMessage(handler.obtainMessage(0, blogList));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		try {
			new Thread(runnable).start();
			handler = new Handler() {

				@SuppressWarnings("unchecked")
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if (msg.what == 0) {
						ArrayList<Blog> blogs = (ArrayList<Blog>) msg.obj;
						BindListData(blogs);
					}
				}
			};
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initData() {
		blogList.clear();

		path = Config.TOP_VIEW_POSTS_48_HOUR;
		pageSize = Config.BLOG_PAGE_SIZE;
		path = path.replace("{ITEMCOUNT}", "" + pageSize);
		Log.i("ReaddingFragment", "��ҳ�����б��ַ��" + path);
		Log.i("ReaddingFragment", "��ȡ��ҳ�����б�  --->  ��ʼ");
		blogList = AppUtils.getBlogList(path);
		Log.i("ReaddingFragment", "��ȡ��ҳ�����б�  --->  ���");
	}

	private void BindListData(ArrayList<Blog> blogs) {
		adapter = new BlogListAdapter(getActivity(), blogs);
		this.setListAdapter(adapter);
	}
}
