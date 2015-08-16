package fle.core.block.machine;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.block.IBlockWithTileBehaviour;
import fle.api.util.ITextureLocation;
import fle.api.util.Register;
import fle.core.block.BlockSubTile;
import fle.core.block.ItemSubTile;

public class ItemClayInventory extends ItemSubTile
{
	private Map<Integer, IIcon[]> iconMap;
	
	public ItemClayInventory(Block aBlock)
	{
		super(aBlock);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getSpriteNumber()
	{
		return 1;
	}
	
	@Override
	public void registerIcons(IIconRegister register)
	{
		Register<IBlockWithTileBehaviour<BlockSubTile>> register1 = ((BlockSubTile) block).getRegister();
		iconMap = new HashMap();
		for(IBlockWithTileBehaviour<BlockSubTile>  tBehavior : register1)
		{
			String name = register1.name(tBehavior);
			ITextureLocation locate = ((BlockSubTile) block).getTextureName(name);
			IIcon[] icons = new IIcon[locate.getLocateSize()];
			for(int i = 0; i < icons.length; ++i)
			{
				icons[i] = register.registerIcon(locate.getTextureFileName(i) + ":" + locate.getTextureName(i));
			}
			iconMap.put(register1.serial(tBehavior), icons);
		}
	}
	
	@Override
	public IIcon getIconFromDamage(int meta)
	{
		return getIconFromDamageForRenderPass(0, meta);
	}
	
	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	
	@Override
	public int getRenderPasses(int metadata)
	{
		return iconMap.get(metadata).length;
	}
	
	@Override
	public IIcon getIconFromDamageForRenderPass(int pass, int meta)
	{
		return iconMap.get(meta)[pass];
	}
}