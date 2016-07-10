package farcore.lib.block;

import farcore.lib.entity.EntityFallingBlockExtended;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface ISmartFallableBlock
{
	void onStartFalling(World world, int x, int y, int z);
	
	boolean canFallingBlockStay(World world, int x, int y, int z, int meta);
	
	boolean onFallOnGround(World world, int x, int y, int z, int meta, int height, NBTTagCompound tileNBT);
	
	boolean onDropFallenAsItem(World world, int x, int y, int z, int meta, NBTTagCompound tileNBT);

	/**
	 * Called when block fall on an entity.
	 * @param world
	 * @param block
	 * @param target
	 * @return The attack damage of block.
	 */
	float onFallOnEntity(World world, EntityFallingBlockExtended block, Entity target);
}