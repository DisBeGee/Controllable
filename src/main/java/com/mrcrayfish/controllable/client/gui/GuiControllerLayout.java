package com.mrcrayfish.controllable.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mrcrayfish.controllable.Reference;
import com.mrcrayfish.controllable.client.Buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class GuiControllerLayout extends Screen
{
    protected GuiControllerLayout(ITextComponent titleIn) {
		super(titleIn);
		// TODO Auto-generated constructor stub
	}

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/controller.png");

    private List<ControllerButton> controllerButtons = new ArrayList<>();

    @Override
    public void init()
    {
        controllerButtons.add(new ControllerButton(Buttons.A, 29, 9, 7, 0, 3, 3, 5));
        controllerButtons.add(new ControllerButton(Buttons.B, 32, 6, 13, 0, 3, 3, 5));
        controllerButtons.add(new ControllerButton(Buttons.X, 26, 6, 16, 0, 3, 3, 5));
        controllerButtons.add(new ControllerButton(Buttons.Y, 29, 3, 10, 0, 3, 3, 5));
        controllerButtons.add(new ControllerButton(Buttons.LEFT_BUMPER, 5, -2, 25, 0, 7, 3, 5));
        controllerButtons.add(new ControllerButton(Buttons.RIGHT_BUMPER, 26, -2, 32, 0, 7, 3, 5));
        controllerButtons.add(new ControllerButton(Buttons.LEFT_TRIGGER, 5, -10, 39, 0, 7, 6, 5));
        controllerButtons.add(new ControllerButton(Buttons.RIGHT_TRIGGER, 26, -10, 39, 0, 7, 6, 5));
        controllerButtons.add(new ControllerButton(Buttons.DPAD_DOWN, 6, 9, 19, 0, 3, 3, 5));
        controllerButtons.add(new ControllerButton(Buttons.DPAD_RIGHT, 9, 6, 19, 0, 3, 3, 5));
        controllerButtons.add(new ControllerButton(Buttons.DPAD_LEFT, 3, 6, 19, 0, 3, 3, 5));
        controllerButtons.add(new ControllerButton(Buttons.DPAD_UP, 6, 3, 19, 0, 3, 3, 5));
        controllerButtons.add(new ControllerButton(Buttons.SELECT, 14, 4, 22, 0, 3, 2, 5));
        controllerButtons.add(new ControllerButton(Buttons.START, 21, 4, 22, 0, 3, 2, 5));
        controllerButtons.add(new ControllerButton(Buttons.HOME, 17, 8, 46, 0, 4, 4, 5));
        controllerButtons.add(new ControllerAxis(Buttons.LEFT_THUMB_STICK, 9, 12, 0, 0, 7, 7, 5));
        controllerButtons.add(new ControllerAxis(Buttons.RIGHT_THUMB_STICK, 22, 12, 0, 0, 7, 7, 5));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground();

        GlStateManager.enableBlend();
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE);
        int width = 38 * 5;
        int height = 29 * 5;
        int x = this.width / 2 - width / 2;
        int y = this.height / 2 - 50;
        blit(x, y, 50, 0, 38, 29, width, height, 256, 256);
        GlStateManager.disableBlend();
        controllerButtons.forEach(controllerButton -> controllerButton.draw(x, y, mouseX, mouseY));
    }
}
