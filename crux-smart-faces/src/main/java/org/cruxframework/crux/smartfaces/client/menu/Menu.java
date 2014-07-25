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
package org.cruxframework.crux.smartfaces.client.menu;

import org.cruxframework.crux.core.client.collection.FastList;
import org.cruxframework.crux.core.client.screen.Screen;
import org.cruxframework.crux.smartfaces.client.panel.BasePanel;
import org.cruxframework.crux.smartfaces.client.panel.SelectablePanel;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;

/**
 * A cross device menu
 * @author Samuel Almeida Cardoso (samuel@cruxframework.org)
 * @author Claudio Holanda (claudio.junior@cruxframework.org)
 * @author Thiago da Rosa de Bustamante
 *
 */
public class Menu extends Composite implements HasAnimation, HasEnabled 
{
	private static final String SPACE = " ";
	
	public static enum LargeType
	{
		VERTICAL_TREE(STYLE_FACES_VERTICAL + SPACE + STYLE_FACES_TREE),
		VERTICAL_SLIDE(STYLE_FACES_VERTICAL + SPACE + STYLE_FACES_SLIDE),
		VERTICAL_ACCORDION(STYLE_FACES_VERTICAL + SPACE + STYLE_FACES_ACCORDION),
		VERTICAL_DROPDOWN(STYLE_FACES_VERTICAL + SPACE + STYLE_FACES_DROPDOWN),
		HORIZONTAL_TREE(STYLE_FACES_HORIZONTAL + SPACE + STYLE_FACES_TREE),
		HORIZONTAL_ACCORDION(STYLE_FACES_HORIZONTAL + SPACE + STYLE_FACES_ACCORDION),
		HORIZONTAL_DROPDOWN(STYLE_FACES_HORIZONTAL + SPACE + STYLE_FACES_DROPDOWN);
		
		String styleName;
		LargeType(String styleName)
		{
			this.styleName = styleName;	
		}
	}
	
	public static enum SmallType
	{
		VERTICAL_TREE(STYLE_FACES_VERTICAL + SPACE + STYLE_FACES_TREE),
		VERTICAL_SLIDE(STYLE_FACES_VERTICAL + SPACE + STYLE_FACES_SLIDE),
		VERTICAL_ACCORDION(STYLE_FACES_VERTICAL + SPACE + STYLE_FACES_ACCORDION),
		HORIZONTAL_ACCORDION(STYLE_FACES_HORIZONTAL + SPACE + STYLE_FACES_ACCORDION);
		
		String styleName;
		SmallType(String styleName)
		{
			this.styleName = styleName;	
		}
	}
	
	public    static final String STYLE_FACES_MENU = "facesMenu";
	protected static final String STYLE_FACES_SLIDE = "facesMenu-slide";
	protected static final String STYLE_FACES_DROPDOWN = "facesMenu-dropdown";
	protected static final String STYLE_FACES_TREE = "facesMenu-tree";
	protected static final String STYLE_FACES_ACCORDION = "facesMenu-accordion";
	protected static final String STYLE_FACES_HORIZONTAL = "facesMenu-horizontal";
	protected static final String STYLE_FACES_VERTICAL = "facesMenu-vertical";
	protected static final String STYLE_FACES_OPEN = "facesMenu-open";
	protected static final String STYLE_FACES_HAS_CHILDREN = "facesMenu-hasChildren";
	protected static final String STYLE_FACES_EMPTY = "facesMenu-empty";
	protected static final String STYLE_FACES_LI = "facesMenu-li";
	protected static final String STYLE_FACES_UL = "facesMenu-ul";
	
	private boolean enabled = true;
	private MenuItem root;
	private MenuPanel menuPanel = new MenuPanel();
	private SmallType currentSmallType = null;
	private LargeType currentLargeType = null;
	
	public Menu()
	{
		this(LargeType.VERTICAL_ACCORDION, SmallType.VERTICAL_ACCORDION);
	}
	
	public Menu(LargeType largeType)
	{
		this(null, null);
		setLargeType(largeType);
		
		switch (largeType) 
		{
			case HORIZONTAL_ACCORDION:
				setSmallType(SmallType.HORIZONTAL_ACCORDION);
				break;
			case HORIZONTAL_DROPDOWN:
				setSmallType(SmallType.HORIZONTAL_ACCORDION);
				break;
			case HORIZONTAL_TREE:
				setSmallType(SmallType.HORIZONTAL_ACCORDION);
				break;
			case VERTICAL_ACCORDION:
				setSmallType(SmallType.VERTICAL_ACCORDION);
				break;
			case VERTICAL_DROPDOWN:
				setSmallType(SmallType.VERTICAL_ACCORDION);
				break;
			case VERTICAL_SLIDE:
				setSmallType(SmallType.VERTICAL_SLIDE);
				break;
			case VERTICAL_TREE:
				setSmallType(SmallType.VERTICAL_TREE);
				break;
			default:
				break;
		}
	}
	
	public Menu(SmallType smallType)
	{
		this(null, null);
		setSmallType(smallType);
		
		switch (smallType) 
		{
			case HORIZONTAL_ACCORDION:
				setLargeType(LargeType.HORIZONTAL_ACCORDION);
				break;
			case VERTICAL_ACCORDION:
				setLargeType(LargeType.VERTICAL_ACCORDION);
				break;
			case VERTICAL_SLIDE:
				setLargeType(LargeType.VERTICAL_SLIDE);
				break;
			case VERTICAL_TREE:
				setLargeType(LargeType.VERTICAL_TREE);
				break;
			default:
				break;
		}
	}
	
