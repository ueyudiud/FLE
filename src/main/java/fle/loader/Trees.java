/*
 * copyright 2016-2018 ueyudiud
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
import static farcore.lib.bio.GeneData.v;

import farcore.lib.tree.Tree;
import farcore.lib.tree.TreeFamily;
import farcore.lib.tree.TreeOrder;
import fle.core.tree.TreeBirch;
import fle.core.tree.TreeCeiba;
import fle.core.tree.TreeOak;
import fle.core.tree.TreeOakBlack;
import fle.core.tree.TreeSpruce;

/**
 * @author ueyudiud
 */
public class Trees
{
	public static void init()
	{
		{
			Tree _oak = new TreeOak(oak);
			TreeOrder.ORDER.addFamily(TreeFamily.builder("oak").addDefSpecie(_oak, v(0, 0, 0, 0), v(1, 1, 1, 1)).build());
			oak.builder().setTree(_oak);
		}
		{
			Tree _oak_black = new TreeOakBlack(oak_black);
			TreeOrder.ORDER.addFamily(TreeFamily.builder("oak_black").addDefSpecie(_oak_black, v(0, 0, 0, 0), v(1, 1, 1, 1)).build());
			oak_black.builder().setTree(_oak_black);
		}
		{
			Tree _spruce = new TreeSpruce(spruce);
			TreeOrder.ORDER.addFamily(TreeFamily.builder("spruce").addDefSpecie(_spruce, v(0, 0, 0, 0), v(1, 1, 1, 1)).build());
			spruce.builder().setTree(_spruce);
		}
		{
			Tree _birch = new TreeBirch(birch);
			TreeOrder.ORDER.addFamily(TreeFamily.builder("birch").addDefSpecie(_birch, v(0, 0, 0, 0), v(1, 1, 1, 1)).build());
			birch.builder().setTree(_birch);
		}
		{
			Tree _ceiba = new TreeCeiba(ceiba);
			TreeOrder.ORDER.addFamily(TreeFamily.builder("ceiba").addDefSpecie(_ceiba, v(0, 0, 0, 0), v(1, 1, 1, 1)).build());
			ceiba.builder().setTree(_ceiba);
		}
		{
			Tree _acacia = new TreeCeiba(acacia);
			TreeOrder.ORDER.addFamily(TreeFamily.builder("acacia").addDefSpecie(_acacia, v(0, 0, 0, 0), v(1, 1, 1, 1)).build());
			acacia.builder().setTree(_acacia);
		}
		{
			Tree _aspen = new TreeCeiba(aspen);
			TreeOrder.ORDER.addFamily(TreeFamily.builder("aspen").addDefSpecie(_aspen, v(0, 0, 0, 0), v(1, 1, 1, 1)).build());
			aspen.builder().setTree(_aspen);
		}
		{
			Tree _morus = new TreeCeiba(morus);
			TreeOrder.ORDER.addFamily(TreeFamily.builder("morus").addDefSpecie(_morus, v(0, 0, 0, 0), v(1, 1, 1, 1)).build());
			morus.builder().setTree(_morus);
		}
		{
			Tree _willow = new TreeCeiba(willow);
			TreeOrder.ORDER.addFamily(TreeFamily.builder("willow").addDefSpecie(_willow, v(0, 0, 0, 0), v(1, 1, 1, 1)).build());
			willow.builder().setTree(_willow);
		}
		{
			Tree _lacquer = new TreeCeiba(lacquer);
			TreeOrder.ORDER.addFamily(TreeFamily.builder("lacquer").addDefSpecie(_lacquer, v(0, 0, 0, 0), v(1, 1, 1, 1)).build());
			lacquer.builder().setTree(_lacquer);
		}
		{
			Tree _citrus = new TreeCeiba(citrus);
			TreeOrder.ORDER.addFamily(TreeFamily.builder("citrus").addDefSpecie(_citrus, v(0, 0, 0, 0), v(1, 1, 1, 1)).build());
			citrus.builder().setTree(_citrus);
		}
		{
			Tree _apple = new TreeCeiba(apple);
			TreeOrder.ORDER.addFamily(TreeFamily.builder("apple").addDefSpecie(_apple, v(0, 0, 0, 0), v(1, 1, 1, 1)).build());
			apple.builder().setTree(_apple);
		}
	}
}
