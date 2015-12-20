package fle.resource.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import flapi.plant.CropCard;
import fle.core.init.IB;
import fle.resource.block.BlockFleCrop;
import fle.resource.block.TileEntityCrop;

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
		if((target.getMaterial() == Material.air || target.isReplaceable(aWorld, x, y, z)) &&
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
		return flag;
	}	
}