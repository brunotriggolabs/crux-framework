/*
 * Copyright 2011 cruxframework.org.
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
package org.cruxframework.crux.core.rebind;

import org.cruxframework.crux.core.client.screen.Screen;
import org.cruxframework.crux.core.client.screen.views.BindRootView;
import org.cruxframework.crux.core.client.screen.views.BindView;
import org.cruxframework.crux.core.client.screen.views.View;
import org.cruxframework.crux.core.client.screen.views.ViewAware;
import org.cruxframework.crux.core.client.screen.views.ViewBindable;
import org.cruxframework.crux.core.client.utils.EscapeUtils;

import com.google.gwt.core.ext.GeneratorContextExt;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;

/**
 * @author Thiago da Rosa de Bustamante
 *
 */
public abstract class AbstractViewBindableProxyCreator extends AbstractWrapperProxyCreator
{
	private JClassType viewBindableType;
	private JClassType viewAwareType;

	public AbstractViewBindableProxyCreator(TreeLogger logger, GeneratorContextExt context, JClassType baseIntf)
    {
	    this(logger, context, baseIntf, true);
    }

	public AbstractViewBindableProxyCreator(TreeLogger logger, GeneratorContextExt context, JClassType baseIntf, boolean cacheable)
    {
	    super(logger, context, baseIntf, cacheable);
	    viewBindableType = context.getTypeOracle().findType(ViewBindable.class.getCanonicalName());
	    viewAwareType = context.getTypeOracle().findType(ViewAware.class.getCanonicalName());
    }

	@Override
	protected void generateProxyFields(SourcePrinter srcWriter) throws CruxGeneratorException
	{
		super.generateProxyFields(srcWriter);
		srcWriter.println("private "+View.class.getCanonicalName()+" view;");
	}
	
	@Override
	protected void generateProxyContructor(SourcePrinter srcWriter) throws CruxGeneratorException
	{
		srcWriter.println("public "+getProxySimpleName()+"(){");
		generateViewIdentificationBlock(srcWriter, baseIntf);
		srcWriter.println("}");
	}

	private boolean generateViewIdentificationBlock(SourcePrinter srcWriter, JClassType type)
    {
	    BindRootView bindRootView = type.getAnnotation(BindRootView.class);
	    BindView bindView = type.getAnnotation(BindView.class);

	    boolean ret = false;
	    if (bindRootView != null && bindView != null)
	    {
	    	throw new CruxGeneratorException("ViewBindable class ["+baseIntf.getQualifiedSourceName()+"] can be annotated with BindView or with BindRootView, but not with both...");
	    }
	    if (bindRootView != null)
	    {
	    	srcWriter.println("this.view = "+Screen.class.getCanonicalName()+".getRootView();");
	    	ret = true;
	    }
	    else if (bindView != null)
	    {
	    	srcWriter.println("this.view = "+View.class.getCanonicalName()+".getView("+EscapeUtils.quote(bindView.value())+");");
	    	ret = true;
	    }
	    else
	    {
	    	JClassType[] interfaces = type.getImplementedInterfaces();
	    	if (interfaces != null)
	    	{
	    		for (JClassType intf : interfaces)
                {
	                ret = generateViewIdentificationBlock(srcWriter, intf);
	                if (ret)
	                {
	                	break;
	                }
                }
	    	}
	    }
	    return ret;
    }
	
	@Override
    protected void generateProxyMethods(SourcePrinter srcWriter) throws CruxGeneratorException
    {
		super.generateProxyMethods(srcWriter);
	    generateViewBindableMethods(srcWriter);
    }
	
	@Override
    protected void generateProxyMethods(SourcePrinter srcWriter, JClassType clazz) throws CruxGeneratorException
    {
    	JMethod[] methods = clazz.getOverridableMethods();
    	for (JMethod method : methods)
    	{
    		if (method.getEnclosingType().equals(viewBindableType) || method.getEnclosingType().equals(viewAwareType))
    		{
    			continue;
    		}
    		generateWrapperMethod(method, srcWriter);
    	}
    }
    
	protected void generateViewBindableMethods(SourcePrinter sourceWriter)
    {
		sourceWriter.println("public "+View.class.getCanonicalName()+" getView(){");
		sourceWriter.println("return this.view;");
		sourceWriter.println("}");
		sourceWriter.println();
		sourceWriter.println("public void setView("+View.class.getCanonicalName()+" view){");
		sourceWriter.println("this.view = view;");
		sourceWriter.println("}");
		sourceWriter.println();
    }
	
	protected void generateViewGetterMethod(SourcePrinter srcWriter)
	{
		srcWriter.println("public IsWidget _getFromView(String widgetName){");
		srcWriter.println("IsWidget ret = view.getWidget(widgetName);");
		srcWriter.println("if (ret == null){");
		srcWriter.println("String widgetNameFirstUpper;");
		srcWriter.println("if (widgetName.length() > 1){"); 
		srcWriter.println("widgetNameFirstUpper = Character.toUpperCase(widgetName.charAt(0)) + widgetName.substring(1);");
		srcWriter.println("}");
		srcWriter.println("else{"); 
		srcWriter.println("widgetNameFirstUpper = \"\"+Character.toUpperCase(widgetName.charAt(0));");
		srcWriter.println("}");
		srcWriter.println("ret = view.getWidget(widgetNameFirstUpper);");
		srcWriter.println("}");
		srcWriter.println("return ret;");
		srcWriter.println("}");
	}
}
