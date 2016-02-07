package fle.resource.block.auto;

import java.util.List;

import com.sun.org.apache.bcel.internal.generic.NEW;

import farcore.block.BlockBase;
import farcore.block.item.ItemBlockBase;
import farcore.substance.Substance;
import farcore.tile.TEBase;
import farcore.world.BlockPos;
import flapi.block.BlockFleMultipassRender;
import flapi.block.item.ItemFleMultipassRender;
import flapi.util.Values;
import fle.resource.block.BlockResourceBase;
import fle.resource.tile.TileEntityRock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockResource<T extends TEBase> extends BlockResourceBase
implements ITileEntityProvider
{
	public BlockResource(String unlocalized,
			Class<? extends ItemFleMultipassRender> clazz, Material materialIn)
	{
		super(clazz, unlocalized, materialIn);
	}
	
	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		return getMainSubstance(new BlockPos(world, x, y, z)).getBlockHardness();
	}
	
	@Override
	public float getExplosionResistance(Entity exploder, World world, int x, int y, int z,
			double explosionX, double explosionY, double explosionZ)
	{
		return getBlockHardness(world, x, y, z) * 1.5F;
	}
	
	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, 
			int x, int y, int z, int meta)
	{
		return true;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase placer, ItemStack stack)
	{
		T tile = getTile(new BlockPos(world, x, y, z));
		if (tile != null)
		{
			initTile(tile, placer, stack);
		}
		super.onBlockPlacedBy(world, x, y, z, placer, stack);
	}

	public abstract void getSubBlocks(Item item, CreativeTabs tab, List list);
	
	public abstract boolean isToolEffective(String type, int meta);
	
	public void getDrops(List<ItemStack> list, World world, EntityPlayer player, 
			int x, int y, int z, int meta, int fortune, boolean silkHarvest, TileEntity tile)
	{
		try
		{
			getDrops(list, player, fortune, silkHarvest, (T) tile);
		}
		catch(ClassCastException exception)
		{
			return;
		}
	}
	
	protected abstract void getDrops(List<ItemStack> list, EntityPlayer player, int fortune,
			boolean silkHarvest, T tile);

	public abstract Substance getMainSubstance(BlockPos pos);
	
	public abstract T getTile(BlockPos pos);

	protected abstract void initTile(T tile, EntityLivingBase placer, ItemStack stack);
	
	@Override
	public abstract T createNewTileEntity(World world, int meta);
}