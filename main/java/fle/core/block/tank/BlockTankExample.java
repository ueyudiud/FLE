package fle.core.block.tank;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import fle.api.FleAPI;
import fle.api.block.BlockHasTile;
import fle.api.block.IDebugableBlock;
import fle.core.te.tank.TileEntityTankExample;

public class BlockTankExample extends BlockHasTile implements IDebugableBlock
{
	public BlockTankExample(String aName, String aLocalized)
	{
		super(aName, Material.rock);
		GameRegistry.registerTileEntity(TileEntityTankExample.class, "tankExample");
		FleAPI.lm.registerLocal(getUnlocalizedName() + ".name", aLocalized);
	}
	
	@Override
	public void onNeighborBlockChange(World aWorld, int x, int y, int z,
			Block block)
	{
		super.onNeighborBlockChange(aWorld, x, y, z, block);
		TileEntity tile = aWorld.getTileEntity(x, y, z);
		if(tile instanceof TileEntityTankExample)
		{
			((TileEntityTankExample) tile).onNeibourChange(true);
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World aWorld, int aMeta)
	{
		return new TileEntityTankExample();
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			TileEntity tile, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(this));
		return ret;
	}

	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList)
	{
		TileEntity tile = aWorld.getTileEntity(x, y, z);
		if(tile instanceof TileEntityTankExample) aList.addAll(((TileEntityTankExample) tile).getDebugInfo());
	}
}