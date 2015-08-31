package fle.core.block.machine;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import fle.api.FleValue;
import fle.api.block.IBlockWithTileBehaviour;
import fle.api.block.IDebugableBlock;
import fle.api.block.IGuiBlock;
import fle.api.world.BlockPos;
import fle.core.block.BlockSubTile;
import fle.core.block.behaviour.BehaviourTerrine;
import fle.core.util.BlockTextureManager;

public class BlockClayInventory1 extends BlockSubTile implements IGuiBlock
{
	public BlockClayInventory1 init()
	{
		registerSub(0, "terrine", "Terrine", new BlockTextureManager("clay/1001"), new BehaviourTerrine());
		return this;
	}
	
	public final void registerSub(int index, String aName, String aLocalized,
			IBlockWithTileBehaviour<BlockSubTile> blockBehavior)
	{
		registerSub(index, aName, aLocalized, null, blockBehavior);
	}
	
	public BlockClayInventory1(String aName)
	{
		super(ItemClayInventory1.class, aName, Material.clay);
		setResistance(1.8F);
		setHardness(1.5F);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		blockIcon = register.registerIcon(FleValue.TEXTURE_FILE + ":iconsets/argil_smelted");
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		return blockIcon;
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return blockIcon;
	}
	
	@Override
	public IIcon getIcon(int aPass, BlockPos aPos, int aSide)
	{
		return blockIcon;
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
		return FleValue.FLE_NOINV_RENDER_ID;
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