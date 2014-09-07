package com.arlen.cnblogs.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class NewsItemHandler extends DefaultHandler {
	private final String CONTENT_TAG = "Content";

	private String newsContent;
	private boolean isStartParse;
	private StringBuilder currentData;

	public NewsItemHandler() {
		super();
	}

	public NewsItemHandler(String blogContent) {
		super();
		this.newsContent = blogContent;
	}

	public String getNewsContent() {
		return newsContent;
	}

	@Override
	public void startDocument() throws SAXException {
		Log.i("HomeItemHandler", "�ĵ�������ʼ");
		super.startDocument();
		currentData = new StringBuilder();
	}

	@Override
	public void endDocument() throws SAXException {
		Log.i("HomeItemHandler", "�ĵ���������");
		super.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (localName.equalsIgnoreCase(CONTENT_TAG)) {
			newsContent = "";
			isStartParse = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		super.endElement(uri, localName, qName);
		if (isStartParse) {// ����Ŀ��
			String chars = currentData.toString();
			Log.i("Blog", "���ڽ���" + localName);
			// ����
			if (localName.equalsIgnoreCase(CONTENT_TAG)) {// ����
				newsContent = chars;
				isStartParse = false;
			}
			currentData.setLength(0);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		currentData.append(ch, start, length);
	}

}
