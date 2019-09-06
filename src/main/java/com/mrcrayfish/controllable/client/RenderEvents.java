package com.mrcrayfish.controllable.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mrcrayfish.controllable.Reference;
import com.mrcrayfish.controllable.event.AvailableActionsEvent;
import com.mrcrayfish.controllable.event.RenderAvailableActionsEvent;
import com.mrcrayfish.controllable.event.RenderPlayerPreviewEvent;

import net.minecraft.block.Block;
import net.minecraft.block.ContainerBlock;
import net.minecraft.client.MainWindow;
//import net.minecraft.block.BlockContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
//import net.minecraft.client.gui.Gui;
//import net.minecraft.client.gui.GuiIngame;
//import net.minecraft.client.gui.ScaledResolution;
//import net.minecraft.client.gui.inventory.GuiContainer;
//import net.minecraft.client.gui.inventory.GuiInventory;
//import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItem;
//import net.minecraft.inventory.Slot;
//import net.minecraft.item.EnumAction;
//import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class RenderEvents
{
    private static final ResourceLocation CONTROLLER_BUTTONS = new ResourceLocation(Reference.MOD_ID, "textures/gui/buttons.png");

    private Map<Integer, Action> actions = new HashMap<>();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        if(event.phase == TickEvent.Phase.START && mc.player != null && !mc.gameSettings.hideGUI)
        {
            actions = new HashMap<>();

            if(mc.currentScreen instanceof ContainerScreen)
            {
                if(mc.player.inventory.getItemStack().isEmpty())
                {
                	ContainerScreen container = (ContainerScreen) mc.currentScreen;
                    if(container.getSlotUnderMouse() != null)
                    {
                        Slot slot = container.getSlotUnderMouse();
                        if(slot.getHasStack())
                        {
                            actions.put(Buttons.A, new Action(I18n.format("controllable.action.pickup_stack"), Action.Side.LEFT));
                            actions.put(Buttons.X, new Action(I18n.format("controllable.action.pickup_item"), Action.Side.LEFT));
                            actions.put(Buttons.B, new Action(I18n.format("controllable.action.quick_move"), Action.Side.LEFT));
                        }
                    }
                }
                else
                {
                    actions.put(Buttons.A, new Action(I18n.format("controllable.action.place_stack"), Action.Side.LEFT));
                    actions.put(Buttons.X, new Action(I18n.format("controllable.action.place_item"), Action.Side.LEFT));
                }

                actions.put(Buttons.Y, new Action(I18n.format("controllable.action.close_inventory"), Action.Side.RIGHT));
            }
            else if(mc.currentScreen == null)
            {
                boolean blockHit = mc.objectMouseOver != null && mc.objectMouseOver.getType() == RayTraceResult.Type.BLOCK;
                boolean canOpenBlock = false;
                if(blockHit)
                {
                    canOpenBlock = mc.world.getBlockState(((BlockRayTraceResult)mc.objectMouseOver).getPos()).getBlock() instanceof ContainerBlock;
                }

                if(!mc.player.isHandActive())
                {
                    if(blockHit)
                    {
                        actions.put(Buttons.RIGHT_TRIGGER, new Action(I18n.format("controllable.action.break"), Action.Side.RIGHT));
                    }
                    else
                    {
                        actions.put(Buttons.RIGHT_TRIGGER, new Action(I18n.format("controllable.action.attack"), Action.Side.RIGHT));
                    }
                }

                ItemStack offHandStack = mc.player.getHeldItemOffhand();
                if(offHandStack.getUseAction() != UseAction.NONE)
                {
                    switch(offHandStack.getUseAction())
                    {
                        case EAT:
                            if(mc.player.getFoodStats().needFood())
                            {
                                actions.put(Buttons.LEFT_TRIGGER, new Action(I18n.format("controllable.action.eat"), Action.Side.RIGHT));
                            }
                            break;
                        case DRINK:
                            actions.put(Buttons.LEFT_TRIGGER, new Action(I18n.format("controllable.action.drink"), Action.Side.RIGHT));
                            break;
                        case BLOCK:
                            actions.put(Buttons.LEFT_TRIGGER, new Action(I18n.format("controllable.action.block"), Action.Side.RIGHT));
                            break;
                        case BOW:
                            actions.put(Buttons.LEFT_TRIGGER, new Action(I18n.format("controllable.action.pull_bow"), Action.Side.RIGHT));
                            break;
                        default:
                        	break;
                    }
                }

                ItemStack currentItem = mc.player.inventory.getCurrentItem();
                if(currentItem.getUseAction() != UseAction.NONE)
                {
                    switch(currentItem.getUseAction())
                    {
                        case EAT:
                            if(mc.player.getFoodStats().needFood())
                            {
                                actions.put(Buttons.LEFT_TRIGGER, new Action(I18n.format("controllable.action.eat"), Action.Side.RIGHT));
                            }
                            break;
                        case DRINK:
                            actions.put(Buttons.LEFT_TRIGGER, new Action(I18n.format("controllable.action.drink"), Action.Side.RIGHT));
                            break;
                        case BLOCK:
                            actions.put(Buttons.LEFT_TRIGGER, new Action(I18n.format("controllable.action.block"), Action.Side.RIGHT));
                            break;
                        case BOW:
                            actions.put(Buttons.LEFT_TRIGGER, new Action(I18n.format("controllable.action.pull_bow"), Action.Side.RIGHT));
                            break;
                        default:
                        	break;
                    }
                }
                else if(currentItem.getItem() instanceof BlockItem)
                {
                    if(blockHit)
                    {
                    	BlockItem block = (BlockItem) currentItem.getItem();
                        //if(block.getBlock().canPlaceBlockAt(mc.world, ((BlockRayTraceResult)mc.objectMouseOver).getPos().offset(((BlockRayTraceResult)mc.objectMouseOver).getFace()))) //Is this a good idea?
                        if(Block.func_220055_a(mc.world, ((BlockRayTraceResult)mc.objectMouseOver).getPos(), ((BlockRayTraceResult)mc.objectMouseOver).getFace())); //Replacing with undefined decompiled function
                        {
                            actions.put(Buttons.LEFT_TRIGGER, new Action(I18n.format("controllable.action.place_block"), Action.Side.RIGHT));
                        }
                    }
                }
                else if(!currentItem.isEmpty() && !mc.player.isHandActive())
                {
                    actions.put(Buttons.LEFT_TRIGGER, new Action(I18n.format("controllable.action.use_item"), Action.Side.RIGHT));
                }

                if(!mc.player.isSneaking() && blockHit && canOpenBlock && !mc.player.isHandActive())
                {
                    actions.put(Buttons.LEFT_TRIGGER, new Action(I18n.format("controllable.action.interact"), Action.Side.RIGHT));
                }

                //actions.put(Buttons.A, new Action(I18n.format("controllable.action.jump"), Action.Side.LEFT)); //TODO make a verbose action config option

                actions.put(Buttons.Y, new Action(I18n.format("controllable.action.inventory"), Action.Side.LEFT));

                if(!mc.player.getHeldItemOffhand().isEmpty() || !mc.player.inventory.getCurrentItem().isEmpty())
                {
                    //actions.put(Buttons.X, new Action(I18n.format("controllable.action.swap_hands"), Action.Side.LEFT));  //TODO make a verbose action config option
                }

                if(mc.player.isRidingHorse()) //change from isRiding()... Does this exclude boats then?
                {
                    actions.put(Buttons.LEFT_THUMB_STICK, new Action(I18n.format("controllable.action.dismount"), Action.Side.RIGHT));
                }
                else
                {
                    //actions.put(Buttons.LEFT_THUMB_STICK, new Action(I18n.format("controllable.action.sneak"), Action.Side.RIGHT));  //TODO make a verbose action config option
                }

                if(!mc.player.inventory.getCurrentItem().isEmpty())
                {
                    actions.put(Buttons.DPAD_DOWN, new Action(I18n.format("controllable.action.drop_item"), Action.Side.LEFT));
                }
            }

            MinecraftForge.EVENT_BUS.post(new AvailableActionsEvent(actions));
        }
    }

    @SubscribeEvent
    public void onRenderScreen(TickEvent.RenderTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;

        if(ControllerInput.lastUse <= 0)
            return;

        Minecraft mc = Minecraft.getInstance();
        if(mc.gameSettings.hideGUI)
            return;

        GlStateManager.pushMatrix();
        {
            if(!MinecraftForge.EVENT_BUS.post(new RenderAvailableActionsEvent()))
            {
                //IngameGui guiIngame = mc.ingameGUI;
                
                boolean isChatVisible = false;
                //Have no idea if the below even works
                //Note: Does not work. TODO: Fix this drawnChatLines feature that no longer works due to Minecraft privatizing drawnChatLines
                /*
                try {
	                Field field = ObfuscationReflectionHelper.findField(NewChatGui.class, "drawnChatLines"); //How insanely inefficient is this? Calling a reflection every renderScreen tick?
	                field.setAccessible(true);
	                List<ChatLine> chatgui = (List<ChatLine>) field.get(guiIngame.getChatGUI());
	                
	                isChatVisible = mc.currentScreen == null && chatgui.stream().anyMatch(chatLine -> guiIngame.getTicks() - chatLine.getUpdatedCounter() < 200); //TODO no idea if this will work
                } catch (Exception e) {
                	e.printStackTrace();
                } */
                int leftIndex = 0;
                int rightIndex = 0;
                for(Integer button : actions.keySet())
                {
                    Action action = actions.get(button);
                    Action.Side side = action.getSide();

                    float texU = (button % 19) * 13F;
                    float texV = (button / 19) * 13F;
                    int size = 13;

                    MainWindow mainwindow = mc.mainWindow;
                    int x = side == Action.Side.LEFT ? 5 : mainwindow.getScaledWidth() - 5 - size;
                    int y = mainwindow.getScaledHeight() + (side == Action.Side.LEFT ? leftIndex : rightIndex) * -15 - size - 5;

                    mc.getTextureManager().bindTexture(CONTROLLER_BUTTONS);
                    GlStateManager.color3f(1.0F, 1.0F, 1.0F);
                    GlStateManager.disableLighting();

                    if(isChatVisible && side == Action.Side.LEFT && leftIndex >= 2)
                        continue;

                    /* Draw buttons icon */
                    IngameGui.blit(x, y, texU, texV, size, size, 256, 256);

                    /* Draw description text */
                    if(side == Action.Side.LEFT)
                    {
                        mc.fontRenderer.drawString(action.getDescription(), x + 18, y + 3, Color.WHITE.getRGB());
                        leftIndex++;
                    }
                    else
                    {
                        int width = mc.fontRenderer.getStringWidth(action.getDescription());
                        mc.fontRenderer.drawString(action.getDescription(), x - 5 - width, y + 3, Color.WHITE.getRGB());
                        rightIndex++;
                    }
                }
            }

            if(mc.player != null && mc.currentScreen == null)
            {
                if(!MinecraftForge.EVENT_BUS.post(new RenderPlayerPreviewEvent()))
                {
                    //GuiInventory.drawEntityOnScreen(20, 45, 20, 0, 0, mc.player);
                }
            }
        }
        GlStateManager.popMatrix();
    }
}
