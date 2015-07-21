package fla.core.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.util.SubTag;
import fla.api.world.BlockPos;

public abstract class BlockArgilBase extends BlockBaseHasTile
{
	private String[] itemTextureName = new String[16];
	
	protected BlockArgilBase(Material material) 
	{
		super(material, SubTag.BLOCK_FLE_RENDER, SubTag.BLOCK_HARDNESS, SubTag.BLOCK_RESISTANCE, SubTag.BLOCK_NOT_SOILD);
	}
	
	public BlockArgilBase setItemTextureName(String textureName, int meta)
	{
		itemTextureName[meta] = textureName;
		return this;
	}
	
	public String getItemIconName(int meta) 
	{
		return itemTextureName[meta] == null ? "MISSING_ICON_ITEM_" + getIdFromBlock(this) + "_" + getUnlocalizedName() : itemTextureName[meta];
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
	protected boolean canRecolour(World world, BlockPos pos,
			ForgeDirection side, int colour) 
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
		return true;
	}
	
	public abstract int getMaxDamage();
	
	public abstract void addNBTToTileByItemStack(ItemStack stack);

	public abstract void addNBTToItemStackByTile(ItemStack stack, TileEntity tile, int meta);
	
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
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null)
		{
			ItemStack drop = new ItemStack(block.getItemDropped(meta, world.rand, 0), 1, block.damageDropped(meta));
			addNBTToItemStackByTile(drop, tile, meta);
			dropBlockAsItem(world, x, y, z, drop);
		}
		super.breakBlock(world, x, y, z, block, meta);
	}
}