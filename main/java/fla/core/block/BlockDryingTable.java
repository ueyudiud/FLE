package fla.core.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.util.FlaValue;
import fla.api.world.BlockPos;
import fla.core.Fla;
import fla.core.tileentity.TileEntityDryingTable;
import fla.core.tileentity.TileEntityPolishTable;

public class BlockDryingTable extends BlockBaseHasTile
{
	public BlockDryingTable()
	{
		super(Material.wood);
		setCreativeTab(CreativeTabs.tabDecorations);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) 
	{
		return super.canPlaceBlockAt(world, x, y, z) && world.getBlock(x, y - 1, z).isSideSolid(world, x, y, z, ForgeDirection.UP);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x,
			int y, int z, EntityPlayer player,
			int side, float xPos, float yPos,
			float zPos)
	{
		if(world.isRemote)
		{
			return true;
		}
		else
		{
			TileEntityDryingTable tile = (TileEntityDryingTable) world.getTileEntity(x, y, z);
			if(tile != null)
			{
				player.openGui(Fla.MODID, 2, world, x, y, z);
			}
			return true;
		}
	}

	@Override
	public ForgeDirection getBlockDirection(BlockPos pos) 
	{
		return ForgeDirection.UNKNOWN;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityDryingTable();
	}

	@Override
	public IIcon getIcon(BlockPos pos, ForgeDirection side) 
	{
		return blockIcon;
	}

	@Override
	public IIcon getIcon(int meta, ForgeDirection side) 
	{
		return blockIcon;
	}

	@Override
	public int getRenderType() 
	{
		return FlaValue.ALL_RENDER_ID;
	}

	@Override
	public boolean isNormalCube() 
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() 
	{
		return false;
	}

	@Override
	public boolean hasSubs() 
	{
		return false;
	}

	@Override
	protected boolean canRecolour(World world, BlockPos pos,
			ForgeDirection side, int colour) 
	{
		return false;
	}
}