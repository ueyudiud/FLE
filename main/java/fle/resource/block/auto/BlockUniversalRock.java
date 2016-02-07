package fle.resource.block.auto;

import static flapi.util.Values.TEXTURE_FILE;
import static fle.resource.block.auto.ResourceIcons.rock;
import static fle.resource.block.auto.ResourceIcons.rockState;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import farcore.FarCore;
import farcore.block.interfaces.IHarvestCheck;
import farcore.substance.Substance;
import farcore.world.BlockPos;
import flapi.FleResource;
import flapi.util.Values;
import fle.core.enums.EnumRockState;
import fle.resource.block.item.ItemRock;
import fle.resource.tile.TileEntityRock;
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

public class BlockUniversalRock extends BlockResource<TileEntityRock> implements IHarvestCheck
{
	public BlockUniversalRock(String unlocalized)
	{
		super(unlocalized, ItemRock.class, Material.rock);
		FarCore.registerTileEntity(TileEntityRock.class, "fle.rock",
				Substance.LOADER, Substance.SAVER);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		rock = new IIcon[FleResource.rock.size()];
		rockState = new IIcon[EnumRockState.values().length];
		int i;
		for(i = 0; i < rock.length; ++i)
			rock[i] = register.registerIcon(
					TEXTURE_FILE + ":rock/" + FleResource.rock.get(i).getName());
		for(i = 0; i < rockState.length; ++i)
			rockState[i] = register.registerIcon(
					TEXTURE_FILE + ":rock/override/" + EnumRockState.values()[i]);
	}
	
	@Override
	protected boolean hasMultipassRender() 
	{
		return true;
	}
	
	@Override
	public int getRenderPasses(IBlockAccess world, int x, int y, int z)
	{
		return 2;
	}
	
	@Override
	public int getRenderPasses(ItemStack stack)
	{
		return 2;
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		BlockPos pos = new BlockPos(world, x, y, z);
		switch(renderingPass())
		{
		case 0 : return ResourceIcons.rock
				[FleResource.rock.serial(getRock(pos))];
		case 1 : return ResourceIcons.rockState
				[getRockState(pos).ordinal()];
		default : return Values.EMPTY_BLOCK_ICON;
		}
	}
	
	@Override
	public IIcon getIcon(int side, ItemStack stack)
	{
		switch(renderingPass())
		{
		case 0 : return ResourceIcons.rock
				[FleResource.rock.serial(getRock(stack))];
		case 1 : return ResourceIcons.rockState
				[getRockState(stack).ordinal()];
		default : return Values.EMPTY_BLOCK_ICON;
		}
	}
	
	@Override
	public IIcon getParticleIcon(int meta)
	{
		return Blocks.stone.getIcon(2, 0);
	}
	
	@Override
	public boolean isToolEffective(String type, int meta)
	{
		return "hardHammer".equals(type) || "pickaxe".equals(type)
				|| "crusher".equals(type);
	}
	
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for (String tag : FleResource.rock.asNameCollection())
		{
			if ("void".equals(tag))
				continue;
			list.add(setRock(new ItemStack(item), tag, EnumRockState.resource));
		}
	}
	
	private final Set<String> set = ImmutableSet.of("pickaxe", "hardHammer", "crusher");
	
	@Override
	public Set<String> getAccessTools(BlockPos pos)
	{
		return set;
	}
	
	@Override
	public int getToolLevel(BlockPos pos, String tool)
	{
		return getRock(pos).blockMineLevel;
	}
	
	public TileEntityRock getTile(BlockPos pos)
	{
		return !(pos.tile() instanceof TileEntityRock)
				? new TileEntityRock()
				: (TileEntityRock) pos.tile();
	}
	
	public ItemStack setRock(ItemStack stack, Substance value)
	{
		return setRock(stack, value, EnumRockState.resource);
	}
	
	public ItemStack setRock(ItemStack stack, Substance value,
			EnumRockState state)
	{
		return setRock(stack, FleResource.rock.name(value), state);
	}
	
	public ItemStack setRock(ItemStack stack, String value, EnumRockState state)
	{
		NBTTagCompound nbt = setupNBT(stack);
		nbt.setString("rock", value);
		nbt.setString("state", state.name());
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
	
	public Substance getRock(BlockPos pos)
	{
		return getTile(pos).rock;
	}
	
	public EnumRockState getRockState(BlockPos pos)
	{
		return getTile(pos).state;
	}
	
	public EnumRockState getRockState(ItemStack stack)
	{
		return EnumRockState
				.valueOf(setupNBT(stack).getString("state"));
	}
	
	@Override
	public TileEntityRock createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityRock();
	}

	@Override
	protected void getDrops(List<ItemStack> list, EntityPlayer player, int fortune, 
			boolean silkHarvest, TileEntityRock tile)
	{
		EnumRockState s = ((TileEntityRock) tile).state;
		if (s == EnumRockState.resource && !silkHarvest)
		{
			s = EnumRockState.cobble;
		}
		list.add(setRock(new ItemStack(this, 1), ((TileEntityRock) tile).rock, s));
	}

	@Override
	public Substance getMainSubstance(BlockPos pos)
	{
		return getRock(pos);
	}

	@Override
	protected void initTile(TileEntityRock tile, EntityLivingBase placer, ItemStack stack)
	{
		tile.rock = getRock(stack);
		tile.state = getRockState(stack);	
	}
}