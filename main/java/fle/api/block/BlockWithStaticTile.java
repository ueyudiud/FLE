package fle.api.block;

import java.util.ArrayList;

import fle.api.te.TEStatic;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockWithStaticTile extends BlockHasTile
{
	protected BlockWithStaticTile(Class<? extends ItemFleBlock> aItemClass,
			String aName, String aLocalized, Material aMaterial)
	{
		super(aItemClass, aName, aLocalized, aMaterial);
	}

	protected BlockWithStaticTile(String aName, String aLocalized,
			Material aMaterial)
	{
		super(aName, aLocalized, aMaterial);
	}

	public abstract TEStatic createNewTileEntity(World aWorld, int aMeta);

	@Override
	protected boolean onBlockActivated(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer, ForgeDirection dir, float xPos, float yPos,
			float zPos)
	{
		if(aWorld.getTileEntity(x, y, z) instanceof TEStatic)
		{
			return ((TEStatic) aWorld.getTileEntity(x, y, z)).onBlockActivated(dir, aPlayer, xPos, yPos, zPos);
		}
		return super.onBlockActivated(aWorld, x, y, z, aPlayer, dir, xPos, yPos, zPos);
	}
	
	@Override
	public void onNeighborBlockChange(World aWorld, int x, int y, int z,
			Block block)
	{
		if(aWorld.getTileEntity(x, y, z) instanceof TEStatic)
		{
			((TEStatic) aWorld.getTileEntity(x, y, z)).markForUpdate();
		}
		super.onNeighborBlockChange(aWorld, x, y, z, block);
	}
	
	@Override
	public void breakBlock(World aWorld, int x, int y, int z, Block aBlock,
			int aMeta)
	{
		if(aWorld.getTileEntity(x, y, z) instanceof TEStatic)
		{
			((TEStatic) aWorld.getTileEntity(x, y, z)).onBlockBreak(aMeta);
		}
		super.breakBlock(aWorld, x, y, z, aBlock, aMeta);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			TileEntity tile, int metadata, int fortune)
	{
		return tile == null ? new ArrayList() : ((TEStatic) tile).getDrop(metadata, fortune);
	}
}
