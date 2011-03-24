package org.cruxframework.crux.showcase.client.controller;

import org.cruxframework.crux.core.client.controller.Controller;
import org.cruxframework.crux.core.client.controller.Expose;
import org.cruxframework.crux.core.client.screen.Screen;
import org.cruxframework.crux.showcase.client.dto.Contact;
import org.cruxframework.crux.widgets.client.collapsepanel.CollapsePanel;
import org.cruxframework.crux.widgets.client.dialog.MessageBox;
import org.cruxframework.crux.widgets.client.grid.DataRow;
import org.cruxframework.crux.widgets.client.grid.Grid;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

@Controller("widgetGridController")
public class WidgetGridController {
	
	@Expose
	public void onLoad() {
		Grid grid = Screen.get("widgetGrid", Grid.class);
		grid.loadData();
		
		HTMLPanel htmlPanel = Screen.get("invisibleHTMLPanel", HTMLPanel.class);
		htmlPanel.setVisible(true);
		
		CollapsePanel collapsePanel = Screen.get("testeSuperCollapse", CollapsePanel.class);
		collapsePanel.setVisible(true);

		SimplePanel simplePanel = Screen.get("testeSimpleInvisible", SimplePanel.class);
		simplePanel.setVisible(true);
	}
	
	@Expose
	public void onClickCall(ClickEvent event) {
				
		Image image = (Image) event.getSource();
		Grid grid = Screen.get("widgetGrid", Grid.class);
		DataRow row = grid.getRow(image);
		
		Contact contact = (Contact) row.getBoundObject();
		
		MessageBox.show(
			"Information", 
			"Calling " + contact.getName() + " - [" + contact.getPhone() + "]", 
			null
		);
	}
}