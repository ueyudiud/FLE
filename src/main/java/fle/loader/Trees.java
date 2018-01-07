/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.loader;

import static farcore.data.M.acacia;
import static farcore.data.M.apple;
import static farcore.data.M.aspen;
import static farcore.data.M.birch;
import static farcore.data.M.ceiba;
import static farcore.data.M.citrus;
import static farcore.data.M.lacquer;
import static farcore.data.M.morus;
import static farcore.data.M.oak;
import static farcore.data.M.oak_black;
import static farcore.data.M.spruce;
import static farcore.data.M.willow;

import fle.core.tree.TreeAcacia;
import fle.core.tree.TreeApple;
import fle.core.tree.TreeAspen;
import fle.core.tree.TreeBirch;
import fle.core.tree.TreeCeiba;
import fle.core.tree.TreeCitrus;
import fle.core.tree.TreeLacquer;
import fle.core.tree.TreeMorus;
import fle.core.tree.TreeOak;
import fle.core.tree.TreeOakBlack;
import fle.core.tree.TreeSpruce;
import fle.core.tree.TreeWillow;

/**
 * @author ueyudiud
 */
public class Trees
{
	public static void init()
	{
		oak.builder().setTree(new TreeOak(oak).setDefFamily());
		oak_black.builder().setTree(new TreeOakBlack(oak_black).setDefFamily());
		spruce.builder().setTree(new TreeSpruce(spruce).setDefFamily());
		birch.builder().setTree(new TreeBirch(birch).setDefFamily());
		ceiba.builder().setTree(new TreeCeiba(ceiba).setDefFamily());
		acacia.builder().setTree(new TreeAcacia(acacia).setDefFamily());
		aspen.builder().setTree(new TreeAspen(aspen).setDefFamily());
		morus.builder().setTree(new TreeMorus(morus).setDefFamily());
		willow.builder().setTree(new TreeWillow(willow).setDefFamily());
		lacquer.builder().setTree(new TreeLacquer(lacquer).setDefFamily());
		citrus.builder().setTree(new TreeCitrus(citrus).setDefFamily());
		apple.builder().setTree(new TreeApple(apple).setDefFamily());
	}
}
