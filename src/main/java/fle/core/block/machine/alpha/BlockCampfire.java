package fle.core.block.machine.alpha;

import java.util.ArrayList;

import farcore.block.BlockBase;
import farcore.block.BlockHasTile;
import farcore.block.ItemBlockBase;
import fle.api.FleAPI;
import fle.core.tile.TileEntityCampfire;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCampfire extends BlockHasTile
{
	public BlockCampfire()
	{
		super("campfire", ItemBlockBase.class, Material.circuits);
		blockHardness = 0.5F;
		blockResistance = 0.2F;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xPos,
			float yPos, float zPos)
	{
		if(!world.isRemote)
		{
			FleAPI.openGui(0, player, world, x, y, z);
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityCampfire();
	}
}