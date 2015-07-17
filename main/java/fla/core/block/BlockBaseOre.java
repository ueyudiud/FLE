package fla.core.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import fla.api.util.FlaValue;
import fla.api.world.BlockPos;

public abstract class BlockBaseOre extends BlockBaseRock
{
	protected IIcon[] icons;
	protected BlockBaseOre()
	{
		super(Material.rock);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHarvestLevel("unknown", 999);
	}

	@Override
	public int getRenderType() 
	{
		return FlaValue.ALL_RENDER_ID;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x,
			int y, int z, EntityLivingBase base,
			ItemStack itemstack) 
	{
		world.setBlockMetadataWithNotify(x, y, z, itemstack.getItemDamage(), 2);
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side,
			float xPos, float yPos, float zPos, int meta) 
	{
		return meta;
	}

	@Override
	protected boolean canRecolour(World world, BlockPos pos,
			ForgeDirection side, int colour) 
	{
		return false;
	}

	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z,
			Explosion explosion) 
	{
		
	}

	public IIcon getBlockTextureFromSide(boolean isRock, IBlockAccess world, int x,
			int y, int z) 
	{
		return isRock ? Blocks.stone.getIcon(world, x, y, z, 2) : this.getIcon(world, x, y, z, 2);
	}

	public IIcon getBlockTextureFromSide(boolean isRock, int meta) 
	{
		return isRock ? Blocks.stone.getIcon(2, meta) : icons[meta];
	}

	@Override
	public IIcon getIcon(BlockPos pos, ForgeDirection side) 
	{
		return getIcon(pos.getBlockMeta(), side);
	}

	@Override
	public IIcon getIcon(int meta, ForgeDirection side) 
	{
		return this.getBlockTextureFromSide(false, meta);
	}
}
