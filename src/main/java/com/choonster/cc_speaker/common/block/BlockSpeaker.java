package com.choonster.cc_speaker.common.block;

import com.choonster.cc_speaker.CC_Speaker;
import com.choonster.cc_speaker.common.tile.TileEntitySpeaker;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockSpeaker extends BlockContainer implements IPeripheralProvider {
	private Icon icon;

	public BlockSpeaker(int id){
		super(id, Material.iron);
		setUnlocalizedName("cc_speaker");
		setCreativeTab(CC_Speaker.creativeTab);
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 *
	 * @param world
	 */
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntitySpeaker();
	}

	@Override
	public void registerIcons(IconRegister iconRegister) {
		icon = iconRegister.registerIcon("cc_speaker:cc_speaker");
	}

	@Override
	public Icon getIcon(int side, int meta) {
		return icon;
	}

	/**
	 * Produce an peripheral implementation from a block location.
	 *
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param side
	 * @return a peripheral, or null if there is not a peripheral here you'd like to handle.
	 * @see dan200.computercraft.api.ComputerCraftAPI#registerPeripheralProvider(dan200.computercraft.api.peripheral.IPeripheralProvider)
	 */
	@Override
	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
		TileEntity tileEntity = world.getBlockTileEntity(x,y,z);

		if (tileEntity instanceof TileEntitySpeaker){
			return (TileEntitySpeaker)tileEntity;
		}

		return null;
	}
}
