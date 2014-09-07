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
import com.arlen.cnblogs.entity.News;
import com.arlen.cnblogs.utils.AppUtils;

public class NewsListHandler extends DefaultHandler {

	private final String ENTRY_TAG = "entry"; // �����
	private final String ID_TAG = "id"; // ���ͱ��
	private final String TITLE_TAG = "title"; // ���ͱ���
	private final String SUMMARY_TAG = "summary"; // ����ժҪ
	private final String PUBLISHED_TAG = "published"; // ����ʱ��
	private final String UPDATED_TAG = "updated"; // ����ʱ��
	private final String LINK_TAG = "link"; // ��������
	private final String LINK_HREF_TAG = "href"; // �������ӵ�ַ
	private final String DIGGS_TAG = "diggs"; // �Ƽ�����
	private final String VIEWS_TAG = "views"; // �������
	private final String COMMENTS_TAG = "comments"; // ���۴���
	private final String TOPIC_TAG = "topic"; // ���۴���
	private final String TOPI_ICON_TAG = "topicIcon"; // ���۴���
	private final String SOURCE_NAME_TAG = "sourceName"; // ���۴���

	private List<News> newsList;
	private News newsEntry;
	private boolean isStartParse;
	private StringBuilder currentData;

	public NewsListHandler() {
		super();
	}

	public NewsListHandler(List<News> newsList) {
		this.newsList = newsList;
	}

	public List<News> getNewsList() {
		return newsList;
	}

	/**
	 * ��ʼ�����ĵ�
	 */
	@Override
	public void startDocument() throws SAXException {
		Log.i("HomeListHandler", "��ʼ�����ĵ�");
		super.startDocument();
		newsList = new ArrayList<News>();
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
			newsEntry = new News();
			isStartParse = true;
			currentData.setLength(0);
		} else if (isStartParse && localName.equalsIgnoreCase(LINK_TAG)) {
			String path = attributes.getValue(LINK_HREF_TAG);
			if (!path.isEmpty()) {
				newsEntry.setNewsLink(AppUtils.parseStringToUrl(path));
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
				newsEntry.setNewsId(id);
			} else if (localName.equalsIgnoreCase(TITLE_TAG)) {
				chars = StringEscapeUtils.unescapeHtml(chars);
				newsEntry.setNewsTitle(chars);
			} else if (localName.equalsIgnoreCase(SUMMARY_TAG)) {
				chars = StringEscapeUtils.unescapeHtml(chars);
				newsEntry.setNewsSummary(chars);
			} else if (localName.equalsIgnoreCase(PUBLISHED_TAG)) {
				String dateTimeString = chars.split("T")[0] + " "
						+ chars.split("T")[1].split("\\+")[0];
				Date publisheDate = AppUtils.parseStringToDate(dateTimeString);
				newsEntry.setPublishedDate(publisheDate);
			} else if (localName.equalsIgnoreCase(UPDATED_TAG)) {
				String dateTimeString = chars.split("T")[0] + " "
						+ chars.split("T")[1].split("Z")[0];
				Date updatedDate = AppUtils.parseStringToDate(dateTimeString);
				newsEntry.setUpdatedDate(updatedDate);
			} else if (localName.equalsIgnoreCase(DIGGS_TAG)) {
				int newsDiggs = Integer.parseInt(chars);
				newsEntry.setNewsDiggs(newsDiggs);
			} else if (localName.equalsIgnoreCase(VIEWS_TAG)) {
				int newsDiggs = Integer.parseInt(chars);
				newsEntry.setNewsViews(newsDiggs);
			} else if (localName.equalsIgnoreCase(COMMENTS_TAG)) {
				int blogComments = Integer.parseInt(chars);
				newsEntry.setNewsComments(blogComments);
			} else if (localName.equalsIgnoreCase(TOPIC_TAG)) {
				newsEntry.setNewsTopic(chars);
			} else if (localName.equalsIgnoreCase(TOPI_ICON_TAG)) {
				if (!chars.isEmpty()) {
					URL topicIcon = AppUtils.parseStringToUrl(chars);
					newsEntry.setTopicIcon(topicIcon);
				}
			} else if (localName.equalsIgnoreCase(SOURCE_NAME_TAG)) {
				newsEntry.setSourceName(chars);
			} else if (localName.equalsIgnoreCase(ENTRY_TAG)) {
				newsList.add(newsEntry);
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
