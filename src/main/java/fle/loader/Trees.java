/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader;

import static farcore.data.M.acacia;
import static farcore.data.M.aspen;
import static farcore.data.M.birch;
import static farcore.data.M.ceiba;
import static farcore.data.M.morus;
import static farcore.data.M.oak;
import static farcore.data.M.oak_black;
import static farcore.data.M.spruce;
import static farcore.data.M.willow;

import farcore.lib.tree.instance.TreeAcacia;
import farcore.lib.tree.instance.TreeAspen;
import farcore.lib.tree.instance.TreeBirch;
import farcore.lib.tree.instance.TreeCeiba;
import farcore.lib.tree.instance.TreeMorus;
import farcore.lib.tree.instance.TreeOak;
import farcore.lib.tree.instance.TreeOakBlack;
import farcore.lib.tree.instance.TreeSpruce;
import farcore.lib.tree.instance.TreeWillow;

/**
 * @author ueyudiud
 */
public class Trees
{
	public static void init()
	{
		oak			.setTree(new TreeOak().setMaterial(oak));
		spruce		.setTree(new TreeSpruce().setMaterial(spruce));
		birch		.setTree(new TreeBirch().setMaterial(birch));
		ceiba		.setTree(new TreeCeiba().setMaterial(ceiba));
		acacia		.setTree(new TreeAcacia().setMaterial(acacia));
		oak_black	.setTree(new TreeOakBlack().setMaterial(oak_black));
		aspen		.setTree(new TreeAspen().setMaterial(aspen));
		morus		.setTree(new TreeMorus().setMaterial(morus));
		willow		.setTree(new TreeWillow().setMaterial(willow));
	}
}