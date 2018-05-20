/*
 * copyright© 2016-2018 ueyudiud
 */
package fargen.api.event;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * The generation events, post in
 * {@link net.minecraftforge.common.MinecraftForge#TERRAIN_GEN_BUS Terrain Gen Bus}.
 * <p>
 * 
 * This event is {@link net.minecraftforge.fml.common.eventhandler.Cancelable Cancelable},
 * the generation will be prevent if event is
 * canceled.
 * 
 * @author ueyudiud
 */
@Cancelable
public class FarGenerationEvent extends Event
{
	public static enum Type
	{
		TREE,
		WILD_CROP;
	}
	
	public final Type				tag;
	public final World				world;
	public final int				chunkX, chunkZ;
	public final IChunkGenerator	generator;
	
	public FarGenerationEvent(Type tag, World world, int chunkX, int chunkZ, IChunkGenerator generator)
	{
		this.tag = tag;
		this.world = world;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.generator = generator;
	}
}
