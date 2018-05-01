/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import nebula.base.IntEntry;
import nebula.base.MutableIntEntry;
import nebula.base.collection.A;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class FarCoreFishingHook
{
	private static int												allWeight;
	private static final List<IntEntry<FishingResultHander>>	FISHING_RESULT_LIST	= new ArrayList<>();
	
	public static void addFisingHandler(int weight, FishingResultHander hander)
	{
		if (weight <= 0) throw new IllegalArgumentException("Weight must be positive.");
		allWeight += weight;
		FISHING_RESULT_LIST.add(new MutableIntEntry<>(hander, weight));
	}
	
	public static List<ItemStack> getFishingResult(World world, BlockPos pos, @Nullable EntityPlayer player, float amountMultiplier, Random rand)
	{
		List<ItemStack> list = A.nonnull();
		for (IntEntry<FishingResultHander> hander : FISHING_RESULT_LIST)
		{
			float mul = amountMultiplier * (0.9F + rand.nextFloat() * .2F) * hander.getValue() / allWeight;
			list.addAll(hander.getKey().addFishingResult(world, pos, player, mul));
		}
		return list;
	}
	
	public static interface FishingResultHander
	{
		@Nullable
		List<ItemStack> addFishingResult(World world, BlockPos pos, @Nullable EntityPlayer player, float amountMultiplier);
	}
}
