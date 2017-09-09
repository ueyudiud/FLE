package farcore.lib.crop;

import java.util.ArrayList;

import farcore.lib.bio.FamilyTemplate;
import farcore.lib.material.Mat;
import nebula.base.function.Applicable;
import nebula.common.util.L;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.EnumPlantType;

public class CropTemplate extends Crop
{
	public EnumPlantType type = EnumPlantType.Crop;
	public EnumPlantType wildType = EnumPlantType.Plains;
	private Applicable<ItemStack> drop;
	private int dropChance;
	private Applicable<ItemStack> dropRare;
	private int grain = 1;
	private int seed = 1;
	
	public CropTemplate(Mat material, int maxStage, int growRequire)
	{
		this.material = material;
		this.maxStage = maxStage;
		this.growRequire = growRequire;
	}
	
	public CropTemplate setFamily(FamilyTemplate<Crop, ICropAccess> family)
	{
		this.family = family;
		family.addSpecies(this);
		return this;
	}
	
	public CropTemplate setNativeData(int grain, int growth, int resistance, int vitality, int saving)
	{
		this.nativeCropData = new int[]{grain, growth, resistance, vitality, saving};
		return this;
	}
	
	public CropTemplate setDrop(Applicable<ItemStack> appliable, int grain)
	{
		return setDrop(appliable, grain, 0, null);
	}
	
	public CropTemplate setDrop(Applicable<ItemStack> appliable1, int grain, int chance, Applicable<ItemStack> appliable2)
	{
		this.drop = appliable1;
		this.grain = grain;
		this.dropChance = chance;
		this.dropRare = appliable2;
		return this;
	}
	
	public CropTemplate setSeedMul(int size)
	{
		this.seed = size;
		return this;
	}
	
	public CropTemplate setMultiplicationProp(int stage, int range, int type)
	{
		this.floweringStage = stage;
		this.floweringRange = range;
		this.spreadType = (byte) type;
		return this;
	}
	
	public CropTemplate setPlantType(EnumPlantType type, EnumPlantType wildType)
	{
		this.type = type;
		this.wildType = wildType;
		return this;
	}
	
	@Override
	public EnumPlantType getPlantType(ICropAccess access)
	{
		return access.isWild() ? this.wildType : this.type;
	}
	
	@Override
	public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
	{
		if(access.stage() == this.maxStage)
		{
			ItemStack stack = applyChildSeed(this.seed + L.nextInt(5 + access.info().resistance / 2 + access.info().grain) / 3, access.info());
			if(stack != null)
			{
				list.add(stack);
			}
			stack = this.drop.apply();
			if (stack != null)
			{
				stack.stackSize = stack.stackSize * (2 + access.rng().nextInt(access.info().grain + this.grain)) / 2;
				list.add(stack);
			}
			if (this.dropChance > 0 && access.rng().nextInt(10000) < this.dropChance)
			{
				stack = this.dropRare.apply();
				if (stack != null)
				{
					list.add(stack);
				}
			}
		}
	}
}