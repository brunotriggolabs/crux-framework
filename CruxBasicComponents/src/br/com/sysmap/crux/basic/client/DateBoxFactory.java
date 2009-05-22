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
package br.com.sysmap.crux.basic.client;

import java.util.Date;
import java.util.List;

import br.com.sysmap.crux.core.client.component.InterfaceConfigException;
import br.com.sysmap.crux.core.client.event.Event;
import br.com.sysmap.crux.core.client.event.EventFactory;
import br.com.sysmap.crux.core.client.event.bind.ChangeEvtBind;
import br.com.sysmap.crux.core.client.event.bind.EvtBind;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.user.datepicker.client.DateBox.Format;

/**
 * Factory for TabPanel widgets
 * @author Thiago da Rosa de Bustamante <code>tr_bustamante@yahoo.com.br</code>
 */
public class DateBoxFactory extends CompositeFactory<DateBox> 
{

	@Override
	protected void processAttributes(final DateBox widget, Element element, String widgetId) throws InterfaceConfigException
	{
		super.processAttributes(widget, element, widgetId);
		
		String tabIndex = element.getAttribute("_tabIndex");
		if (tabIndex != null && tabIndex.length() > 0)
		{
			widget.setTabIndex(Integer.parseInt(tabIndex));
		}
		String enabled = element.getAttribute("_enabled");
		if (enabled != null && enabled.length() > 0)
		{
			widget.setEnabled(Boolean.parseBoolean(enabled));
		}
		String accessKey = element.getAttribute("_accessKey");
		if (accessKey != null && accessKey.length() == 1)
		{
			widget.setAccessKey(accessKey.charAt(0));
		}
		String focus = element.getAttribute("_focus");
		if (focus != null && focus.trim().length() > 0)
		{
			widget.setFocus(Boolean.parseBoolean(focus));
		}

		String value = element.getAttribute("_value");
		if (value != null && value.length() > 0)
		{
			boolean reportError = true;
			String reportFormatError = element.getAttribute("_reportFormatError");
			if (reportFormatError != null && reportFormatError.length() > 0)
			{
				reportError = Boolean.parseBoolean(reportFormatError);
			}
			
			Date date = widget.getFormat().parse(widget, value, reportError);
			widget.setValue(date);
		}		
	}
	
	@Override
	protected DateBox instantiateWidget(Element element, String widgetId) throws InterfaceConfigException 
	{
		List<Element> children = ensureChildrenSpans(element, true);
		if (children.size() > 0)
		{
			Format format = null;
			DatePicker picker = null;
			String pattern = null;
			for (Element childElement : children)
			{
				if (isWidget(childElement))
				{
					picker = (DatePicker) createChildWidget(element, widgetId);
				}
				else 
				{
					pattern = childElement.getInnerHTML();
				}
			}
			if (pattern == null || pattern.trim().length() == 0)
			{
				pattern = element.getAttribute("_pattern");
			}
			if (pattern != null && pattern.trim().length() > 0)
			{
				format = new DateBox.DefaultFormat(DateFormatUtil.getDateTimeFormat(pattern));
			}
			else
			{
				Event eventLoadFormat = EvtBind.getWidgetEvent(element, EventFactory.EVENT_LOAD_FORMAT);
				
				if (eventLoadFormat != null)
				{
					LoadFormatEvent<DateBox> loadFormatEvent = new LoadFormatEvent<DateBox>(widgetId);
					format = (Format) EventFactory.callEvent(eventLoadFormat, loadFormatEvent);
				}
				else 
				{
					format = GWT.create(DefaultFormat.class);
				}
			}
			return new DateBox(picker, null, format);
		}
		else
		{
			return new DateBox();
		}
	}
	
	@Override
	protected void processEvents(DateBox widget, Element element, String widgetId) throws InterfaceConfigException
	{
		super.processEvents(widget, element, widgetId);
		
		ChangeEvtBind.bindValueEvent(element, widget);
	}
}
