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
package br.com.sysmap.crux.widgets.client.filter;

import br.com.sysmap.crux.core.client.utils.EscapeUtils;
import br.com.sysmap.crux.core.rebind.CruxGeneratorException;
import br.com.sysmap.crux.core.rebind.widget.AttributeProcessor;
import br.com.sysmap.crux.core.rebind.widget.ViewFactoryCreator.SourcePrinter;
import br.com.sysmap.crux.core.rebind.widget.WidgetCreatorContext;
import br.com.sysmap.crux.core.rebind.widget.creator.HasAllKeyHandlersFactory;
import br.com.sysmap.crux.core.rebind.widget.creator.HasAnimationFactory;
import br.com.sysmap.crux.core.rebind.widget.creator.HasSelectionHandlersFactory;
import br.com.sysmap.crux.core.rebind.widget.creator.HasTextFactory;
import br.com.sysmap.crux.core.rebind.widget.creator.HasValueChangeHandlersFactory;
import br.com.sysmap.crux.core.rebind.widget.declarative.DeclarativeFactory;
import br.com.sysmap.crux.core.rebind.widget.declarative.TagAttribute;
import br.com.sysmap.crux.core.rebind.widget.declarative.TagAttributes;
import br.com.sysmap.crux.core.rebind.widget.declarative.TagAttributesDeclaration;
import br.com.sysmap.crux.gwt.rebind.CompositeFactory;
import br.com.sysmap.crux.widgets.client.WidgetMsgFactory;

/**
 * Factory for Filter widget
 * @author Gesse S. F. Dafe
 */
@DeclarativeFactory(id="filter", library="widgets", targetWidget=Filter.class)
public class FilterFactory extends CompositeFactory<WidgetCreatorContext> 
	   implements HasAnimationFactory<WidgetCreatorContext>, HasTextFactory<WidgetCreatorContext>, 
	              HasValueChangeHandlersFactory<WidgetCreatorContext>, HasSelectionHandlersFactory<WidgetCreatorContext>,
	              HasAllKeyHandlersFactory<WidgetCreatorContext>
{
	@Override
	@TagAttributes({
		@TagAttribute(value="accessKey", type=Character.class),
		@TagAttribute(value="autoSelectEnabled", type=Boolean.class),
		@TagAttribute(value="focus", type=Boolean.class),
		@TagAttribute(value="limit", type=Integer.class),
		@TagAttribute("popupStyleName"),
		@TagAttribute(value="tabIndex", type=Integer.class),
		@TagAttribute("value"),
		@TagAttribute(value="filterable", processor=FilterableAttributeParser.class, required=true)
	})
	@TagAttributesDeclaration({
	})
	public void processAttributes(SourcePrinter out, WidgetCreatorContext context) throws CruxGeneratorException
	{
		super.processAttributes(out, context);
	}
	
	/**
	 * @author Gesse Dafe
	 */
	public class FilterableAttributeParser extends AttributeProcessor<WidgetCreatorContext>
	{
		/**
		 * @see br.com.sysmap.crux.core.rebind.widget.AttributeProcessor#processAttribute(br.com.sysmap.crux.core.rebind.widget.ViewFactoryCreator.SourcePrinter, br.com.sysmap.crux.core.rebind.widget.WidgetCreatorContext, java.lang.String)
		 */
		public void processAttribute(SourcePrinter out, WidgetCreatorContext context, String propertyValue)
		{
			String widget = context.getWidget();
			String filterableId =context.readWidgetProperty("filterable");
			String filterableWidget = createVariableName("filterable");
			
			printlnPostProcessing("Widget "+filterableWidget+" = null;");
			printlnPostProcessing(filterableWidget+" = Screen.get("+EscapeUtils.quote(filterableId)+");");
			printlnPostProcessing("if("+filterableWidget+" != null){");
			printlnPostProcessing(widget+".setFilterable(("+Filterable.class.getCanonicalName()+"<?>) "+filterableWidget+");");
			printlnPostProcessing("}");
			printlnPostProcessing("else{");
			printlnPostProcessing("throw new RuntimeException("+WidgetMsgFactory.class.getCanonicalName()+".getMessages().filterableNotFoundWhenInstantiantingFilter("+
					EscapeUtils.quote(filterableId)+"));");
			printlnPostProcessing("}");							
		}		
	}
}