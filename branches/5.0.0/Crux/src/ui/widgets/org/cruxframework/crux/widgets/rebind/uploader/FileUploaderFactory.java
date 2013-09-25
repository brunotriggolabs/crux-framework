/*
 * Copyright 2013 cruxframework.org.
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
package org.cruxframework.crux.widgets.rebind.uploader;

import org.cruxframework.crux.core.rebind.screen.widget.WidgetCreator;
import org.cruxframework.crux.core.rebind.screen.widget.WidgetCreatorContext;
import org.cruxframework.crux.core.rebind.screen.widget.creator.HasEnabledFactory;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.DeclarativeFactory;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.TagAttribute;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.TagAttributes;
import org.cruxframework.crux.widgets.client.uploader.FileUploader;

/**
 * @author Thiago da Rosa de Bustamante
 *
 */
@DeclarativeFactory(library="widgets", id="fileUploader", targetWidget=FileUploader.class)
@TagAttributes({
	@TagAttribute(value="multiple", type=Boolean.class, defaultValue="false"),
	@TagAttribute(value="autoUploadFiles", type=Boolean.class, defaultValue="false"),
	@TagAttribute(value="showProgressBar", type=Boolean.class, defaultValue="true"),
	@TagAttribute(value="fileInputText", supportsI18N=true),
	@TagAttribute(value="sendButtonText", supportsI18N=true),
	@TagAttribute("url")
})
public class FileUploaderFactory extends WidgetCreator<WidgetCreatorContext> implements HasEnabledFactory<WidgetCreatorContext>
{
	@Override
    public WidgetCreatorContext instantiateContext()
    {
	    return new WidgetCreatorContext();
    }
}
