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
package br.com.sysmap.crux.gwt.client;

import br.com.sysmap.crux.core.client.declarative.DeclarativeFactory;
import br.com.sysmap.crux.core.client.declarative.TagAttributeDeclaration;
import br.com.sysmap.crux.core.client.declarative.TagAttributesDeclaration;
import br.com.sysmap.crux.core.client.declarative.TagChild;
import br.com.sysmap.crux.core.client.declarative.TagChildAttributes;
import br.com.sysmap.crux.core.client.declarative.TagChildren;
import br.com.sysmap.crux.core.client.utils.StringUtils;
import br.com.sysmap.crux.core.rebind.CruxGeneratorException;
import br.com.sysmap.crux.core.rebind.widget.ViewFactoryCreator.SourcePrinter;
import br.com.sysmap.crux.core.rebind.widget.creator.children.WidgetChildProcessor;
import br.com.sysmap.crux.core.rebind.widget.creator.children.WidgetChildProcessor.AnyWidget;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.layout.client.Layout.Alignment;

class LayoutPanelContext extends AbstractLayoutPanelContext
{
	public double left;
	public double right;
	public double top;
	public double bottom;
	public double width;
	public double height;
	public double animationStartLeft;
	public double animationStartRight;
	public double animationStartTop;
	public double animationStartBottom;
	public double animationStartWidth;
	public double animationStartHeight;
	public String horizontalPosition;
	public String verticalPosition;
	public Unit leftUnit;
	public Unit rightUnit;
	public Unit topUnit;
	public Unit bottomUnit;
	public Unit widthUnit;
	public Unit heightUnit;
}

/**
 * @author Thiago da Rosa de Bustamante
 *
 */
@DeclarativeFactory(id="layoutPanel", library="gwt")
public class LayoutPanelFactory extends AbstractLayoutPanelFactory<LayoutPanelContext>
{
	@Override
	@TagChildren({
		@TagChild(LayoutPanelProcessor.class)
	})		
	public void processChildren(SourcePrinter out, LayoutPanelContext context) throws CruxGeneratorException {}
	
	@TagChildAttributes(minOccurs="0", maxOccurs="unbounded", tagName="layer")
	public static class LayoutPanelProcessor extends WidgetChildProcessor<LayoutPanelContext> 
	{
		@Override
		@TagAttributesDeclaration({
			@TagAttributeDeclaration(value="left", type=Double.class),
			@TagAttributeDeclaration(value="right", type=Double.class),
			@TagAttributeDeclaration(value="top", type=Double.class),
			@TagAttributeDeclaration(value="bottom", type=Double.class),
			@TagAttributeDeclaration(value="width", type=Double.class),
			@TagAttributeDeclaration(value="height", type=Double.class),
			@TagAttributeDeclaration(value="animationStartLeft", type=Double.class),
			@TagAttributeDeclaration(value="animationStartRight", type=Double.class),
			@TagAttributeDeclaration(value="animationStartTop", type=Double.class),
			@TagAttributeDeclaration(value="animationStartBottom", type=Double.class),
			@TagAttributeDeclaration(value="animationStartWidth", type=Double.class),
			@TagAttributeDeclaration(value="animationStartHeight", type=Double.class),
			@TagAttributeDeclaration(value="horizontalPosition", type=Alignment.class),
			@TagAttributeDeclaration(value="verticalPosition", type=Alignment.class),
			@TagAttributeDeclaration(value="leftUnit", type=Unit.class, defaultValue="PX"),
			@TagAttributeDeclaration(value="rightUnit", type=Unit.class, defaultValue="PX"),
			@TagAttributeDeclaration(value="topUnit", type=Unit.class, defaultValue="PX"),
			@TagAttributeDeclaration(value="bottomUnit", type=Unit.class, defaultValue="PX"),
			@TagAttributeDeclaration(value="widthUnit", type=Unit.class, defaultValue="PX"),
			@TagAttributeDeclaration(value="heightUnit", type=Unit.class, defaultValue="PX")
		})
		@TagChildren({
			@TagChild(LayoutPanelWidgetProcessor.class)
		})		
		public void processChildren(SourcePrinter out, LayoutPanelContext context) throws CruxGeneratorException 
		{
			context.left = StringUtils.safeParseDouble(context.readChildProperty("left"));
			context.right = StringUtils.safeParseDouble(context.readChildProperty("right"));
			context.top = StringUtils.safeParseDouble(context.readChildProperty("top"));
			context.bottom = StringUtils.safeParseDouble(context.readChildProperty("bottom"));
			context.width = StringUtils.safeParseDouble(context.readChildProperty("width"));
			context.height = StringUtils.safeParseDouble(context.readChildProperty("height"));
			context.animationStartLeft = StringUtils.safeParseDouble(context.readChildProperty("animationStartLeft"));
			context.animationStartRight = StringUtils.safeParseDouble(context.readChildProperty("animationStartRight"));
			context.animationStartTop = StringUtils.safeParseDouble(context.readChildProperty("animationStartTop"));
			context.animationStartBottom = StringUtils.safeParseDouble(context.readChildProperty("animationStartBottom"));
			context.animationStartWidth = StringUtils.safeParseDouble(context.readChildProperty("animationStartWidth"));
			context.animationStartHeight = StringUtils.safeParseDouble(context.readChildProperty("animationStartHeight"));
			context.horizontalPosition = context.readChildProperty("horizontalPosition");
			context.verticalPosition = context.readChildProperty("verticalPosition");
			context.leftUnit = getUnit(context.readChildProperty("leftUnit"));
			context.rightUnit = getUnit(context.readChildProperty("rightUnit"));
			context.topUnit = getUnit(context.readChildProperty("topUnit"));
			context.bottomUnit = getUnit(context.readChildProperty("bottomUnit"));
			context.widthUnit = getUnit(context.readChildProperty("widthUnit"));
			context.heightUnit = getUnit(context.readChildProperty("heightUnit"));
		}
	}
	
