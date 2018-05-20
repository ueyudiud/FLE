/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tree;

import java.util.Random;

import farcore.lib.bio.BioData;
import farcore.lib.material.Mat;
import farcore.lib.tree.Tree;
import net.minecraft.world.World;

public class TreeAcacia extends Tree
{
	private final TreeGenAcacia generator1 = new TreeGenAcacia(this, 0.022F);
	
	public TreeAcacia(Mat material)
	{
		super(material);
		this.generators.add(this.generator1);
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, BioData info)
	{
		if (info != null)
		{
			this.generator1.setHeight(5 + info.capabilities[0], 6 + info.capabilities[0]);
		}
		else
		{
			this.generator1.setHeight(5, 6);
		}
		return this.generator1.generateTreeAt(world, x, y, z, random, info);
	}
}
