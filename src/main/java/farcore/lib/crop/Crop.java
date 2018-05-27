/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.crop;

import static nebula.V.CF.GOLD;
import static nebula.V.CF.GREEN;
import static nebula.V.CF.LIGHT_PURPLE;

import java.util.ArrayList;
import java.util.List;

import farcore.data.EnumBlock;
import farcore.items.ItemSeed;
import farcore.lib.bio.BioData;
import farcore.lib.bio.IntegratedSpecie;
import farcore.lib.material.Mat;
import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import nebula.common.LanguageManager;
import nebula.common.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

/**
 * @author ueyudiud
 */
public class Crop extends IntegratedSpecie<ICropFamily<?>> implements ICropSpecie, IPlantable
{
	protected final int		maxStage;
	protected final int		growRequire;
	protected final int		floweringStage;
	protected final int		floweringRange;
	protected final byte	spreadType;
	protected EnumPlantType type = EnumPlantType.Crop;
	
	public Crop(Mat material, String localName, int maxStage, int growRequire, int spreadType, int floweringStage, int floweringRange)
	{
		this.material = material;
		LanguageManager.registerLocal("crop." + getRegisteredName() + ".name", localName);
		this.maxStage = maxStage;
		this.growRequire = growRequire;
		this.spreadType = (byte) spreadType;
		this.floweringRange = floweringRange;
		this.floweringStage = floweringStage;
	}
	
	public Crop setType(EnumPlantType type)
	{
		this.type = type;
		return this;
	}
	
	@Override
	public String getRegisteredName()
	{
		return this.material.getRegisteredName();
	}
	
	@Override
	public Mat material()
	{
		return this.material;
	}
	
	@Override
	public void expressTraits(CropInfo info, BioData data)
	{
		info.grain += data.capabilities[0];
		info.growth += data.capabilities[1];
		info.resistance += data.capabilities[2];
		info.vitality += data.capabilities[3];
		info.saving += data.capabilities[4];
	}
	
	@Override
	public int getMaxStage()
	{
		return this.maxStage;
	}
	
	@Override
	public long tickUpdate(ICropAccess access)
	{
		return 1000L;
	}
	
	@Override
	public void onUpdate(ICropAccess access)
	{
		onUpdate01GrowPlant(access);
		onUpdate02Flowering(access);
	}
	
	protected void onUpdate01GrowPlant(ICropAccess access)
	{
		CropInfo info = access.info();
		IWorldPropProvider property = WorldPropHandler.getWorldProperty(access.world());
		int dence = 0;
		int stage = access.stage();
		int base = 6 + info.growth / 2 + access.rng().nextInt(9 + info.growth);
		int waste = 4 + 3 / (info.saving + 1);
		for (Direction facing : Direction.DIRECTIONS_2D)
		{
			if (access.getTE(facing) instanceof ICropAccess)
			{
				++ dence;
			}
		}
		if (dence - info.resistance > 1)
		{
			base -= dence - info.resistance - 1;
		}
		if (stage != 0)
		{
			float britness = access.world().getLightFor(EnumSkyBlock.SKY, access.pos().up()) * property.getSunshine(access);
			if (britness < 4F)
			{
				base -= (int) (4 - britness);
			}
			else if (britness > 12F)
			{
				base += (int) ((britness - 12F) * 0.4F);
				waste++;
			}
		}
		float rainfall = property.getHumidity(access);
		if (rainfall < 0.5F)
		{
			int a = (int) (10 * (0.5F - rainfall) / (1 + info.saving));
			if (a > 0)
			{
				base -= a;
			}
			waste++;
		}
		if (rainfall > 1.2F)
		{
			base++;
			waste--;
		}
		if (access.stage() == this.maxStage - 1)
		{
			base -= (1 + info.grain) / 2;
		}
		if (base > 0)
		{
			access.grow(base);
		}
		if (waste > 0)
		{
			access.useWater(waste);
		}
	}
	
	protected void onUpdate02Flowering(ICropAccess access)
	{
		int stage = access.stage();
		if (stage == this.floweringStage)
		{
			CropInfo info = access.info();
			int count;
			switch (this.spreadType)
			{
			case 2:
				if (info.seed == null && access.rng().nextInt(5) == 0)
				{
					access.pollinate(true, getGamete(info.data, access.rng()));
				}
			case 1:
				if ((count = info.map.get("flowered")) < 5)
				{
					BioData gamete = getGamete(info.data, access.rng());
					if (gamete != null)
					{
						int l = 8 + (1 + access.info().vitality) / 2;
						for (int i = 0; i < l; i++)
						{
							int x = access.rng().nextInt(this.floweringRange) - access.rng().nextInt(this.floweringRange);
							int y = access.rng().nextInt(this.floweringRange) - access.rng().nextInt(this.floweringRange);
							int z = access.rng().nextInt(this.floweringRange) - access.rng().nextInt(this.floweringRange);
							if ((x | y | z) != 0)
							{
								TileEntity tile = access.getTE(x, y, z);
								if (tile instanceof ICropAccess)
								{
									((ICropAccess) tile).pollinate(false, gamete);
								}
							}
						}
					}
					info.map.put("flowered", ++ count);
				}
				break;
			case 3:
				if (info.seed == null)
				{
					access.pollinate(true, getGamete(info.data, access.rng()));
				}
				break;
			}
		}
	}
	
	@Override
	public int getGrowReq(ICropAccess access)
	{
		return this.growRequire;
	}
	
	@Override
	public void addInformation(ICropAccess access, List<String> list)
	{
		list.add("Growing Progress : " + (access.stage() == this.floweringStage ? LIGHT_PURPLE : GREEN) + (access.stage() < this.maxStage ? (int) (access.stageBuffer() + (access.stage() - 1) * this.growRequire) + "/" + (this.maxStage - 1) * this.growRequire : "Mature"));
		CropInfo info = access.info();
		list.add(GOLD + "G" + GREEN + " " + info.grain);
		list.add(GOLD + "G" + GREEN + " " + info.growth);
		list.add(GOLD + "R" + GREEN + " " + info.resistance);
		list.add(GOLD + "V" + GREEN + " " + info.vitality);
		list.add(GOLD + "S" + GREEN + " " + info.saving);
	}
	
	@Override
	public boolean canPlantAt(ICropAccess access)
	{
		return access.isPlantable(access, Direction.D);
	}
	
	@Override
	public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
	{
		
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return this.type;
	}
	
	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return EnumBlock.crop.block.getDefaultState();
	}
	
	public ItemStack applyChildSeed(int size, CropInfo info)
	{
		if (info.seed == null)
		{
			return null;
		}
		return ItemSeed.applySeed(size, this.material, info.seed);
	}
}
