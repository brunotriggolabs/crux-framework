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
package org.cruxframework.crux.gadgets.client.controller;

import org.cruxframework.crux.core.client.Crux;
import org.cruxframework.crux.core.client.controller.Controller;
import org.cruxframework.crux.core.client.controller.Create;
import org.cruxframework.crux.core.client.controller.Expose;
import org.cruxframework.crux.core.client.utils.StringUtils;
import org.cruxframework.crux.gadgets.client.TabLayoutMsg;
import org.cruxframework.crux.gadgets.client.dto.GadgetMetadata;
import org.cruxframework.crux.gadgets.client.dto.GadgetsConfiguration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;

/**
 * @author Thiago da Rosa de Bustamante
 *
 */
@Controller("gadgetConfigController")
public class GadgetConfigController
{
	@Create
	protected TabLayoutMsg messages;
	private GadgetsConfiguration configuration;
	
	@Expose
	public void onLoadProduction()
	{
		onLoad(false);
	}

	@Expose
	public void onLoadDebug()
	{
		onLoad(true);
	}

	/**
	 * Load container configuration
	 * @param debug
	 */
	protected void onLoad(final boolean debug)
	{
		Scheduler.get().scheduleFixedDelay(new RepeatingCommand()
		{
			private int numTries = 0;
			@Override
			public boolean execute()
			{
				boolean ret = !checkLayoutManager();
				if (ret && numTries > 10)
				{
					Crux.getErrorHandler().handleError(messages.layoutManagerNotFound());
					return false;
				}
				if (!ret)
				{
					configure(debug);
				}
				return ret;
			}
		}, 100);
	}
	
	//TODO: receber esses dados
	private native JsArrayString getGadgets()/*-{
	    return ['http://rssgadget.livewaresystems.com/gen/futebolbr/rssgadget/flamengo.xml', 
	            'http://rssgadget.livewaresystems.com/gen/futebolbr/rssgadget/vasco.xml',
	            'http://rssgadget.livewaresystems.com/gen/futebolbr/rssgadget/corinthians.xml'];
    }-*/;

	/**
	 * 
	 * @param userCountry
	 * @param userLanguage
	 * @param containerView
	 * @param secureToken
	 * @param gadgetUrls
	 * @param controller
	 */
	protected native void getGadgetsMetadata(String userCountry, 
			       							 String userLanguage, 
			       							 String containerView, 
			       							 String secureToken, 
			       							 JsArrayString gadgetUrls,
			       							 GadgetConfigController controller)/*-{
		var request = {
			context: {
				country: userCountry,
				language: userLanguage,
				view: containerView,
				container: "default"
			},
			gadgets: []
		};

		for (var i=0; i<gadgetUrls.length; i++) {
			request.gadgets[request.gadgets.length] = {url: gadgetUrls[i], moduleId: 1};
		}

		var makeRequestParams = {
			"CONTENT_TYPE" : "JSON",
			"METHOD" : "POST",
			"POST_DATA" : $wnd.gadgets.json.stringify(request)
		};

		var url = "/gadgets/metadata?st=" + secureToken;
		$wnd.gadgets.io.makeNonProxiedRequest(url,
			function (obj) {
				var numGadgets = obj.data.gadgets.length;
				var result = [];
				for (var i=0; i<numGadgets; i++) {
					var gadgetMetadata = obj.data.gadgets[i];
					result[result.length] = gadgetMetadata;
				}
				controller.@org.cruxframework.crux.gadgets.client.controller.GadgetConfigController::loadConfigurationMetadata(Lcom/google/gwt/core/client/JsArray;)(result);
			},
			makeRequestParams,
			"application/javascript"
		); 	
	
	}-*/;
	
	protected void configure(boolean debug)
    {
		configuration = new GadgetsConfiguration();
		configuration.setDebug(debug);
		String containerURL = getContainerURL();
		String url = Window.Location.getParameter("url");
		configureLocale(configuration);
		configureCurrentView(configuration, url);
		
		JsArrayString gadgets;
		if (StringUtils.isEmpty(url))
		{
			gadgets = getGadgets();
		}
		else
		{
			gadgets = createStringArray();
			gadgets.push(url);
		}
		getGadgetsMetadata(configuration.getCountry(), 
						   configuration.getLanguage(), 
						   configuration.getCurrentView(), 
						   "john.doe:john.doe:appid:cont:url:0:default", 
						   gadgets, this);
	    
    }

	protected native JsArray<JsArray<GadgetMetadata>> createMetadataGrid()/*-{
		return [];
	}-*/;
	
	protected native JsArray<GadgetMetadata> createMetadataArray()/*-{
		return [];
	}-*/;

	protected native JsArrayString createStringArray()/*-{
		return [];
	}-*/;

	protected void loadConfigurationMetadata(JsArray<GadgetMetadata> metadata) //TODO: ajustar isso aki
	{
		//TODO: carregar configuracoes de urlbase, parentcontainerurl, currentView, userId, groupId
		configuration.setMetadata(getGadgetsMetadata(metadata));
		nativeConfigure(configuration);
	}

	private void configureCurrentView(GadgetsConfiguration configuration, String url)
    {
	    String currentView = (StringUtils.isEmpty(url)?"profile":"canvas");
		configuration.setCurrentView(currentView);
    }

	private void configureLocale(GadgetsConfiguration configuration)
    {
	    String country = Window.Location.getParameter("country");
		String language = Window.Location.getParameter("lang");
		if (StringUtils.isEmpty(language) && StringUtils.isEmpty(country))
		{
			String localeName = LocaleInfo.getCurrentLocale().getLocaleName();
			String[] localeParts = localeName.split("_");
			if (localeParts != null && localeParts.length > 0)
			{
				language = localeParts[0];
				if (localeParts.length > 1)
				{
					country = localeParts[1];
				}
			}
		}
		if (StringUtils.isEmpty(language))
		{
			language = "default";
		}
		if (StringUtils.isEmpty(country))
		{
			country = "default";
		}
		configuration.setLanguage(language);
		configuration.setCountry(country);
    }

	/**
	 * 
	 * @return
	 */
	protected native String getContainerURL()/*-{
		return ($doc.location + '')
	}-*/;
	
	
	/**
	 * 
	 * @param metadata
	 * @return
	 */
	protected JsArray<JsArray<GadgetMetadata>> getGadgetsMetadata(JsArray<GadgetMetadata> metadata)
    {
	    JsArray<JsArray<GadgetMetadata>> array = createMetadataGrid();
		array.push(metadata);
		array.push(createMetadataArray());
		array.push(createMetadataArray());
	    return array;
    }
	
	private native void nativeConfigure(GadgetsConfiguration config)/*-{
		$wnd.__configureLayoutManager(config);
	}-*/;

	protected native boolean checkLayoutManager()/*-{
	    if (!$wnd.__configureLayoutManager){
	    	false;
	    }
	    return true;
    }-*/;
}
