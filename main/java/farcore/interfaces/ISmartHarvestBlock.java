package farcore.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ISmartHarvestBlock
{
	boolean canHarvestBlock(World world, int x, int y, int z, int meta, EntityPlayer player);

	void harvestAndDropBlock(World world, int x, int y, int z, int meta, EntityPlayer player);
}