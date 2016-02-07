package fle.resource.block.auto;

import static flapi.util.Values.TEXTURE_FILE;
import static fle.resource.block.auto.ResourceIcons.mineral;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.block.interfaces.IHarvestCheck;
import farcore.substance.Substance;
import farcore.util.Part;
import farcore.world.BlockPos;
import flapi.FleResource;
import flapi.util.Values;
import fle.core.enums.EnumRockState;
import fle.init.Substances;
import fle.resource.block.item.ItemMineral;
import fle.resource.tile.TileEntityMineral;
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

public class BlockMineral extends BlockResource<TileEntityMineral> 
implements IHarvestCheck
{
	public BlockMineral(String unlocalized)
	{
		super(unlocalized, ItemMineral.class, Material.rock);
		FarCore.registerTileEntity(TileEntityMineral.class, "fle.mineral",
				Substance.LOADER, Substance.SAVER);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		mineral = new IIcon[FleResource.mineral.size()];
		for(int i = 0; i < mineral.length; ++i)
			mineral[i] = register.registerIcon(
					TEXTURE_FILE + ":ore/" + FleResource.mineral.get(i).getName());
	}

	@SideOnly(Side.CLIENT)
	@Override
	protected boolean hasMultipassRender() 
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderPasses(IBlockAccess world, int x, int y, int z)
	{
		return 3;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderPasses(ItemStack stack)
	{
		return 3;
	}

	@SideOnly(Side.CLIENT)
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
		case 2 : return ResourceIcons.mineral
				[FleResource.mineral.serial(getMineral(pos))];
		default : return Values.EMPTY_BLOCK_ICON;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, ItemStack stack)
	{
		switch(renderingPass())
		{
		case 0 : return ResourceIcons.rock
				[FleResource.rock.serial(getRock(stack))];
		case 1 : return ResourceIcons.rockState
				[getRockState(stack).ordinal()];
		case 2 : return ResourceIcons.mineral
				[FleResource.mineral.serial(getMineral(stack))];
		default : return Values.EMPTY_BLOCK_ICON;
		}
	}
	
	@Override
	public IIcon getParticleIcon(int meta)
	{
		return Blocks.coal_ore.getIcon(2, 0);
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(world, x, y, z);
		return Math.max(getMineral(pos).getBlockHardness(), getRock(pos).getBlockHardness());
	}
	
	Set<String> set = ImmutableSet.of("pickaxe");
	
	@Override
	public Set<String> getAccessTools(BlockPos pos)
	{
		return set;
	}
	
	@Override
	public int getToolLevel(BlockPos pos, String tool)
	{
		return Math.max(getRock(pos).blockMineLevel,
				getMineral(pos).blockMineLevel);
	}
	
	@Override
	public boolean isToolEffective(String type, int meta)
	{
		return "hardHammer".equals(type) || "pickaxe".equals(type)
				|| "crusher".equals(type);
	}
	
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for (String tag : FleResource.mineral.asNameCollection())
		{
			if ("void".equals(tag))
				continue;
			list.add(setMineral(new ItemStack(item),
						Substances.stone.getName(), EnumRockState.resource, tag, 
						Part.oreCube.resolution));
		}
	}
	
	@Override
	protected void getDrops(List list, EntityPlayer player, int fortune, 
			boolean silkHarvest, TileEntityMineral tile)
	{
		EnumRockState s = tile.state;
		if (s == EnumRockState.resource && !silkHarvest)
		{
			s = EnumRockState.cobble;
		}
		list.add(setMineral(new ItemStack(this, 1),
				tile.rock, s,
				tile.ore,
				tile.amount));
	}
	
	@Override
	protected void initTile(TileEntityMineral tile, EntityLivingBase placer, ItemStack stack)
	{
		tile.ore = getMineral(stack);
		tile.rock = getRock(stack);
		tile.state = getRockState(stack);
		tile.amount = getMineralAmount(stack);
	}
	
	public TileEntityMineral getTile(BlockPos pos)
	{
		return !(pos.tile() instanceof TileEntityMineral)
				? new TileEntityMineral()
				: (TileEntityMineral) pos.tile();
	}
	
	public Substance getRock(BlockPos pos)
	{
		return getTile(pos).rock;
	}
	
	public Substance getMineral(BlockPos pos)
	{
		return getTile(pos).ore;
	}
	
	public Substance getRock(ItemStack stack)
	{
		return FleResource.rock.get(getRockName(stack));
	}
	
	public String getRockName(ItemStack stack)
	{
		return setupNBT(stack).getString("rock");
	}
	
	public EnumRockState getRockState(ItemStack stack)
	{
		return EnumRockState.valueOf(
				setupNBT(stack).getString("state"));
	}
	
	public Substance getMineral(ItemStack stack)
	{
		return FleResource.mineral.get(getMineralName(stack));
	}
	
	public EnumRockState getRockState(BlockPos pos)
	{
		return getTile(pos).state;
	}
	
	public String getMineralName(ItemStack stack)
	{
		return setupNBT(stack).getString("ore");
	}
	
	public int getMineralAmount(ItemStack stack)
	{
		return setupNBT(stack).getInteger("amount");
	}
	
	public ItemStack setMineral(ItemStack stack, Substance value)
	{
		return setMineral(stack, Substances.stone, EnumRockState.resource,
				value, Part.oreCube.resolution);
	}
	
	public ItemStack setMineral(ItemStack stack, Substance value,
			EnumRockState state, Substance value1, int amount)
	{
		return setMineral(stack, FleResource.rock.name(value), state,
				FleResource.mineral.name(value1), amount);
	}
	
	public ItemStack setMineral(ItemStack stack, String value,
			EnumRockState state, String value1, int amount)
	{
		NBTTagCompound nbt = setupNBT(stack);
		nbt.setString("rock", value);
		nbt.setString("state", state.name());
		nbt.setString("ore", value1);
		nbt.setInteger("amount", amount);
		return stack;
	}
	
	@Override
	public TileEntityMineral createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityMineral();
	}

	@Override
	public Substance getMainSubstance(BlockPos pos)
	{
		return getMineral(pos);
	}
}