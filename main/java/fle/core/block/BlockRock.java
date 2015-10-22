package fle.core.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.block.BlockHasSub;
import fle.api.enums.EnumWorldNBT;
import fle.api.material.MaterialOre;
import fle.api.material.MaterialRock;
import fle.api.util.SubTag;
import fle.api.world.BlockPos;
import fle.core.init.IB;

public class BlockRock extends BlockHasSub
{
	public BlockRock() 
	{
		super(ItemRock.class, "fle.rock", Material.rock);
		setStepSound(soundTypeStone);
		for(MaterialRock m : MaterialRock.getRocks())
		{
			FleAPI.lm.registerLocal(new ItemStack(this, 1, MaterialRock.getRocks().serial(m)).getUnlocalizedName() + ".name", m.getRockName());
		}
	}
	  
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity)
	{
		return (!(entity instanceof EntityDragon)) && (super.canEntityDestroy(world, x, y, z, entity));
	}

	public String getHarvestTool(int aMeta)
	{
		return "pickaxe";
	}
	
	public int getRenderType()
	{
		return FleValue.FLE_RENDER_ID;
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
	
	public IIcon getIcon(IBlockAccess aAccess, int aX, int aY, int aZ, int aSide)
	{
		return getIcon(aSide, FLE.fle.getWorldManager().getData(new BlockPos(aAccess, aX, aY, aZ), EnumWorldNBT.Metadata));
	}
	  
	public IIcon getIcon(int aSide, int aMeta)
	{
		try
		{
			return iconMap.get(MaterialRock.getOreFromID(aMeta).getRockName());
		}
		catch(Throwable e)
		{
			return Blocks.stone.getIcon(aSide, aMeta);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister aIconRegister) 
	{
		blockIcon = aIconRegister.registerIcon(FleValue.TEXTURE_FILE + ":" + FleValue.VOID_ICON_FILE);
		iconMap = new HashMap();
		for(MaterialRock tOre : MaterialRock.getRocks())
		{
			iconMap.put(tOre.getRockName(), aIconRegister.registerIcon(FleValue.TEXTURE_FILE + ":rock/" + tOre.getRockName().toLowerCase()));
		}
	}
	
	@Override
	public int onBlockPlaced(World aWorld, int x, int y, int z, int side,
			float xPos, float yPos, float zPos, int meta)
	{
		return getHarvestLevel((int) Math.ceil(MaterialRock.getOreFromID(meta).getPropertyInfo().getHardness()));
	}
	
	@Override
	public int getHarvestLevel(int metadata) 
	{
		return metadata;
	}
	
	public static int getHarvestLevel(MaterialRock rock)
	{
		return (int) (Math.floor(rock.getPropertyInfo().getHardness()) + 1) / 2;
	}
	  
	public float getBlockHardness(World aWorld, int aX, int aY, int aZ)
	{
		try
		{
			return (int) (2.0F + MaterialOre.getOreFromID(getMetadata(aWorld, aX, aY, aZ)).getPropertyInfo().getHardness()) * 0.7F;
		}
		catch(Throwable e)
		{
			return 1.0F + getHarvestLevel(getMetadata(aWorld, aX, aY, aZ)) * 1.0F;
		}
	}
	  
	public float getExplosionResistance(Entity par1Entity, World aWorld, int aX, int aY, int aZ, double explosionX, double explosionY, double explosionZ)
	{
		return 3.0F + getHarvestLevel(aWorld.getBlockMetadata(aX, aY, aZ)) * 1.0F;
	}
	
	public ArrayList<ItemStack> getDrops(World aWorld, int aX, int aY, int aZ, int aMeta, int aFortune)
	{
		ArrayList<ItemStack> list = new ArrayList();
		if(metaThread.get() != null)
		{
			list.add(new ItemStack(this, 1, metaThread.get()));
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
		for (MaterialRock tMaterial : MaterialRock.getRocks())
	    {
	        aList.add(new ItemStack(aItem, 1, MaterialRock.getOreID(tMaterial)));
	    }
	}
	
	@Override
	public int getDamageValue(World aWorld, int x, int y, int z)
	{
		return getMetadata(aWorld, x, y, z);
	}

	@Override
	public int getMetadata(World world, int x, int y, int z) 
	{
		return FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), EnumWorldNBT.Metadata);
	}

	@Override
	public void setMetadata(World world, int x, int y, int z, int metadata) 
	{
		FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), EnumWorldNBT.Metadata, metadata);
	}

	public static ItemStack a(MaterialRock material)
	{
		return a(material, 1);
	}

	public static ItemStack a(MaterialRock material, int size)
	{
		return new ItemStack(IB.rock, size, MaterialRock.getOreID(material));
	}
}