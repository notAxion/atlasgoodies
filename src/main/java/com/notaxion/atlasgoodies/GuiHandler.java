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
import java.util.*;

@SideOnly(Side.CLIENT)
public class GuiHandler {

    private static final int MARKER_BUTTON_BASE_ID = 20000;
    private static final int CLEAR_FILTER_BUTTON_ID = 30000;

    // Reflection fields
    private Field guiAtlasData;

    // State management
    private Map<String, Collection<Marker>> originalMarkersByType = new HashMap<>();
    private String currentFilterType = null;
    private boolean isFiltered = false;

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        GuiScreen gui = event.getGui();

        if (gui instanceof GuiAtlas) {
            GuiAtlas atlasGui = (GuiAtlas) gui;
            System.out.println("Atlas Goodies: Detected Atlas GUI");

            // Reset filter state when GUI opens
            if (!isFiltered) {
                currentFilterType = null;
                originalMarkersByType.clear();
            }

            DimensionMarkersData atlasMarkerData = null;

            try {
                atlasMarkerData = getMarkersData(atlasGui);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            Collection<Marker> markers = atlasMarkerData.getAllMarkers();

            // Store original markers by type if not already filtered
            if (!isFiltered) {
                storeOriginalMarkers(markers);
            }

            final HashSet<String> uniqueTypes = new HashSet<>();
            markers.forEach(marker -> {
                if (marker.getId() > 0) {
                    uniqueTypes.add(marker.getType());
                }
            });

            addButtons(event, atlasGui, uniqueTypes);
        }
    }

    private void storeOriginalMarkers(Collection<Marker> markers) {
        originalMarkersByType.clear();

        for (Marker marker : markers) {
            if (marker.getId() > 0) {
                String type = marker.getType();
                originalMarkersByType.computeIfAbsent(type, k -> new ArrayList<>()).add(marker);
            }
        }
    }

    private void addButtons(GuiScreenEvent.InitGuiEvent.Post event, GuiAtlas atlasGui, HashSet<String> btnTypes) {
        int atlasX = atlasGui.getGuiX();
        int atlasY = atlasGui.getGuiY();

        // Position buttons on the left side of atlas
        int buttonX = atlasX - 70; // Further left to accommodate text
        int buttonY = atlasY + 20;

        int buttonIndex = 0;

        // Add clear filter button if currently filtered
        if (isFiltered) {
            GuiButton clearButton = new GuiButton(CLEAR_FILTER_BUTTON_ID, buttonX, buttonY + (buttonIndex * 25), 65, 20, "Show All");
            event.getButtonList().add(clearButton);
            buttonIndex++;
        }

        // Add marker type filter buttons
        for (String type : btnTypes) {
            String buttonText = getDisplayName(type);
            String displayText = buttonText.length() > 8 ? buttonText.substring(0, 8) : buttonText;

            GuiButton markerButton = new GuiButton(
                    MARKER_BUTTON_BASE_ID + buttonIndex,
                    buttonX,
                    buttonY + (buttonIndex * 25),
                    65,
                    20,
                    displayText
            );

            event.getButtonList().add(markerButton);
            buttonIndex++;
        }

        System.out.println("Atlas Goodies: Added " + buttonIndex + " marker filter buttons");
    }

    private String getDisplayName(String markerType) {
        return markerType.replaceFirst("AntiqueAtlas:", "");
    }

    private DimensionMarkersData getMarkersData(GuiAtlas atlasGui) throws NoSuchFieldException, IllegalAccessException {
        if (guiAtlasData == null) {
            guiAtlasData = atlasGui.getClass().getDeclaredField("localMarkersData");
            guiAtlasData.setAccessible(true);
        }

        Object markersData = guiAtlasData.get(atlasGui);
        return (DimensionMarkersData) markersData;
    }

    @SubscribeEvent
    public void onGuiAction(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.getButton().id == CLEAR_FILTER_BUTTON_ID) {
//            clearFilter(event.getGui());
        }
        else if (event.getButton().id >= MARKER_BUTTON_BASE_ID && event.getButton().id < MARKER_BUTTON_BASE_ID + 100) {
            handleMarkerFilterClick(event);
        }
    }

    private void handleMarkerFilterClick(GuiScreenEvent.ActionPerformedEvent.Post event) {
        GuiScreen gui = event.getGui();
        if (!(gui instanceof GuiAtlas)) return;

        GuiAtlas atlasGui = (GuiAtlas) gui;

        try {
            // Get the clicked button index
            int buttonIndex = event.getButton().id - MARKER_BUTTON_BASE_ID;
            if (isFiltered) buttonIndex--; // Account for clear button

            // Get the marker type from original stored data
            String[] types = originalMarkersByType.keySet().toArray(new String[0]);
            if (buttonIndex >= 0 && buttonIndex < types.length) {
                String selectedType = types[buttonIndex];
                applyFilter(atlasGui, selectedType);

                Minecraft mc = Minecraft.getMinecraft();
                if (mc.player != null) {
                    mc.player.sendMessage(new TextComponentString("Filtering markers: " + getDisplayName(selectedType)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyFilter(GuiAtlas atlasGui, String filterType) throws Exception {
        DimensionMarkersData markersData = getMarkersData(atlasGui);

//        Field stateData = atlasGui.getClass().getDeclaredField("state");
//        stateData.setAccessible(true);
//
//        GuiStates atlasState = (GuiStates) stateData.get(atlasGui);
//
//        Field iStateData = atlasGui.getClass().getDeclaredField("NORMAL");
//        iStateData = atlasGui.getClass().getDeclaredField("HIDING_MARKERS");
//        iStateData = atlasGui.getClass().getDeclaredField("PLACING_MARKER");
//        iStateData = atlasGui.getClass().getDeclaredField("DELETING_MARKER");
//
//        GuiStates.IState stateNormal = null;
//        GuiStates.IState stateHiding = null;
//        GuiStates.IState statePlacing = null;
//        GuiStates.IState stateDeleting = null;


//        // Clear all current markers
//        clearAllMarkers(markersData);
//
//        // Add back only markers of the selected type
//        Collection<Marker> markersToShow = originalMarkersByType.get(filterType);
//        if (markersToShow != null) {
//            for (Marker marker : markersToShow) {
//                addMarkerToData(markersData, marker);
//            }
//        }
//
//        currentFilterType = filterType;
//        isFiltered = true;
//
//        // Refresh the atlas display
//        refreshAtlasDisplay(atlasGui);
    }

    @SubscribeEvent
    public void onGuiClose(GuiScreenEvent.InitGuiEvent.Pre event) {
        // Reset filter when atlas is closed
//        if (event.getGui() instanceof GuiAtlas && isFiltered) {
//            isFiltered = false;
//            currentFilterType = null;
//            System.out.println("Atlas Goodies: Filter reset on GUI close");
//        }
    }
}