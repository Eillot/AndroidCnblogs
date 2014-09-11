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

import com.arlen.cnblogs.entity.Comment;
import com.arlen.cnblogs.utils.AppUtils;

public class CommentListHandler extends DefaultHandler {

	private final String ENTRY_TAG = "entry"; // �����
	private final String ID_TAG = "id"; // ���ͱ��
	private final String TITLE_TAG = "title"; // ���ͱ���
	private final String PUBLISHED_TAG = "published"; // ����ʱ��
	private final String UPDATED_TAG = "updated"; // ����ʱ��
	private final String AUTHOR_NAME_TAG = "name"; // ��������
	private final String AUTHOR_URI_TAG = "uri"; // ������ҳ
	private final String COMMENTS_TAG = "content"; // ���۴���

	private List<Comment> commentList;
	private Comment commentEntry;
	private boolean isStartParse;
	private StringBuilder currentData;

	public CommentListHandler() {
		super();
	}

	public CommentListHandler(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	/**
	 * ��ʼ�����ĵ�
	 */
	@Override
	public void startDocument() throws SAXException {
		Log.i("HomeListHandler", "��ʼ�����ĵ�");
		super.startDocument();
		commentList = new ArrayList<Comment>();
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
			commentEntry = new Comment();
			isStartParse = true;
			currentData.setLength(0);
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
				commentEntry.setCommentId(id);
			} else if (localName.equalsIgnoreCase(TITLE_TAG)) {
				chars = StringEscapeUtils.unescapeHtml(chars);
				commentEntry.setCommentTitle(chars);
			} else if (localName.equalsIgnoreCase(PUBLISHED_TAG)) {
				String dateString = chars.split("T")[0];
				Log.i("endElement", "dateString" + dateString);
				String timeString = chars.split("T")[1].split("\\+")[0];
				Log.i("endElement", "timeString" + timeString);
				String dateTimeString = dateString + " " + timeString;
				Log.i("endElement", "dateTimeString" + dateTimeString);
				Date publishedDate = AppUtils.parseStringToDate(dateTimeString);
				commentEntry.setPublishedDate(publishedDate);
			} else if (localName.equalsIgnoreCase(UPDATED_TAG)) {
				String dateString = chars.split("T")[0];
				String timeString = chars.split("T")[1].split("Z")[0];
				String dateTimeString = dateString + " " + timeString;
				Date updatedDate = AppUtils.parseStringToDate(dateTimeString);
				commentEntry.setUpdatedDate(updatedDate);
			} else if (localName.equalsIgnoreCase(AUTHOR_NAME_TAG)) {
				commentEntry.setAuthorName(chars);
			} else if (localName.equalsIgnoreCase(AUTHOR_URI_TAG)) {
				if (!chars.isEmpty()) {
					URL authorUri = AppUtils.parseStringToUrl(chars);
					commentEntry.setAuthorUri(authorUri);
				}
			} else if (localName.equalsIgnoreCase(COMMENTS_TAG)) {
				String commentContent = StringEscapeUtils.unescapeHtml(chars);
				commentEntry.setCommentContent(commentContent);
			} else if (localName.equalsIgnoreCase(ENTRY_TAG)) {
				commentList.add(commentEntry);
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
