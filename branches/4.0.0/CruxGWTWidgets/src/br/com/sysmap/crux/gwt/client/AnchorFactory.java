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
import br.com.sysmap.crux.core.client.declarative.TagAttribute;
import br.com.sysmap.crux.core.client.declarative.TagAttributes;
import br.com.sysmap.crux.core.rebind.CruxGeneratorException;
import br.com.sysmap.crux.core.rebind.widget.ViewFactoryCreator.SourcePrinter;
import br.com.sysmap.crux.core.rebind.widget.WidgetCreatorContext;
import br.com.sysmap.crux.core.rebind.widget.creator.HasDirectionFactory;
import br.com.sysmap.crux.core.rebind.widget.creator.HasHTMLFactory;
import br.com.sysmap.crux.core.rebind.widget.creator.HasHorizontalAlignmentFactory;
import br.com.sysmap.crux.core.rebind.widget.creator.HasNameFactory;
import br.com.sysmap.crux.core.rebind.widget.creator.HasWordWrapFactory;

/**
 * Represents an AnchorFactory component
 * @author Thiago Bustamante
 */
@DeclarativeFactory(id="anchor", library="gwt")
public class AnchorFactory extends FocusWidgetFactory<WidgetCreatorContext> 
	   implements HasHTMLFactory<WidgetCreatorContext>, HasNameFactory<WidgetCreatorContext>, 
	              HasWordWrapFactory<WidgetCreatorContext>, HasDirectionFactory<WidgetCreatorContext>, 
	              HasHorizontalAlignmentFactory<WidgetCreatorContext>
{
	@Override
	@TagAttributes({
		@TagAttribute("href"),
		@TagAttribute("target")
	})
	public void processAttributes(SourcePrinter out, WidgetCreatorContext context) throws CruxGeneratorException
	{
		super.processAttributes(out, context);
	}
}
