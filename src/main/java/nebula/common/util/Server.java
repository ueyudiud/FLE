/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * @author ueyudiud
 */
public final class Server
{
	private Server() {}
	
	/**
	 * I don't know why this method should use in server.
	 * @param world
	 * @param pos
	 * @param state
	 * @param entity
	 * @param numberOfParticles
	 */
	public static void addBlockLandingEffects(World world, BlockPos pos, IBlockState state,
			EntityLivingBase entity, int numberOfParticles)
	{
		((WorldServer) world).spawnParticle(EnumParticleTypes.BLOCK_DUST, entity.posX, entity.posY, entity.posZ, numberOfParticles, 0.0, 0.0, 0.0, 0.15, new int[] {Block.getStateId(state)});
	}
}