package com.mrcrayfish.controllable.client;

import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.gui.GuiButtonController;
//import com.mrcrayfish.controllable.client.gui.GuiControllerSelection;
import com.studiohartman.jamepad.ControllerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.Button.IPressable;
//import net.minecraft.client.gui.GuiOptions;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Author: MrCrayfish
 */
public class GuiEvents
{
    private ControllerManager manager;

    public GuiEvents(ControllerManager manager)
    {
        this.manager = manager;
    }
    
    public class GuiEventsAction implements IPressable {

		@Override
		public void onPress(Button p_onPress_1_) {
			// TODO Auto-generated method stub
			//Minecraft.getInstance().displayGuiScreen(new GuiControllerSelection(manager, true)); Disable GuiControllerSelection til basic functionality confirmed
		}
    	
    }

    @SubscribeEvent
    public void onOpenGui(GuiScreenEvent.InitGuiEvent event)
    {
        /* Resets the controller button states */
        Controller controller = Controllable.getController();
        if(controller != null)
        {
            controller.resetButtonStates();
        }

        if(event.getGui() instanceof OptionsScreen)
        {
            int y = event.getGui().height / 6 + 72 - 6;
            event.addWidget(new GuiButtonController((event.getGui().width / 2) + 5 + 150 + 4, y, new GuiEventsAction())); //TODO will this work?
        }
    }
/*
    @SubscribeEvent
    public void onAction(GuiScreenEvent.ActionPerformedEvent event)
    {
        if(event.getGui() instanceof OptionsScreen)
        {
            if(event.getButton().id == 6969)
            {
                Minecraft.getMinecraft().displayGuiScreen(new GuiControllerSelection(manager, true));
            }
        }
    } */
}
