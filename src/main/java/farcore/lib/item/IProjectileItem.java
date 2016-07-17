package farcore.lib.item;

import farcore.lib.entity.EntityProjectileItem;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface IProjectileItem
{
	void initEntity(EntityProjectileItem entity);

	void onEntityTick(EntityProjectileItem entity);

	boolean onHitGround(World world, int x, int y, int z, EntityProjectileItem entity);

	boolean onHitEntity(World world, Entity target, EntityProjectileItem entity);
}