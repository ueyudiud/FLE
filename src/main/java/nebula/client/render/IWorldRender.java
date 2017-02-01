package nebula.client.render;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IWorldRender
{
	boolean renderDropOnGround(World world, Entity entity, Random random, int rendererUpdateCount);
}