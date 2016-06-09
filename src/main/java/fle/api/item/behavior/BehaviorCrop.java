package fle.api.item.behavior;

import farcore.block.plant.crop.ItemCrop;
import farcore.enums.Direction;
import farcore.enums.EnumBlock;
import farcore.lib.crop.CropCard;
import farcore.lib.crop.CropManager;
import farcore.lib.crop.TileEntityCrop;
import fle.core.plant.crop.CropMillet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BehaviorCrop extends BehaviorBase
{
	String card;
	
	public BehaviorCrop(String card)
	{
		this.card = card;
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ)
	{
		CropCard card = CropManager.getCrop(this.card);
		Direction direction = Direction.directions[side];
		x += direction.x;
		y += direction.y;
		z += direction.z;
		if(!player.canPlayerEdit(x, y, z, side, stack))
		{
			return false;
		}
		if(world.getBlock(x, y, z).isAir(world, x, y, z) && 
				card == null || !card.canPlantAt(world, x, y, z))
		{
			return true;
		}
		else
		{
			EnumBlock.crop.spawn(world, x, y, z);
			int g = ItemCrop.getGeneration(stack);
			String dna = ItemCrop.getDNA(stack);
			TileEntity tile;
			if((tile = world.getTileEntity(x, y, z)) instanceof TileEntityCrop)
			{
				((TileEntityCrop) tile).initCrop(g, dna, card);
			}
			stack.stackSize--;
			return true;
		}
	}
}