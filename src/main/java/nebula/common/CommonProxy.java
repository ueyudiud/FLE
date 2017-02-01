/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

/**
 * @author ueyudiud
 */
public class CommonProxy
{
	public World worldInstance(int id)
	{
		return DimensionManager.getWorld(id);
	}
	
	public void registerRender(Object object)
	{
		
	}
	
	public String getLocale()
	{
		return LanguageManager.ENGLISH;
	}
	
	public File fileDir()
	{
		return new File(".");
	}
	
	public EntityPlayer playerInstance()
	{
		return null;
	}
	
	public <T extends Comparable<T>> void registerCompactModel(boolean splitFile, Block block, String modid, String path, IProperty<T> property,
			IProperty...properties)
	{
		
	}
	
	public void setModelLocate(Item item, int meta, String modid, String name, String type)
	{
		
	}
	
	public void setModelLocate(Item item, int meta, String modid, String name)
	{
		
	}
	
	public void registerBiomeColorMultiplier(Block...block)
	{
		
	}
	
	public String translateToLocalByI18n(String unlocal, Object...parameters)
	{
		return null;
	}
	
	public void loadClient()
	{
		
	}
}