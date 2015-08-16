package fle.core.block;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.UP;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.block.BlockHasTile;
import fle.api.world.BlockPos;
import fle.core.init.IB;
import fle.core.te.TileEntityFirewood;

public class BlockFirewood extends BlockHasTile
{
	private IIcon burningSideIcon;
	private IIcon burningTopIcon;
	private IIcon sideIcon;
	
	public BlockFirewood()
	{
		super("firewood", Material.wood);
		setHardness(1.0F);
		setResistance(1.0F);
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
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityFirewood(false);
	}
	
	@Override
	public IIcon getIcon(IBlockAccess aWorld, int x, int y, int z, int side)
	{
		return isSmoking(aWorld, x, y, z) ? (side == 0 || side == 1 ? burningTopIcon : burningSideIcon) : (side == 0 || side == 1 ? blockIcon : sideIcon);
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return side == 0 || side == 1 ? blockIcon : sideIcon;
	}

	@Override
	public boolean hasSubs() 
	{
		return false;
	}
	
	public boolean isSmoking(IBlockAccess world, int x, int y, int z) 
	{
		TileEntityFirewood tile = (TileEntityFirewood) world.getTileEntity(x, y, z);
		if(tile != null)
		{
			return tile.isSmoking();
		}
		return super.isBurning(world, x, y, z);
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
        if(isSmoking(world, x, y, z))
        {
            float f = (float)x + rand.nextFloat();
            float f1 = (float)y + 1.5F + rand.nextFloat() * 6.0F / 16.0F;
            float f2 = (float)z + rand.nextFloat();

            world.spawnParticle("smoke", (double) f, (double) f1, (double) f2, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("smoke", (double) f, (double) f1 + 0.2D, (double) f2, 0.0D, 0.0D, 0.0D);
        }
    }

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			TileEntity tileRaw, int metadata, int fortune)
	{
		TileEntityFirewood tile = (TileEntityFirewood) tileRaw;
		ArrayList<ItemStack> list = new ArrayList();
		list.add(new ItemStack(IB.firewood, tile.getCharcoalContain()));
		return list;
	}
}