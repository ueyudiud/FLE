package farcore.block;

import java.util.ArrayList;
import java.util.List;

import farcore.resource.BlockTextureManager;
import flapi.util.BTI;
import flapi.util.BlockTextureHandler;
import flapi.util.ITextureHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.RotationHelper;

/**
 * The base class of block.
 * @author ueyudiud
 *
 */
public class BlockFle extends BlockFleTexture4
{
	protected int maxSize;
	
	private MapColor mapColor = null;

	protected BlockFle(String unlocalized, Material Material)
	{
		super(unlocalized, Material);
	}
	protected BlockFle(Class<? extends ItemBlock> clazz, String unlocalized, Material Material)
	{
		super(clazz, unlocalized, Material);
	}
	
	public BlockFle setMapColor(MapColor aColor)
	{
		mapColor = aColor;
		return this;
	}
	
	@Override
	public BlockFle setBlockTextureName(String name)
	{
		if(useClassicIconHandler())
		{
			super.setBlockTextureName(name);
		}
		else
		{
			setBlockTexture(new BlockTextureManager(name));
		}
		return this;
	}
	
	public BlockFle setBlockTexture(ITextureHandler<BTI> handler)
	{
		this.iconHandler = new BlockTextureHandler(handler);
		return this;
	}
	
	@Override
	public BlockFle setHardness(float hardness)
	{
		super.setHardness(hardness);
		return this;
	}
	
	@Override
	public BlockFle setResistance(float resistance)
	{
		super.setResistance(resistance);
		return this;
	}
	
	@Override
	public BlockFle setCreativeTab(CreativeTabs tab)
	{
		super.setCreativeTab(tab);
		return this;
	}
	
	@Override
	public BlockFle setStepSound(SoundType sound)
	{
		super.setStepSound(sound);
		return this;
	}
	
	public BlockFle setMaxStackSize(int size)
	{
		this.maxSize = size;
		return this;
	}
	
	/**
	 * Get map color from block.
	 */
	@Override
	public MapColor getMapColor(int metadata)
	{
		return mapColor == null ? super.getMapColor(metadata) : mapColor;
	}
	
	/**
	 * Get max stack size of block.
	 * @return
	 */
	public int getMaxStackSize()
	{
		return maxSize;
	}
}