	@TagChildAttributes(type=AnyWidget.class)
	public static class LayoutPanelWidgetProcessor extends WidgetChildProcessor<LayoutPanelContext> 
	{
		@Override
		public void processChildren(SourcePrinter out, LayoutPanelContext context) throws CruxGeneratorException 
		{
			String childWidget = getWidgetCreator().createChildWidget(out, context.getChildElement());
			String rootWidget = context.getWidget();
			out.println(rootWidget+".add("+childWidget+");");

			if (context.animationDuration > 0)
			{
				processAnimation(out, context, childWidget);
			}
			else
			{
				out.println(getConstraintsCommand(rootWidget, childWidget, context, false));
			}
			
			if (!StringUtils.isEmpty(context.horizontalPosition))
			{
				out.println(rootWidget+".setWidgetHorizontalPosition("+childWidget+", "+Alignment.class.getCanonicalName()+"."+context.horizontalPosition+");");
			}
			if (!StringUtils.isEmpty(context.verticalPosition))
			{
				out.println(rootWidget+".setWidgetVerticalPosition("+childWidget+", "+Alignment.class.getCanonicalName()+"."+context.verticalPosition+");");
			}
		}

		/**
		 * @param context
		 * @param childWidget
		 */
		private void processAnimation(SourcePrinter out, LayoutPanelContext context, String childWidget)
		{
			if (hasAnimation(context))
			{
				String rootWidget = context.getWidget();
				out.println(getConstraintsCommand(rootWidget, childWidget, context, true));
				context.addChildWithAnimation(getConstraintsCommand(rootWidget, childWidget, context, false));
			}
		}

		/**
		 * @param context
		 * @return
		 */
		private boolean hasAnimation(LayoutPanelContext context) 
		{
			return context.animationStartBottom != Double.MIN_VALUE
				|| context.animationStartHeight != Double.MIN_VALUE
				|| context.animationStartLeft != Double.MIN_VALUE
				|| context.animationStartRight != Double.MIN_VALUE
				|| context.animationStartTop != Double.MIN_VALUE
				|| context.animationStartWidth != Double.MIN_VALUE;
		}

		/**
		 * @param rootWidget
		 * @param childWidget
		 * @param context
		 * @param startPosition
		 */
		private String getConstraintsCommand(String rootWidget, String childWidget, LayoutPanelContext context, boolean startPosition)
		{
			double left = startPosition ? context.animationStartLeft : context.left;
			double right = startPosition ? context.animationStartRight : context.right;
			double top = startPosition ? context.animationStartTop : context.top;
			double bottom = startPosition ? context.animationStartBottom : context.bottom;
			double width = startPosition ? context.animationStartWidth : context.width;
			double height = startPosition ? context.animationStartHeight : context.height;			
			
			String UnitClassName = Unit.class.getCanonicalName();
			
			String constraintsCommand;
			
			if (left != Double.MIN_VALUE && right != Double.MIN_VALUE)
			{
				constraintsCommand = rootWidget+".setWidgetLeftRight("+childWidget+", "+left+", "+UnitClassName+"."+context.leftUnit+", "+
									right+", "+UnitClassName+"."+context.rightUnit+");";
			}
			else if (left != Double.MIN_VALUE && width != Double.MIN_VALUE)
			{
				constraintsCommand = rootWidget+".setWidgetLeftWidth("+childWidget+", "+left+", "+UnitClassName+"."+context.leftUnit+", "+
									width+", "+UnitClassName+"."+context.widthUnit+");";
			}
			else if (right != Double.MIN_VALUE && width != Double.MIN_VALUE)
			{
				constraintsCommand = rootWidget+".setWidgetRightWidth("+childWidget+", "+right+", "+UnitClassName+"."+context.rightUnit+", "+
									width+", "+UnitClassName+"."+context.widthUnit+");";
			}
			else if (top != Double.MIN_VALUE && bottom != Double.MIN_VALUE)
			{
				constraintsCommand = rootWidget+".setWidgetTopBottom("+childWidget+", "+top+", "+UnitClassName+"."+context.topUnit+", "+
									bottom+", "+UnitClassName+"."+context.bottomUnit+");";
			}
			else if (top != Double.MIN_VALUE && height != Double.MIN_VALUE)
			{
				constraintsCommand = rootWidget+".setWidgetTopHeight("+childWidget+", "+top+", "+UnitClassName+"."+context.topUnit+", "+
									height+", "+UnitClassName+"."+context.heightUnit+");";
			}
			else if (bottom != Double.MIN_VALUE && height != Double.MIN_VALUE)
			{
				constraintsCommand = rootWidget+".setWidgetBottomHeight("+childWidget+", "+bottom+", "+UnitClassName+"."+context.bottomUnit+", "+
									height+", "+UnitClassName+"."+context.heightUnit+");";
			}
			else
			{
				constraintsCommand = "";
			}
			return constraintsCommand;
		}
	}
}
