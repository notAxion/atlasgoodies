package com.notaxion.atlasgoodies;

import hunternif.mc.atlas.client.gui.GuiAtlas;
import hunternif.mc.atlas.marker.DimensionMarkersData;
import hunternif.mc.atlas.marker.Marker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;

@SideOnly(Side.CLIENT)
public class GuiHandler {

    private static final int HELLO_BUTTON_ID = 12345;
    public Field guiAtlasData;

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        GuiScreen gui = event.getGui();

        // Check if this is specifically the GuiAtlas class
        if (gui instanceof GuiAtlas) {
            GuiAtlas atlasGui = (GuiAtlas) gui;
            System.out.println("Atlas Goodies: Detected Atlas GUI");

            DimensionMarkersData atlasMarkerData = null;

            try {
                atlasMarkerData = getMarkersData(atlasGui);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            Collection<Marker> markers = atlasMarkerData.getAllMarkers();

            final HashSet<String> uniqueTypes = new HashSet<>();
            markers.forEach(marker -> {
                if (marker.getId() > 0) {
                    uniqueTypes.add(marker.getType());
                }
            });

            addButtons(event, atlasGui, uniqueTypes);
        }
    }

    private void addButtons(GuiScreenEvent.InitGuiEvent.Post event, GuiAtlas atlasGui, HashSet<String> btnTypes) {
        // Get the atlas GUI dimensions and position
        int atlasX = atlasGui.getGuiX();
        int atlasY = atlasGui.getGuiY();

        // Position our button with the existing left-side buttons
        int buttonX = atlasX - 10;
        int buttonY = atlasY + 14;

        GuiButton helloButton = new GuiButton(HELLO_BUTTON_ID, buttonX, buttonY, 20, 20, "H");
        event.getButtonList().add(helloButton);
//        for (String type: btnTypes) {
            // create GuiBookmarkButton
            // add the button with someway to add the listener
//        }

        System.out.println("Atlas Goodies: Added Hello button at " + buttonX + ", " + buttonY);
        System.out.println("Atlas GUI position: " + atlasX + ", " + atlasY);
    }

    private DimensionMarkersData getMarkersData(GuiAtlas atlasGui) throws NoSuchFieldException, IllegalAccessException {
        if (guiAtlasData == null) {
            guiAtlasData = atlasGui.getClass().getDeclaredField("localMarkersData");
            guiAtlasData.setAccessible(true);
        }

        Object markersData = guiAtlasData.get(atlasGui);

//        if (markersData instanceof DimensionMarkersData)
        return (DimensionMarkersData) markersData;
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