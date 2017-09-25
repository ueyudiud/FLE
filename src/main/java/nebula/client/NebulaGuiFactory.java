/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import nebula.Nebula;
import nebula.common.LanguageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

/**
 * @author ueyudiud
 */
public class NebulaGuiFactory implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{
		
	}
	
	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass()
	{
		return NebulaGuiFactory.GuiNebulaConfig.class;
	}
	
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
	
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
	{
		return null;
	}
	
	public static class GuiNebulaConfig extends GuiConfig
	{
		public GuiNebulaConfig(GuiScreen screen)
		{
			super(screen, getElements(), Nebula.NAME, false, false, LanguageManager.translateToLocal("config.nebula"));
		}
		
		private static List<IConfigElement> getElements()
		{
			return ImmutableList.of();
		}
	}
}