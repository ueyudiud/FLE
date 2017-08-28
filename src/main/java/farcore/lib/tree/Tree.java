/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.tree;

import static farcore.FarCore.worldGenerationFlag;
import static net.minecraft.block.BlockLeaves.CHECK_DECAY;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import farcore.data.EnumItem;
import farcore.lib.bio.FamilyTemplate;
import farcore.lib.bio.GeneticMaterial;
import farcore.lib.bio.IFamily;
import farcore.lib.block.wood.BlockLeaves;
import farcore.lib.block.wood.BlockLeavesCore;
import farcore.lib.block.wood.BlockLogArtificial;
import farcore.lib.block.wood.BlockLogNatural;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyWood;
import farcore.lib.tile.instance.TECoreLeaves;
import nebula.base.function.Appliable;
import nebula.client.model.StateMapperExt;
import nebula.client.util.IRenderRegister;
import nebula.client.util.Renders;
import nebula.common.data.Misc;
import nebula.common.item.ItemSubBehavior;
import nebula.common.tile.IToolableTile;
import nebula.common.tool.EnumToolType;
import nebula.common.util.A;
import nebula.common.util.Direction;
import nebula.common.util.Game;
import nebula.common.util.L;
import nebula.common.util.Worlds;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public abstract class Tree extends PropertyWood implements ITree, IRenderRegister
{
	public static final Appliable.AppliableCached<ItemStack> LEAVES_APPLIER1 =
			Appliable.wrapCached(()-> EnumItem.crop_related.item != null ? ((ItemSubBehavior) EnumItem.crop_related.item).getSubItem("broadleaf") : null);
	public static final Appliable.AppliableCached<ItemStack> LEAVES_APPLIER2 =
			Appliable.wrapCached(()-> EnumItem.crop_related.item != null ? ((ItemSubBehavior) EnumItem.crop_related.item).getSubItem("coniferous") : null);
	
	protected FamilyTemplate<Tree, ISaplingAccess> family;
	/** logNative logArtifical leaves leavesCore */
	protected Block[] blocks;
	protected long[] nativeTreeValue = Misc.LONGS_EMPTY;
	protected int[] nativeTreeDatas = Misc.INTS_EMPTY;
	protected int leavesCheckRange = 4;
	protected boolean isBroadLeaf = true;
	
	public Tree(Mat material, float hardness, float ashcontent, float burnHeat)
	{
		super(material, 1, 1.5F + hardness / 4F, 0.4F + hardness / 8F, ashcontent, burnHeat);
	}
	public Tree(Mat material, int harvestLevel, float hardness, float explosionResistance, float ashcontent,
			float burnHeat)
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
		mapper = new StateMapperExt(this.material.modid, "leaves", null, net.minecraft.block.BlockLeaves.CHECK_DECAY);
		mapper.setVariants("type", this.material.name);
		Renders.registerCompactModel(mapper, this.blocks[2], null);
		Renders.registerCompactModel(mapper, this.blocks[3], null);
		Game.registerBiomeColorMultiplier(this.blocks[2]);
		Game.registerBiomeColorMultiplier(this.blocks[3]);
	}
	
	public Tree setDefFamily()
	{
		this.family = new FamilyTemplate<>(this);
		return this;
	}
	
	public Tree setFamily(FamilyTemplate<Tree, ISaplingAccess> family)
	{
		this.family = family;
		family.addSpecies(this);
		return this;
	}
	
	@Override
	public final String getRegisteredName()
	{
		return this.material.name;
	}
	
	@Override
	public IFamily<ISaplingAccess> getFamily()
	{
		return this.family;
	}
	
	@Override
	public GeneticMaterial createNativeGeneticMaterial()
	{
		return new GeneticMaterial(this.family.getRegisteredName(), 0, this.nativeTreeValue.clone(), this.nativeTreeDatas.clone());
	}
	
	@Override
	public GeneticMaterial createGameteGeneticMaterial(ISaplingAccess biology, GeneticMaterial gm)
	{
		if ((gm.coders.length & 0x1) != 0) return null;
		Random random = biology.rng();
		long[] coders = A.createLongArray(gm.coders.length >> 1, idx-> {
			long a = gm.coders[idx << 1];
			long b = gm.coders[idx << 1 | 1];
			long result = 0;
			for (int i = 0; i < Long.SIZE; ++i)
			{
				long x = 1L << i;
				if (getGameteResult(idx << 6 | i, random, (a & x) != 0, (b & x) != 0))
				{
					result |= x;
				}
			}
			return result;
		});
		int[] datas = gm.nativeValues.clone();
		return new GeneticMaterial(this.family.getRegisteredName(), gm.generation + 1, coders, datas);
	}
	
	protected boolean getGameteResult(int idx, Random random, boolean a, boolean b)
	{
		return random.nextBoolean() ? a : b;
	}
	
	@Override
	public void expressTrait(ISaplingAccess biology, GeneticMaterial gm)
	{
		TreeInfo info = biology.info();
		info.height		+= gm.nativeValues[0];
		info.growth		+= gm.nativeValues[1];
		info.resistance	+= gm.nativeValues[2];
		info.vitality	+= gm.nativeValues[3];
	}
	
	@Override
	public void initInfo(BlockLogNatural logNatural, BlockLogArtificial logArtificial,
			BlockLeaves leaves, BlockLeavesCore leavesCore)
	{
		this.blocks = new Block[]{logNatural, logArtificial, leaves, leavesCore};
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
		if (checkDency)
		{
			if (world.isAreaLoaded(pos, this.leavesCheckRange))
			{
				if (shouldLeavesDency(world, pos))
				{
					checkDecayLeaves(world, pos, this.leavesCheckRange);
				}
			}
		}
		else
		{
			if (!canLeaveGrowNearby(world, pos))
			{
				beginLeavesDecay(world, pos);
			}
		}
	}
	
	protected boolean isNaturalLog(Block block)
	{
		return block == this.blocks[0];
	}
	
	protected boolean canLeaveGrowNearby(World world, BlockPos pos)
	{
		return Worlds.isBlockNearby(world, pos, this.blocks[0], false);
	}
	
	protected boolean shouldLeavesDency(World world, BlockPos pos)
	{
		return world.getBlockState(pos).getValue(CHECK_DECAY);
	}
	
	protected void checkDecayLeaves(World world, BlockPos pos, final int searchRadius)
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
		}
		else
		{
			stopLeavesDency(world, pos);
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
		for(int i = -length; i <= length; ++i)
		{
			for(int j = -length; j <= length; ++j)
			{
				for(int k = -length; k <= length; ++k)
				{
					(state = world.getBlockState(pos2 = pos.add(i, j, k))).getBlock().beginLeavesDecay(state, world, pos2);
				}
			}
		}
	}
	
	protected void onLeavesDead(World world, BlockPos pos)
	{
		Worlds.spawnDropsInWorld(world, pos, getLeavesDrops(world, pos, world.getBlockState(pos), 0, false, new ArrayList()));
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
		Worlds.switchProp(world, pos, CHECK_DECAY, true, 2);
	}
	
	public void stopLeavesDency(World world, BlockPos pos)
	{
		Worlds.switchProp(world, pos, CHECK_DECAY, false, 2);
	}
	
	@Override
	public boolean onLogRightClick(EntityPlayer player, World world, BlockPos pos, Direction side, float xPos,
			float yPos, float zPos, boolean isArt)
	{
		return false;
	}
	
	@Override
	public ActionResult<Float> onToolClickLog(EntityPlayer player, EnumToolType tool, ItemStack stack, World world,
			BlockPos pos, Direction side, float hitX, float hitY, float hitZ, boolean isArt)
	{
		return IToolableTile.DEFAULT_RESULT;
	}
	
	@Override
	public ActionResult<Float> onToolClickLeaves(EntityPlayer player, EnumToolType tool, ItemStack stack, World world,
			BlockPos pos, Direction side, float hitX, float hitY, float hitZ)
	{
		return IToolableTile.DEFAULT_RESULT;
	}
	
	@Override
	public List<ItemStack> getLogOtherDrop(World world, BlockPos pos, ArrayList list)
	{
		return list;
	}
	
	@Override
	public ArrayList<ItemStack> getLeavesDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune,
			boolean silkTouching, ArrayList list)
	{
		if (L.nextInt(8) == 0)
		{
			list.add(new ItemStack(EnumItem.branch.item, 1, this.material.id));
		}
		if (L.nextInt(7) == 0)
		{
			list.add(ItemStack.copyItemStack((this.isBroadLeaf ? LEAVES_APPLIER1 : LEAVES_APPLIER2).apply()));
		}
		return list;
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
	
	protected void generateTreeLeaves(World world, BlockPos pos, int meta, float generateCoreLeavesChance, TreeInfo info)
	{
		meta &= 0x7;
		int state = worldGenerationFlag ? 2 : 3;
		if(world.rand.nextFloat() <= generateCoreLeavesChance)
		{
			Worlds.setBlock(world, pos, this.blocks[3], meta, state);
			Worlds.setTileEntity(world, pos, new TECoreLeaves(this, info), !worldGenerationFlag);
		}
		else
		{
			Worlds.setBlock(world, pos, this.blocks[2], meta, state);
		}
	}
	
	@Override
	public <T extends Block> T getBlock(BlockType type)
	{
		switch (type)
		{
		case LOG :
			return (T) this.blocks[0];
		case LOG_ART :
			return (T) this.blocks[1];
		case LEAVES :
			return (T) this.blocks[2];
		case LEAVES_CORE :
			return (T) this.blocks[3];
		default:
			return null;
		}
	}
}