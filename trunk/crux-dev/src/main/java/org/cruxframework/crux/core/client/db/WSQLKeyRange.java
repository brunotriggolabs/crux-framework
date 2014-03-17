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
package org.cruxframework.crux.core.client.db;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayMixed;


/**
 * Specify a range of Keys. Used to openCursors on object stores or indexes.
 * @param <K> The type of the key referenced by this KeyRange .
 * @author Thiago da Rosa de Bustamante
 *
 */
public class WSQLKeyRange<K> implements KeyRange<K> 
{
	private final JsArrayMixed properties;
	
	protected WSQLKeyRange(JsArrayMixed properties)
    {
		this.properties = properties;
    }
	
    public boolean isLowerOpen()
    {
    	return properties.getBoolean(2);
    }

    public boolean isUpperOpen()
    {
    	return properties.getBoolean(3);
    }
	
	public static JavaScriptObject getNativeKeyRange(KeyRange<?> range)
	{
		return getNativeKeyRange(((WSQLKeyRange<?>)range).properties);
	}
	
	private static native JavaScriptObject getNativeKeyRange(JsArrayMixed properties)/*-{
		return {
			lower: properties[0],
			upper: properties[1]
		};
	}-*/;
}