package farcore.lib.crop;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.util.LanguageManager;
import farcore.util.U;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

public abstract class CropBase implements CropCard
{
	protected static final Random random = new Random();
	
	public int minGrowWaterReq;
	public int minAliveWaterReq;
	public int waterWaste;
	public int maxGrowWaterUse;
	public int maxTemp;
	public int minGrowTemp;
	public int minAliveTemp;
	public final String name;
	public int maxStage;
	public int growReq;
	public long tickUpdate = 1000L;
	
	public CropBase(String name)
	{
		this.name = name;
	}
	
	public CropBase setMaxStage(int maxStage)
	{
		this.maxStage = maxStage;
		return this;
	}
	
	public CropBase setGrowReq(int growReq)
	{
		this.growReq = growReq;
		return this;
	}
	
	public CropBase setTickUpdate(long tickUpdate)
	{
		this.tickUpdate = tickUpdate;
		return this;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerLocalizedName(LanguageManager manager)
	{
		manager.registerLocal("crop." + name() + ".name", U.Lang.upcaseFirst(name()));
	}
	
	public abstract void decodeDNA(ICropAccess biology, String dna);

	public String makeNativeDNA()
	{
		return instanceDNA(random);
	}

	public abstract String makeOffspringDNA(String par1, String par2);

	public final String name()
	{
		return name;
	}

	public abstract String instanceDNA(Random random);

	public int getMaxStage()
	{
		return maxStage;
	}

	public int getGrowReq(ICropAccess access)
	{
		return growReq;
	}

	@Override
	public long tickUpdate(ICropAccess access)
	{
		return tickUpdate;
	}

	public abstract void getDrops(ICropAccess access, List<ItemStack> list);

	public void onUpdate(ICropAccess access)
	{
		CropInfo info = access.getInfo();
		float temp = access.getTemp();
		int maxT = (maxTemp * (100 + info.hotResistance)) / 100;
		int minT = (minAliveTemp * (100 - info.coldResistance)) / 100;
		if(temp > maxT || temp < minT)
		{
			access.killCrop();
		}
		else
		{
			int minT2 = (minGrowTemp * (100 - info.coldResistance)) / 100;
			if(access.isWild())
			{
				if(random.nextBoolean())
				{
					access.grow(2 + random.nextInt(2));
				}
				else if(random.nextInt(928 + info.weedResistance) == 0)
				{
					access.killCrop();
				}
				return;
			}
			int minW = minGrowWaterReq * (100 - info.dryResistance) / 100;
			int water = access.getWaterLevel();
			if(water < minW)
			{
				access.absorbWater(3, 2, minGrowWaterReq - water + 50, false);
			}
			else if(temp >= minT2)
			{
				int u = maxGrowWaterUse;
				if(info.dryResistance > 2)
				{
					u -= random.nextInt(info.dryResistance / 2);
				}
				int arg = access.useWater((int) (u - access.getRainfall()));
				
				access.grow(6 + u / 2 + random.nextInt(4 + info.growth + u / 2));
			}
			minW = minAliveWaterReq * (100 - info.dryResistance) / 100;
			if(water < minW)
			{
				access.killCrop();
			}
			int u = waterWaste;
			if(info.dryResistance > 2)
			{
				u -= random.nextInt(info.dryResistance / 2);
			}
			access.useWater(u);
		}
	}

	@Override
	public void addInformation(ICropAccess access, List<String> infos)
	{
		CropInfo info = access.getInfo();
		infos.add(EnumChatFormatting.GOLD + "GA" + EnumChatFormatting.GREEN + " " + info.grain);
		infos.add(EnumChatFormatting.GOLD + "GO" + EnumChatFormatting.GREEN + " " + info.growth);
		infos.add(EnumChatFormatting.GOLD + "CR" + EnumChatFormatting.GREEN + " " + info.coldResistance);
		infos.add(EnumChatFormatting.GOLD + "HR" + EnumChatFormatting.GREEN + " " + info.hotResistance);
		infos.add(EnumChatFormatting.GOLD + "WR" + EnumChatFormatting.GREEN + " " + info.weedResistance);
		infos.add(EnumChatFormatting.GOLD + "DR" + EnumChatFormatting.GREEN + " " + info.dryResistance);
	}
}