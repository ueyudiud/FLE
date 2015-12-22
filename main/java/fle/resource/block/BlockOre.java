package fle.resource.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.FleAPI;
import flapi.block.IHarvestHandler;
import flapi.block.interfaces.IDebugableBlock;
import flapi.block.old.BlockHasSub;
import flapi.enums.EnumWorldNBT;
import flapi.event.FluidIconRegisterEvent;
import flapi.material.MaterialOre;
import flapi.util.FleLog;
import flapi.util.FleValue;
import flapi.world.BlockPos;
import fle.FLE;
import fle.core.init.IB;
import fle.core.world.FWM;

public class BlockOre extends BlockHasSub implements IDebugableBlock, IHarvestHandler
{
	public BlockOre()
	{
		super(ItemOre.class, "blockores", Material.rock);
		setStepSound(soundTypeStone);
		for(MaterialOre m : MaterialOre.getOres())
		{
			FleAPI.langManager.registerLocal(new ItemStack(this, 1, MaterialOre.getOres().serial(m)).getUnlocalizedName() + ".name", m.getOreName() + " Ore");
		}
	}
	
	@Override
	public int onBlockPlaced(World aWorld, int x, int y, int z, int side,
			float xPos, float yPos, float zPos, int meta)
	{
		return getHarvestLevel((int) Math.ceil(MaterialOre.getOreFromID(meta).getPropertyInfo().getHardness()));
	}
		
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity)
	{
		return (!(entity instanceof EntityDragon)) && (super.canEntityDestroy(world, x, y, z, entity));
	}

	public String getHarvestTool(int aMeta)
	{
		return "pickaxe";
	}
	  
	public int getHarvestLevel(int aMeta)
	{
		return aMeta;
	}
	
	public static int getHarvestLevel(MaterialOre ore)
	{
		return ore.getPropertyInfo().getHarvestLevel();
	}
	  
	public float getBlockHardness(World world, int x, int y, int z)
	{
		try
		{
			return 1.0F + getOre(world, x, y, z).getPropertyInfo().getHardness();
		}
		catch(Throwable e)
		{
			return 1.0F + getHarvestLevel(getDamageValue(world, x, y, z));
		}
	}
	  
	public float getExplosionResistance(Entity entity, World world, int x, int aY, int aZ, double explosionX, double explosionY, double explosionZ)
	{
		try
		{
			return 1.0F + getOre(world, x, aY, aZ).getPropertyInfo().getDenseness() * 5F;
		}
		catch(Throwable e)
		{
			return 1.0F + getHarvestLevel(getDamageValue(world, x, aY, aZ)) * 1.0F;
		}
	}
	
	@Override
	public int getDamageValue(World world, int x, int y, int z)
	{
		return a().getData(new BlockPos(world, x, y, z), EnumWorldNBT.Metadata);
	}
	  
	protected boolean canSilkHarvest()
	{
		return false;
	}
	
	public String getUnlocalizedName()
	{
		return "blockores";
	}
	
	public int getRenderType()
	{
		return FleValue.FLE_RENDER_ID;
	}
	  
	public boolean canBeReplacedByLeaves(IBlockAccess aWorld, int aX, int aY, int aZ)
	{
		return false;
	}
	  
	public boolean isNormalCube(IBlockAccess aWorld, int aX, int aY, int aZ)
	{
		return true;
	}

	public boolean renderAsNormalBlock()
	{
		return true;
	}
	  
	public boolean isOpaqueCube()
	{
		return true;
	}
	
	private Map<String, IIcon> iconMap;
	
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		try
		{
			return iconMap.get(getOre(world, x, y, z).getOreName());
		}
		catch(Throwable e)
		{
			return blockIcon;
		}
	}
	  
	public IIcon getIcon(int aSide, int aMeta)
	{
		try
		{
			return iconMap.get(MaterialOre.getOreFromID(aMeta).getOreName());
		}
		catch(Throwable e)
		{
			return blockIcon;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister) 
	{
		blockIcon = iconRegister.registerIcon(FleValue.TEXTURE_FILE + ":void");
		FleLog.getLogger().info("Far Land Era start loading fluid icon.");
		MinecraftForge.EVENT_BUS.post(new FluidIconRegisterEvent(iconRegister));
		iconMap = new HashMap();
		for(MaterialOre tOre : MaterialOre.getOres())
		{
			iconMap.put(tOre.getOreName(), iconRegister.registerIcon(FleValue.TEXTURE_FILE + ":ore/" + tOre.getOreName().toLowerCase()));
		}
	}
	
	public ArrayList<ItemStack> getDrops(World aWorld, int aX, int aY, int aZ, int aMeta, int aFortune)
	{
		return new ArrayList<ItemStack>();
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item aItem, CreativeTabs aTab, List aList)
	{
		for (MaterialOre tMaterial : MaterialOre.getOres())
	    {
	        aList.add(new ItemStack(aItem, 1, MaterialOre.getOreID(tMaterial)));
	    }
	}
	
	@Override
	public boolean canHarvestBlock(World world, int x, int y, int z,
			int metadata, String toolKey, int level)
	{
		metaThread.set(getDamageValue(world, x, y, z));
		return "pickaxe".equals(toolKey) && getOre(world, x, y, z).getPropertyInfo().getHarvestLevel() <= level;
	}
	
	@Override
	public ArrayList<ItemStack> getHarvestDrop(World world, int x, int y,
			int z, int metadata, String toolKey, int level)
	{
		ArrayList<ItemStack> list = new ArrayList();
		if(metaThread.get() != null)
		{
			list.add(new ItemStack(IB.oreChip, 2, metaThread.get()));
		}
		else
		{
			list.add(new ItemStack(Blocks.stone));
		}
	    return list;
	}
	
	private static FWM a()
	{
		return FLE.fle.getWorldManager();
	}
	
	public static void setData(BlockPos pos, int meta)
	{
		a().setData(pos, EnumWorldNBT.Metadata, meta);
	}
	
	public static void setData(BlockPos pos, Block baseRock, int baseMeta, int meta)
	{
		a().setData(pos, EnumWorldNBT.Base, GameData.getBlockRegistry().getId(baseRock));
		a().setData(pos, EnumWorldNBT.BaseMeta, baseMeta);
		a().setData(pos, EnumWorldNBT.Metadata, meta);
	}
	
	public static Block getOreBase(IBlockAccess world, int x, int y, int z)
	{
		Block block = GameData.getBlockRegistry().getObjectById(
				a().getData(new BlockPos(world, x, y, z), EnumWorldNBT.Base));
		return block == Blocks.air ? Blocks.stone : block;
	}
	
	public static int getOreBaseMeta(IBlockAccess world, int x, int y, int z)
	{
		return a().getData(new BlockPos(world, x, y, z), EnumWorldNBT.BaseMeta);
	}
	
	public static MaterialOre getOre(IBlockAccess world, int x, int y, int z)
	{
		return MaterialOre.getOreFromID(
				a().getData(new BlockPos(world, x, y, z), EnumWorldNBT.Metadata));
	}

	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList)
	{
		MaterialOre material = getOre(aWorld, x, y, z);
		if(material == null)
			aList.add("This ore is lost NBT! Please report this bug if this world did'n lost chunk NBT before.");

		Block tBlock = getOreBase(aWorld, x, y, z);
		int metadata = getOreBaseMeta(aWorld, x, y, z);
		
		aList.add(new StringBuilder().append("Base Block Name:").append(new ItemStack(tBlock, 1, metadata).getDisplayName()).toString());
		aList.add(new StringBuilder().append("Ore Name:").append(material.getOreName()).toString());
	}
}