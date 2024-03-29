package org.cruxframework.crux.smartgwt.rebind.tab;

import org.cruxframework.crux.core.rebind.screen.widget.WidgetCreator;
import org.cruxframework.crux.core.rebind.screen.widget.WidgetCreatorContext;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.DeclarativeFactory;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.TagAttribute;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.TagAttributes;
import org.cruxframework.crux.smartgwt.client.tab.TabSet;

/**
 * Factory for TabSet SmartGWT
 * @author Flávia Yeshua
 *
 */
@DeclarativeFactory(library="smartgwt", id="tabSet", targetWidget=TabSet.class)

@TagAttributes({
	@TagAttribute("closeTabIcon"),
	@TagAttribute("locateTabsBy"),
	@TagAttribute("moreTabImage"),
	@TagAttribute("moreTabTitle"),
	@TagAttribute("paneContainerClassName"),
	@TagAttribute("pickerButtonHSrc"),
	@TagAttribute("pickerButtonSrc"),
	@TagAttribute("pickerButtonVSrc"),
	@TagAttribute("scrollerHSrc"),
	@TagAttribute("scrollerSrc"),
	@TagAttribute("scrollerVSrc"),
	@TagAttribute("simpleTabBaseStyle")
}) 

public class TabSetFactory extends WidgetCreator<WidgetCreatorContext>
							
{
	@Override
    public WidgetCreatorContext instantiateContext()
    {
	    return new WidgetCreatorContext();
    }
}