	public Menu(LargeType largeType, SmallType smallType)
	{
		initWidget(menuPanel);
		root = new MenuItem(null);
		menuPanel.add(root);
		root.setMenu(this);
		setStyleName(getBaseStyleName());

		setLargeType(largeType);
		setSmallType(smallType);
	}

	public String getBaseStyleName()
	{
		return STYLE_FACES_MENU;
	}

	@Override
	public boolean isAnimationEnabled() 
	{
		return false;
	}

	@Override
	public void setAnimationEnabled(boolean enable) 
	{
	}

	@Override
	public boolean isEnabled() 
	{
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) 
	{
		this.enabled = enabled;
		
		FastList<MenuItem> itemsWithEnabledProperty = MenuUtils.findHasEnabledInMenu(root);
		
		if(itemsWithEnabledProperty != null)
		{
			for(int i=0; i<itemsWithEnabledProperty.size();i++)
			{
				((HasEnabled) itemsWithEnabledProperty.get(i).getItemWidget()).setEnabled(enabled);
			}
		}
	}

	public void clear() 
	{
		if(this.root != null)
		{
			this.root.clear();
		}
	}

	/**
	 * Define the menu orientation.
	 */
	public void setLargeType(LargeType largeType) 
	{
		if(largeType != null)
		{
			String deviceName = Screen.getCurrentDevice().toString();
			if(!getStyleName().contains(deviceName))
			{
				addStyleName(deviceName);
			}
			
			removeAllLargeTypes();
			addStyleName(largeType.styleName);
			this.currentLargeType = largeType;
		}
	}
	
	/**
	 * Define how menu will be rendered inside page.
	 */
	public void setSmallType(SmallType smallType) 
	{
		if(smallType != null)
		{
			String deviceName = Screen.getCurrentDevice().toString();
			if(!getStyleName().contains(deviceName))
			{
				addStyleName(deviceName);
			}

			removeAllSmallTypes();
			addStyleName(smallType.styleName);
			this.currentSmallType = smallType ;
		}
	}
	
	private void removeAllLargeTypes()
	{
		for(LargeType type : LargeType.values())
		{
			removeStyleName(type.styleName);
		}
	}
	
	private void removeAllSmallTypes()
	{
		for(SmallType type : SmallType.values())
		{
			removeStyleName(type.styleName);
		}
	}

	/**
	 * Adds a root item.
	 * @return the inserted item. 
	 */
	public MenuItem addItem(Widget widget) 
	{
		return root.addItem(widget);
	}

	/**
	 * Adds a label root item.
	 * @return the inserted item. 
	 */
	public MenuItem addItem(String labelText) 
	{
		return root.addItem(labelText);
	}
	
	/**
	 * Adds a label root item.
	 * @return the inserted item. 
	 */
	public MenuItem addItem(SafeHtml html) 
	{
		return root.addItem(html);
	}

	/**
	 * Adds a label item.
	 * @return the inserted item. 
	 */
	public MenuItem addItem(MenuItem placeToInsert, String labelText) 
	{
		if(placeToInsert == null)
		{
			placeToInsert = this.root;
		}
		return placeToInsert.addItem(labelText);
	}
	
	/**
	 * Adds a label item.
	 * @return the inserted item. 
	 */
	public MenuItem addItem(MenuItem placeToInsert, SafeHtml html) 
	{
		if(placeToInsert == null)
		{
			placeToInsert = this.root;
		}
		return placeToInsert.addItem(html);
	}

	public MenuItem addItem(MenuItem placeToInsert, Widget w) 
	{
		if(placeToInsert == null)
		{
			placeToInsert = this.root;
		}
		return placeToInsert.addItem(w);
	}

	public MenuItem getItem(int index)
	{
		return root.getItem(index);
	}
	
	public MenuItem getItem(String path)
	{
		return root.getItem(path);
	}
	
	public int getItemCount()
	{
		return root.getItemCount();
	}
	
	public int indexOf(MenuItem item)
	{
		return root.indexOf(item);
	}
	
	public boolean removeItem(int index)
	{
		return root.removeItem(index);
	}
	
	public void openAll() 
	{
		MenuUtils.addOrRemoveClass(STYLE_FACES_OPEN, true, MenuUtils.getAllMenuItems(this.root));
	}

	public void open(MenuItem menuItem) 
	{
		if(menuItem == null)
		{
			return;
		}
		
		menuItem.open();
	}

	public void closeAll() 
	{
		MenuUtils.addOrRemoveClass(STYLE_FACES_OPEN, false, MenuUtils.getAllMenuItems(this.root));
	}

	public void close(MenuItem menuItem) 
	{
		if(menuItem == null)
		{
			return;
		}
		
		menuItem.close();
	}

	protected void adopt(MenuItem item)
	{
		menuPanel.adopt(item);
	}
	
	protected void orphan(MenuItem item)
	{
		menuPanel.orphan(item);
	}
	
	protected static class MenuPanel extends BasePanel
	{

		protected MenuPanel()
        {
	        super("nav");
        }
		
		protected void add(MenuItem item)
		{
		    DOM.appendChild(getElement(), item.getElement());
		}	

		protected void adopt(MenuItem item)
		{
			SelectablePanel itemPanel = item.getItemPanel();
			if (itemPanel != null)
			{
			    getChildren().add(itemPanel);
				adopt(itemPanel);
			}
		}
		
		protected void orphan(MenuItem item)
		{
			SelectablePanel itemPanel = item.getItemPanel();
			if (itemPanel != null)
			{
				DOM.removeChild(getElement(), item.getElement());
			    getChildren().remove(itemPanel);
				orphan(itemPanel);
			}
		}
	}

	public SmallType getCurrentSmallType() 
	{
		return currentSmallType;
	}

	public LargeType getCurrentLargeType() 
	{
		return currentLargeType;
	}
}
