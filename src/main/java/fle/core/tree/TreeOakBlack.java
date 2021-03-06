/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.tree;

import java.util.Random;

import farcore.lib.bio.BioData;
import farcore.lib.material.Mat;
import farcore.lib.tree.ISaplingAccess;
import farcore.lib.tree.Tree;
import net.minecraft.world.World;

public class TreeOakBlack extends Tree
{
	private final TreeGenClassic generator1 = new TreeGenClassic(this, 0.02F);
	
	public TreeOakBlack(Mat material)
	{
		super(material);
		this.generators.add(this.generator1);
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, BioData info)
	{
		if (info != null)
		{
			this.generator1.setHeight(info.capabilities[0] / 2 + 4, info.capabilities[0] + 3);
		}
		else
		{
			this.generator1.setHeight(4, 3);
		}
		return this.generator1.generateTreeAt(world, x, y, z, random, info);
	}
	
	@Override
	public int getGrowAge(ISaplingAccess access)
	{
		return 90;
	}
}
