/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.material;

import static farcore.data.SubTags.BRICK;
import static farcore.data.SubTags.CLAY;
import static farcore.data.SubTags.CROP;
import static farcore.data.SubTags.DIRT;
import static farcore.data.SubTags.HANDLE;
import static farcore.data.SubTags.METAL;
import static farcore.data.SubTags.ORE;
import static farcore.data.SubTags.PLANT;
import static farcore.data.SubTags.ROCK;
import static farcore.data.SubTags.SAND;
import static farcore.data.SubTags.TREE;
import static farcore.data.SubTags.WOOD;

import farcore.lib.block.behavior.RockBehavior;
import farcore.lib.crop.ICropSpecie;
import farcore.lib.material.ore.IOreProperty;
import farcore.lib.plant.IPlant;
import farcore.lib.tree.Tree;
import nebula.common.util.SubTag;

/**
 * Only for testing.
 * 
 * @author ueyudiud
 */
class MatBuilderTest extends Builder
{
	MatBuilderTest(Mat material)
	{
		super(material);
	}
	
	@Override
	public Builder setBrick(int harvestLevel, float hardness, float resistance)
	{
		return setTag(BRICK);
	}
	
	@Override
	public Builder setCrop(ICropSpecie crop)
	{
		return setTag(CROP);
	}
	
	@Override
	public Builder setOreProperty(int harvestLevel, float hardness, float resistance, IOreProperty oreProperty, SubTag type)
	{
		return setTag(ORE);
	}
	
	@Override
	public Builder setMetalic(int harvestLevel, float hardness, float resistance)
	{
		return setTag(METAL);
	}
	
	@Override
	public Builder setHandable(float toughness)
	{
		return setTag(HANDLE);
	}
	
	@Override
	public Builder setPlant(IPlant plant)
	{
		return setTag(PLANT);
	}
	
	@Override
	public Builder setRock(RockBehavior behavior)
	{
		return setTag(ROCK);
	}
	
	@Override
	public Builder setTree(Tree tree, boolean createBlock)
	{
		return setTag(TREE);
	}
	
	@Override
	public Builder setSoil(float hardness, float resistance)
	{
		return setTag(DIRT);
	}
	
	@Override
	public Builder setClay(float hardness, float resistance)
	{
		return setTag(CLAY);
	}
	
	@Override
	public Builder setSand(float hardness, float resistance)
	{
		return setTag(SAND);
	}
	
	@Override
	public Builder setWood(float woodHardness, float ashcontent, float woodBurnHeat)
	{
		return setTag(WOOD);
	}
}
