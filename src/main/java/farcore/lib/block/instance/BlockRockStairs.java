/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.block.instance;

/**
 * @author ueyudiud
 */
//public class BlockRockStairs extends BlockStairV1 implements IThermalCustomBehaviorBlock
//{
//	private final Mat material;
//	private final BlockRockOld parent;
//	private String localName;
//	/**
//	 * The meta of main block.
//	 */
//	private int meta;
//	/**
//	 * The group of rock slab.
//	 */
//	private final BlockRockStairs[] group;
//	public final PropertyRock property;
//
//	public BlockRockStairs(int id, BlockRockOld parent, BlockRockStairs[] group, String name, Mat material,
//			String localName)
//	{
//		super(name + ".stairs", Material.ROCK);
//		this.material = parent.material;
//		this.parent = parent;
//		this.localName = localName;
//		this.meta = id;
//		this.group = group;
//		this.property = parent.property;
//		setHardness(this.property.hardness * 0.8F);
//		setResistance(this.property.explosionResistance * 0.75F);
//		if(EnumRockType.values()[id].displayInTab)
//		{
//			setCreativeTab(CT.tabBuilding);
//		}
//		setTickRandomly(true);
//		setDefaultState(getDefaultState().withProperty(BlockRockOld.HEATED, false));
//	}
//
//	@Override
//	public void postInitalizedBlocks()
//	{
//		super.postInitalizedBlocks();
//		LanguageManager.registerLocal(getTranslateNameForItemStack(0), this.localName + " Stairs");
//	}
//
//	@Override
//	@SideOnly(Side.CLIENT)
//	public void registerRender()
//	{
//		super.registerRender();
//		StateMapperExt mapper = new StateMapperStiars(this.material.modid, "rock/stairs/" + this.material.name, null, BlockRockOld.HEATED);
//		mapper.setVariants("type", EnumRockType.values()[this.meta].getName());
//		Renders.registerCompactModel(mapper, this, 1);
//	}
//
//	@Override
//	protected BlockStateContainer createBlockState()
//	{
//		return new BlockStateContainer(this, FACING, HALF, SHAPE, BlockRockOld.HEATED);
//	}
//
//	@Override
//	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
//	{
//		return true;
//	}
//
//	@Override
//	public String getHarvestTool(IBlockState state)
//	{
//		return "pickaxe";
//	}
//
//	@Override
//	public boolean isToolEffective(String type, IBlockState state)
//	{
//		return getHarvestTool(state).equals(type);
//	}
//
//	@Override
//	public int getHarvestLevel(IBlockState state)
//	{
//		EnumRockType type = EnumRockType.values()[this.meta];
//		switch (type)
//		{
//		case cobble_art:
//			return 1;
//		case cobble :
//		case mossy :
//			return this.property.harvestLevel / 2;
//		default:
//			return this.property.harvestLevel;
//		}
//	}
//
//	@Override
//	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
//	{
//		return EnumRockType.values()[this.meta].burnable;
//	}
//
//	@Override
//	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
//	{
//		return isFlammable(world, pos, face) ? 40 : 0;
//	}
//
//	@Override
//	public boolean onBurn(World world, BlockPos pos, float burnHardness, Direction direction)
//	{
//		if(isFlammable(world, pos, direction.of()))
//		{
//			Worlds.setBlock(world, pos, this.group[EnumRockType.values()[this.meta].noMossy],
//					Worlds.getBlockMeta(world, pos), 3);
//			return true;
//		}
//		return false;
//	}
//
//	@Override
//	public boolean onBurningTick(World world, BlockPos pos, Random rand, Direction fireSourceDir, IBlockState fireState)
//	{
//		return false;
//	}
//
//	@Override
//	public double getThermalConduct(World world, BlockPos pos)
//	{
//		return this.material.thermalConductivity;
//	}
//
//	@Override
//	public int getFireEncouragement(World world, BlockPos pos)
//	{
//		return 0;
//	}
//}