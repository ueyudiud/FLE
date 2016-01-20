package farcore.block;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import farcore.entity.EntityFleFallingBlock;

public interface IFallable
{
    void onStartFalling(EntityFleFallingBlock entity);

	void onHitEntity(EntityFleFallingBlock entity);
	
	List<ItemStack> onBlockDropAsItem(EntityFleFallingBlock entity);

    boolean canFallInto(World worldIn, BlockPos pos);
    
    void onEndFalling(World world, BlockPos pos);
}