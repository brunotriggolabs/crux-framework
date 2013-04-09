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
package org.cruxframework.crux.widgets.client.colorpicker;

import org.cruxframework.crux.widgets.client.util.ColorUtils;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;

public class HuePicker extends Composite implements HasValueChangeHandlers<Integer>
{
	private Canvas canvas;
	private int handleY = 90;
	private boolean mouseDown;

	public HuePicker()
	{
		canvas = Canvas.createIfSupported();
		canvas.setStylePrimaryName("huePicker");
		canvas.setPixelSize(26, 180);
		canvas.setCoordinateSpaceHeight(180);
		canvas.setCoordinateSpaceWidth(26);

		initWidget(canvas);

		canvas.addMouseDownHandler(new MouseDownHandler()
		{
			public void onMouseDown(MouseDownEvent event)
			{
				handleY = event.getRelativeY(canvas.getElement());
				drawGradient();
				fireValueChanged(getHue());

				mouseDown = true;
			}
		});
		canvas.addMouseMoveHandler(new MouseMoveHandler()
		{
			public void onMouseMove(MouseMoveEvent event)
			{
				if (mouseDown)
				{
					handleY = event.getRelativeY(canvas.getElement());
					drawGradient();
					fireValueChanged(getHue());
				}
			}
		});
		canvas.addMouseUpHandler(new MouseUpHandler()
		{
			public void onMouseUp(MouseUpEvent event)
			{
				mouseDown = false;
			}
		});
		canvas.addMouseOutHandler(new MouseOutHandler()
		{
			public void onMouseOut(MouseOutEvent event)
			{
				mouseDown = false;
			}
		});
	}

	@Override
	protected void onAttach()
	{
		super.onAttach();
		drawGradient();
	}

	private void drawGradient()
	{
		Context2d ctx = canvas.getContext2d();

		// draw gradient
		ctx.setFillStyle("#ffffff"); 
		ctx.fillRect(0, 0, 26, 180);
		for (int y = 0; y <= 179; y++)
		{
			String hex = ColorUtils.hsl2hex(y * 2, 100, 100);
			ctx.setFillStyle("#" + hex); 
			ctx.fillRect(3, y, 20, 1);
		}

		// draw handle
		if (handleY >= 0)
		{
			ctx.setFillStyle("#000000"); 

			ctx.beginPath();
			ctx.moveTo(3, handleY);
			ctx.lineTo(0, handleY - 3);
			ctx.lineTo(0, handleY + 3);
			ctx.closePath();
			ctx.fill();

			ctx.moveTo(23, handleY);
			ctx.lineTo(26, handleY - 3);
			ctx.lineTo(26, handleY + 3);
			ctx.closePath();
			ctx.fill();
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Integer> handler)
	{
		return addHandler(handler, ValueChangeEvent.getType());
	}

	private void fireValueChanged(int hue)
	{
		ValueChangeEvent.fire(this, hue);
	}

	public int getHue()
	{
		return handleY * 2;
	}

	public void setHue(int hue)
	{
		handleY = (int) Math.min(Math.max(Math.round(hue / 2d), 0d), 179d);
		drawGradient();
		fireValueChanged(hue);
	}
}
