package farcore.interfaces;

import farcore.entity.EntityFallingBlockExtended;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public interface ISmartFallableBlock
{
	void onStartFalling(World world, int x, int y, int z);
	
	boolean canFallingBlockStay(World world, int x, int y, int z, int meta);
	
	boolean onFallOnGround(World world, int x, int y, int z, int meta, int height, NBTTagCompound tileNBT);
	
	boolean onDropFallenAsItem(World world, int x, int y, int z, int meta, NBTTagCompound tileNBT);

	float onFallOnEntity(World world, EntityFallingBlockExtended block, Entity target);
}