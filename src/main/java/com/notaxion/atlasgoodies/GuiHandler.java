package com.notaxion.atlasgoodies;

import hunternif.mc.atlas.client.gui.GuiAtlas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHandler {

    private static final int HELLO_BUTTON_ID = 12345;

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        GuiScreen gui = event.getGui();

        // Check if this is specifically the GuiAtlas class
        if (gui instanceof GuiAtlas) {
            GuiAtlas atlasGui = (GuiAtlas) gui;
            System.out.println("Atlas Goodies: Detected Atlas GUI");

            // Get the atlas GUI dimensions and position
            int atlasX = atlasGui.getGuiX();
            int atlasY = atlasGui.getGuiY();

            // Position our button with the existing right-side buttons
            // The existing buttons are at: atlasX + GuiAtlas.WIDTH + some offset
            int buttonX = atlasX + GuiAtlas.WIDTH + 5; // 5 pixels right of the atlas
            int buttonY = atlasY + 110; // Adjust this to position it after existing buttons

            GuiButton helloButton = new GuiButton(HELLO_BUTTON_ID, buttonX, buttonY, 20, 20, "H");
            event.getButtonList().add(helloButton);

            System.out.println("Atlas Goodies: Added Hello button at " + buttonX + ", " + buttonY);
            System.out.println("Atlas GUI position: " + atlasX + ", " + atlasY);
        }
    }

    @SubscribeEvent
    public void onGuiAction(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.getButton().id == HELLO_BUTTON_ID ) {
            // Send "Hello World" to chat
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player != null) {
                mc.player.sendMessage(new TextComponentString("Hello World from Atlas Goodies!"));
            }
            System.out.println("Atlas Goodies: Hello button clicked!");
        }
    }
}