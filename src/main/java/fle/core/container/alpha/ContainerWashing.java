package fle.core.container.alpha;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.interfaces.gui.IGuiUpdatable;
import farcore.lib.container.ContainerBase;
import farcore.lib.container.SlotBase;
import farcore.lib.container.SlotOutput;
import farcore.util.U;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.world.World;

public class ContainerWashing extends ContainerBase<InventoryWashing> implements IGuiUpdatable
{	
	public ContainerWashing(EntityPlayer player, World world, int x, int y, int z)
	{
		super(new InventoryWashing(), player);
		addPlayerSlot();
		int k = this.inventorySlots.size();
		addSlotToContainer(new SlotBase(inventory, InventoryWashing.input, 35, 19));
		addSlotToContainer(new SlotBase(inventory, InventoryWashing.tool, 53, 19));
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				addSlotToContainer(new SlotOutput(inventory, 2 + i + j * 3, 92 + i * 18, 19 + j * 18));
			}
		}
		TransLocate locate1, locate2, locate3;
		addTransLocate(locate1 = new TransLocate("input", k + 0));
		addTransLocate(locate2 = new TransLocate("tool", k + 1));
		addTransLocate(locate3 = new TransLocate("output", k + 2, k + 11, false, true));
		locate3.append(locatePlayer);
		locate2.append(locateHand).append(locateBag);
		locate1.append(locateBag).append(locateHand);
		locateBag.append(locate1).append(locate2).append(locateHand);
		locateHand.append(locate2).append(locate1).append(locateBag);
	}
	
	@Override
	protected int getUpdateSize()
	{
		return 1;
	}
	
	@Override
	protected int getUpdate(int id)
	{
		return id == 0 ? inventory.progress : 0;
	}
	
	@Override
	protected void setUpdate(int id, int value)
	{
		if(id == 0)
		{
			inventory.progress = value;
		}
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		U.Worlds.spawnDropsInWorldByPlayerOpeningContainer(player, inventory);
	}
	
	public void onWashing()
	{
		inventory.tryWashingRecipe(player);
	}

	@Override
	public void onActive(int type, int contain)
	{
		if(type == 0 && contain == 0)
		{
			onWashing();
			detectAndSendChanges();
		}
	}
}