/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.tree;

import static farcore.FarCore.worldGenerationFlag;
import static net.minecraft.block.BlockLeaves.CHECK_DECAY;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.FarCore;
import farcore.blocks.flora.BlockLeaves;
import farcore.blocks.flora.BlockLeavesCore;
import farcore.blocks.flora.BlockLogArtificial;
import farcore.blocks.flora.BlockLogNatural;
import farcore.data.Config;
import farcore.data.EnumBlock;
import farcore.data.EnumItem;
import farcore.data.EnumToolTypes;
import farcore.data.MC;
import farcore.data.MP;
import farcore.lib.bio.BioData;
import farcore.lib.bio.IntegratedSpecie;
import farcore.lib.compat.jei.ToolDisplayRecipeMap;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyWood;
import farcore.lib.tile.instance.TECoreLeaves;
import nebula.base.function.Applicable;
import nebula.client.model.StateMapperExt;
import nebula.client.util.IRenderRegister;
import nebula.client.util.Renders;
import nebula.common.item.ItemSubBehavior;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import nebula.common.tile.IToolableTile;
import nebula.common.tool.EnumToolType;
import nebula.common.util.Direction;
import nebula.common.util.Game;
import nebula.common.util.L;
import nebula.common.util.W;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public abstract class Tree extends IntegratedSpecie<TreeFamily> implements ITree, IRenderRegister
{
	public static final Applicable<ItemStack>	LEAVES_APPLIER1	= Applicable.asCached(() -> EnumItem.crop_related.item != null ? ((ItemSubBehavior) EnumItem.crop_related.item).getSubItem("broadleaf") : null);
	public static final Applicable<ItemStack>	LEAVES_APPLIER2	= Applicable.asCached(() -> EnumItem.crop_related.item != null ? ((ItemSubBehavior) EnumItem.crop_related.item).getSubItem("coniferous") : null);
	
	public final PropertyWood	property;
	/** logNative logArtifical leaves leavesCore */
	protected Block[]			blocks;
	protected int				leavesCheckRange	= 4;
	protected boolean			isBroadLeaf			= true;
	
	protected List<TreeGenAbstract> generators = new ArrayList<>(4);
	
	public Tree(Mat material)
	{
		this.material = material;
		this.property = material.getProperty(MP.property_wood);
	}
	
	@Override
	public Mat material()
	{
		return this.material;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		StateMapperExt mapper = new StateMapperExt(this.material.modid, "log", null);
		mapper.setVariants("type", this.material.name);
		Renders.registerCompactModel(mapper, this.blocks[0], null);
		Renders.registerCompactModel(mapper, this.blocks[1], null);
		mapper = new StateMapperExt(this.material.modid, "leaves", null, net.minecraft.block.BlockLeaves.CHECK_DECAY);
		mapper.setVariants("type", this.material.name);
		Renders.registerCompactModel(mapper, this.blocks[2], null);
		Renders.registerCompactModel(mapper, this.blocks[3], null);
		Game.registerBiomeColorMultiplier(this.blocks[2]);
		Game.registerBiomeColorMultiplier(this.blocks[3]);
	}
	
	@Override
	public final String getRegisteredName()
	{
		return this.material.name;
	}
	
	protected boolean getGameteResult(int idx, Random random, boolean a, boolean b)
	{
		return random.nextBoolean() ? a : b;
	}
	
	@Override
	public void initInfo(BlockLogNatural logNatural, BlockLogArtificial logArtificial, BlockLeaves leaves, BlockLeavesCore leavesCore)
	{
		this.blocks = new Block[] { logNatural, logArtificial, leaves, leavesCore };
		for (TreeGenAbstract generator : this.generators)
		{
			generator.initalizeBlock(logNatural, leaves, leavesCore);
		}
	}
	
	@Override
	public boolean tickLogUpdate()
	{
		return false;
	}
	
	@Override
	public void updateLog(World world, BlockPos pos, Random rand, boolean isArt)
	{
		
	}
	
	@Override
	public void updateLeaves(World world, BlockPos pos, Random rand, boolean checkDency)
	{
		updateAndResult(world, pos, rand, checkDency);
	}
	
	/**
	 * Doing leaves decay, return <code>true</code> if leaves is dead.
	 * 
	 * @param world
	 * @param pos
	 * @param rand
	 * @param checkDency
	 * @return
	 */
	protected boolean updateAndResult(World world, BlockPos pos, Random rand, boolean checkDency)
	{
		if (checkDency)
		{
			if (world.isAreaLoaded(pos, this.leavesCheckRange))
			{
				if (shouldLeavesDency(world, pos))
				{
					return checkDecayLeaves(world, pos, this.leavesCheckRange);
				}
			}
			return false;
		}
		else
		{
			if (!canLeaveGrowNearby(world, pos))
			{
				beginLeavesDecay(world, pos);
			}
			return false;
		}
	}
	
	protected boolean isNaturalLog(Block block)
	{
		return block == this.blocks[0];
	}
	
	protected boolean canLeaveGrowNearby(World world, BlockPos pos)
	{
		return W.isBlockNearby(world, pos, this.blocks[0], false);
	}
	
	protected boolean shouldLeavesDency(World world, BlockPos pos)
	{
		return world.getBlockState(pos).getValue(CHECK_DECAY);
	}
	
	protected boolean checkDecayLeaves(World world, BlockPos pos, final int searchRadius)
	{
		final int range = 2 * searchRadius + 1;
		int[][][] checkBuffer = new int[range][range][range];
		MutableBlockPos pos1 = new MutableBlockPos();
		IBlockState state;
		
		final int sx = searchRadius;
		for (int i = -sx; i <= sx; ++i)
		{
			final int sy = searchRadius - Math.abs(i);
			for (int j = -sy; j <= sy; ++j)
			{
				final int sz = sy - Math.abs(j);
				for (int k = -sz; k <= sz; ++k)
				{
					state = world.getBlockState(pos1.setPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k));
					Block block = state.getBlock();
					checkBuffer[i + searchRadius][j + searchRadius][k + searchRadius] = isNaturalLog(block) ? 0 : !isLeaves(block) ? -1 : -2;
				}
			}
		}
		
		for (int pass = 1; pass <= searchRadius; ++pass)
		{
			for (int i = -sx; i <= sx; ++i)
			{
				final int sy = searchRadius - Math.abs(i);
				for (int j = -sy; j <= sy; ++j)
				{
					final int sz = sy - Math.abs(j);
					for (int k = -sz; k <= sz; ++k)
					{
						state = world.getBlockState(pos1.setPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k));
						if (checkBuffer[i + searchRadius][j + searchRadius][k + searchRadius] == pass - 1)
						{
							if (i + searchRadius >= 1 && checkBuffer[i + searchRadius - 1][j + searchRadius][k + searchRadius] == -2)
							{
								checkBuffer[i + searchRadius - 1][j + searchRadius][k + searchRadius] = pass;
							}
							if (i + 1 <= searchRadius && checkBuffer[i + searchRadius + 1][j + searchRadius][k + searchRadius] == -2)
							{
								checkBuffer[i + searchRadius + 1][j + searchRadius][k + searchRadius] = pass;
							}
							if (j + searchRadius >= 1 && checkBuffer[i + searchRadius][j + searchRadius - 1][k + searchRadius] == -2)
							{
								checkBuffer[i + searchRadius][j + searchRadius - 1][k + searchRadius] = pass;
							}
							if (j + 1 <= searchRadius && checkBuffer[i + searchRadius][j + searchRadius + 1][k + searchRadius] == -2)
							{
								checkBuffer[i + searchRadius][j + searchRadius + 1][k + searchRadius] = pass;
							}
							if (k + searchRadius >= 1 && checkBuffer[i + searchRadius][j + searchRadius][k + searchRadius - 1] == -2)
							{
								checkBuffer[i + searchRadius][j + searchRadius][k + searchRadius - 1] = pass;
							}
							if (k + 1 <= searchRadius && checkBuffer[i + searchRadius][j + searchRadius][k + searchRadius + 1] == -2)
							{
								checkBuffer[i + searchRadius][j + searchRadius][k + searchRadius + 1] = pass;
							}
						}
					}
				}
			}
		}
		
		if (checkBuffer[searchRadius][searchRadius][searchRadius] < 0)
		{
			onLeavesDead(world, pos);
			return true;
		}
		else
		{
			stopLeavesDency(world, pos);
			return false;
		}
	}
	
	protected boolean isLeaves(World world, BlockPos pos)
	{
		return isLeaves(world.getBlockState(pos).getBlock());
	}
	
	protected boolean isLeaves(Block block)
	{
		return block == this.blocks[2] || block == this.blocks[3];
	}
	
	protected void beginLeavesDency(int length, World world, BlockPos pos)
	{
		BlockPos pos2;
		IBlockState state;
		for (int i = -length; i <= length; ++i)
		{
			for (int j = -length; j <= length; ++j)
			{
				for (int k = -length; k <= length; ++k)
				{
					(state = world.getBlockState(pos2 = pos.add(i, j, k))).getBlock().beginLeavesDecay(state, world, pos2);
				}
			}
		}
	}
	
	protected void onLeavesDead(World world, BlockPos pos)
	{
		if (Config.droppingWhenDecay)
		{
			W.spawnDropsInWorld(world, pos, getLeavesDrops(world, pos, world.getBlockState(pos), 0, false, new ArrayList()));
		}
		world.setBlockToAir(pos);
	}
	
	@Override
	public void breakLog(World world, BlockPos pos, IBlockState state, boolean isArt)
	{
		
	}
	
	@Override
	public void breakLeaves(World world, BlockPos pos, IBlockState state)
	{
		beginLeavesDency(this.leavesCheckRange, world, pos);
	}
	
	@Override
	public void beginLeavesDecay(World world, BlockPos pos)
	{
		W.switchProp(world, pos, CHECK_DECAY, true, 2);
	}
	
	public void stopLeavesDency(World world, BlockPos pos)
	{
		W.switchProp(world, pos, CHECK_DECAY, false, 2);
	}
	
	@Override
	public boolean onLogRightClick(EntityPlayer player, World world, BlockPos pos, Direction side, float xPos, float yPos, float zPos, boolean isArt)
	{
		return false;
	}
	
	@Override
	public ActionResult<Float> onToolClickLog(EntityPlayer player, EnumToolType tool, int level, ItemStack stack, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ, boolean isArt)
	{
		return IToolableTile.DEFAULT_RESULT;
	}
	
	@Override
	public ActionResult<Float> onToolClickLeaves(EntityPlayer player, EnumToolType tool, int level, ItemStack stack, World world, BlockPos pos, Direction side, float hitX, float hitY, float hitZ)
	{
		return IToolableTile.DEFAULT_RESULT;
	}
	
	@Override
	public List<ItemStack> getLogOtherDrop(World world, BlockPos pos, List<ItemStack> list)
	{
		return list;
	}
	
	@Override
	public List<ItemStack> getLeavesDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune, boolean silkTouching, List<ItemStack> list)
	{
		if (L.nextInt(8) == 0)
		{
			list.add(ItemMulti.createStack(this.material, MC.branch));
		}
		if (L.nextInt(7) == 0)
		{
			list.add(ItemStack.copyItemStack((this.isBroadLeaf ? LEAVES_APPLIER1 : LEAVES_APPLIER2).apply()));
		}
		return list;
	}
	
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
	}
	
	@Override
	public int onSaplingUpdate(ISaplingAccess access)
	{
		return 1;
	}
	
	@Override
	public int getGrowAge(ISaplingAccess access)
	{
		return 80;
	}
	
	protected void generateTreeLeaves(World world, BlockPos pos, int meta, float generateCoreLeavesChance, BioData data)
	{
		meta &= 0x7;
		int state = worldGenerationFlag ? 2 : 3;
		if (world.rand.nextFloat() <= generateCoreLeavesChance)
		{
			W.setBlock(world, pos, this.blocks[3], meta, state);
			W.setTileEntity(world, pos, new TECoreLeaves(data), !worldGenerationFlag);
		}
		else
		{
			W.setBlock(world, pos, this.blocks[2], meta, state);
		}
	}
	
	@Override
	public <T extends Block> T getBlock(BlockType type)
	{
		if (this.blocks == null)//When it is not initialized yet.
		{
			return null;
		}
		switch (type)
		{
		case LOG:
			return (T) this.blocks[0];
		case LOG_ART:
			return (T) this.blocks[1];
		case LEAVES:
			return (T) this.blocks[2];
		case LEAVES_CORE:
			return (T) this.blocks[3];
		default:
			return null;
		}
	}
}
