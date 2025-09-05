package com.notaxion.atlasgoodies;

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

        // Check if this is the Antique Atlas GUI
        if (gui.getClass().getName().contains("GuiAtlas")) {
            System.out.println("Atlas Goodies: Detected Atlas GUI - " + gui.getClass().getName());

            // Add our custom button
            // Position it at the bottom right of the existing buttons
            int buttonX = gui.width - 30; // 30 pixels from right edge
            int buttonY = 200; // Adjust this based on where you want it

            GuiButton helloButton = new GuiButton(HELLO_BUTTON_ID, buttonX, buttonY, 20, 20, "H");
            event.getButtonList().add(helloButton);

            System.out.println("Atlas Goodies: Added Hello button at " + buttonX + ", " + buttonY);
        }
    }

    @SubscribeEvent
    public void onGuiAction(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.getButton().id == HELLO_BUTTON_ID) {
            // Send "Hello World" to chat
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player != null) {
                mc.player.sendMessage(new TextComponentString("Hello World from Atlas Goodies!"));
            }
            System.out.println("Atlas Goodies: Hello button clicked!");
        }
    }
}