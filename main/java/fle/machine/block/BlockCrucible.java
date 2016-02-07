package fle.machine.block;

import static fle.init.Substances.coal;

import java.util.List;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.registry.GameRegistry;
import farcore.FarCore;
import farcore.chem.RecipeCache;
import farcore.substance.SStack;
import farcore.substance.Substance;
import farcore.substance.SubstanceRegistry;
import farcore.substance.SubstanceRegistry.Key$;
import farcore.util.IDebugable;
import farcore.util.Part;
import farcore.world.BlockPos;
import flapi.block.BlockFle;
import fle.machine.tileentity.StackHandler;
import fle.machine.tileentity.TileEntityCrucible;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class BlockCrucible extends BlockFle 
implements ITileEntityProvider, IDebugable
{
	public BlockCrucible(Class<? extends ItemBlock> clazz, String unlocalized)
	{
		super(clazz, unlocalized, Material.iron);
		FarCore.registerTileEntity(TileEntityCrucible.class, "fle.crucible", 
				StackHandler.LOADER, StackHandler.SAVER);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float xPos, float yPos, float zPos)
	{
		if(world.getTileEntity(x, y, z) instanceof TileEntityCrucible)
		{
			TileEntityCrucible tile = 
					(TileEntityCrucible) world.getTileEntity(x, y, z);
			if(player.getCurrentEquippedItem() != null)
			{
				ItemStack stack = player.getCurrentEquippedItem();
				if(SubstanceRegistry.getSubstance(stack) == null) return false;
				if(!world.isRemote)
				{
					Key$ key = SubstanceRegistry.getSubstance(stack);
					tile.add(key.substance, key.part, key.part.resolution * stack.stackSize);
				}
				player.setCurrentItemOrArmor(0, null);
				return true;
			}
			else
			{
				SStack stack1 = null;
				ItemStack stack2 = null;
				for(SStack stack : ImmutableList.copyOf(tile.getAll()))
				{
					if(stack.toIS() != null)
					{
						Object[] objects = stack.toISWithAmount();
						stack2 = (ItemStack) objects[0];
						stack1 = new SStack(stack.getSubstance(), stack.part, (Integer) objects[1]);
					}
				}
				if(stack2 != null)
				{
					tile.remove(stack1.getSubstance(), stack1.part, stack1.size);
					if(!world.isRemote)
					{
						player.dropPlayerItemWithRandomChoice(stack2, false);
					}
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		return false;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z,
			Entity entity)
	{
		if(world.getTileEntity(x, y, z) instanceof TileEntityCrucible &&
				entity instanceof EntityItem)
		{
			EntityItem item = (EntityItem) entity;
			if(item.getEntityItem() == null || 
					SubstanceRegistry.getSubstance(item.getEntityItem()) == null) return;
			Key$ key = SubstanceRegistry.getSubstance(item.getEntityItem());
			TileEntityCrucible tile = 
					(TileEntityCrucible) world.getTileEntity(x, y, z);
			tile.add(key.substance, key.part, key.part.resolution * item.getEntityItem().stackSize);
			item.setDead();
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityCrucible();
	}

	@Override
	public void addInfomationToList(BlockPos pos, List<String> list)
	{
		list.add("==========================");
		list.add("=      Crucible Infomation      =");
		list.add("==========================");
		for(SStack stack : ((TileEntityCrucible) pos.tile()).getAll())
		{
			list.add(EnumChatFormatting.AQUA + stack.getSubstance().getName() + EnumChatFormatting.WHITE + "x" + stack.size);
		}
		for(RecipeCache cache : ((TileEntityCrucible) pos.tile()).stacks.caches)
		{
			list.add(EnumChatFormatting.GOLD.toString() + cache.recipe.name() + 
					EnumChatFormatting.WHITE + cache.getTick() + "/" + cache.getMaxTick());
		}
	}
}