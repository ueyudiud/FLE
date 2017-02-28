/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.crop;

import static com.mojang.realmsclient.gui.ChatFormatting.GOLD;
import static com.mojang.realmsclient.gui.ChatFormatting.GREEN;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.mojang.realmsclient.gui.ChatFormatting;

import farcore.data.EnumBlock;
import farcore.lib.bio.FamilyTemplate;
import farcore.lib.bio.GeneticMaterial;
import farcore.lib.material.Mat;
import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import nebula.common.util.A;
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
public class Crop implements ICrop, IPlantable
{
	protected FamilyTemplate<Crop, ICropAccess> family;
	protected Mat material;
	protected long[] nativeCropValue = new long[2];
	protected int[] nativeCropData = new int[5];
	protected int maxStage;
	protected int floweringStage = -1;
	protected int floweringRange = 0;
	protected byte spreadType = 0;
	protected int growRequire = 1000;
	
	@Override
	public final String getRegisteredName()
	{
		return this.material.getRegisteredName();
	}
	
	@Override
	public final FamilyTemplate<Crop, ICropAccess> getFamily()
	{
		return this.family;
	}
	
	@Override
	public GeneticMaterial createNativeGeneticMaterial()
	{
		return new GeneticMaterial(this.family.getRegisteredName(), 0, this.nativeCropValue.clone(), this.nativeCropData.clone());
	}
	
	@Override
	@Nullable
	public GeneticMaterial createGameteGeneticMaterial(ICropAccess biology, GeneticMaterial gm)
	{
		if ((gm.coders.length & 0x1) != 0) return null;
		Random random = biology.rng();
		long[] coders = A.createLongArray(gm.coders.length >> 1, idx-> {
			long a = gm.coders[idx << 1];
			long b = gm.coders[idx << 1 | 1];
			long result = 0;
			for (int i = 0; i < Long.SIZE; ++i)
			{
				long x = 1L << i;
				if (getGameteResult(idx << 6 | i, random, (a & x) != 0, (b & x) != 0))
				{
					result |= x;
				}
			}
			if (random.nextInt(10000) < 10)
			{
				result ^= (1L << random.nextInt(64));
			}
			return result;
		});
		int[] datas = gm.nativeValues.clone();
		return new GeneticMaterial(this.family.getRegisteredName(), gm.generation + 1, coders, datas);
	}
	
	protected boolean getGameteResult(int idx, Random random, boolean a, boolean b)
	{
		return random.nextBoolean() ? a : b;
	}
	
	@Override
	public void expressTrait(ICropAccess biology, GeneticMaterial gm)
	{
		CropInfo info = biology.info();
		info.grain		+= gm.nativeValues[0];
		info.growth		+= gm.nativeValues[1];
		info.resistance	+= gm.nativeValues[2];
		info.vitality	+= gm.nativeValues[3];
		info.saving		+= gm.nativeValues[4];
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
		for(Direction facing : Direction.DIRECTIONS_2D)
		{
			if(access.getTE(facing) instanceof ICropAccess)
			{
				++dence;
			}
		}
		if(dence - info.resistance > 1)
		{
			base -= dence - info.resistance - 1;
		}
		if(stage != 0)
		{
			float britness = access.world().getLightFromNeighborsFor(EnumSkyBlock.SKY, access.pos()) * property.getSunshine(access);
			if(britness < 4F)
			{
				base -= (int) (4 - britness);
			}
			else if(britness > 12F)
			{
				base += (int) ((britness - 12F) * 0.4F);
				waste ++;
			}
		}
		float rainfall = property.getHumidity(access);
		if(rainfall < 0.5F)
		{
			int a = (int) (10 * (0.5F - rainfall) / (1 + info.saving));
			if (a > 0)
			{
				base -= a;
			}
			waste ++;
		}
		if (rainfall > 1.2F)
		{
			base ++;
			waste --;
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
		if(stage == this.floweringStage)
		{
			int count;
			switch (this.spreadType)
			{
			case 2 :
				if(access.info().gamete == null && access.rng().nextInt(5) == 0)
				{
					access.pollinate(createGameteGeneticMaterial(access, access.info().geneticMaterial));
				}
			case 1 :
				if((count = access.info().map.get("flowered")) < 5)
				{
					GeneticMaterial material = createGameteGeneticMaterial(access, access.info().geneticMaterial);
					int l = 8 + (1 + access.info().vitality) / 2;
					for(int i = 0; i < l; i++)
					{
						int x = access.rng().nextInt(this.floweringRange) - access.rng().nextInt(this.floweringRange);
						int y = access.rng().nextInt(this.floweringRange) - access.rng().nextInt(this.floweringRange);
						int z = access.rng().nextInt(this.floweringRange) - access.rng().nextInt(this.floweringRange);
						if((x | y | z) != 0)
						{
							TileEntity tile = access.getTE(x, y, z);
							if(tile instanceof ICropAccess)
							{
								((ICropAccess) tile).pollinate(material);
							}
						}
					}
					access.info().map.put("flowered", ++count);
				}
				break;
			case 3 :
				if(access.info().gamete == null)
				{
					access.pollinate(createGameteGeneticMaterial(access, access.info().geneticMaterial));
				}
				break;
			default:
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
		list.add("Growing Progress : " + (access.stage() == this.floweringStage ? ChatFormatting.LIGHT_PURPLE : ChatFormatting.GREEN) +
				(access.stage() < this.maxStage ?
						(int) (access.stageBuffer() + (access.stage() - 1) * this.growRequire) + "/" + (this.maxStage - 1) * this.growRequire :
						"Mature"));
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
		return access.isPlantable(this, Direction.D);
	}
	
	@Override
	public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
	{
		
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Crop;
	}
	
	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return EnumBlock.crop.block.getDefaultState();
	}
}