package fle.core.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.google.common.collect.Sets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.block.BlockFle;
import fle.api.block.IDebugableBlock;
import fle.api.enums.EnumDamageResource;
import fle.api.enums.EnumWorldNBT;
import fle.api.item.ItemFle;
import fle.api.recipe.ItemOreStack;
import fle.api.world.BlockPos;
import fle.core.init.IB;
import fle.core.recipe.RecipeHelper;
import fle.core.te.TileEntityOilLamp;

public class BlockOilLamp extends BlockFle implements ITileEntityProvider, IDebugableBlock
{	
	private IIcon wickIcon;
	private IIcon fuelIcon;

	public BlockOilLamp(String aName)
	{
		super(ItemOilLamp.class, aName, Material.rock);
		maxStackSize = 1;
		setBlockBounds(0.3125F, 0.0F, 0.3125F, 0.6875F, 0.25F, 0.6875F);
	}
	
	@Override
	public float getBlockHardness(World aWorld, int x,
			int y, int z) 
	{
		Block tBlock = getRockBlock(aWorld, x, y, z);
		return tBlock == null ? Blocks.stone.getBlockHardness(aWorld, x, y, z) : tBlock.getBlockHardness(aWorld, x, y, z);
	}
	
	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x,
			int y, int z, double explosionX, double explosionY,
			double explosionZ) 
	{
		Block tBlock = getRockBlock(world, x, y, z);
		return tBlock == null ? Blocks.stone.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ) : tBlock.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
	}
	
	private Set<Block> sets;
	
	@Override
	public void getSubBlocks(Item aItem, CreativeTabs aTab,
			List aList)
	{
		ItemStack tStackRaw = new ItemStack(aItem, 1, 0);
		ItemStack tStack;
		if(sets == null)
			sets = Sets.newHashSet(Blocks.stone, Blocks.netherrack, Blocks.end_stone, Blocks.bedrock, Blocks.sandstone, Blocks.obsidian);
		for(Block tBlock : sets)
		{
			tStack = tStackRaw.copy();
			ItemOilLamp.setToolMaterial(tStack, tBlock, 0);
			ItemOilLamp.setAmount(tStack, 0);
			aList.add(tStack.copy());
			ItemOilLamp.setAmount(tStack, 3000);
			aList.add(tStack.copy());
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityOilLamp();
	}
	
	@Override
	public boolean hasTileEntity(int metadata) 
	{
		return true;
	}
	
	@Override
	public boolean isOpaqueCube() 
	{
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public int getRenderType()
	{
		return FleValue.FLE_NOINV_RENDER_ID;
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		return world.getTileEntity(x, y, z) instanceof TileEntityOilLamp ? (((TileEntityOilLamp)world.getTileEntity(x, y, z)).isBurning() ? 12 : 0) : 0;
	}
	
	@Override
	public boolean canBlockStay(World aWorld, int x,
			int y, int z)
	{
		return aWorld.getBlock(x, y - 1, z).isSideSolid(aWorld, x, y, z, ForgeDirection.UP);
	}
	
	@Override
	public boolean onBlockActivated(World aWorld, int x, int y, int z, EntityPlayer aPlayer,
			ForgeDirection aSide, float xPos, float yPos, float zPos)
	{
		BlockPos tPos = new BlockPos(aWorld, x, y, z);
		if(tPos.getBlockTile() instanceof TileEntityOilLamp)
		{
			TileEntityOilLamp tile = (TileEntityOilLamp) tPos.getBlockTile();

			if(aPlayer.getCurrentEquippedItem() != null)
			{
				if(new ItemOreStack("wick").isStackEqul(aPlayer.getCurrentEquippedItem()))
				{
					RecipeHelper.onInputItemStack(aPlayer);
					tile.hasWick = true;
				}
				else if(new ItemOreStack("craftingToolFirestarter").isStackEqul(aPlayer.getCurrentEquippedItem()))
				{
					FleAPI.damageItem(aPlayer, aPlayer.getCurrentEquippedItem(), EnumDamageResource.UseTool, 1F);
					tile.setBurning();
				}
				aWorld.markBlockForUpdate(x, y, z);
			}
			else
			{
				tile.setDisburning();
				aWorld.markBlockForUpdate(x, y, z);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World aWorld, int x,
			int y, int z, EntityLivingBase aEntity,
			ItemStack aStack)
	{
		FLE.fle.getWorldManager().setData(new BlockPos(aWorld, x, y, z), EnumWorldNBT.Facing, FleAPI.getIndexFromDirection(getPointFacing(aWorld, x, y, z, aEntity)));
		super.onBlockPlacedBy(aWorld, x, y, z, aEntity, aStack);
	}
	
	@Override
	public void breakBlock(World aWorld, int x, int y, int z, Block aBlock,
			int aMeta)
	{
		BlockPos tPos = new BlockPos(aWorld, x, y, z);
		short[] is = FLE.fle.getWorldManager().removeData(tPos);
		TileEntityOilLamp tile = (TileEntityOilLamp) tPos.getBlockTile();
		Block material = tile.getRock();
		FluidStack amount = getFluidContain(aWorld, x, y, z);
		ItemStack aStack = new ItemStack(this, 1, 0);
		ItemOilLamp.setAmount(aStack, amount == null ? 0 : amount.amount);
		ItemOilLamp.setToolMaterial(aStack, material, tile.getRockMeta());
		dropBlockAsItem(aWorld, x, y, z, aStack);
		super.breakBlock(aWorld, x, y, z, aBlock, aMeta);
	}
	
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
		TileEntityOilLamp tile = (TileEntityOilLamp) world.getTileEntity(x, y, z);
    	if(tile.isBurning())
    	{
    		double f1 = (double)x + 0.49325D;
    		double f2 = (double)y + 0.4D + rand.nextFloat() / 32F;
    		double f3 = (double)z + 0.50625D;
    		world.spawnParticle("flame", f1, f2, f3, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("smoke", f1, f2, f3, 0.0D, 0.2D, 0.0D);
    	}
    }
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			int metadata, int fortune)
	{
		return new ArrayList();
	}
	
	public static Block getRockBlock(World aWorld, int x, int y, int z)
	{
		BlockPos tPos = new BlockPos(aWorld, x, y, z);
		if(tPos.getBlockTile() instanceof TileEntityOilLamp)
		{
			return ((TileEntityOilLamp) tPos.getBlockTile()).getRock();
		}
		return Blocks.stone;
	}
	
	public static FluidStack getFluidContain(World aWorld, int x, int y, int z)
	{
		BlockPos tPos = new BlockPos(aWorld, x, y, z);
		if(tPos.getBlockTile() instanceof TileEntityOilLamp)
		{
			return ((TileEntityOilLamp) tPos.getBlockTile()).fluid.getFluid();
		}
		return null;
	}
	
	public static boolean isBurning(World aWorld, int x, int y, int z)
	{
		BlockPos tPos = new BlockPos(aWorld, x, y, z);
		if(tPos.getBlockTile() instanceof TileEntityOilLamp)
		{
			return ((TileEntityOilLamp) tPos.getBlockTile()).isBurning;
		}
		return false;
	}
	
	public static void setOilLamp(World aWorld, int x, int y, int z, ItemStack aStack, int amount, boolean hasWick, boolean isBurning)
	{
		BlockPos tPos = new BlockPos(aWorld, x, y, z);
		if(tPos.getBlockTile() instanceof TileEntityOilLamp)
		{
			TileEntityOilLamp tile = (TileEntityOilLamp) tPos.getBlockTile();
			tile.setRockBlock(Block.getBlockFromItem(aStack.getItem()), aStack.getItemDamage());
			tile.fluid.setFluid(new FluidStack(IB.animalOil, amount));
			tile.hasWick = hasWick;
			tile.isBurning = isBurning;
		}
		aWorld.markBlockForUpdate(x, y, z);
	}

	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		wickIcon = register.registerIcon(FleValue.TEXTURE_FILE + ":iconsets/wick");
		fuelIcon = register.registerIcon(FleValue.TEXTURE_FILE + ":iconsets/oil");
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return Blocks.stone.getBlockTextureFromSide(side);
	}
	
	public IIcon getWickIcon()
	{
		return wickIcon;
	}

	public IIcon getFuelIcon()
	{
		return fuelIcon;
	}

	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList) 
	{
		TileEntityOilLamp tile = (TileEntityOilLamp) aWorld.getTileEntity(x, y, z);
		if(tile != null)
		{
			FluidTankInfo info = tile.fluid.getInfo();
			if(info.fluid != null)
			{
				aList.add("Lamp fuel: " + info.fluid.getLocalizedName());
				aList.add("Lamp amount: " + FleValue.format_L.format_c(info.fluid.amount));
			}
			else
			{
				aList.add("Lamp amount: " + FleValue.format_L.format_c(0));
			}
			if(tile.getRock() != null)
			{
				aList.add("This lamp is made of " + new ItemStack(tile.getRock(), 1, tile.getRockMeta()).getDisplayName());
			}
		}
	}
}