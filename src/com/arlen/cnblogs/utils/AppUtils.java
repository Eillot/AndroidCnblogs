package com.arlen.cnblogs.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.arlen.cnblogs.entity.Blog;
import com.arlen.cnblogs.entity.User;
import com.arlen.cnblogs.handler.BlogItemHandler;
import com.arlen.cnblogs.handler.BlogListHandler;
import com.arlen.cnblogs.handler.UserListHandler;
import com.arlen.cnblogs.mail.MailSenderInfo;
import com.arlen.cnblogs.mail.SimpleMailSender;

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
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				Config.SIMPLE_DATA_FORMATE);
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
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					Config.SIMPLE_DATA_FORMATE);
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
	 * ʹ��JavaMail�����ʼ�
	 * 
	 * @param content
	 */
	public static void sendEmailByJavaMail(String content) {
		try {
			// �����ʼ�
			MailSenderInfo mailSenderInfo = new MailSenderInfo();
			mailSenderInfo.setMailServerHost(Config.MAIL_SERVER_HOST);
			mailSenderInfo.setMailServerPort(Config.MAIL_SERVER_PORT);
			mailSenderInfo.setValidate(true);
			mailSenderInfo.setUserName(Config.MAIL_ACCUNT); // ��������ַ
			mailSenderInfo.setPassword(Config.MAIL_PASSWORD);// ������������
			mailSenderInfo.setFromAddress(Config.MAIL_ACCUNT);
			mailSenderInfo.setToAddress(Config.AUTHOR_EMAIL);
			mailSenderInfo.setSubject(Config.MAIL_SUBJECT);
			mailSenderInfo.setContent(content);

			// �����ʼ�
			SimpleMailSender simpleMailSender = new SimpleMailSender();
			simpleMailSender.sendTextMail(mailSenderInfo);
			Log.i("sendEmailByJavaMail", "���ͳɹ�");
		} catch (Exception e) {
			Log.e("sendEmailByJavaMail", "����ʧ��");
			e.printStackTrace();
		}
	}

	/**
	 * ͨ��URL��ȡXML������
	 * 
	 * @param path
	 * @return
	 */
	public static InputStream getXmlStreamByUrl(String path) {
		InputStream inputStream = null;
		Log.i("getXmlStreamByUrl", "��ȡXML InputStream    " + path);
		try {
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.setConnectTimeout(3 * 1000);
			connection.setRequestMethod("GET");
			connection.connect();
			int code = connection.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				inputStream = connection.getInputStream();
			}

			if (inputStream != null) {
				Log.i("getXmlStreamByUrl", "��ȡ�ɹ�");
			}

		} catch (Exception e) {
			Log.e("getXmlStreamByUrl", "��ȡʧ��");
			e.printStackTrace();
		}
		return inputStream;
	}

	/**
	 * ��ȡ�����б�
	 * 
	 * @param path
	 * @return
	 */
	public static List<Blog> getBlogList(String path) {
		Log.i("getBlogList", "��ȡ�����б� XML" + path);
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			BlogListHandler handler = new BlogListHandler();
			reader.setContentHandler(handler);
			InputStream inputStream = getXmlStreamByUrl(path);
			InputSource inputSource = new InputSource(inputStream);
			reader.parse(inputSource);
			Log.i("getBlogList", "��ȡ�����б� XML ���");
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
	 * @return
	 */
	public static String getBlogContent(String path) {
		String blogContent = "";
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			BlogItemHandler handler = new BlogItemHandler();
			reader.setContentHandler(handler);
			InputStream inputStream = getXmlStreamByUrl(path);
			InputSource inputSource = new InputSource(inputStream);
			reader.parse(inputSource);
			Log.i("getBlogList", "��ȡ�����б� XML ���");
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
	 * ����xml�����ַ�
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceXmlTag(String str) {
//		str = str.replace("<p>", "\t\t");
		str = str.replace("</p>", "\r\n");
		str = str.replace("<br />", "\n");
		str = str.replace("<br/>", "\n");

		Pattern pattern = Pattern.compile("<img.+?>", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(str);
		str = matcher.replaceAll("****ͼƬ��ʱ�޷���ʾ****");

		pattern = Pattern.compile("<.+?>", Pattern.DOTALL);
		matcher = pattern.matcher(str);
		str = matcher.replaceAll("");

		str = str.replace("&nbsp;&nbsp;", "\t");
		str = str.replace("&nbsp;", " ");
		str = str.replace("&#39;", "\\");
		str = str.replace("&quot;", "\\");
		str = str.replace("&gt;", ">");
		str = str.replace("&lt;", "<");
		str = str.replace("&amp;", "&");

		return str;
	}

	public static List<User> getUserList(String path) {
		Log.i("getBlogList", "��ȡ�����б� XML" + path);
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			UserListHandler handler = new UserListHandler();
			reader.setContentHandler(handler);
			InputStream inputStream = getXmlStreamByUrl(path);
			InputSource inputSource = new InputSource(inputStream);
			reader.parse(inputSource);
			Log.i("getBlogList", "��ȡ�����б� XML ���");
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

}
