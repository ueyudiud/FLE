package fle.core.block;

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
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.block.BlockHasSub;
import fle.api.block.IDebugableBlock;
import fle.api.material.MaterialOre;
import fle.api.world.BlockPos;

public class BlockOre extends BlockHasSub implements IDebugableBlock
{
	public BlockOre()
	{
		super(ItemOre.class, "blockores", Material.rock);
		setStepSound(soundTypeStone);
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
		return 1 + (int) Math.floor(ore.getPropertyInfo().getHardness());
	}
	  
	public float getBlockHardness(World aWorld, int aX, int aY, int aZ)
	{
		return 1.0F + getHarvestLevel(aWorld.getBlockMetadata(aX, aY, aZ)) * 1.0F;
	}
	  
	public float getExplosionResistance(Entity par1Entity, World aWorld, int aX, int aY, int aZ, double explosionX, double explosionY, double explosionZ)
	{
		return 1.0F + getHarvestLevel(aWorld.getBlockMetadata(aX, aY, aZ)) * 1.0F;
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
	  
	public boolean hasTileEntity(int aMeta)
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
	
	public IIcon getIcon(IBlockAccess aAccess, int aX, int aY, int aZ, int aSide)
	{
		return getIcon(aSide, FLE.fle.getWorldManager().getData(new BlockPos(aAccess, aX, aY, aZ), 0));
	}
	  
	public IIcon getIcon(int aSide, int aMeta)
	{
	    return iconMap.get(MaterialOre.getOreFromID(aMeta).getOreName());
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister aIconRegister) 
	{
		iconMap = new HashMap();
		for(MaterialOre tOre : MaterialOre.getOres())
		{
			iconMap.put(tOre.getOreName(), aIconRegister.registerIcon(FleValue.TEXTURE_FILE + ":ore/" + tOre.getOreName().toLowerCase()));
		}
	}
	
	private ThreadLocal<Integer> thread = new ThreadLocal();
	  
	public int getDamageValue(World aWorld, int aX, int aY, int aZ)
	{
		return FLE.fle.getWorldManager().getData(new BlockPos(aWorld, aX, aY, aZ), 0);
	}
	  
	public void breakBlock(World aWorld, int aX, int aY, int aZ, Block par5, int par6)
	{
		BlockPos tPos = new BlockPos(aWorld, aX, aY, aZ);
		thread.set(FLE.fle.getWorldManager().getData(tPos, 0));
		super.breakBlock(aWorld, aX, aY, aZ, par5, par6);
		FLE.fle.getWorldManager().removeData(tPos);
	}
	
	private boolean drop = false;
	  
	public ArrayList<ItemStack> getDrops(World aWorld, int aX, int aY, int aZ, int aMeta, int aFortune)
	{
		ArrayList<ItemStack> list = new ArrayList();
		if(thread.get() != null)
		{
			list.add(new ItemStack(this, 1, thread.get()));
		}
		else
		{
			list.add(new ItemStack(Blocks.stone));
		}
	    return list;
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
	public int getMetadata(World world, int x, int y, int z) 
	{
		return getDamageValue(world, x, y, z);
	}

	@Override
	public void setMetadata(World world, int x, int y, int z, int metadata) 
	{
		setData(new BlockPos(world, x, y, z), metadata);
	}
	
	public static void setData(BlockPos aPos, int meta)
	{
		setData(aPos, Blocks.stone, 0, meta);;
	}
	
	public static void setData(BlockPos aPos, Block baseRock, int baseMeta, int meta)
	{
		FLE.fle.getWorldManager().setData(aPos, 0, meta);
		FLE.fle.getWorldManager().setData(aPos, 1, GameData.getBlockRegistry().getId(baseRock));
		FLE.fle.getWorldManager().setData(aPos, 2, baseMeta);
	}
	
	public static Block getOreBase(IBlockAccess world, int x, int y, int z)
	{
		int index = FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), 1);
		Block ret = GameData.getBlockRegistry().getObjectById(index);
		return ret == Blocks.air ? Blocks.stone : ret;
	}
	
	public static int getOreBaseMeta(IBlockAccess world, int x, int y, int z)
	{
		return FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), 2);
	}
	
	public static int getOre(IBlockAccess world, int x, int y, int z)
	{
		return FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), 0);
	}

	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList)
	{
		MaterialOre material = MaterialOre.getOreFromID(getOre(aWorld, x, y, z));
		if(material == null)
			aList.add("This ore is lost NBT! Please report this bug if this world did'n lost chunk NBT before.");

		Block tBlock = getOreBase(aWorld, x, y, z);
		int metadata = getOreBaseMeta(aWorld, x, y, z);
		
		aList.add(new StringBuilder().append("Base Block Name:").append(new ItemStack(tBlock, 1, metadata).getDisplayName()).toString());
		aList.add(new StringBuilder().append("Ore Name:").append(material.getOreName()).toString());
	}
}