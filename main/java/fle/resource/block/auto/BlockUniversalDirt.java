package fle.resource.block.auto;

import static flapi.util.Values.TEXTURE_FILE;
import static fle.resource.block.auto.ResourceIcons.dirt;
import static fle.resource.block.auto.ResourceIcons.dirtCoverSide;
import static fle.resource.block.auto.ResourceIcons.dirtCoverTop;
import static fle.resource.block.auto.ResourceIcons.dirtStateSide;
import static fle.resource.block.auto.ResourceIcons.dirtStateTop;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.block.BlockHook;
import farcore.substance.Substance;
import farcore.world.BlockPos;
import flapi.FleResource;
import flapi.util.Values;
import fle.core.enums.EnumDirtCover;
import fle.core.enums.EnumDirtState;
import fle.resource.block.item.ItemDirt;
import fle.resource.tile.TileEntityDirt;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockUniversalDirt extends BlockResource<TileEntityDirt>
{	
	public BlockUniversalDirt(String unlocalized)
	{
		super(unlocalized, ItemDirt.class, Material.gourd);
		setTickRandomly(true);
		FarCore.registerTileEntity(TileEntityDirt.class, "fle.dirt",
				Substance.LOADER, Substance.SAVER);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		dirt = new IIcon[FleResource.dirt.size()];
		dirtCoverSide = new IIcon[EnumDirtCover.values().length];
		dirtCoverTop = new IIcon[EnumDirtCover.values().length];
		dirtStateSide = new IIcon[EnumDirtState.values().length];
		dirtStateTop = new IIcon[EnumDirtState.values().length];
		int i;
		for(i = 0; i < dirt.length; ++i)
			dirt[i] = register.registerIcon(
					TEXTURE_FILE + ":dirt/" + FleResource.dirt.get(i).getName());
		for(i = 0; i < dirtStateSide.length; ++i)
			dirtStateSide[i] = register.registerIcon(
					TEXTURE_FILE + ":dirt/state/" + EnumDirtState.values()[i] + "_side");
		for(i = 0; i < dirtStateTop.length; ++i)
			dirtStateTop[i] = register.registerIcon(
					TEXTURE_FILE + ":dirt/state/" + EnumDirtState.values()[i] + "_top");
		for(i = 0; i < dirtCoverSide.length; ++i)
			dirtCoverSide[i] = register.registerIcon(
					TEXTURE_FILE + ":dirt/cover/" + EnumDirtCover.values()[i] + "_side");
		for(i = 0; i < dirtCoverTop.length; ++i)
			dirtCoverTop[i] = register.registerIcon(
					TEXTURE_FILE + ":dirt/cover/" + EnumDirtCover.values()[i] + "_top");
	}
	
	@Override
	public int getRenderColor(ItemStack stack)
	{
		return renderingPass() == 1 ? 
				getState(stack).getDefaultColor() : 0xFFFFFF;
	}
	
	@Override
	public int blockColor(IBlockAccess world, int x, int y, int z)
	{
		return renderingPass() == 1 ?
			getTile(new BlockPos(world, x, y, z)).state.getTopColor(world, x, y, z) :
				0xFFFFFF;
	}
	
	@Override
	protected boolean hasMultipassRender() 
	{
		return true;
	}
	
	@Override
	public int getRenderPasses(IBlockAccess world, int x, int y, int z)
	{
		return 3;
	}
	
	@Override
	public int getRenderPasses(ItemStack stack)
	{
		return 2;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		BlockPos pos = new BlockPos(world, x, y, z);
		switch(renderingPass())
		{
		case 0 : return ResourceIcons.dirt[FleResource.dirt.serial(getDirt(pos))];
		case 1 : return side == 1 ?
				ResourceIcons.dirtStateTop[getTile(pos).state.ordinal()] :
					side != 0 ?
							ResourceIcons.dirtStateSide[getTile(pos).state.ordinal()] :
								Values.EMPTY_BLOCK_ICON;
		case 2 : return side == 1 ?
				ResourceIcons.dirtCoverTop[getTile(pos).cover.ordinal()] :
					side != 0 ?
							ResourceIcons.dirtCoverSide[getTile(pos).cover.ordinal()] :
								Values.EMPTY_BLOCK_ICON;
		default : return Values.EMPTY_BLOCK_ICON;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, ItemStack stack)
	{
		switch(renderingPass())
		{
		case 0 : return ResourceIcons.dirt[FleResource.dirt.serial(getDirt(stack))];
		case 1 : return side == 1 ? 
				ResourceIcons.dirtStateTop[getState(stack).ordinal()] :
					side != 0 ?
							ResourceIcons.dirtStateSide[getState(stack).ordinal()] :
								Values.EMPTY_BLOCK_ICON;
		default : return Values.EMPTY_BLOCK_ICON;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getParticleIcon(int meta)
	{
		return Blocks.dirt.getIcon(2, 0);
	}
	
	@Override
	public int getLightOpacity(IBlockAccess world, int x, int y, int z)
	{
		return isSideSolid(world, x, y, z, ForgeDirection.UP) ? 255 : 100;
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z,
			ForgeDirection side)
	{
		return getTile(new BlockPos(world, x, y, z)).state == EnumDirtState.farmland
				? side != ForgeDirection.UP : true;
	}
	
	@Override
	public TileEntityDirt createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityDirt();
	}
	
	@Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z)
	{
		return getTile(new BlockPos(world, x, y, z)).state != EnumDirtState.farmland;
	}
	
	public Substance getDirt(ItemStack stack)
	{
		return FleResource.dirt.get(getDirtName(stack));
	}
	
	public Substance getDirt(BlockPos pos)
	{
		return getTile(pos).dirt;
	}
		
	public String getDirtName(ItemStack stack)
	{
		return setupNBT(stack).getString("dirt");
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		for (String tag : FleResource.dirt.asNameCollection())
		{
			if ("void".equals(tag))
			{
				continue;
			}
			for(EnumDirtState state : EnumDirtState.values())
				list.add(setDirt(new ItemStack(item), tag, state));
		}
	}
	
	public TileEntityDirt getTile(BlockPos pos)
	{
		return !(pos.tile() instanceof TileEntityDirt)
				? new TileEntityDirt()
				: (TileEntityDirt) pos.tile();
	}
	
	@Override
	public boolean isToolEffective(String type, int meta)
	{
		return "shovel".equals(type);
	}
	
	@Override
	protected void initTile(TileEntityDirt tile, EntityLivingBase placer, ItemStack stack)
	{
		NBTTagCompound nbt = setupNBT(stack).getCompoundTag("nutrition");
		tile.getNBTHandler().loadFromNBT(tile, nbt);
		tile.dirt = getDirt(stack);
		tile.state = getState(stack);
	}
	
	@Override
	protected void getDrops(List<ItemStack> list, EntityPlayer player, int fortune, boolean silkHarvest,
			TileEntityDirt tile)
	{
		if(silkHarvest)
		{
			ItemStack stack = setDirt(new ItemStack(this, 1),
					((TileEntityDirt) tile).dirt.getName(),
					((TileEntityDirt) tile).state);
			setupNBT(stack).setTag("nutrition", 
					((TileEntityDirt) tile).getNBTHandler()
					.saveToNBT(tile, new NBTTagCompound()));
			list.add(stack);
		}
		else
		{
			ItemStack drop = setDirt(new ItemStack(this),
					((TileEntityDirt) tile).dirt.getName(), EnumDirtState.dirt);
			((TileEntityDirt) tile).getNBTHandler().saveToNBT(tile,
					setupNBT(drop).getCompoundTag("nutrition"));
			list.add(drop);
		}
	}
	
	@Override
	public void onFallenUpon(World worldIn, int x, int y, int z, Entity entityIn,
			float fallDistance)
	{
		BlockPos pos = new BlockPos(worldIn, x, y, z);
		if (entityIn instanceof EntityLivingBase
				&& getTile(pos).state == EnumDirtState.farmland)
		{
			if (!worldIn.isRemote
					&& worldIn.rand.nextFloat() < fallDistance - 0.5F)
			{
				if (!(entityIn instanceof EntityPlayer) && !worldIn
						.getGameRules().getGameRuleBooleanValue("mobGriefing"))
				{
					return;
				}
				
				getTile(pos).state = EnumDirtState.dirt;
			}
			
		}
		super.onFallenUpon(worldIn, x, y, z, entityIn, fallDistance);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z,
			Entity entityIn)
	{
		switch (getTile(new BlockPos(worldIn, x, y, z)).cover)
		{
		case snow:
		{
			entityIn.motionX *= 1.05D;
			entityIn.motionZ *= 1.05D;
			break;
		}
		case water:
		{
			entityIn.motionX *= 1.025D;
			entityIn.motionZ *= 1.025D;
			break;
		}
		default:
			break;
		}
	}
	
	@Override
	public void breakBlock(World aWorld, int x, int y, int z, Block aBlock, int aMeta)
	{
		getTile(new BlockPos(aWorld, x, y, z)).cover = EnumDirtCover.nothing;
		super.breakBlock(aWorld, x, y, z, aBlock, aMeta);
	}
	
	@Override
	public void fillWithRain(World worldIn, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(worldIn, x, y, z);
		if (worldIn.rand.nextInt(5) == 0 && 
				getTile(pos).cover == EnumDirtCover.nothing)
		{
			getTile(pos).cover = 					
							worldIn.getBiomeGenForCoords(x, z).func_150559_j()
									? EnumDirtCover.snow
									: EnumDirtCover.water;
		}
		super.fillWithRain(worldIn, x, y, z);
	}
	
	@Override
	public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction,
			IPlantable plantable) 
	{
		BlockPos pos = new BlockPos(world, x, y, z);
		BlockPos offset = pos.offset(direction);
		return plantable.getPlantType(world, offset.x, offset.y, offset.z) == EnumPlantType.Crop
						? getTile(pos).state == EnumDirtState.farmland
						: BlockHook.canSustainPlant(pos,
								getTile(pos).dirt, direction, plantable);
	}
	
	public EnumDirtState getState(ItemStack stack)
	{
		return EnumDirtState
				.valueOf(setupNBT(stack).getString("state"));
	}
	
	public ItemStack setDirt(ItemStack stack, String tag, EnumDirtState state)
	{
		NBTTagCompound nbt = setupNBT(stack);
		nbt.setString("dirt", tag);
		nbt.setString("state", state.name());
		return stack;
	}

	@Override
	public Substance getMainSubstance(BlockPos pos)
	{
		return getDirt(pos);
	}
}