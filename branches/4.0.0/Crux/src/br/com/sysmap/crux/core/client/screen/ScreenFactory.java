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
package br.com.sysmap.crux.core.client.screen;

import java.util.logging.Logger;

import br.com.sysmap.crux.core.client.Crux;
import br.com.sysmap.crux.core.client.datasource.DataSource;
import br.com.sysmap.crux.core.client.datasource.RegisteredDataSources;
import br.com.sysmap.crux.core.client.formatter.Formatter;
import br.com.sysmap.crux.core.client.formatter.RegisteredClientFormatters;
import br.com.sysmap.crux.core.client.screen.parser.CruxMetaData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.logging.client.LogConfiguration;

/**
 * Factory for CRUX screen. 
 * @author Thiago da Rosa de Bustamante
 *
 */
public class ScreenFactory 
{
	private static Logger logger = Logger.getLogger(ScreenFactory.class.getName());

	private static ScreenFactory instance = null;
	 
	private RegisteredClientFormatters registeredClientFormatters = null;
	private RegisteredDataSources registeredDataSources = null;
	private ViewFactory viewFactory = null;
	private Screen screen = null;
	
	
	/**
	 * Constructor
	 */
	private ScreenFactory()
	{
		this.registeredDataSources = GWT.create(RegisteredDataSources.class);
	}
	
	/**
	 * Retrieve the ScreenFactory instance.
	 * @return
	 */
	public static ScreenFactory getInstance()
	{
		if (instance == null)
		{
			instance = new ScreenFactory();
		}
		return instance;
	}
	
	/**
	 * Create a new DataSource instance
	 * @param dataSource dataSource name, declared with <code>@DataSource</code> annotation
	 * @return new dataSource instance
	 */
	public DataSource<?> createDataSource(String dataSource)
	{
		return this.registeredDataSources.getDataSource(dataSource);
	}

	/**
	 * 
	 * @param formatter
	 * @return
	 */
	public Formatter getClientFormatter(String formatter)
	{
		if (this.registeredClientFormatters == null)
		{
			this.registeredClientFormatters = (RegisteredClientFormatters) GWT.create(RegisteredClientFormatters.class);
		}

		return this.registeredClientFormatters.getClientFormatter(formatter);
	}
	
	/**
	 * @deprecated - Use createDataSource(java.lang.String) instead.
	 * @param dataSource
	 * @return
	 */
	@Deprecated
	public DataSource<?> getDataSource(String dataSource)
	{
		return createDataSource(dataSource);
	}

	/**
	 * Get the screen associated with current page. If not created yet, create it.
	 * @return
	 */
	public Screen getScreen()
	{
		if (screen == null)
		{
			create();
		}
		return screen;
	}
	
	/**
	 * 
	 */
	private void create()
	{
		CruxMetaData metaData = CruxMetaData.loadMetaData();
		screen = new Screen(metaData.getScreenId(), metaData.getLazyDependencies());
		this.viewFactory = (ViewFactory) GWT.create(ViewFactory.class);
		
		if (LogConfiguration.loggingIsEnabled())
		{
			logger.info(Crux.getMessages().screenFactoryCreatingView(screen.getIdentifier()));
		}
		this.viewFactory.createView(screen.getIdentifier());
	}
}
