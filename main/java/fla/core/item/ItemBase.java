package fla.core.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.item.IPlaceableItem;
import fla.api.util.InfoBuilder;
import fla.core.Fla;

public class ItemBase extends Item
{
	protected boolean customName = false;
	
	protected InfoBuilder<ItemStack> ib;
	protected IIcon[] icons;
	
	@Override
	public String getUnlocalizedName(ItemStack stack) 
	{
		return this.hasSubtypes && (!customName) ? super.getUnlocalizedName(stack) + ":" + stack.getItemDamage() : super.getUnlocalizedName(stack);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack i, EntityPlayer e, List l, boolean u)
	{
		if(ib != null)
		{
			l.addAll(ib.getInfo(i));
		}
	}
	
	@Override
	public boolean doesSneakBypassUse(World world, int x, int y, int z,
			EntityPlayer player) 
	{
		if(this instanceof IPlaceableItem && Fla.fla.km.get().isPlaceKeyDown(player))
		{
			int size = ((IPlaceableItem) this).canItemPlace(player.getCurrentEquippedItem());
			if(size == 0) return false;
			MovingObjectPosition mop = getMovingObjectPositionFromPlayer(world, player, false);
			if(mop.typeOfHit == MovingObjectType.BLOCK)
			{
				ForgeDirection dir = ForgeDirection.getOrientation(mop.sideHit);
				int x1 = mop.blockX + dir.offsetX;
				int y1 = mop.blockY + dir.offsetY;
				int z1 = mop.blockZ + dir.offsetZ;
				ItemStack ret = ((IPlaceableItem) this).setPlacedBlock(player);
				ItemStack item = player.getCurrentEquippedItem().copy();
				if(ret != null && (!world.isRemote))
					if(ret.getItem() instanceof ItemBlock)
						if(ForgeHooks.onPlaceItemIntoWorld(ret, player, world, x1, y1, z1, mop.sideHit, (float) mop.hitVec.xCoord, (float) mop.hitVec.yCoord, (float) mop.hitVec.zCoord))
							item.stackSize -= size;
				player.setCurrentItemOrArmor(0, item);
				
			}
		}
		return false;
	}
	
	protected void setupNBT(ItemStack stack)
	{
		if(stack.stackTagCompound == null)
		{
			stack.setTagCompound(new NBTTagCompound());
		}
	}
}
