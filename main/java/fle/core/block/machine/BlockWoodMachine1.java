package fle.core.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import fle.api.FleValue;
import fle.api.block.IBlockWithTileBehaviour;
import fle.api.block.IGuiBlock;
import fle.core.block.BlockSubTile;
import fle.core.block.behaviour.BehaviourDrying;
import fle.core.block.behaviour.BehaviourFrame;
import fle.core.block.behaviour.BehaviourPolish;
import fle.core.block.behaviour.BehaviourStoneMill;
import fle.core.util.BlockTextureManager;

public class BlockWoodMachine1 extends BlockSubTile implements IGuiBlock
{
	public BlockWoodMachine1 init()
	{
		registerSub(0, "dryingTable", "Drying Table", new BlockTextureManager(new String[]{"machine/drying_table"}), new BehaviourDrying());
		registerSub(1, "woodenFrame", "Wood Frame", new BlockTextureManager(FleValue.VOID_ICON_FILE), new BehaviourFrame());
		registerSub(2, "stoneMill", "Stone Mill", new BlockTextureManager(FleValue.VOID_ICON_FILE), new BehaviourStoneMill());
		return this;
	}
	
	public BlockWoodMachine1(String aName)
	{
		super(aName, Material.wood);
		setResistance(1.0F);
		setHardness(1.0F);
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
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public int getRenderType()
	{
		return FleValue.FLE_RENDER_ID;
	}
}