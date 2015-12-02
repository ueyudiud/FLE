package fle.core.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.crop.CropCard;
import fle.core.block.BlockFleCrop;
import fle.core.init.IB;
import fle.core.te.TileEntityCrop;

public class FleCropGen extends FleSurfaceGen
{
	Block cropBlock = IB.crop;
	CropCard crop;
	
	public FleCropGen(int size, int count, CropCard aCrop)
	{
		super(size, count);
		crop = aCrop;
	}

	@Override
	public boolean generateAt(World aWorld, Random aRand, int x, int y, int z)
	{
		boolean flag = false;
		Block target = aWorld.getBlock(x, y, z);
		if((aWorld.isAirBlock(x, y, z) || aWorld.getBlock(x, y, z).isReplaceable(aWorld, x, y, z)) &&
				cropBlock.canBlockStay(aWorld, x, y, z))
		{
			BlockFleCrop.flag = true;
			if(aWorld.setBlock(x, y, z, cropBlock))
			{
				if(aWorld.getTileEntity(x, y, z) instanceof TileEntityCrop)
				{
					TileEntityCrop tile = (TileEntityCrop) aWorld.getTileEntity(x, y, z);
					tile.setupCrop(crop);
					tile.setCropAge(aRand.nextInt(crop.getMaturation()));
					tile.isWild = true;
					flag = true;
				}
			}
			BlockFleCrop.flag = false;
		}
		else
		{
			return false;
		}
		if(flag)
		{
			aWorld.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
		}
		return flag;
	}	
}