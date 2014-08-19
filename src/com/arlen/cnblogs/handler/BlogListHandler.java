package com.arlen.cnblogs.handler;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.arlen.cnblogs.entity.Blog;
import com.arlen.cnblogs.utils.AppUtils;

public class BlogListHandler extends DefaultHandler {

	private final String ENTRY_TAG = "entry"; // �����
	private final String ID_TAG = "id"; // ���ͱ��
	private final String TITLE_TAG = "title"; // ���ͱ���
	private final String SUMMARY_TAG = "summary"; // ����ժҪ
	private final String PUBLISHED_TAG = "published"; // ����ʱ��
	private final String UPDATED_TAG = "updated"; // ����ʱ��
	private final String AUTHOR_NAME_TAG = "name"; // ��������
	private final String AUTHOR_URI_TAG = "uri"; // ������ҳ
	private final String AUTHOR_AVATAR_TAG = "avatar"; // ����ͷ��
	private final String LINK_TAG = "link"; // ��������
	private final String LINK_HREF_TAG = "href"; // �������ӵ�ַ
	private final String BLOGAPP_TAG = "blogapp"; // �����û���
	private final String DIGGS_TAG = "diggs"; // �Ƽ�����
	private final String VIEWS_TAG = "views"; // �������
	private final String COMMENTS_TAG = "comments"; // ���۴���

	private List<Blog> blogList;
	private Blog blogEntry;
	private boolean isStartParse;
	private StringBuilder currentData;

	public BlogListHandler() {
		super();
	}

	public BlogListHandler(List<Blog> blogList) {
		this.blogList = blogList;
	}

	public List<Blog> getBlogList() {
		return blogList;
	}

	/**
	 * ��ʼ�����ĵ�
	 */
	@Override
	public void startDocument() throws SAXException {
		Log.i("HomeListHandler", "��ʼ�����ĵ�");
		super.startDocument();
		blogList = new ArrayList<Blog>();
		currentData = new StringBuilder();
	}

	/**
	 * �ĵ�����
	 */
	@Override
	public void endDocument() throws SAXException {
		Log.i("HomeListHandler", "�ĵ��������");
		super.endDocument();
	}

	/**
	 * Ԫ�ؿ�ʼ
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		Log.i("HomeListHandler", "startElement " + localName);
		if (localName.equalsIgnoreCase(ENTRY_TAG)) {
			blogEntry = new Blog();
			isStartParse = true;
			currentData.setLength(0);
		} else if (isStartParse && localName.equalsIgnoreCase(LINK_TAG)) {
			String path = attributes.getValue(LINK_HREF_TAG);
			if (!path.isEmpty()) {
				blogEntry.setBlogLink(AppUtils.parseStringToUrl(path));
			}
		}
	}

	/**
	 * Ԫ�ؽ���
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if (isStartParse) {
			String chars = currentData.toString();
			Log.i("HomeListHandler", "���ڽ���:" + localName + " ---> " + chars);

			if (localName.equalsIgnoreCase(ID_TAG)) {
				int id = Integer.parseInt(chars);
				blogEntry.setBlogId(id);
			} else if (localName.equalsIgnoreCase(TITLE_TAG)) {
				chars = StringEscapeUtils.unescapeHtml(chars);
				blogEntry.setBlogTitle(chars);
			} else if (localName.equalsIgnoreCase(SUMMARY_TAG)) {
				chars = StringEscapeUtils.unescapeHtml(chars);
				blogEntry.setBlogSummary(chars);
			} else if (localName.equalsIgnoreCase(PUBLISHED_TAG)) {
				String dateString = chars.split("T")[0];
				Log.i("endElement", "dateString" + dateString);
				String timeString = chars.split("T")[1].split("\\+")[0];
				Log.i("endElement", "timeString" + timeString);
				String dateTimeString = dateString + " " + timeString;
				Log.i("endElement", "dateTimeString" + dateTimeString);
				Date publisheDate = AppUtils.parseStringToDate(dateTimeString);
				blogEntry.setPublishedDate(publisheDate);
			} else if (localName.equalsIgnoreCase(UPDATED_TAG)) {
				String dateString = chars.split("T")[0];
				String timeString = chars.split("T")[1].split("Z")[0];
				String dateTimeString = dateString + " " + timeString;
				Date updatedDate = AppUtils.parseStringToDate(dateTimeString);
				blogEntry.setUpdatedDate(updatedDate);
			} else if (localName.equalsIgnoreCase(AUTHOR_NAME_TAG)) {
				blogEntry.setAuthorName(chars);
			} else if (localName.equalsIgnoreCase(AUTHOR_URI_TAG)) {
				if (!chars.isEmpty()) {
					URL authorUri = AppUtils.parseStringToUrl(chars);
					blogEntry.setAuthorUri(authorUri);
				}
			} else if (localName.equalsIgnoreCase(AUTHOR_AVATAR_TAG)) {
				if (!chars.isEmpty()) {
					URL authorAvatar = AppUtils.parseStringToUrl(chars);
					blogEntry.setAuthorAvatar(authorAvatar);
				}
			} else if (localName.equalsIgnoreCase(LINK_TAG)) {

			} else if (localName.equalsIgnoreCase(LINK_HREF_TAG)) {

			} else if (localName.equalsIgnoreCase(BLOGAPP_TAG)) {
				blogEntry.setBlogApp(chars);
			} else if (localName.equalsIgnoreCase(DIGGS_TAG)) {
				int blogDiggs = Integer.parseInt(chars);
				blogEntry.setBlogDiggs(blogDiggs);
			} else if (localName.equalsIgnoreCase(VIEWS_TAG)) {
				int blogViews = Integer.parseInt(chars);
				blogEntry.setBlogViews(blogViews);
			} else if (localName.equalsIgnoreCase(COMMENTS_TAG)) {
				int blogComments = Integer.parseInt(chars);
				blogEntry.setBlogComments(blogComments);
			} else if (localName.equalsIgnoreCase(ENTRY_TAG)) {
				blogList.add(blogEntry);
				isStartParse = false;
			}
			currentData.setLength(0);
		}
	}

	/**
	 * ��ȡԪ������
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		currentData.append(ch, start, length);
		Log.i("endElement", "currentDate  ----> " + currentData);
	}

}
