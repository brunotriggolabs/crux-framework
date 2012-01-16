package org.cruxframework.crux.crossdevice.rebind;

import org.cruxframework.crux.core.rebind.CruxGeneratorException;
import org.cruxframework.crux.core.rebind.screen.widget.ViewFactoryCreator.SourcePrinter;
import org.cruxframework.crux.core.rebind.screen.widget.WidgetCreatorContext;
import org.cruxframework.crux.core.rebind.screen.widget.creator.HasSelectionHandlersFactory;
import org.cruxframework.crux.core.rebind.screen.widget.creator.children.AnyWidgetChildProcessor;
import org.cruxframework.crux.core.rebind.screen.widget.creator.children.WidgetChildProcessor;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.DeclarativeFactory;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.TagAttribute;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.TagAttributes;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.TagChild;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.TagChildren;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.TagConstraints;
import org.cruxframework.crux.crossdevice.client.TopToolBar;
import org.cruxframework.crux.gwt.rebind.ComplexPanelFactory;

@DeclarativeFactory(library="crossDevice", id="topTollBar", targetWidget=TopToolBar.class)
@TagChildren({
	@TagChild(TopToolBarFactory.GripProcessor.class),
	@TagChild(TopToolBarFactory.CanvasProcessor.class)
})
@TagAttributes({
	@TagAttribute(value="gripHeight", type=Integer.class, required=true)
})

public class TopToolBarFactory  extends ComplexPanelFactory<WidgetCreatorContext> implements HasSelectionHandlersFactory<WidgetCreatorContext>
{
    @TagConstraints(minOccurs="1", maxOccurs="unbounded", tagName="grip")
	@TagChildren({
		@TagChild(GripWidgetProcessor.class)
	})
    public static class GripProcessor extends WidgetChildProcessor<WidgetCreatorContext> {}
	
	@TagConstraints(widgetProperty="gripWidget")
	public static class GripWidgetProcessor extends AnyWidgetChildProcessor<WidgetCreatorContext> {}

	@TagConstraints(minOccurs="1", maxOccurs="unbounded", tagName="canvas")
	@TagChildren({
		@TagChild(CanvasWidgetProcessor.class)
	})
    public static class CanvasProcessor extends  WidgetChildProcessor<WidgetCreatorContext> {}

	@TagConstraints(minOccurs="0", maxOccurs="unbounded")
	public static class CanvasWidgetProcessor extends AnyWidgetChildProcessor<WidgetCreatorContext> {}

    @Override
    public WidgetCreatorContext instantiateContext()
    {
	    return new WidgetCreatorContext();
    }
	
	@Override
	public void instantiateWidget(SourcePrinter out, WidgetCreatorContext context) throws CruxGeneratorException
	{
		String className = getWidgetClassName();
		out.println("final "+className + " " + context.getWidget()+" = GWT.create("+className+".class);");
	}
}
