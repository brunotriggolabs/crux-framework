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
package br.com.sysmap.crux.widgets.rebind.wizard;

import br.com.sysmap.crux.core.client.utils.EscapeUtils;
import br.com.sysmap.crux.core.rebind.screen.widget.EvtProcessor;
import br.com.sysmap.crux.core.rebind.screen.widget.ViewFactoryCreator;
import br.com.sysmap.crux.core.rebind.screen.widget.ViewFactoryCreator.SourcePrinter;

/**
 * @author Thiago da Rosa de Bustamante -
 *
 */
public class EnterEvtBind extends EvtProcessor
{
	private static final String EVENT_NAME = "onEnter";

	/**
	 * @see br.com.sysmap.crux.core.rebind.screen.widget.EvtProcessor#getEventName()
	 */
	public String getEventName()
	{
		return EVENT_NAME;
	}

	@Override
    public void processEvent(SourcePrinter out, String eventValue, String widget, String widgetId)
    {
		String event = ViewFactoryCreator.createVariableName("evt");
		
		out.println("final Event "+event+" = Events.getEvent("+EscapeUtils.quote(getEventName())+", "+ EscapeUtils.quote(eventValue)+");");
		out.println(widget+".addEnterEvent("+event+");");
    }	
}
