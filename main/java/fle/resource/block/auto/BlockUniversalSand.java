package fle.resource.block.auto;

import static flapi.util.Values.TEXTURE_FILE;
import static fle.resource.block.auto.ResourceIcons.sand;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.FarCore;
import farcore.block.BlockFactory;
import farcore.block.BlockHook;
import farcore.block.interfaces.IFallable;
import farcore.entity.EntityFleFallingBlock;
import farcore.nbt.ToolInfo.ToolType;
import farcore.substance.Substance;
import farcore.world.BlockPos;
import flapi.FleResource;
import flapi.util.Values;
import fle.resource.block.item.ItemSand;
import fle.resource.tile.TileEntitySand;
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
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockUniversalSand extends BlockResource<TileEntitySand>
		implements IFallable
{
	public BlockUniversalSand(String unlocalized)
	{
		super(unlocalized, ItemSand.class, Material.sand);
		FarCore.registerTileEntity(TileEntitySand.class, "fle.sand",
				Substance.LOADER, Substance.SAVER);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		sand = new IIcon[FleResource.sand.size()];
		for(int i = 0; i < sand.length; ++i)
			sand[i] = register.registerIcon(
					TEXTURE_FILE + ":sand/" + FleResource.sand.get(i).getName());
	}
	
	@Override
	protected boolean hasMultipassRender()
	{
		return true;
	}
	
	@Override
	public IIcon getFallingIcon(EntityFleFallingBlock entity, int side)
	{
		try
		{
			return ResourceIcons.sand[FleResource.sand
					.serial(entity.tileEntityData
							.getCompoundTag("tile").getString("sand"))];
		}
		catch(Exception exception)
		{
			return Values.EMPTY_BLOCK_ICON;
		}
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		return ResourceIcons.sand
				[FleResource.sand.serial(getSand(new BlockPos(world, x, y, z)))];
	}
	
	@Override
	public IIcon getIcon(int side, ItemStack stack)
	{
		return ResourceIcons.sand
				[FleResource.sand.serial(getSand(stack))];
	}
	
	@Override
	public IIcon getParticleIcon(int meta)
	{
		return Blocks.sand.getIcon(2, 0);
	}
	
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for (String tag : FleResource.sand.asNameCollection())
		{
			if ("void".equals(tag))
				continue;
			list.add(setSand(new ItemStack(item), tag));
		}
	}
	
	public void onBlockAdded(World worldIn, int x, int y, int z)
	{
		worldIn.scheduleBlockUpdate(x, y, z, this, tickRate(worldIn));
	}
	
	/**
	 * Called when a neighboring block changes.
	 */
	public void onNeighborBlockChange(World worldIn, int x, int y, int z, 
			Block neighborBlock)
	{
		worldIn.scheduleBlockUpdate(x, y, z, this, tickRate(worldIn));
	}
	
	public void updateTick(World worldIn, int x, int y, int z,
			Random rand)
	{
		if (!worldIn.isRemote)
		{
			checkFallable(worldIn, x, y, z);
		}
	}
	
	private void checkFallable(World worldIn, int x, int y, int z)
	{
		if (canFallInto(worldIn, x, y - 1, z) && y >= 0)
		{
			BlockFactory.makeFallingBlock(worldIn, new BlockPos(worldIn, x, y, z), this);
		}
	}
	
	/**
	 * How many world ticks before ticking
	 */
	public int tickRate(World worldIn)
	{
		return 2;
	}
	
	public boolean canFallInto(World world, int x, int y, int z)
	{
		if (world.isAirBlock(x, y, z))
			return true;
		Block block = world.getBlock(x, y, z);
		Material material = block.getMaterial();
		return block == Blocks.fire || material == Material.air
				|| material == Material.water || material == Material.lava;
	}
	
	@Override
	public TileEntitySand createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntitySand();
	}
	
	@Override
	public void onStartFalling(EntityFleFallingBlock entity)
	{
	
	}
	
	@Override
	public void onEndFalling(BlockPos pos)
	{
	
	}
	
	@Override
	public void onHitEntity(EntityFleFallingBlock entity)
	{
	
	}
	
	@Override
	protected void getDrops(List<ItemStack> list, EntityPlayer player, int fortune, boolean silkHarvest,
			TileEntitySand tile)
	{
		list.add(setSand(new ItemStack(this), tile.sand.getName()));
	}
	
	@Override
	protected void initTile(TileEntitySand tile, EntityLivingBase placer, ItemStack stack)
	{
		tile.sand = getSand(stack);	
	}
	
	@Override
	public List<ItemStack> onBlockDropAsItem(EntityFleFallingBlock entity)
	{
		ArrayList<ItemStack> list = new ArrayList();
		list.add(setSand(new ItemStack(this), getSandName(entity.tileEntityData)));
		return list;
	}

	@Override
	public boolean isToolEffective(String type, int meta)
	{
		return ToolType.shovel.name.equals(type);
	}
	
	@Override
	public boolean canSustainPlant(IBlockAccess world, int x, int y, int z,
			ForgeDirection direction, IPlantable plantable)
	{
		BlockPos pos = new BlockPos(world, x, y, z);
		return BlockHook.canSustainPlant(pos,
				getSand(pos), direction, plantable);
	}
	
	public TileEntitySand getTile(BlockPos pos)
	{
		return !(pos.tile() instanceof TileEntitySand)
				? new TileEntitySand()
				: (TileEntitySand) pos.tile();
	}
	
	public Substance getSand(BlockPos pos)
	{
		return getTile(pos).sand;
	}
		
	public Substance getSand(ItemStack stack)
	{
		return FleResource.sand.get(getSandName(stack));
	}
	
	public String getSandName(ItemStack stack)
	{
		return setupNBT(stack).getString("sand");
	}
	
	private String getSandName(NBTTagCompound tileData)
	{
		return tileData.getCompoundTag("tile").getString("sand");
	}
	
	public ItemStack setSand(ItemStack stack, String value)
	{
		setupNBT(stack).setString("sand", value);
		return stack;
	}

	@Override
	public boolean canFallInto(BlockPos pos)
	{
		return canFallInto(pos.world(), pos.x, pos.y, pos.z);
	}

	@Override
	public Substance getMainSubstance(BlockPos pos)
	{
		return getSand(pos);
	}
}