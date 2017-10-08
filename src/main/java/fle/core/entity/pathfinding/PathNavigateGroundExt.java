/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.entity.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class PathNavigateGroundExt extends PathNavigateGround
{
	public PathNavigateGroundExt(EntityLiving entitylivingIn, World worldIn)
	{
		super(entitylivingIn, worldIn);
	}
	
	@Override
	protected PathFinder getPathFinder()
	{
		this.nodeProcessor = new NodeProcessorBlockBreakable();
		//		this.nodeProcessor.setCanEnterDoors(true);
		return new PathFinder(this.nodeProcessor);
		//		return super.getPathFinder();
	}
}