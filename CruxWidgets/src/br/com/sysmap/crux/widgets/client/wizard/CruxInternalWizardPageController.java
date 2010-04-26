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

import br.com.sysmap.crux.core.client.controller.ExposeOutOfModule;
import br.com.sysmap.crux.core.client.event.EventClientHandlerInvoker;
import br.com.sysmap.crux.core.client.event.EventProcessor;
import br.com.sysmap.crux.core.client.screen.InvokeControllerEvent;
import br.com.sysmap.crux.core.client.screen.ModuleComunicationSerializer;
import br.com.sysmap.crux.core.client.screen.Screen;
import br.com.sysmap.crux.widgets.client.dialog.PopupData;
import br.com.sysmap.crux.widgets.client.dynatabs.DynaTabsControllerInvoker;

/**
 * @author Thiago da Rosa de Bustamante - <code>tr_bustamante@yahoo.com.br</code>
 *
 */
public class CruxInternalWizardPageController extends DynaTabsControllerInvoker implements EventClientHandlerInvoker
{
	/**
	 * 
	 */
	public CruxInternalWizardPageController()
    {
		ModuleComunicationSerializer serializer = Screen.getCruxSerializer();
		serializer.registerCruxSerializable(PopupData.class.getName(), new PopupData());
    }
	
	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public boolean onLeave(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		WizardPage wizardPage = Screen.get(WizardPage.PAGE_UNIQUE_ID, WizardPage.class);
		if (wizardPage != null)
		{
			LeaveEvent leaveEvent = LeaveEvent.fire(wizardPage, new PageWizardProxy(wizardId));
			return leaveEvent.isCanceled();
		}
		
		return false;
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public void onEnter(InvokeControllerEvent event)
	{
		WizardPage wizardPage = Screen.get(WizardPage.PAGE_UNIQUE_ID, WizardPage.class);
		if (wizardPage != null)
		{
			String wizardId = (String) event.getParameter(0);
			String previousStep = (String)event.getParameter(1);
			EnterEvent.fire(wizardPage, new PageWizardProxy(wizardId), previousStep);
		}
	}
	
	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public void onCommand(InvokeControllerEvent event)
	{
		WizardPage wizardPage = Screen.get(WizardPage.PAGE_UNIQUE_ID, WizardPage.class);
		if (wizardPage != null)
		{
			String wizardId = (String) event.getParameter(0);
			String commandId = (String)event.getParameter(1);
			WizardCommandEvent wizardCommandEvent = new WizardCommandEvent(new PageWizardProxy(wizardId));
			wizardPage.fireCommandEvent(commandId, wizardCommandEvent);
		}
	}

	/**
	 * @return
	 */
	@ExposeOutOfModule
	public WizardCommandData[] listCommands()
	{
		WizardPage wizardPage = Screen.get(WizardPage.PAGE_UNIQUE_ID, WizardPage.class);
		if (wizardPage != null)
		{
			return wizardPage.listCommands();
		}
		return null;
		
	}
	
	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public boolean cancel(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		return Screen.get(wizardId, Wizard.class).cancel();
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public boolean finish(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		return Screen.get(wizardId, Wizard.class).finish();
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public boolean first(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		return Screen.get(wizardId, Wizard.class).selectStep(0, true);
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public int getStepOrder(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		String stepId = (String) event.getParameter(1);
		return Screen.get(wizardId, Wizard.class).getStepOrder(stepId);
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public boolean next(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
	    Wizard wizard = Screen.get(wizardId, Wizard.class);
		int currentStep = wizard.getCurrentStepIndex();
		return wizard.selectStep(currentStep+1, true);
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public boolean previous(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
	    Wizard wizard = Screen.get(wizardId, Wizard.class);
		int currentStep = wizard.getCurrentStepIndex();
		return wizard.selectStep(currentStep-1, true);
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public boolean selectStep(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		String stepId = (String) event.getParameter(1);
		Boolean ignoreLeaveEvents = (Boolean) event.getParameter(2);
		return Screen.get(wizardId, Wizard.class).selectStep(stepId, ignoreLeaveEvents);
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public String getButtonsHeight(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			return controlBar.getButtonsHeight();
		}
		return null;
	}
	
	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public String getButtonsStyle(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			return controlBar.getButtonsStyle();
		}
		return null;
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public String getButtonsWidth(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			return controlBar.getButtonsWidth();
		}
		return null;
	}
	
	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public String getCancelLabel(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			return controlBar.getCancelLabel();
		}
		return null;
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public String getFinishLabel(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			return controlBar.getFinishLabel();
		}
		return null;
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public String getNextLabel(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			return controlBar.getNextLabel();
		}
		return null;
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public String getPreviousLabel(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			return controlBar.getPreviousLabel();
		}
		return null;
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public int getSpacing(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			return controlBar.getSpacing();
		}
		return 0;
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public boolean isVertical(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			return controlBar.isVertical();
		}
		return false;
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public void setButtonsHeight(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		String buttonHeight = (String) event.getParameter(1);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			controlBar.setButtonsHeight(buttonHeight);
		}
	}
	
	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public void setButtonsStyle(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		String buttonStyle = (String) event.getParameter(1);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			controlBar.setButtonsStyle(buttonStyle);
		}
	}
	
	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public void setButtonsWidth(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		String buttonWidth = (String) event.getParameter(1);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			controlBar.setButtonsWidth(buttonWidth);
		}
	}
	
	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public void setCancelLabel(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		String cancelLabel = (String) event.getParameter(1);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			controlBar.setCancelLabel(cancelLabel);
		}
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public void setFinishLabel(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		String finishLabel = (String) event.getParameter(1);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			controlBar.setFinishLabel(finishLabel);
		}
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public void setNextLabel(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		String nextLabel = (String) event.getParameter(1);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			controlBar.setNextLabel(nextLabel);
		}
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public void setPreviousLabel(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		String previousLabel = (String) event.getParameter(1);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			controlBar.setPreviousLabel(previousLabel);
		}
	}

	/**
	 * @param event
	 * @return
	 */
	@ExposeOutOfModule
	public void setSpacing(InvokeControllerEvent event)
	{
		String wizardId = (String) event.getParameter(0);
		Integer spacing = (Integer) event.getParameter(1);
		WizardControlBar controlBar = Screen.get(wizardId, Wizard.class).getControlBar();
		if (controlBar != null)
		{
			controlBar.setSpacing(spacing);
		}
	}

	/**
	 * @see br.com.sysmap.crux.core.client.event.EventClientHandlerInvoker#invoke(java.lang.String, java.lang.Object, boolean, br.com.sysmap.crux.core.client.event.EventProcessor)
	 */
	public void invoke(String method, Object sourceEvent, boolean fromOutOfModule, EventProcessor eventProcessor) throws Exception
	{
		Object returnValue = null;
		boolean hasReturn = false;

		try
		{
			if ("selectStep".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = selectStep((InvokeControllerEvent)sourceEvent);
			}
			else if("previous".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = previous((InvokeControllerEvent)sourceEvent);
			}
			else if("next".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = next((InvokeControllerEvent)sourceEvent);
			}
			else if("getStepOrder".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = getStepOrder((InvokeControllerEvent)sourceEvent);
			}
			else if("first".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = first((InvokeControllerEvent)sourceEvent);
			}
			else if("finish".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = finish((InvokeControllerEvent)sourceEvent);
			}
			else if("listCommands".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = listCommands();
			}
			else if("cancel".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = cancel((InvokeControllerEvent)sourceEvent);
			}
			else if("onCommand".equals(method) && fromOutOfModule)
			{
				onCommand((InvokeControllerEvent)sourceEvent);
			}
			else if("onEnter".equals(method) && fromOutOfModule)
			{
				onEnter((InvokeControllerEvent)sourceEvent);
			}
			else if("onLeave".equals(method) && fromOutOfModule)
			{
				onLeave((InvokeControllerEvent)sourceEvent);
			}
			else if("getButtonsHeight".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = getButtonsHeight((InvokeControllerEvent)sourceEvent);
			}
			else if("getButtonsStyle".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = getButtonsStyle((InvokeControllerEvent)sourceEvent);
			}
			else if("getButtonsWidth".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = getButtonsWidth((InvokeControllerEvent)sourceEvent);
			}
			else if("getCancelLabel".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = getCancelLabel((InvokeControllerEvent)sourceEvent);
			}
			else if("getFinishLabel".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = getFinishLabel((InvokeControllerEvent)sourceEvent);
			}
			else if("getNextLabel".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = getNextLabel((InvokeControllerEvent)sourceEvent);
			}
			else if("getPreviousLabel".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = getPreviousLabel((InvokeControllerEvent)sourceEvent);
			}
			else if("getSpacing".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = getSpacing((InvokeControllerEvent)sourceEvent);
			}
			else if("isVertical".equals(method) && fromOutOfModule)
			{
				hasReturn = true;
				returnValue = isVertical((InvokeControllerEvent)sourceEvent);
			}
			else if("setButtonsHeight".equals(method) && fromOutOfModule)
			{
				setButtonsHeight((InvokeControllerEvent)sourceEvent);
			}
			else if("setButtonsStyle".equals(method) && fromOutOfModule)
			{
				setButtonsStyle((InvokeControllerEvent)sourceEvent);
			}
			else if("setButtonsWidth".equals(method) && fromOutOfModule)
			{
				setButtonsWidth((InvokeControllerEvent)sourceEvent);
			}
			else if("setCancelLabel".equals(method) && fromOutOfModule)
			{
				setCancelLabel((InvokeControllerEvent)sourceEvent);
			}
			else if("setFinishLabel".equals(method) && fromOutOfModule)
			{
				setFinishLabel((InvokeControllerEvent)sourceEvent);
			}
			else if("setNextLabel".equals(method) && fromOutOfModule)
			{
				setNextLabel((InvokeControllerEvent)sourceEvent);
			}
			else if("setPreviousLabel".equals(method) && fromOutOfModule)
			{
				setPreviousLabel((InvokeControllerEvent)sourceEvent);
			}
			else if("setSpacing".equals(method) && fromOutOfModule)
			{
				setSpacing((InvokeControllerEvent)sourceEvent);
			}
		}
		catch (Throwable e)
		{
			eventProcessor.setException(e);
		} 

		if (hasReturn)
		{
			eventProcessor.setHasReturn(true);
			eventProcessor.setReturnValue(returnValue);
		}
	}

	/**
	 * @see br.com.sysmap.crux.core.client.screen.ScreenBindableObject#isAutoBindEnabled()
	 */
	public boolean isAutoBindEnabled()
    {
	    return false;
    }

	/**
	 * @see br.com.sysmap.crux.core.client.screen.ScreenBindableObject#updateControllerObjects()
	 */
	public void updateControllerObjects()
    {
    }

	/**
	 * @see br.com.sysmap.crux.core.client.screen.ScreenBindableObject#updateScreenWidgets()
	 */
	public void updateScreenWidgets()
    {
    }
}
