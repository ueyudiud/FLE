package fle.core.block.behaviour;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.core.block.BlockSubTile;
import fle.core.gui.ContainerShifter;
import fle.core.gui.GuiSifter;
import fle.core.te.TileEntitySifter;

public class BehaviourSifter extends BehaviourTile
{
	public BehaviourSifter()
	{
		super("Sifter", TileEntitySifter.class);
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new ContainerShifter(aPlayer.inventory, (TileEntitySifter) aWorld.getTileEntity(x, y, z));
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new GuiSifter(aPlayer, (TileEntitySifter) aWorld.getTileEntity(x, y, z));
	}
}