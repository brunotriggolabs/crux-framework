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
package br.com.sysmap.crux.ext.client.component;

import br.com.sysmap.crux.core.client.component.Component;

import com.google.gwt.dom.client.Element;

/**
 * Represents a Frame Component
 * @author Thiago Bustamante
 */
public class Frame extends Component
{
	protected com.google.gwt.user.client.ui.Frame frameWidget;
	
	public Frame(String id) 
	{
		this(id, new com.google.gwt.user.client.ui.Frame());
	}

	protected Frame(String id, com.google.gwt.user.client.ui.Frame widget) 
	{
		super(id, widget);
		this.frameWidget = widget;
	}

	@Override
	protected void renderAttributes(Element element) 
	{
		super.renderAttributes(element);

		String url = element.getAttribute("_url");
		if (url != null && url.length() > 0)
		{
			setUrl(url);
		}
	}
	
	/**
	 * Gets the URL of the frame's resource.
	 * 
	 * @return the frame's URL
	 */
	public String getUrl() 
	{
		return frameWidget.getUrl();
	}

	/**
	 * Sets the URL of the resource to be displayed within the frame.
	 * 
	 * @param url the frame's new URL
	 */
	public void setUrl(String url) 
	{
		frameWidget.setUrl(url);
	}
}
