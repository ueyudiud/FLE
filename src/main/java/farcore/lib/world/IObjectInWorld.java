package farcore.lib.world;

import net.minecraft.nbt.NBTBase;
import net.minecraft.world.World;

/**
 * The object current in world (Such as explosion, lather, etc).
 * See farcore world handler.
 * @author ueyudiud
 * @see farcore.handler.FarCoreWorldHandler
 */
public interface IObjectInWorld
{
	World world();

	double[] position();

	void readFromNBT(NBTBase nbt);

	NBTBase writeFromNBT();

	boolean isDead();
}