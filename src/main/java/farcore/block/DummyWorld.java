package farcore.block;

import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.storage.ISaveHandler;

public abstract class DummyWorld extends World
{

	public DummyWorld(ISaveHandler p_i45369_1_, String p_i45369_2_, WorldSettings p_i45369_3_,
			WorldProvider p_i45369_4_, Profiler p_i45369_5_) {
		super(p_i45369_1_, p_i45369_2_, p_i45369_3_, p_i45369_4_, p_i45369_5_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canSnowAtBody(int x, int y, int z, boolean light)
	{
        BiomeGenBase biomegenbase = this.getBiomeGenForCoords(x, z);
        float f = U.Worlds.getBiomeBaseTemperature(this, x, y, z);

        if (f > 0.15F)
        {
            return false;
        }
        else if (!light)
        {
            return true;
        }
        else
        {
            if (y >= 0 && y < 256 && this.getSavedLightValue(EnumSkyBlock.Block, x, y, z) < 10)
            {
                Block block = this.getBlock(x, y, z);

                if (block.getMaterial() == Material.air && Blocks.snow_layer.canPlaceBlockAt(this, x, y, z))
                {
                    return true;
                }
            }

            return false;
        }
	}
}