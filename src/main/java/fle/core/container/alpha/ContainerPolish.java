package fle.core.container.alpha;

import farcore.FarCore;
import farcore.enums.EnumDamageResource;
import farcore.interfaces.gui.IGuiUpdatable;
import farcore.inventory.Inventory;
import farcore.lib.container.ContainerBase;
import farcore.lib.container.SlotBase;
import farcore.lib.container.SlotOutput;
import farcore.lib.stack.AbstractStack;
import farcore.util.U;
import fle.api.recipe.machine.PolishRecipe;
import fle.api.recipe.machine.PolishRecipe.PolishCondition;
import fle.load.Langs;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerPolish extends ContainerBase<Inventory> implements IGuiUpdatable
{
	private World world;
	private int x, y, z;
	private Block block;
	public ItemStack polish;
	public PolishCondition[][] conditions;
	
	public ContainerPolish(World world, int x, int y, int z, EntityPlayer player)
	{
		super(new Inventory(3, Langs.inventoryPolish, 64), player);
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.block = world.getBlock(x, y, z);
		addPlayerSlot();
		int k = this.inventorySlots.size();
		addSlotToContainer(new SlotBase(inventory, 0, 20, 35));
		addSlotToContainer(new SlotBase(inventory, 1, 104, 22));
		addSlotToContainer(new SlotOutput(inventory, 2, 133, 35)
		{
			@Override
			public void onSlotChanged()
			{
				super.onSlotChanged();
				if(ContainerPolish.this.inventory.stacks[2] == null)
				{
					polish = null;
					conditions = null;
				}
			}
			
			@Override
			public ItemStack decrStackSize(int size)
			{
				ItemStack stack = super.decrStackSize(size);
				if(stack != null)
				{
					polish = null;
					conditions = null;
					return stack;
				}
				return null;
			}
				});
		TransLocate locate1, locate2, locate3;
		addTransLocate(locate1 = new TransLocate("input", k + 0));
		addTransLocate(locate2 = new TransLocate("tool", k + 1));
		addTransLocate(locate3 = new TransLocate("output", k + 2, true));
		locate3.append(locatePlayer);
		locate2.append(locateHand).append(locateBag);
		locate1.append(locateBag).append(locateHand);
		locateBag.append(locate1).append(locate2).append(locateHand);
		locateHand.append(locate2).append(locate1).append(locateBag);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		U.Worlds.spawnDropsInWorldByPlayerOpeningContainer(player, inventory);
	}
	
	public void cleanMap()
	{
		polish = null;
		conditions = null;
		inventory.stacks[2] = null;
	}
	
	public void polishItem(int locate)
	{
		if(polish != null)
		{
			int level = PolishRecipe.getResourceLevel(polish);
			int level1;
			if((level1 = PolishRecipe.getToolLevel(inventory.stacks[1])) >= level)
			{
				int x = locate % 3;
				int y = locate / 3;
				conditions[y][x] = PolishRecipe.onCrafting(inventory.stacks[1], conditions[y][x]);
				inventory.stacks[2] = PolishRecipe.getResult(polish, conditions);
				U.Inventorys.damage(inventory.stacks[1], null, .25F * (float) level / (float) level1, EnumDamageResource.USE);
				if(inventory.stacks[1].stackSize <= 0)
					inventory.stacks[1] = null;
				detectAndSendChanges();
			}
		}
		else
		{
			AbstractStack stack;
			if((stack = PolishRecipe.isResource(inventory.stacks[0])) != null)
			{
				polish = inventory.decrStackSize(0, stack.size(inventory.stacks[0]));
				if(polish != null)
				{
					conditions = new PolishCondition[3][3];
					for(int i = 0; i < 3; ++i)
						for(int j = 0; j < 3; ++j)
						{
							conditions[i][j] = PolishRecipe.DEFAULT;
						}
					polishItem(locate);
				}
			}
		}
	}
	
	@Override
	public void onActive(int type, int contain)
	{
		if(type == 0)
		{
			cleanMap();
		}
		else if(type == 1)
		{
			polishItem(contain);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return player.getDistanceSq((double) x + .5, (double) y + .5, (double) z + .5) <= 64D &&
				world.getBlock(x, y, z) == block;
	}
}