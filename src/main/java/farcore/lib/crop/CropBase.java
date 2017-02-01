package farcore.lib.crop;

import static com.mojang.realmsclient.gui.ChatFormatting.GOLD;
import static com.mojang.realmsclient.gui.ChatFormatting.GREEN;

import java.util.ArrayList;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import farcore.data.EnumBlock;
import farcore.lib.bio.DNAHandler;
import farcore.lib.bio.GeneticMaterial;
import farcore.lib.material.Mat;
import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import nebula.common.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.IPlantable;

public abstract class CropBase implements ICrop
{
	protected Mat material;
	protected int maxStage;
	protected int floweringStage = -1;
	protected int floweringRange = 0;
	protected int growReq = 1000;
	protected byte spreadType = 0;
	protected DNAHandler[] helper;
	
	public CropBase(Mat material)
	{
		this.material = material;
	}
	
	@Override
	public String getRegisteredName()
	{
		return this.material.name;
	}
	
	@Override
	public String getTranslatedName(GeneticMaterial dna)
	{
		return this.material.name;
	}
	
	@Override
	public int getMaxStage()
	{
		return this.maxStage;
	}
	
	@Override
	public GeneticMaterial applyNativeDNA()
	{
		return GeneticMaterial.newGeneticMaterial(getRegisteredName(), 0, DNAHandler.createNativeDNAs(this.helper));
	}
	
	@Override
	public long tickUpdate(ICropAccess access)
	{
		return 100L;
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
		int base = 6 + access.rng().nextInt(9 + info.growth);
		for(Direction facing : Direction.DIRECTIONS_2D)
		{
			if(access.getTE(facing) instanceof ICropAccess)
			{
				++dence;
			}
		}
		if(dence - info.weedResistance > 1)
		{
			base -= dence - info.weedResistance - 1;
		}
		if(stage != 0)
		{
			float britness = access.world().getLightFor(EnumSkyBlock.SKY, access.pos().up()) * property.getSunshine(access);
			if(britness < 4F)
			{
				base -= (int) (4 - britness);
			}
			else if(britness > 12F)
			{
				base += (int) ((britness - 12F) * 0.4F);
			}
		}
		float rainfall = property.getHumidity(access);
		if(info.dryResistance < 3 && rainfall < 0.1F)
		{
			base --;
		}
		if(info.dryResistance < 1 && rainfall < 0.3F)
		{
			base --;
		}
		if(rainfall > 1.2F)
		{
			base ++;
		}
		if(base > 0)
		{
			access.grow(base);
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
					access.pollinate(access.info().geneticMaterial.generateGameteDNA(access, access.rng(), true));
				}
			case 1 :
				if((count = access.info().map.get("flowered")) < 5)
				{
					GeneticMaterial material = access.info().geneticMaterial.generateGameteDNA(access, access.rng(), true);
					for(int i = 0; i < 8; i++)
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
					access.pollinate(access.info().geneticMaterial.generateGameteDNA(access, access.rng(), true));
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
		return this.growReq;
	}
	
	@Override
	public void addInformation(ICropAccess access, List<String> infos)
	{
		infos.add("Growing Progress : " + (access.stage() == this.floweringStage ? ChatFormatting.LIGHT_PURPLE : ChatFormatting.GREEN) +
				(access.stage() + 1 < this.maxStage ?
						(int) (access.stageBuffer() + access.stage() * this.growReq) + "/" + (this.maxStage - 1) * this.growReq :
						"Mature"));
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
		return (state = access.getBlockState(Direction.D)).getBlock()
				.canSustainPlant(state, access.world(), access.pos().down(), EnumFacing.UP, (IPlantable) EnumBlock.crop.block);
	}
	
	@Override
	public void getDrops(ICropAccess access, ArrayList<ItemStack> list)
	{
		
	}
}