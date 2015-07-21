package fla.core.block;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import fla.api.util.SubTag;
import fla.api.world.BlockPos;
import fla.core.FlaBlocks;
import fla.core.item.ItemSub;
import fla.core.tileentity.TileEntityFirewood;

public class BlockFirewood extends BlockBaseHasTile
{
	private IIcon burningSideIcon;
	private IIcon burningTopIcon;
	private IIcon sideIcon;
	
	public BlockFirewood()
	{
		super(Material.wood, SubTag.BLOCK_HARDNESS, SubTag.BLOCK_RESISTANCE);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		blockIcon = register.registerIcon(getTextureName() + "_top");
		sideIcon= register.registerIcon(getTextureName() + "_side");
		burningTopIcon = register.registerIcon(getTextureName() + "_burning_top");
		burningSideIcon= register.registerIcon(getTextureName() + "_burning_side");
	}

	@Override
	public ForgeDirection getBlockDirection(BlockPos pos) 
	{
		return ForgeDirection.UP;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int x)
	{
		return new TileEntityFirewood();
	}

	@Override
	public IIcon getIcon(BlockPos pos, ForgeDirection side) 
	{
		return pos.getBlockMeta() == 1 ? 
				(side == UP || side == DOWN ? burningTopIcon : burningSideIcon) : 
					(side == UP || side == DOWN ? blockIcon : sideIcon);
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
	
	/**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        int l = world.getBlockMetadata(x, y, z);
        if(l == 1)
        {
            float f = (float)x + rand.nextFloat();
            float f1 = (float)y + 1.5F + rand.nextFloat() * 6.0F / 16.0F;
            float f2 = (float)z + rand.nextFloat();

            world.spawnParticle("smoke", (double) f, (double) f1, (double) f2, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("smoke", (double) f, (double) f1 + 0.2D, (double) f2, 0.0D, 0.0D, 0.0D);
        }
    }
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block,
			int meta) 
	{
		TileEntityFirewood tile = (TileEntityFirewood) world.getTileEntity(x, y, z);
		if(tile != null)
		{
			dropBlockAsItem(world, x, y, z, new ItemStack(FlaBlocks.firewood, tile.getCharcoalContain()));
		}
		super.breakBlock(world, x, y, z, block, meta);
	}
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
    		int metadata, int fortune) 
    {
    	return new ArrayList();
    }
}