package fle.core.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import fle.api.block.IBlockWithTileBehaviour;
import fle.api.block.IGuiBlock;
import fle.core.block.BlockSubTile;
import fle.core.block.behaviour.BehaviourBoilingHeater;
import fle.core.block.behaviour.BehaviourCeramicBricks;
import fle.core.block.behaviour.BehaviourCeramicFurnaceFirebox;
import fle.core.block.behaviour.BehaviourCeramicFurnaceInlet;
import fle.core.block.behaviour.BehaviourCeramicFurnaceOutlet;
import fle.core.block.behaviour.BehaviourLavaHeatTransfer;
import fle.core.util.BlockTextureManager;

public class BlockStoneMachine extends BlockSubTile implements IGuiBlock
{
	public BlockStoneMachine init()
	{
		registerSub(0, "lavaHeatTransfer", "Lava Heat Transfer", new BlockTextureManager("rock/compactstone"), new BehaviourLavaHeatTransfer());
		registerSub(1, "ceramicBricks", "Ceramic Bricks", new BlockTextureManager("machine/calcinator"), new BehaviourCeramicBricks());
		registerSub(2, "ceramicFurnaceInlet", "Ceramic Furnace Inlet", new BlockTextureManager(new String[]{"machine/calcinator_top", "machine/calcinator"}), new BehaviourCeramicFurnaceInlet());
		registerSub(3, "ceramicFurnaceOutlet", "Ceramic Furnace Outlet", new BlockTextureManager(new String[]{"machine/calcinator_front", "machine/calcinator", "machine/calcinator"}), new BehaviourCeramicFurnaceOutlet());
		registerSub(4, "ceramicFurnaceFirebox", "Ceramic Furnace Firebox", new BlockTextureManager(new String[]{"machine/calcinator_firebox", "iconsets/ceramic", "iconsets/ceramic"}), new BehaviourCeramicFurnaceFirebox());
		registerSub(5, "boiling_heater", "Argil Boiling Heater", new BlockTextureManager(new String[]{"machine/boiling_heater_front", "machine/boiling_heater_top", "machine/boiling_heater_down", "machine/boiling_heater_side"}), new BehaviourBoilingHeater());
		return this;
	}

	public BlockStoneMachine(String aName)
	{
		super(aName, Material.rock);
		setHardness(2.0F);
		setResistance(4.0F);
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		IBlockWithTileBehaviour<BlockSubTile> behaviour = blockBehaviors.get(getDamageValue(aWorld, x, y, z));
		if(behaviour instanceof IGuiBlock)
		{
			return ((IGuiBlock) behaviour).openContainer(aWorld, x, y, z, aPlayer);
		}
		return null;
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		IBlockWithTileBehaviour<BlockSubTile> behaviour = blockBehaviors.get(getDamageValue(aWorld, x, y, z));
		if(behaviour instanceof IGuiBlock)
		{
			return ((IGuiBlock) behaviour).openGui(aWorld, x, y, z, aPlayer);
		}
		return null;
	}
}