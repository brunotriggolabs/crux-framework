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
package org.cruxframework.crux.smartfaces.rebind.pager;

import org.cruxframework.crux.core.rebind.screen.widget.creator.AbstractPagerFactory;
import org.cruxframework.crux.core.rebind.screen.widget.declarative.DeclarativeFactory;
import org.cruxframework.crux.core.shared.Experimental;
import org.cruxframework.crux.smartfaces.client.pager.PredictivePager;
import org.cruxframework.crux.smartfaces.rebind.Constants;

/**
 * @author Thiago da Rosa de Bustamante
 *
 * - EXPERIMENTAL - 
 * THIS CLASS IS NOT READY TO BE USED IN PRODUCTION. IT CAN CHANGE FOR NEXT RELEASES
 */
@Experimental
@DeclarativeFactory(id="predictivePager", library=Constants.LIBRARY_NAME, targetWidget=PredictivePager.class,
					description="A pager widget that can predict the datasource size at the load instant.")
public class PredictivePagerFactory extends AbstractPagerFactory {}
