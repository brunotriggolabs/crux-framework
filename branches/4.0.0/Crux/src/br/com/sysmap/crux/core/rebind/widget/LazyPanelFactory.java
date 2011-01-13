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
package br.com.sysmap.crux.core.rebind.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONObject;

import br.com.sysmap.crux.core.client.screen.LazyPanel;
import br.com.sysmap.crux.core.client.screen.ViewFactoryUtils;
import br.com.sysmap.crux.core.client.utils.EscapeUtils;
import br.com.sysmap.crux.core.rebind.widget.ViewFactoryCreator.SourcePrinter;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.logging.client.LogConfiguration;

/**
 * A Factory that wraps an element with a panel which content is only rendered when it is accessed for the first time.
 * 
 * @author Thiago da Rosa de Bustamante
 */
public class LazyPanelFactory 
{
	static final String LAZY_PANEL_TYPE = "_CRUX_LAZY_PANEL_";
	
	private final ViewFactoryCreator factory;
	private final TreeLogger logger;
	
	/**
	 * Singleton constructor
	 */
	public LazyPanelFactory(ViewFactoryCreator factory, TreeLogger logger) 
	{
		this.factory = factory;
		this.logger = logger;
	}
	
	/**
	 * Create an wrapper lazyPanel capable of creating an widget for the given CruxMetaData element. 
	 * 
	 * @param factoryPrinter Printer for the calling factory method.
	 * @param element CruxMetaData element that will be used to create the wrapped widget
	 * @param targetPanelId Identifier of the parent panel, that required the lazy wrapping operation.
	 * @param wrappingType the lazyPanel wrapping model.
	 * @return
	 */
	public String getLazyPanel(SourcePrinter factoryPrinter, final JSONObject element, String targetPanelId, LazyPanelWrappingType wrappingType) 
	{
		String lazyId = ViewFactoryUtils.getLazyPanelId(targetPanelId, wrappingType);
		logger.log(Type.INFO, "Delaying the widget ["+element.optString("id")+"] creation. Instantiating a new lazyPanel ["+lazyId+"] to wrap this widget...");
		
		String lazyPanel = ViewFactoryCreator.createVariableName("lazy");

		SourcePrinter lazyPrinter = factory.getSubTypeWriter(lazyPanel+"Class", LazyPanel.class.getCanonicalName(), 
														null, 
														getImports());
		
		generateFields(lazyPrinter, lazyPanel+"Class");
		generateConstructor(lazyPrinter, lazyPanel+"Class", lazyId);
		generateCreateWidgetMethod(lazyPrinter, element, targetPanelId, lazyId);
		
		lazyPrinter.commit();
		
		factoryPrinter.println(lazyPanel+"Class " + lazyPanel + " = new "+lazyPanel+"Class();");
		
		return lazyPanel;
	}
	
	/**
	 * @param printer
	 * @param className
	 */
	private void generateFields(SourcePrinter printer, String className)
    {
		printer.println("private static Logger logger = Logger.getLogger("+className+".class.getName());");
    }

	/**
	 * @param printer
	 * @param className
	 * @param widgetId
	 */
	private void generateConstructor(SourcePrinter printer, String className, String widgetId)
    {
		printer.println("public "+className+"(){");
		printer.println("super("+EscapeUtils.quote(widgetId)+")");
		printer.println("}");
    }

	/**
	 * @param printer
	 * @param element
	 * @param targetPanelId
	 */
	private void generateCreateWidgetMethod(SourcePrinter printer, JSONObject element, String targetPanelId, String lazyId)
    {
		printer.println("@Override");
		printer.println("public Widget createWidget(){");

		printer.println("if (LogConfiguration.loggingIsEnabled()){");
		printer.println("logger.log(Level.FINE, \"Creating ["+lazyId+"] wrapped widget...\");");
		printer.println("}");
    	
		factory.createPostProcessingScope();

		String newWidget = factory.newWidget(printer, element, targetPanelId, factory.getMetaElementType(element));

		factory.commitPostProcessing(printer);

		printer.println("if (LogConfiguration.loggingIsEnabled()){");
		printer.println("logger.log(Level.FINE, \"["+lazyId+"]  wrapped widget created.\");");
		printer.println("}");

		printer.println("return " + newWidget);    
		printer.println("}");    
	}

	/**
	 * Gets the list of classes used by the LazyPanel handler.
	 * @return
	 */
	private String[] getImports()
    {
		List<String> imports = new ArrayList<String>();
		
		String[] factoryImports = factory.getImports();
		for (String imp : factoryImports)
        {
	        imports.add(imp);
        }
		
		imports.add(LogConfiguration.class.getCanonicalName());
		imports.add(Logger.class.getCanonicalName());
		
	    return imports.toArray(new String[imports.size()]);
    }

	/**
	 * Contains the available lazyPanel wrapping models. {@code wrapChildren} is used 
	 * by widgets that needs to create some of its children lazily. {@code wrapWholeWidget}
	 * is used when the whole widget must be rendered lazily, like when {@code ScreenFactory}
	 * is parsing the CruxMetaData and find an invisible panel.
	 * 
	 * @author Thiago da Rosa de Bustamante
	 */
	public static enum LazyPanelWrappingType{wrapChildren, wrapWholeWidget}
}
