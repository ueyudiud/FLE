package fle.core.gui.alpha;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.gui.GuiBase;
import farcore.lib.gui.GuiIconButton;
import farcore.util.U;
import fle.core.container.alpha.ContainerWashing;
import fle.core.container.alpha.InventoryWashing;
import fle.load.Icons;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class GuiWashing extends GuiBase<InventoryWashing>
{
	public GuiWashing(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new ContainerWashing(player, world, x, y, z));
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		buttonList.add(new GuiIconButton(0, xoffset + 72, yoffset + 18, 18, Icons.washing));
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		super.actionPerformed(button);
		sendToContainer(0, 0);
	}

	@Override
	protected void drawOther(int xOffset, int uOffset, int mouseXPosition, int mouseYPosition)
	{
	    if (inventory.isWashing())
	    {
	    	drawTexturedModalRect(xoffset + 54, yoffset + 36, 176, 0, 27, 11);
	    	drawTexturedModalRect(xoffset + 55, yoffset + 52, 176, 11, 27, inventory.getWashingProgress(22));
	    }
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return new ResourceLocation("fle", "textures/gui/ore_washing.png");
	}
	
	@Override
	public boolean hasCustomName()
	{
		return false;
	}
}