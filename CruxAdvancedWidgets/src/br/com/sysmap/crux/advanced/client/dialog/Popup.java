/*
 * Copyright 2009 Sysmap Solutions Software e Consultoria Ltda.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package br.com.sysmap.crux.advanced.client.dialog;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author Thiago da Rosa de Bustamante <code>tr_bustamante@yahoo.com.br</code>
 *
 */
public class Popup extends Widget implements HasCloseHandlers<Popup>, HasAnimation
{
	public static final String DEFAULT_STYLE_NAME = "crux-Popup" ;
	private static PopupController popupController = null;
	private String title;
	private String url;
	private String styleName;
	private boolean animationEnabled;
	private boolean closeable = true;
	protected static Popup popup;
	
	/**
	 * 
	 */
	public Popup()
	{
		setElement(DOM.createSpan());
	}

	public HandlerRegistration addCloseHandler(CloseHandler<Popup> handler)
	{
		return addHandler(handler, CloseEvent.getType());
	}	

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getStyleName()
	{
		return styleName;
	}

	public void setStyleName(String styleName)
	{
		this.styleName = styleName;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public boolean isAnimationEnabled()
	{
		return animationEnabled;
	}

	public void setAnimationEnabled(boolean animationEnabled)
	{
		this.animationEnabled = animationEnabled;
	}
	
	public boolean isCloseable()
	{
		return closeable;
	}

	public void setCloseable(boolean closeable)
	{
		this.closeable = closeable;
	}

	/**
	 * 
	 */
	public void show()
	{
		if (popupController == null)
		{
			popupController = new PopupController(); 
		}
		popup = this;
		popupController.showPopup(new PopupData(title, url, styleName!=null?styleName:DEFAULT_STYLE_NAME, animationEnabled, closeable));
	}
	
	/**
	 * 
	 */
	public void hide()
	{
		if (popupController != null && popup != null)
		{
			popupController.hide();
			popup = null;
		}
	}
	
	/**
	 * 
	 * @param title
	 * @param url
	 * @param closeHandler
	 */
	public static void show(String title, String url,  CloseHandler<Popup> closeHandler)
	{
		show(title, url, closeHandler, DEFAULT_STYLE_NAME, false, true);
	}
	
	/**
	 * 
	 * @param title
	 * @param url
	 * @param closeHandler
	 * @param styleName
	 * @param animationEnabled
	 */
	public static void show(String title, String url, CloseHandler<Popup> closeHandler, String styleName, boolean animationEnabled, boolean closeable)
	{
		Popup popup = new Popup(); 
		popup.setTitle(title);
		popup.setUrl(url);
		popup.setStyleName(styleName);
		popup.setAnimationEnabled(animationEnabled);
		if (closeHandler != null)
		{
			popup.addCloseHandler(closeHandler);
		}
		
		popup.show();
	}
	
	public static void close ()
	{
		if (popup != null)
		{
			popup.hide();
		}
	}
}
