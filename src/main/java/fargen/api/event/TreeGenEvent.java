/*
 * copyright© 2016-2018 ueyudiud
 */
package fargen.api.event;

import farcore.lib.tree.ITreeGenerator;
import nebula.base.function.WeightedRandomSelector;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This event posted in
 * {@link net.minecraftforge.common.MinecraftForge#TERRAIN_GEN_BUS}. Called when
 * surface generator is generating trees.
 * 
 * @author ueyudiud
 */
public class TreeGenEvent extends Event
{
	public final World									world;
	public final int									chunkX;
	public final int									chunkZ;
	public final float									temperature;
	public final float									rainfall;
	public final WeightedRandomSelector<ITreeGenerator>	selector;
	
	public TreeGenEvent(World world, int x, int z, float temperature, float rainfall, WeightedRandomSelector<ITreeGenerator> selector)
	{
		this.world = world;
		this.chunkX = x;
		this.chunkZ = z;
		this.temperature = temperature;
		this.rainfall = rainfall;
		this.selector = selector;
	}
}
