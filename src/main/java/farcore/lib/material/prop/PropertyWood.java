/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.material.prop;

import farcore.blocks.wood.BlockPlank;
import farcore.lib.material.Mat;

public class PropertyWood extends PropertyBlockable
{
	public float		ashcontent;
	public float		burnHeat;
	public BlockPlank	plank;
	
	public PropertyWood(Mat material, int harvestLevel, float hardness, float explosionResistance, float ashcontent, float burnHeat)
	{
		super(material, harvestLevel, hardness, explosionResistance);
		this.ashcontent = ashcontent;
		this.burnHeat = burnHeat;
	}
}
