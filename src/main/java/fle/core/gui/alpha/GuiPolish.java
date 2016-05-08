package fle.core.gui.alpha;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.inventory.Inventory;
import farcore.lib.container.ContainerBase;
import farcore.lib.gui.GuiBase;
import farcore.lib.gui.GuiIconButton;
import fle.api.recipe.machine.PolishRecipe.PolishCondition;
import fle.core.container.alpha.ContainerPolish;
import fle.load.Icons;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class GuiPolish extends GuiBase<Inventory>
{
	public GuiPolish(World world, int x, int y, int z, EntityPlayer player)
	{
		super(new ContainerPolish(world, x, y, z, player));
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		buttonList.add(new GuiIconButton(0, xoffset + 103, yoffset + 48, 18, Icons.polish_delete));
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 3; ++j)
			{
				buttonList.add(new GuiIconButton(1 + j * 3 + i, xoffset + 45 + 17 * i, yoffset + 18 + 17 * j, 16));
			}
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		super.actionPerformed(button);
		if(button.id == 0)
		{
			sendToContainer(0, 0);
			((ContainerPolish) container).cleanMap();
		}
		else
		{
			((ContainerPolish) container).polishItem(button.id - 1);
			sendToContainer(1, button.id - 1);
		}
	}

	@Override
	protected void drawOther(int xOffset, int yOffset, int mouseXPosition, int mouseYPosition)
	{
		if(((ContainerPolish) container).polish != null)
		{
			PolishCondition[][] conditions = ((ContainerPolish) container).conditions;
			for(int y = 0; y < 3; ++y)
				for(int x = 0; x < 3; ++x)
				{
					drawCondition(45 + 17 * x, 18 + 17 * y, conditions[y][x]);
				}
		}
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return new ResourceLocation("fle", "textures/gui/polish.png");
	}
}