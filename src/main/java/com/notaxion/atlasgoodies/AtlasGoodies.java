package com.notaxion.atlasgoodies;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = AtlasGoodies.MODID, name = AtlasGoodies.NAME, version = AtlasGoodies.VERSION, dependencies = "required-after:antiqueatlas")
public class AtlasGoodies {
    public static final String MODID = "atlasgoodies";
    public static final String NAME = "Atlas Goodies";
    public static final String VERSION = "1.0.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("Atlas Goodies: Pre-initialization");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Atlas Goodies: Initialization");

        // Register GUI event handler only on client side
        if (event.getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new GuiHandler());
            System.out.println("Atlas Goodies: Registered GUI handler");
        }
    }
}