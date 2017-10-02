/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.block.instance;

/**
 * @author ueyudiud
 */
//public class BlockRockSlab extends BlockSlab
//implements IThermalCustomBehaviorBlock, IExtendedDataBlock
//{
//	public BlockRockSlab(BlockRock block)
//	{
//		super("rock.slab." + block.material.name, Material.ROCK);
//		setSoundType(SoundType.STONE);
//		setTickRandomly(true);
//	}
//
//	@Override
//	public void postInitalizedBlocks()
//	{
//		final Function<ItemStack, IBlockState> applier = stack -> getStateFromData(stack.getItemDamage());
//		for(EnumRockType type : EnumRockType.values())
//		{
//			ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
//			for(Mat material : MATERIAL.getAllowedValues())
//			{
//				ItemStack stack = createStackedBlock(material, type);
//				LanguageManager.registerLocal(getTranslateNameForItemStack(stack), String.format(type.local, material.localName) + " Slab");
//				builder.add(stack);
//				switch (type)
//				{
//				case resource :
//					OreDict.registerValid(MC.slabStone.getOreName(material), stack);
//					break;
//				case cobble :
//				case mossy :
//					OreDict.registerValid(MC.slabCobble.getOreName(material), stack);
//					break;
//				case brick :
//				case brick_compacted :
//				case brick_crushed :
//				case brick_mossy :
//					OreDict.registerValid(MC.slabBrick.getOreName(material), stack);
//					break;
//				default:
//					break;
//				}
//			}
//			List<ItemStack> list = builder.build();
//			switch (type)
//			{
//			case resource :
//				OreDictExt.registerOreFunction("stone", this.item,
//						((IDataChecker<IBlockState>) state -> state.getValue(TYPE) == type).from(applier), list);
//				break;
//			case cobble :
//				OreDictExt.registerOreFunction("cobble", this.item,
//						((IDataChecker<IBlockState>) state -> state.getValue(TYPE) == type).from(applier), list);
//				break;
//			default:
//				break;
//			}
//		}
//	}
//
//	@Override
//	@SideOnly(Side.CLIENT)
//	public void registerRender()
//	{
//		StateMapperExt mapper = new StateMapperExt(FarCore.ID, "rock/slab", MATERIAL, HEATED);
//		ModelLoader.setCustomStateMapper(this, mapper);
//		ModelLoader.setCustomMeshDefinition(this.item,
//				stack -> mapper.getModelResourceLocation(getStateFromData(stack.getItemDamage())));
//		for(Mat material : MATERIAL.getAllowedValues())
//		{
//			for(EnumRockType type : EnumRockType.values())
//			{
//				ModelLoader.registerItemVariants(this.item, mapper.getModelResourceLocation(EnumBlock.rock_slab.apply(material, type)));
//			}
//		}
//	}
//
//	@Override
//	protected BlockStateContainer createBlockState()
//	{
//		return new BlockStateContainer(this, MATERIAL, TYPE, HEATED, EnumSlabState.PROPERTY);
//	}
//
//	@Override
//	public void registerStateToRegister(IBlockStateRegister register)
//	{
//		register.registerStates(this, MATERIAL, TYPE, HEATED, EnumSlabState.PROPERTY);
//	}
//
//	@Override
//	@SideOnly(Side.CLIENT)
//	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
//	{
//		//Add at rock type.
//	}
//
//	@Override
//	public String getTranslateNameForItemStack(int metadata)
//	{
//		return getUnlocalizedName() + "@" + metadata;
//	}
//
//	@Override
//	protected String getLocalName() { return null; }
//
//	@Override
//	protected IBlockState initDefaultState(IBlockState state)
//	{
//		return state.withProperty(EnumSlabState.PROPERTY, EnumSlabState.DOWN).withProperty(MATERIAL, M.stone).withProperty(TYPE, EnumRockType.resource).withProperty(HEATED, false);
//	}
//
//	ItemStack createStackedBlock(Mat material, EnumRockType type)
//	{
//		//		ItemStack stack = new ItemStack(this.item, 1, material.id);
//		//		ItemStacks.getOrSetupNBT(stack, true).setShort("data", (short) type.ordinal());
//		//		return stack;
//		return new ItemStack(this.item, 1, type.ordinal() << 12 | MATERIAL.indexOf(material));
//	}
//
//	@Override
//	public int getMetaFromState(IBlockState state)
//	{
//		return 0;
//	}
//
//	@Override
//	public IBlockState getStateFromMeta(int meta)
//	{
//		return getStateFromData(meta);
//	}
//
//	//Meta part | ID part
//	//FFFF      | FFFF
//
//	@Override
//	public int getDataFromState(IBlockState state)
//	{
//		int data = 0;
//		data |= damageDropped(state);
//		data |= state.getValue(EnumSlabState.PROPERTY).ordinal() << 16;
//		if(state.getValue(HEATED))
//			data |= 0x100000;
//		return data;
//	}
//
//	@Override
//	public IBlockState getStateFromData(int meta)
//	{
//		try
//		{
//			IBlockState state = getDefaultState();
//			state = state.withProperty(MATERIAL, MATERIAL.getMaterialFromID(meta & 0xFFF, M.stone));
//			state = state.withProperty(TYPE, EnumRockType.values()[(meta >> 12) & 0xF]);
//			state = state.withProperty(EnumSlabState.PROPERTY, EnumSlabState.values()[(meta >> 16) & 0xF]);
//			if((meta & 0x100000) != 0) state = state.withProperty(HEATED, true);
//			return state;
//		}
//		catch (Exception exception)
//		{
//			Log.warn("The id : {} is invalid for rock, use default id replaced.", meta);
//			return getDefaultState();
//		}
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
//		Mat material = state.getValue(MATERIAL);
//		PropertyRock property = material.getProperty(MP.property_rock);
//		EnumRockType type = state.getValue(TYPE);
//		switch (type)
//		{
//		case cobble_art:
//			return 1;
//		case cobble :
//		case mossy :
//			return property.harvestLevel / 2;
//		default:
//			return property.harvestLevel;
//		}
//	}
//
//	@Override
//	public IBlockState getBlockPlaceState(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
//			float hitZ, ItemStack stackIn, EntityLivingBase placer)
//	{
//		IBlockState state = getDefaultState().withProperty(EnumSlabState.PROPERTY,
//				placer.isSneaking() ? (hitY > .5F ? EnumSlabState.UP : EnumSlabState.DOWN) :
//					EnumSlabState.values()[facing.getOpposite().ordinal()]);
//		int data = stackIn.getItemDamage();
//		Mat material = MATERIAL.getMaterialFromID(data & 0xFFF, M.stone);
//		state = state.withProperty(MATERIAL, material);
//		state = state.withProperty(TYPE, EnumRockType.values()[(data >> 12) & 0xF]);
//		return state;
//	}
//
//	@Override
//	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
//	{
//		float hardness = blockState.getValue(MATERIAL).getProperty(MP.property_rock).hardness * blockState.getValue(EnumSlabState.PROPERTY).dropMul * .5F;
//		switch (blockState.getValue(TYPE))
//		{
//		case cobble :
//			hardness *= .5F;
//			break;
//		case cobble_art :
//			hardness *= .1F;
//			break;
//		case brick :
//		case brick_compacted :
//		case brick_crushed :
//		case brick_mossy :
//			hardness *= 1.2F;
//			break;
//		default:
//			break;
//		}
//		return hardness;
//	}
//
//	@Override
//	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
//	{
//		return world.getBlockState(pos).getValue(MATERIAL).getProperty(MP.property_rock).explosionResistance;
//	}
//
//	@Override
//	public boolean onBurn(World world, BlockPos pos, float burnHardness, Direction direction)
//	{
//		IBlockState state = world.getBlockState(pos);
//		if(state.getValue(TYPE).burnable)
//		{
//			world.setBlockState(pos, state.withProperty(TYPE, EnumRockType.values()[state.getValue(TYPE).noMossy]), 3);
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
//		return world.getBlockState(pos).getValue(MATERIAL).thermalConductivity;
//	}
//
//	@Override
//	public int getFireEncouragement(World world, BlockPos pos)
//	{
//		return 0;
//	}
//
//	@Override
//	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
//	{
//		return world.getBlockState(pos).getValue(TYPE).burnable;
//	}
//
//	@Override
//	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
//	{
//		return isFlammable(world, pos, face) ? 40 : 0;
//	}
//
//	@Override
//	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
//	{
//		return 0;
//	}
//
//	@Override
//	public void onHeatChanged(World world, BlockPos pos, Direction direction, double amount)
//	{
//		Mat material = world.getBlockState(pos).getValue(MATERIAL);
//		if(amount >= material.getProperty(MP.property_rock).minTemperatureForExplosion * material.heatCapacity)
//		{
//			Worlds.switchProp(world, pos, HEATED, true, 2);
//			world.scheduleUpdate(pos, this, tickRate(world));
//		}
//	}
//
//	@Override
//	public int damageDropped(IBlockState state)
//	{
//		int data = 0;
//		data |= MATERIAL.indexOf(state.getValue(MATERIAL));
//		data |= state.getValue(TYPE).ordinal() << 12;
//		return data;
//	}
//}