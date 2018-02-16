/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.tree;

import java.util.Random;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.EnumToolTypes;
import farcore.data.MC;
import farcore.lib.compat.jei.ToolDisplayRecipeMap;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.tile.instance.TECoreLeaves;
import farcore.lib.tree.Tree;
import farcore.lib.world.CalendarHandler;
import farcore.lib.world.ICalendar;
import nebula.client.model.StateMapperExt;
import nebula.client.util.Renders;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import nebula.common.util.Direction;
import nebula.common.util.Game;
import nebula.common.util.Properties;
import nebula.common.util.Worlds;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public abstract class TreeWithFruitLeaves extends Tree
{
	public static final IProperty<Integer> FRUIT_STAGE = Properties.create("fruit_stage", 0, 4);
	
	public TreeWithFruitLeaves(Mat material)
	{
		super(material);
	}
	
	public TreeWithFruitLeaves(Mat material, int harvestLevel, float hardness, float explosionResistance, float ashcontent, float burnHeat)
	{
		super(material, harvestLevel, hardness, explosionResistance, ashcontent, burnHeat);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		StateMapperExt mapper = new StateMapperExt(this.material.modid, "log", null);
		mapper.setVariants("type", this.material.name);
		Renders.registerCompactModel(mapper, this.blocks[0], null);
		Renders.registerCompactModel(mapper, this.blocks[1], null);
		mapper = new StateMapperExt(this.material.modid, "leaves/" + this.material.name, null, net.minecraft.block.BlockLeaves.CHECK_DECAY);
		Renders.registerCompactModel(mapper, this.blocks[2], null);
		Renders.registerCompactModel(mapper, this.blocks[3], null);
		Game.registerBiomeColorMultiplier(this.blocks[2]);
		Game.registerBiomeColorMultiplier(this.blocks[3]);
	}
	
	@Override
	public IBlockState initLeavesState(IBlockState state)
	{
		return state.withProperty(FRUIT_STAGE, 0);
	}
	
	@Override
	public BlockStateContainer createLeavesStateContainer(Block block)
	{
		return new BlockStateContainer(block, net.minecraft.block.BlockLeaves.CHECK_DECAY, FRUIT_STAGE);
	}
	
	@Override
	public void updateLeaves(World world, BlockPos pos, Random rand, boolean checkDency)
	{
		if (updateAndResult(world, pos, rand, checkDency)) return;
		if (checkDency) return;
		IBlockState state = world.getBlockState(pos);
		ICalendar calendar;
		if (canGrowFruit((calendar = CalendarHandler.getCalendar(world)), world))
		{
			int i0, i;
			switch (i0 = i = state.getValue(FRUIT_STAGE))
			{
			case 0:
				if (rand.nextInt(5) == 0)
				{
					i++;
				}
				break;
			case 1:
			case 2:
				if (rand.nextInt(3) == 0)
				{
					i++;
				}
				break;
			case 3:
				if (canMatureFruit(calendar, world) && rand.nextBoolean())
				{
					i++;
				}
				break;
			case 4:
				if (rand.nextInt(9) == 0)
				{
					Worlds.spawnDropInWorld(world, pos, createFruit(world, pos, state));
					i = 0;
				}
				break;
			default:
				break;
			}
			if (i0 != i) world.setBlockState(pos, state.withProperty(FRUIT_STAGE, i));
		}
		else
		{
			switch (state.getValue(FRUIT_STAGE))
			{
			case 4:
				Worlds.spawnDropInWorld(world, pos, createFruit(world, pos, state));
			case 3:
			case 2:
			case 1:
				world.setBlockState(pos, state.withProperty(FRUIT_STAGE, 0));
			}
		}
	}
	
	@Override
	public boolean onLeavesRightClick(EntityPlayer player, World world, BlockPos pos, IBlockState state, Direction side, float xPos, float yPos, float zPos, TECoreLeaves tile)
	{
		if (state.getValue(FRUIT_STAGE) == 4)
		{
			if (!world.isRemote)
			{
				ItemStack stack = createFruit(world, pos, state);
				if (!player.inventory.addItemStackToInventory(stack))
				{
					Worlds.spawnDropInWorld(world, pos, stack);
				}
				world.setBlockState(pos, state.withProperty(FRUIT_STAGE, 0));
			}
			return true;
		}
		return false;
	}
	
	@Override
	@Optional.Method(modid = FarCore.JEI)
	public void addDropRecipe()
	{
		ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.blocks[0], 1, -1),
				new AbstractStack[] { EnumToolTypes.AXE.stack() },
				new AbstractStack[] {new BaseStack(ItemMulti.createStack(this.material, MC.log_cutted))},
				new int[][] {{10000}});
		ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.blocks[0], 1, -1),
				new AbstractStack[] { EnumToolTypes.BIFACE.stack() },
				new AbstractStack[] {new BaseStack(ItemMulti.createStack(this.material, MC.log_cutted))},
				new int[][] {{10000}});
		ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(this.blocks[2], 1, -1),
				new AbstractStack[0],
				new AbstractStack[] {
						new BaseStack(ItemMulti.createStack(this.material, MC.branch)),
						new BaseStack(this.isBroadLeaf ? LEAVES_APPLIER1.get() : LEAVES_APPLIER2.get()),
						new BaseStack(EnumBlock.sapling.block, 1, this.material.id)},
				new int[][] {{1250}, {1427}, null});
		IBlockState state = this.blocks[2].getDefaultState().withProperty(FRUIT_STAGE, 4);
		ToolDisplayRecipeMap.addToolDisplayRecipe(new BaseStack(state),
				new AbstractStack[0],
				new AbstractStack[] {new BaseStack(createFruit(null, null, state))},
				null);
	}
	
	protected abstract ItemStack createFruit(World world, BlockPos pos, IBlockState state);
	
	protected abstract boolean canGrowFruit(ICalendar calendar, World world);
	
	protected abstract boolean canMatureFruit(ICalendar calendar, World world);
}
