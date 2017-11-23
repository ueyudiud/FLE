/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.api.event;

import static nebula.common.util.SubTag.getNewSubTag;

import nebula.common.util.SubTag;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * The generation events, post in
 * {@link net.minecraftforge.common.MinecraftForge#TERRAIN_GEN_BUS}.
 * <p>
 * 
 * This event is cancelable, the generation will be prevent if event is
 * canceled.
 * 
 * @author ueyudiud
 */
@Cancelable
public class FarGenerationEvent extends Event
{
	public static final SubTag TREE = getNewSubTag("TREE_GEN"), WILD_CROP = getNewSubTag("WILD_CROP_GEN");
	
	public final SubTag				tag;
	public final World				world;
	public final int				chunkX, chunkZ;
	public final IChunkGenerator	generator;
	
	public FarGenerationEvent(SubTag tag, World world, int chunkX, int chunkZ, IChunkGenerator generator)
	{
		this.tag = tag;
		this.world = world;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.generator = generator;
	}
}
