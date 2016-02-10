package farcore.block;

import java.util.Set;

import farcore.FarCore;
import farcore.block.interfaces.IHarvestCheck;
import farcore.substance.Substance;
import farcore.util.SubTag;
import farcore.world.BlockPos;
import farcore.world.Direction;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockHook
{
	private static final byte CHECK_AREA = 32;
	
	public static boolean canSustainPlant(BlockPos pos,
			Substance substance, ForgeDirection direction, IPlantable plantable)
	{
		if (substance == null)
			return false;
		BlockPos offset = pos.offset(direction);
		Block plant = plantable.getPlant(pos.access, offset.x, offset.y, offset.z);
		EnumPlantType plantType = plantable.getPlantType(pos.access, pos.x, pos.y, pos.z);
				
		switch (plantType)
		{
		case Desert:
			return substance.contain(SubTag.PLANTABLE_DESERT);
		case Nether:
			boolean isNether = substance.contain(SubTag.PLANTABLE_NETHER);
			boolean hasLava = isBlockLava(pos.offset(Direction.NORTH))
					|| isBlockLava(pos.offset(Direction.SOUTH))
					|| isBlockLava(pos.offset(Direction.EAST))
					|| isBlockLava(pos.offset(Direction.WEST));
			return isNether && hasLava;
		case Crop:
			return substance.contain(SubTag.PLANTABLE_CROP);
		case Cave:
			return substance.contain(SubTag.PLANTABLE_CAVE);
		case Plains:
			return substance.contain(SubTag.PLANTABLE_PLAINS);
		case Water:
			return isBlockWater(pos) && pos.meta() == 0;
		case Beach:
			boolean isBeach = substance.contain(SubTag.PLANTABLE_BEACH);
			boolean hasWater = isBlockWater(pos.offset(Direction.NORTH))
					|| isBlockWater(pos.offset(Direction.SOUTH))
					|| isBlockWater(pos.offset(Direction.EAST))
					|| isBlockWater(pos.offset(Direction.WEST));
			return isBeach && hasWater;
		}
		return false;
	}
	
	public static boolean isBlockLava(BlockPos pos)
	{
		return pos.block() == Blocks.lava;
	}
	
	public static boolean isBlockWater(BlockPos pos)
	{
		return FarCore.mod.getWorldManager().isWater(pos);
	}
	
	public static boolean canHarvestBlock(Block block, EntityPlayer player, BlockPos pos)
    {
        if (block.getMaterial().isToolNotRequired())
        {
            return true;
        }
        if(block instanceof IHarvestCheck)
        {
        	ItemStack stack = player.inventory.getCurrentItem();
        	
        	Set<String> set = stack.getItem().getToolClasses(stack);
        	IHarvestCheck check = (IHarvestCheck) block;
        	Set<String> access = check.getAccessTools(pos);
        	for(String tool : set)
        	{
        		if(!access.contains(tool)) continue;
            	int toolLevel = stack.getItem().getHarvestLevel(stack, tool);
            	int toolLevelNeed = check.getToolLevel(pos, tool);
            	if (toolLevelNeed < 0)
            	{
            		return true;
            	}
            	else if(toolLevel >= toolLevelNeed)
            	{
            		return true;
            	}
        	}

        	return false;
        }

        return ForgeHooks.canHarvestBlock(block, player, pos.meta());
    }
}