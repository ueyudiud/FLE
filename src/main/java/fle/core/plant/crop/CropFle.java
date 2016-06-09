package fle.core.plant.crop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.enums.EnumBlock;
import farcore.interfaces.ISmartPlantableBlock;
import farcore.lib.crop.CropBase;
import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICropAccess;
import farcore.util.SubTag;
import farcore.util.U;
import fle.core.item.ItemCropSeed;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class CropFle extends CropBase implements ISmartPlantableBlock
{
	public EnumPlantType type = EnumPlantType.Crop;
	
	public CropFle(String name)
	{
		super(name);
	}
	
	public CropFle setTextureName(String textureName)
	{
		this.textureName = textureName;
		return this;
	}

	@Override
	public void decodeDNA(ICropAccess biology, String dna)
	{
		CropInfo info = biology.getInfo();
		applyBaseEffect(info);
		info.map.clear();
		String[] list = U.Lang.split(dna, ',');
		for(String string : list)
		{
			if(string == "") continue;
			applyEffect(info, string);
		}
	}
	
	protected void applyBaseEffect(CropInfo info)
	{
		
	}
	
	protected void applyEffect(CropInfo info, String prop)
	{
		
	}
	
	@Override
	public String makeChildDNA(int generation, String par)
	{
		List<String> list = new ArrayList();
		Iterator<String> itr;

		for(String string : U.Lang.split(par, ','))
		{
			if(string == "") continue;
			if(!(list.contains(string) || removeMutation(generation, string)))
				list.add(string);
		}
		addMutation(generation, list);
		String ret = "";
		itr = list.iterator();
		while(itr.hasNext())
		{
			String s = itr.next();
			ret += s;
			if(itr.hasNext())
			{
				ret += ",";
			}
		}
		return ret;
	}
	
	protected void addMutation(int generation, List<String> list)
	{
		
	}
	
	protected boolean removeMutation(int generation, String prop)
	{
		return false;
	}

	public String makeOffspringDNA(String par1, String par2)
	{
		List<String> list = new ArrayList();
		Iterator<String> itr;

		for(String string : U.Lang.split(par1, ','))
		{
			if(string == "") continue;
			if(!list.contains(string))
				list.add(string);
		}
		for(String string : U.Lang.split(par2, ','))
		{
			if(string == "") continue;
			if(!list.contains(string))
				list.add(string);
		}
		String ret = "";
		itr = list.iterator();
		while(itr.hasNext())
		{
			String s = itr.next();
			ret += s;
			if(itr.hasNext())
			{
				ret += ",";
			}
		}
		return ret;
	}
	
	protected boolean harmonicCheck(Random rand, int x, double chance, double mul)
	{
		if(x <= 0) return false;
		x += 1;
		double ch = 1D / (1D / (Math.log(x) * mul) + 1D / chance);
		return rand.nextDouble() <= ch;
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	public String textureName;
	
	@SideOnly(Side.CLIENT)
	public void registerIcon(IIconRegister register)
	{
		icons = new IIcon[getMaxStage()];
		for(int i = 0; i < icons.length; ++i)
		{
			icons[i] = register.registerIcon(textureName + "_stage_" + i);
		}
	}

	@SideOnly(Side.CLIENT)
	public EnumRenderType getRenderType()
	{
		return EnumRenderType.cross;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack)
	{
		return icons[getMaxStage() - 1];
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ICropAccess access)
	{
		return icons[access.getStage()];
	}
	
	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z) 
	{
		return EnumBlock.crop.block();
	}
	
	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
	{
		return 0;
	}
	
	@Override
	public String getSmartPlantType(IBlockAccess world, int x, int y, int z)
	{
		return SubTag.PlantType_Crop.name();
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return type;
	}
	
	@Override
	public boolean useDefaultType()
	{
		return true;
	}
	
	@Override
	public boolean canPlantAt(World world, int x, int y, int z)
	{
		return U.Plants.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
	}
	
	public ItemStack applyChildSeed(int size, CropInfo info)
	{
		return ItemCropSeed.applySeed(size, this, info.generations + 1, makeChildDNA(info.generations, info.DNA));
	}
}