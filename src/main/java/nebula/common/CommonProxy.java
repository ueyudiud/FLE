/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common;

import java.io.File;

import nebula.common.item.IItemBehaviorsAndProperties;
import nebula.common.item.IItemBehaviorsAndProperties.IIP_Containerable;
import nebula.common.tile.IGuiTile;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * @author ueyudiud
 */
public class CommonProxy implements IGuiHandler
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
			IProperty<?>...properties)
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
	
	@Override
	public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID >= 0)
		{
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			if(tile instanceof IGuiTile)
			{
				return ((IGuiTile) tile).openContainer(ID, player);
			}
		}
		else if (ID < 0)
		{
			switch (ID)
			{
			case -1 :
				if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof IItemBehaviorsAndProperties.IIP_Containerable)
				{
					return ((IIP_Containerable) player.getHeldItemMainhand().getItem()).openContainer(world, new BlockPos(x, y, z), player, player.getHeldItemMainhand());
				}
				break;
			default:
				break;
			}
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
}