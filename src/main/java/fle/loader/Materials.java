/*
 * copyright© 2016 ueyudiud
 */

package fle.loader;

import farcore.data.M;
import fle.api.tile.IDitchTile.DitchBlockHandler;

/**
 * @author ueyudiud
 */
public class Materials
{
	public static void preinit()
	{
		DitchBlockHandler.addMaterial(M.stone);
		DitchBlockHandler.addMaterial(M.compact_stone);
		DitchBlockHandler.addMaterial(M.andesite);
		DitchBlockHandler.addMaterial(M.basalt);
		DitchBlockHandler.addMaterial(M.diorite);
		DitchBlockHandler.addMaterial(M.gabbro);
		DitchBlockHandler.addMaterial(M.granite);
		DitchBlockHandler.addMaterial(M.kimberlite);
		DitchBlockHandler.addMaterial(M.limestone);
		DitchBlockHandler.addMaterial(M.marble);
		DitchBlockHandler.addMaterial(M.netherrack);
		DitchBlockHandler.addMaterial(M.obsidian);
		DitchBlockHandler.addMaterial(M.peridotite);
		DitchBlockHandler.addMaterial(M.rhyolite);
		DitchBlockHandler.addMaterial(M.graniteP);
		DitchBlockHandler.addMaterial(M.oak);
		DitchBlockHandler.addMaterial(M.spruce);
		DitchBlockHandler.addMaterial(M.birch);
		DitchBlockHandler.addMaterial(M.ceiba);
		DitchBlockHandler.addMaterial(M.aspen);
		DitchBlockHandler.addMaterial(M.willow);
		DitchBlockHandler.addMaterial(M.morus);
	}
}