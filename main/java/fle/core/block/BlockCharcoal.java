package fle.core.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.FleAPI;
import flapi.block.old.BlockHasTile;
import fle.core.item.ItemFleSub;
import fle.core.te.TileEntityFirewood;

public class BlockCharcoal extends BlockHasTile
{
	@SideOnly(Side.CLIENT)
	private IIcon sideIcon;

	public BlockCharcoal()
	{
		super("charcoal", Material.wood);
		setHardness(0.8F);
		setResistance(1.2F);
		FleAPI.langManager.registerLocal(new ItemStack(this).getUnlocalizedName() + ".name", "Charcoal");
	}
	
	@Override
	public void breakBlock(World aWorld, int x, int y, int z, Block aBlock,
			int aMeta)
	{
		super.breakBlock(aWorld, x, y, z, aBlock, aMeta);
		if(aWorld.getBlock(x, y + 1, z) == Blocks.fire)
		{
			aWorld.setBlockToAir(x, y + 1, z);
		}
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
	public IIcon getIcon(int side, int meta)
	{
		return side == 0 || side == 1 ? blockIcon : sideIcon;
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
	
	/**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        if(isBurning(world, x, y, z))
        {
            float f = (float)x + rand.nextFloat();
            float f1 = (float)y + 1.5F + rand.nextFloat() * 6.0F / 16.0F;
            float f2 = (float)z + rand.nextFloat();

            world.spawnParticle("smoke", (double) f, (double) f1, (double) f2, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("smoke", (double) f, (double) f1 + 0.2D, (double) f2, 0.0D, 0.0D, 0.0D);
        }
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
		return super.getDrops(world, x, y, z, metadata, fortune);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			TileEntity tileRaw, int metadata, int fortune)
	{
		TileEntityFirewood tile = (TileEntityFirewood) tileRaw;
		ArrayList<ItemStack> list = new ArrayList();
		list.add(ItemFleSub.a("charred_log", tile.getCharcoalContain()));
		return list;
	}
}