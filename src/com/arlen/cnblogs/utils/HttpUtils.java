package com.arlen.cnblogs.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.arlen.cnblogs.entity.Blog;
import com.arlen.cnblogs.entity.Comment;
import com.arlen.cnblogs.entity.News;
import com.arlen.cnblogs.entity.User;
import com.arlen.cnblogs.handler.BlogItemHandler;
import com.arlen.cnblogs.handler.BlogListHandler;
import com.arlen.cnblogs.handler.CommentListHandler;
import com.arlen.cnblogs.handler.NewsItemHandler;
import com.arlen.cnblogs.handler.NewsListHandler;
import com.arlen.cnblogs.handler.UserListHandler;

public class HttpUtils {

	private static final String TAG = HttpUtils.class.getSimpleName();

	/**
	 * ��ȡXML������
	 * 
	 * @param path
	 * @return InputStream
	 */
	public static InputStream getStream(String path) {
		InputStream inputStream = null;
		Log.i(TAG, "��ȡ InputStream " + path);
		try {
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.setReadTimeout(3 * 1000);
			connection.setConnectTimeout(5 * 1000);
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.connect();
			int code = connection.getResponseCode();
			Log.d(TAG, "��ȡ InputStream ResponseCode " + code);
			if (code == HttpURLConnection.HTTP_OK) {
				inputStream = connection.getInputStream();
			}

			if (inputStream != null) {
				Log.i(TAG, "��ȡ InputStream �ɹ�");
			}

		} catch (Exception e) {
			Log.e(TAG, "��ȡ InputStream ʧ��");
			e.printStackTrace();
		}
		return inputStream;
	}

	/**
	 * ��ȡ�����б� XML
	 * 
	 * @param path
	 * @return List<Blog>
	 */
	public static List<Blog> getBlogList(String path) {
		Log.i(TAG, "��ȡ�����б� XML " + path);
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			BlogListHandler handler = new BlogListHandler();
			reader.setContentHandler(handler);
			InputStream inputStream = getStream(path);
			InputSource inputSource = new InputSource(inputStream);
			reader.parse(inputSource);
			Log.i(TAG, "��ȡ�����б� XML ���");
			return handler.getBlogList();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param path
	 * @return String
	 */
	public static String getBlogContent(String path) {
		Log.i(TAG, "��ȡ���� ���� " + path);
		String blogContent = "";
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			BlogItemHandler handler = new BlogItemHandler();
			reader.setContentHandler(handler);
			InputStream inputStream = getStream(path);
			InputSource inputSource = new InputSource(inputStream);
			reader.parse(inputSource);
			Log.i(TAG, "��ȡ���� ����  ���");
			return handler.getBlogContent();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return blogContent;
	}

	/**
	 * ��ȡ�û��б�
	 * 
	 * @param path
	 * @return List<User>
	 */
	public static List<User> getUserList(String path) {
		Log.i(TAG, "��ȡ�û��б� XML" + path);
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			UserListHandler handler = new UserListHandler();
			reader.setContentHandler(handler);
			InputStream inputStream = getStream(path);
			InputSource inputSource = new InputSource(inputStream);
			reader.parse(inputSource);
			Log.i(TAG, "��ȡ�����б� XML ���");
			return handler.getUserList();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ�����б�
	 * 
	 * @param path
	 * @return List<News>
	 */
	public static List<News> getNewsList(String path) {
		Log.i(TAG, "��ȡ�����б� XML " + path);
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			NewsListHandler handler = new NewsListHandler();
			reader.setContentHandler(handler);
			InputStream inputStream = getStream(path);
			InputSource inputSource = new InputSource(inputStream);
			reader.parse(inputSource);
			Log.i(TAG, "��ȡ�����б� XML ���");
			return handler.getNewsList();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param path
	 * @return String
	 */
	public static String getNewsContent(String path) {
		Log.i(TAG, "��ȡ�������� " + path);
		String newsContent = "";
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			NewsItemHandler handler = new NewsItemHandler();
			reader.setContentHandler(handler);
			InputStream inputStream = getStream(path);
			InputSource inputSource = new InputSource(inputStream);
			reader.parse(inputSource);
			Log.i(TAG, "��ȡ�������� ���");
			return handler.getNewsContent();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newsContent;
	}

	/**
	 * ��ȡ�����б�
	 * 
	 * @param path
	 * @return List<Comment>
	 */
	public static List<Comment> getCommentList(String path) {
		Log.i(TAG, "��ȡ�����б� XML " + path);
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			CommentListHandler handler = new CommentListHandler();
			reader.setContentHandler(handler);
			InputStream inputStream = getStream(path);
			InputSource inputSource = new InputSource(inputStream);
			reader.parse(inputSource);
			Log.i(TAG, "��ȡ�����б� XML ���");
			return handler.getCommentList();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ��½��֤��
	 * 
	 * @param path
	 * @return bitmap
	 */
	public static Bitmap getBitmap(String path) {
		Bitmap bitmap = null;
		InputStream inputStream = getStream(path);
		bitmap = BitmapFactory.decodeStream(inputStream);
		return bitmap;
	}

}
