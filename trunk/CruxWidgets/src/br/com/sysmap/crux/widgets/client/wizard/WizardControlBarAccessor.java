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
package br.com.sysmap.crux.widgets.client.wizard;


/**
 * @author Thiago da Rosa de Bustamante - <code>tr_bustamante@yahoo.com.br</code>
 *
 */
public class WizardControlBarAccessor
{
	private WizardControlBarProxy proxy;
	
	/**
	 * @param proxy
	 */
	WizardControlBarAccessor(WizardControlBarProxy proxy)
	{
		this.proxy = proxy;
	}
	
	/**
	 * 
	 */
	public void finish()
	{
		proxy.finish();
	}
	
	/**
	 * 
	 */
	public void cancel()
    {
		proxy.cancel();
    }

	/**
	 * 
	 */
	public void next()
    {
	    proxy.next();
    }

	/**
	 * 
	 */
	public void previous()
    {
	    proxy.previous();
    }
	
	/**
	 * @param spacing
	 */
	public void setSpacing(int spacing)
	{
		proxy.setSpacing(spacing);
	}
	
	/**
	 * @return
	 */
	public int getSpacing()
	{
		return proxy.getSpacing();
	}
	
	/**
	 * @return
	 */
	public boolean isVertical()
	{
		return proxy.isVertical();
	}

	/**
	 * @return
	 */
	public String getPreviousLabel()
    {
    	return proxy.getPreviousLabel();
    }

	/**
	 * @return
	 */
	public String getNextLabel()
    {
    	return proxy.getNextLabel();
    }

	/**
	 * @return
	 */
	public String getCancelLabel()
    {
    	return proxy.getCancelLabel();
    }

	/**
	 * @return
	 */
	public String getFinishLabel()
    {
    	return proxy.getFinishLabel();
    }

	/**
	 * @param previousLabel
	 */
	public void setPreviousLabel(String previousLabel)
	{
		proxy.setPreviousLabel(previousLabel);
	}
	
	/**
	 * @param nextLabel
	 */
	public void setNextLabel(String nextLabel)
	{
		proxy.setNextLabel(nextLabel);
	}
	
	/**
	 * @param cancelLabel
	 */
	public void setCancelLabel(String cancelLabel)
	{
		proxy.setCancelLabel(cancelLabel);
	}
	
	/**
	 * @param finishLabel
	 */
	public void setFinishLabel(String finishLabel)
	{
		proxy.setFinishLabel(finishLabel);
	}
	
	public String getButtonsWidth()
    {
    	return proxy.getButtonsWidth();
    }

	public void setButtonsWidth(String buttonWidth)
    {
		proxy.setButtonsWidth(buttonWidth);
    }

	public String getButtonsHeight()
    {
    	return proxy.getButtonsHeight();
    }

	public void setButtonsHeight(String buttonHeight)
    {
		proxy.setButtonsHeight(buttonHeight);
    }

	public String getButtonsStyle()
    {
    	return proxy.getButtonsStyle();
    }

	public void setButtonsStyle(String buttonStyle)
    {
		proxy.setButtonsStyle(buttonStyle);
    }
}
