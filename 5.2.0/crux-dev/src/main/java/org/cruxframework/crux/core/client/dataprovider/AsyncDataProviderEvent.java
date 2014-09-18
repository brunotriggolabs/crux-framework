/*
 * Copyright 2014 cruxframework.org.
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
package org.cruxframework.crux.core.client.dataprovider;

import java.util.List;

import org.cruxframework.crux.core.client.collection.Array;
import org.cruxframework.crux.core.client.event.BaseEvent;

/**
 * @author Thiago da Rosa de Bustamante
 *
 */
public class AsyncDataProviderEvent<T> extends BaseEvent<AsyncDataProvider<T>>
{
	private final int startRecord;
	private final int endRecord;

	protected AsyncDataProviderEvent(AsyncDataProvider<T> source, int startRecord, int endRecord)
    {
	    super(source);
		this.startRecord = startRecord;
		this.endRecord = endRecord;
    }
	
	public int getEndRecord()
	{
		return endRecord;
	}

	public int getStartRecord()
	{
		return startRecord;
	}

	public void updateData(List<T> data)
	{
		getSource().updateData(data);
	}

	public void updateData(T[] data)
	{
		getSource().updateData(data);
	}

	public void updateData(Array<T> data)
	{
		getSource().updateData(data);
	}
}
