/*
 * $Id$
 *
 * Copyright (C) 2007 Josh Guilfoyle <jasta@devtcg.org>
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 */

package org.devtcg.rssreader.view;

import java.net.URISyntaxException;
import java.util.Map;

import org.devtcg.rssreader.provider.RSSReader;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.ContentURI;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * Simple widget that draws the channel heading for RSSPostView and 
 * RSSPostList.  Will optionally show either the channel logo, or the
 * channel icon + channel text.
 * 
 * TODO: I have no idea what I'm doing here.  The abstraction for
 * RSSChannelHead seems correct, but the implementation is surely all wrong.
 */
public class ChannelHead extends LinearLayout
{
	private ImageView mLogo;
	private ImageView mIcon;
	private TextView mLogoText;

	private Rect mRect;
	private Paint mGray;
	private Paint mBlack1;
	private Paint mBlack2;
	
	/* Default padding for each widget. */
	private static final int paddingTop = 2;
	private static final int paddingBottom = 6;
	
	public ChannelHead(Context context, AttributeSet attrs, Map inflateParams)
	{
		super(context, attrs, inflateParams);
		
		mRect = new Rect();
		mGray = new Paint();		
		mGray.setStyle(Paint.Style.STROKE);
		mGray.setColor(0xff9c9e9c);
		
		mBlack1 = new Paint();		
		mBlack1.setStyle(Paint.Style.STROKE);
		mBlack1.setColor(0xbb000000);

		mBlack2 = new Paint();		
		mBlack2.setStyle(Paint.Style.STROKE);
		mBlack2.setColor(0x33000000);
	}

	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		Rect r = mRect;

		getDrawingRect(r);
		canvas.drawLine(r.left, r.bottom - 4, r.right, r.bottom - 4, mGray);
		canvas.drawLine(r.left, r.bottom - 3, r.right, r.bottom - 3, mGray);
		canvas.drawLine(r.left, r.bottom - 2, r.right, r.bottom - 2, mBlack1);
		canvas.drawLine(r.left, r.bottom - 1, r.right, r.bottom - 1, mBlack2);

		super.dispatchDraw(canvas);
	}
	
	public void setLogo(String channelName, String iconData, String logoData)
	{
		/* TODO */
		assert(logoData == null);
		
		if (mIcon == null)
		{
			mIcon = new ImageView(getContext());
			mIcon.setPadding(10, paddingTop, 0, paddingBottom);
			
			LayoutParams iconRules =
			  new LayoutParams(16 + 10, 16 + paddingTop + paddingBottom);
			
			iconRules.gravity = Gravity.CENTER_VERTICAL;
			
			addView(mIcon, iconRules);
		}
		
		if (iconData != null)
		{
			try {
				mIcon.setImageURI(new ContentURI(iconData));
			} catch (URISyntaxException e) { }
		}
		
		if (mLogoText == null)
		{
			mLogoText = new TextView(getContext());
			mLogoText.setPadding(6, paddingTop, 0, paddingBottom);
			mLogoText.setTypeface(Typeface.DEFAULT_BOLD);
			mLogoText.setMaxLines(1);
			addView(mLogoText, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));			
		}

		mLogoText.setText(channelName);
	}
	
	/* Convenience method to access the logo data from a Channel cursor. */
	public void setLogo(Cursor cursor)
	{
		setLogo(cursor.getString(cursor.getColumnIndex(RSSReader.Channels.TITLE)),
		  cursor.getString(cursor.getColumnIndex(RSSReader.Channels.ICON)),
		  cursor.getString(cursor.getColumnIndex(RSSReader.Channels.LOGO)));
	}
	
	private String mPostTitle;
	private boolean mPostTitleVisible;
	
	public void setPost(String postTitle)
	{
		mPostTitle = postTitle;
	}
	
	public void setPost(Cursor cursor)
	{
		setPost(cursor.getString(cursor.getColumnIndex(RSSReader.Posts.TITLE)));
	}
	
	public void showPostTitle()
	{
		mLogoText.setText(mPostTitle);		
		mPostTitleVisible = true;
	}
	
	public void showChannelTitle()
	{
		mLogoText.setText("Test Channel");
		mPostTitleVisible = false;
	}
	
	public boolean isPostTitleVisible()
	{
		return mPostTitleVisible; 
	}
}
