package fla.core.block;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.UP;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.util.SubTag;
import fla.api.world.BlockPos;
import fla.core.item.ItemSub;
import fla.core.tileentity.TileEntityFirewood;

public class BlockCharcoal extends BlockBaseHasTile
{
	@SideOnly(Side.CLIENT)
	private IIcon sideIcon;
	
	public BlockCharcoal()
	{
		super(Material.wood, SubTag.BLOCK_HARDNESS.copy(1.8F), SubTag.BLOCK_RESISTANCE.copy(1.5F));
	}

	@Override
	public ForgeDirection getBlockDirection(BlockPos pos) 
	{
		return ForgeDirection.UP;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityFirewood(true);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		sideIcon = register.registerIcon(getTextureName() + "_side");
		blockIcon = register.registerIcon(getTextureName() + "_top");
	}

	@Override
	public IIcon getIcon(BlockPos pos, ForgeDirection side) 
	{
		return side == UP || side == DOWN ? blockIcon : sideIcon;
	}

	@Override
	public IIcon getIcon(int meta, ForgeDirection side) 
	{
		return side == UP || side == DOWN ? blockIcon : sideIcon;
	}

	@Override
	public int getRenderType() 
	{
		return 0;
	}
	
	@Override
	public boolean isOpaqueCube() 
	{
		return true;
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
	
	@Override
	public boolean isBurning(IBlockAccess world, int x, int y, int z) 
	{
		TileEntityFirewood tile = (TileEntityFirewood) world.getTileEntity(x, y, z);
		if(tile != null)
		{
			return tile.isBurning();
		}
		return super.isBurning(world, x, y, z);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			int metadata, int fortune) 
	{
		return new ArrayList();
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block,
			int meta) 
	{
		TileEntityFirewood tile = (TileEntityFirewood) world.getTileEntity(x, y, z);
		if(tile != null)
		{
			dropBlockAsItem(world, x, y, z, ItemSub.a("charred_log", tile.getCharcoalContain()));
		}
		super.breakBlock(world, x, y, z, block, meta);
	}
}