package farcore.lib.crop;

import static com.mojang.realmsclient.gui.ChatFormatting.GOLD;
import static com.mojang.realmsclient.gui.ChatFormatting.GREEN;

import java.util.ArrayList;
import java.util.List;

import farcore.data.EnumBlock;
import farcore.lib.crop.dna.CropDNAHelper;
import farcore.lib.material.Mat;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.IPlantable;

public abstract class CropBase implements ICrop
{
	protected CropDNAHelper dnaHelper;
	protected Mat material;
	protected int maxStage;
	protected int growReq = 1000;
	
	public CropBase(Mat material)
	{
		this.material = material;
	}
	
	public CropBase setDNAHelper(CropDNAHelper helper)
	{
		dnaHelper = helper;
		return this;
	}
	
	@Override
	public String getRegisteredName()
	{
		return material.name;
	}
	
	@Override
	public void decodeDNA(ICropAccess biology, String dna)
	{
		CropInfo info = biology.info();
		dnaHelper.decodeDNA(info, dna);
	}
	
	@Override
	public String makeNativeDNA()
	{
		return dnaHelper.nativeDNA;
	}
	
	@Override
	public String makeChildDNA(int generation, String par)
	{
		return dnaHelper.borderDNA(par, harmonic(generation, 2.5E-2, 1.0));
	}
	
	protected float harmonic(int x, double chance, double mul)
	{
		if(x <= 0) return 0F;
		x += 1;
		return 1F / (float) (1D / (Math.log(x) * mul) + 1D / chance);
	}
	
	@Override
	public String makeOffspringDNA(String par1, String par2)
	{
		return dnaHelper.mixedDNA(par1, par2);
	}
	
	@Override
	public String getLocalName(String dna)
	{
		return material.localName;
	}
	
	@Override
	public int getMaxStage()
	{
		return maxStage;
	}
	
	@Override
	public long tickUpdate(ICropAccess access)
	{
		return 800L;
	}
	
	@Override
	public void onUpdate(ICropAccess access)
	{
		access.grow(1);
	}
	
	@Override
	public int getGrowReq(ICropAccess access)
	{
		return growReq;
	}
	
	@Override
	public void addInformation(ICropAccess access, List<String> infos)
	{
		CropInfo info = access.info();
		infos.add(GOLD + "GA" + GREEN + " " + info.grain);
		infos.add(GOLD + "GO" + GREEN + " " + info.growth);
		infos.add(GOLD + "CR" + GREEN + " " + info.coldResistance);
		infos.add(GOLD + "HR" + GREEN + " " + info.hotResistance);
		infos.add(GOLD + "WR" + GREEN + " " + info.weedResistance);
		infos.add(GOLD + "DR" + GREEN + " " + info.dryResistance);
	}
	
	@Override
	public boolean canPlantAt(ICropAccess access)
	{
		IBlockState state;
		return (state = access.getBlock(0, -1, 0)).getBlock()
				.canSustainPlant(state, access.world(), access.pos().down(), EnumFacing.UP, (IPlantable) EnumBlock.crop.block);
	}
	
	@Override
	public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
	{
		
	}
}