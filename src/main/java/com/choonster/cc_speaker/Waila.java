package com.choonster.cc_speaker;

import com.choonster.cc_speaker.common.block.BlockSpeaker;
import com.choonster.cc_speaker.common.tile.TileEntitySpeaker;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class Waila implements IWailaDataProvider {
	public static final Waila INSTANCE = new Waila();

	public static void register(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(INSTANCE, TileEntitySpeaker.class);
		registrar.registerSyncedNBTKey("label", TileEntitySpeaker.class);
	}

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		if (accessor.getTileEntity() instanceof TileEntitySpeaker) {
			String label = accessor.getNBTData().getString("label");
			label = !label.equals("") ? label : StatCollector.translateToLocal("gui.label.none");
			currenttip.add(StatCollector.translateToLocalFormatted("gui.label.desc", label));
		}

		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return currenttip;
	}
}
