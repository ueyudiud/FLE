package fle.resource.block.auto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.FarCore;
import farcore.item.enums.EnumItemSize;
import farcore.substance.Substance;
import farcore.world.BlockPos;
import flapi.FleResource;
import flapi.util.Values;
import fle.core.enums.EnumRockSize;
import fle.resource.block.item.ItemStoneChip;
import fle.resource.tile.TileEntityStoneChip;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockUniversalStoneChip extends BlockResource<TileEntityStoneChip>
{	
	public BlockUniversalStoneChip(String unlocalized)
	{
		super(unlocalized, ItemStoneChip.class, Material.circuits);
		setTickRandomly(true);
		FarCore.registerTileEntity(TileEntityStoneChip.class, "stoneChip",
				Substance.LOADER, Substance.SAVER);
	}
	
	@Override
	public int getRenderType()
	{
		return Values.FLE_CUSTOM_RENDER_ID;
	}
	
	@Override
	public IIcon getParticleIcon(int meta)
	{
		return Blocks.cobblestone.getIcon(2, 0);
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, 
			int x, int y, int z)
	{
		EnumRockSize size = getSize(new BlockPos(world, x, y, z));
		return AxisAlignedBB.getBoundingBox(
				(double) x + .5F - size.getWidth() * .5F,
				(double) y,
				(double) z + .5F - size.getWidth() * .5F,
				(double) x + .5F + size.getWidth() * .5F,
				(double) y + size.getHeiht(),
				(double) z + .5F + size.getWidth() * .5F);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, 
			int x, int y, int z)
	{
		EnumRockSize size = getSize(new BlockPos(world, x, y, z));
		return AxisAlignedBB.getBoundingBox(
				(double) x + .5F - size.getWidth() * .5F,
				(double) y,
				(double) z + .5F - size.getWidth() * .5F,
				(double) x + .5F + size.getWidth() * .5F,
				(double) y + size.getHeiht(),
				(double) z + .5F + size.getWidth() * .5F);
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z,
			Random rand)
	{
		if (!world.isRemote && !canPlaceBlockAt(world, x, y, z))
		{
			dropBlockAsItem(world, x, y, z, getDamageValue(world, x, y, z), 0);
			world.setBlockToAir(x, y, z);
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,
			Block block)
	{
		updateTick(world, x, y, z, world.rand);
	}
	
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for (EnumRockSize state : EnumRockSize.values())
		{
			for (String tag : FleResource.rock.asNameCollection())
			{
				if ("void".equals(tag))
					continue;
				list.add(setRock(new ItemStack(item), tag, state));
			}
		}
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
	{
		return worldIn.isSideSolid(x, y - 1, z, ForgeDirection.UP);
	}
	
	@Override
	protected void getDrops(List<ItemStack> list, EntityPlayer player, int fortune, boolean silkHarvest,
			TileEntityStoneChip tile)
	{
		list.add(setRock(new ItemStack(this, 1),
				((TileEntityStoneChip) tile).rock,
				((TileEntityStoneChip) tile).size));
	}
	
	@Override
	protected void initTile(TileEntityStoneChip tile, EntityLivingBase placer, ItemStack stack)
	{
		tile.rock = getRock(stack);
		tile.size = getRockSize(stack);
	}
	
	public TileEntityStoneChip getTile(BlockPos pos)
	{
		return !(pos.tile() instanceof TileEntityStoneChip)
				? new TileEntityStoneChip()
				: (TileEntityStoneChip) pos.tile();
	}
	
	public Substance getRock(BlockPos pos)
	{
		return getTile(pos).rock;
	}
	
	public EnumRockSize getSize(BlockPos pos)
	{
		return getTile(pos).size;
	}
	
	public ItemStack setRock(ItemStack stack, Substance rock, EnumRockSize size)
	{
		return setRock(stack, rock.getName(), size);
	}
	
	public ItemStack setRock(ItemStack stack, String rock, EnumRockSize size)
	{
		NBTTagCompound nbt = setupNBT(stack);
		nbt.setString("rock", rock);
		nbt.setString("size", size.name());
		return stack;
	}
	
	public String getRockName(ItemStack stack)
	{
		return setupNBT(stack).getString("rock");
	}
	
	public Substance getRock(ItemStack stack)
	{
		return FleResource.rock.get(getRockName(stack));
	}
	
	public EnumRockSize getRockSize(ItemStack stack)
	{
		return EnumRockSize
				.valueOf(setupNBT(stack).getString("size"));
	}
	
	@Override
	public TileEntityStoneChip createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityStoneChip();
	}
	
	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		return 0.2F;
	}

	@Override
	public boolean isToolEffective(String type, int meta)
	{
		return true;
	}

	@Override
	public Substance getMainSubstance(BlockPos pos)
	{
		return getRock(pos);
	}
}