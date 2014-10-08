package com.choonster.cc_speaker;

import com.choonster.cc_speaker.common.block.BlockSpeaker;
import com.choonster.cc_speaker.common.tile.TileEntitySpeaker;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

import java.io.File;

@Mod(modid = Constants.MODID, name = Constants.MODNAME, dependencies = "required-after:ComputerCraft")
public class CC_Speaker {

	@Instance(Constants.MODID)
	public static CC_Speaker instance;

	public static BlockSpeaker blockSpeaker;

	public static CreativeTabs creativeTab;

	private static Configuration configuration;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		File configFile = event.getSuggestedConfigurationFile();
		configuration = new Configuration(configFile);
		configuration.load();
	}

	private int getBlock(String name, int defaultID) {
		return configuration.getBlock(name, defaultID).getInt(defaultID);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		creativeTab = new CreativeTabs("tabCCSpeaker"){
			@Override
			public ItemStack getIconItemStack() {
				return new ItemStack(blockSpeaker);
			}
		};

		GameRegistry.registerTileEntity(TileEntitySpeaker.class, "cc_speaker");

		blockSpeaker = new BlockSpeaker(getBlock("Loudspeaker", 1235));
		GameRegistry.registerBlock(blockSpeaker, blockSpeaker.getUnlocalizedName());

		ComputerCraftAPI.registerPeripheralProvider(blockSpeaker);

		configuration.save();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
}
