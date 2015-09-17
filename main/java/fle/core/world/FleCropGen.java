package fle.core.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
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
		if(aWorld.getBiomeGenForCoords(x, z).getFloatTemperature(x, y, z) < 0.1F) return false;
		BlockFleCrop.flag = true;
		boolean flag = false;
		if(cropBlock.canBlockStay(aWorld, x, y, z))
		{
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
		}
		BlockFleCrop.flag = false;
		if(flag)
		{
			aWorld.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
		}
		return flag;
	}	
}