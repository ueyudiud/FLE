package fle.core.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class TankBlockInfo
{
	public static final TankBlockInfo DEFAULT = new TankBlockInfo();

	private AttributeMap map = new AttributeMap();
	
	private TankBlockInfo(){}
	public TankBlockInfo(Block material)
	{
		this(material, 0);
	}
	public TankBlockInfo(Block material, int metadata)
	{
		map.setAttribute(Attribute.block_material, material);
		map.setAttribute(Attribute.metadata, metadata);
	}
	
	public Block getMaterial()
	{
		return map.getAttribute(Attribute.block_material);
	}
	
	public int getMetadata()
	{
		return map.getAttribute(Attribute.metadata);
	}
}