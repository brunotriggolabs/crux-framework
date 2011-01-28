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
package br.com.sysmap.crux.widgets.client.rollingpanel;

import org.json.JSONObject;

import br.com.sysmap.crux.core.client.utils.EscapeUtils;
import br.com.sysmap.crux.core.client.utils.StringUtils;
import br.com.sysmap.crux.core.rebind.CruxGeneratorException;
import br.com.sysmap.crux.core.rebind.widget.ViewFactoryCreator.SourcePrinter;
import br.com.sysmap.crux.core.rebind.widget.WidgetCreator;
import br.com.sysmap.crux.core.rebind.widget.WidgetCreatorContext;
import br.com.sysmap.crux.core.rebind.widget.creator.HasHorizontalAlignmentFactory;
import br.com.sysmap.crux.core.rebind.widget.creator.HasVerticalAlignmentFactory;
import br.com.sysmap.crux.core.rebind.widget.creator.align.AlignmentAttributeParser;
import br.com.sysmap.crux.core.rebind.widget.creator.align.HorizontalAlignment;
import br.com.sysmap.crux.core.rebind.widget.creator.align.VerticalAlignment;
import br.com.sysmap.crux.core.rebind.widget.creator.children.ChoiceChildProcessor;
import br.com.sysmap.crux.core.rebind.widget.creator.children.WidgetChildProcessor;
import br.com.sysmap.crux.core.rebind.widget.creator.children.WidgetChildProcessor.AnyWidget;
import br.com.sysmap.crux.core.rebind.widget.declarative.DeclarativeFactory;
import br.com.sysmap.crux.core.rebind.widget.declarative.TagAttribute;
import br.com.sysmap.crux.core.rebind.widget.declarative.TagAttributeDeclaration;
import br.com.sysmap.crux.core.rebind.widget.declarative.TagAttributes;
import br.com.sysmap.crux.core.rebind.widget.declarative.TagAttributesDeclaration;
import br.com.sysmap.crux.core.rebind.widget.declarative.TagChild;
import br.com.sysmap.crux.core.rebind.widget.declarative.TagChildAttributes;
import br.com.sysmap.crux.core.rebind.widget.declarative.TagChildren;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;

class RollingPanelContext extends WidgetCreatorContext
{

	public String verticalAlignment;
	public String horizontalAlignment;
	public String width;
	public String height;
	
}

/**
 * @author Thiago da Rosa de Bustamante
 *
 */
@DeclarativeFactory(id="rollingPanel", library="widgets", targetWidget=RollingPanel.class)
public class RollingPanelFactory extends WidgetCreator<RollingPanelContext>
       implements HasHorizontalAlignmentFactory<RollingPanelContext>, 
                  HasVerticalAlignmentFactory<RollingPanelContext>
{
	@Override
	public String instantiateWidget(SourcePrinter out, JSONObject metaElem, String widgetId) throws CruxGeneratorException
	{
		String varName = createVariableName("widget");
		String className = getWidgetClassName();
		String verticalAttr = metaElem.optString("vertical");
		boolean vertical = false;
		if (!StringUtils.isEmpty(verticalAttr))
		{
			vertical = Boolean.parseBoolean(verticalAttr);
		}
		out.println(className + " " + varName+" = new "+className+"("+vertical+");");
		return varName;
	}

	@Override
	@TagAttributesDeclaration({
		@TagAttributeDeclaration(value="vertical", type=Boolean.class, defaultValue="false")
	})
	@TagAttributes({
		@TagAttribute("horizontalNextButtonStyleName"),
		@TagAttribute("horizontalPreviousButtonStyleName"),
		@TagAttribute("verticalNextButtonStyleName"),
		@TagAttribute("verticalPreviousButtonStyleName"),
		@TagAttribute(value="scrollToAddedWidgets", type=Boolean.class),
		@TagAttribute(value="spacing", type=Integer.class)
	})
	public void processAttributes(SourcePrinter out, RollingPanelContext context) throws CruxGeneratorException
	{
		super.processAttributes(out, context);

	}
	
	@Override
	@TagChildren({
		@TagChild(RollingPanelProcessor.class)
	})		
	public void processChildren(SourcePrinter out, RollingPanelContext context) throws CruxGeneratorException {}
	
	@TagChildAttributes(minOccurs="0", maxOccurs="unbounded")
	public static class  RollingPanelProcessor extends ChoiceChildProcessor<RollingPanelContext> 
	{
		@Override
		@TagChildren({
			@TagChild(RollingCellProcessor.class),
			@TagChild(VerticalWidgetProcessor.class)
		})		
		public void processChildren(SourcePrinter out, RollingPanelContext context) throws CruxGeneratorException  {}
	}
	
	@TagChildAttributes(minOccurs="0", maxOccurs="unbounded", tagName="cell")
	public static class RollingCellProcessor extends WidgetChildProcessor<RollingPanelContext>
	{
		@TagAttributesDeclaration({
			@TagAttributeDeclaration("height"),
			@TagAttributeDeclaration("width"),
			@TagAttributeDeclaration(value="horizontalAlignment", type=HorizontalAlignment.class, defaultValue="defaultAlign"),
			@TagAttributeDeclaration(value="verticalAlignment", type=VerticalAlignment.class)
		})
		@TagChildren({
			@TagChild(value=VerticalWidgetProcessor.class)
		})		
		public void processChildren(SourcePrinter out, RollingPanelContext context) throws CruxGeneratorException 
		{
			context.height = context.readChildProperty("height");
			context.width = context.readChildProperty("width");
			context.horizontalAlignment = context.readChildProperty("horizontalAlignment");
			context.verticalAlignment = context.readChildProperty("verticalAlignment");
		}
	}
		
	@TagChildAttributes(type=AnyWidget.class)
	public static class VerticalWidgetProcessor extends WidgetChildProcessor<RollingPanelContext> 
	{
		@Override
		public void processChildren(SourcePrinter out, RollingPanelContext context) throws CruxGeneratorException
		{
			String child = getWidgetCreator().createChildWidget(out, context.getChildElement());
			String rootWidget = context.getWidget();
			out.println(rootWidget+".add("+child+");");

			if (!StringUtils.isEmpty(context.height))
			{
				out.println(rootWidget+".setCellHeight("+child+", "+EscapeUtils.quote(context.height)+");");
			}
			if (!StringUtils.isEmpty(context.horizontalAlignment))
			{
				out.println(rootWidget+".setCellHorizontalAlignment("+child+", "+ 
						AlignmentAttributeParser.getHorizontalAlignment(context.horizontalAlignment, HasHorizontalAlignment.class.getCanonicalName()+".ALIGN_DEFAULT")+");");
			}
			if (!StringUtils.isEmpty(context.verticalAlignment))
			{
				out.println(rootWidget+".setCellVerticalAlignment("+child+", "+ 
						AlignmentAttributeParser.getVerticalAlignment(context.verticalAlignment)+");");
			}
			if (!StringUtils.isEmpty(context.width))
			{
				out.println(rootWidget+".setCellWidth("+child+", "+EscapeUtils.quote(context.width)+");");
			}
			
			context.height = null;
			context.width = null;
			context.horizontalAlignment = null;
			context.verticalAlignment = null;
		}
	}	
}
