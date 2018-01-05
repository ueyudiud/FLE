/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.block.terria;

import java.util.List;

import com.google.common.collect.ImmutableMap;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.EnumOreAmount;
import farcore.data.EnumRockType;
import farcore.data.M;
import farcore.data.Materials;
import farcore.data.SubTags;
import farcore.instances.MaterialTextureLoader;
import farcore.lib.block.instance.ItemOre;
import farcore.lib.material.Mat;
import farcore.lib.tile.instance.TEOre;
import nebula.client.blockstate.BlockStateWrapper;
import nebula.client.model.flexible.NebulaModelDeserializer;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.block.BlockSingleTE;
import nebula.common.tool.EnumToolType;
import nebula.common.util.ItemStacks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class BlockOre extends BlockSingleTE
{
	public static class OreStateWrapper extends BlockStateWrapper
	{
		public final Mat			ore;
		public final EnumOreAmount	amount;
		public final Mat			rock;
		public final EnumRockType	type;
		
		OreStateWrapper(IBlockState state, TEOre ore)
		{
			super(state);
			this.ore = ore.getOre();
			this.amount = ore.amount;
			this.rock = ore.rock;
			this.type = ore.rockType;
		}
		
		public OreStateWrapper(IBlockState state, Mat ore, EnumOreAmount amount, Mat rock, EnumRockType rockType)
		{
			super(state);
			this.ore = ore;
			this.amount = amount;
			this.rock = rock;
			this.type = rockType;
		}
		
		@Override
		protected BlockStateWrapper wrapState(IBlockState state)
		{
			return new OreStateWrapper(state, this.ore, this.amount, this.rock, this.type);
		}
	}
	
	public static final ThreadLocal<Object[]> ORE_ELEMENT_THREAD = new ThreadLocal<>();
	
	public BlockOre()
	{
		super(FarCore.ID, "ore", Materials.ORE);
		setTickRandomly(true);
		EnumBlock.ore.set(this);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		registerLocalized();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		ResourceLocation location = getRegistryName();
		NebulaModelLoader.registerBlockMetaGenerator(new ResourceLocation(FarCore.ID, "ore/ore"), state -> state instanceof OreStateWrapper ? ((OreStateWrapper) state).ore.name : null);
		NebulaModelLoader.registerBlockMetaGenerator(new ResourceLocation(FarCore.ID, "ore/rock"), state -> state instanceof OreStateWrapper ? ((OreStateWrapper) state).rock.name : null);
		NebulaModelLoader.registerBlockMetaGenerator(new ResourceLocation(FarCore.ID, "ore/rocktype"), state -> state instanceof OreStateWrapper ? ((OreStateWrapper) state).type.getName() : null);
		NebulaModelLoader.registerBlockMetaGenerator(new ResourceLocation(FarCore.ID, "ore/amount"), state -> state instanceof OreStateWrapper ? ((OreStateWrapper) state).amount.name() : null);
		NebulaModelLoader.registerItemMetaGenerator(new ResourceLocation(FarCore.ID, "ore/ore"), stack -> ItemOre.getOre(stack).name);
		NebulaModelLoader.registerItemMetaGenerator(new ResourceLocation(FarCore.ID, "ore/rock"), stack -> ItemOre.getRockMaterial(ItemStacks.getOrSetupNBT(stack, false)).name);
		NebulaModelLoader.registerItemMetaGenerator(new ResourceLocation(FarCore.ID, "ore/rocktype"), stack -> ItemOre.getRockType(ItemStacks.getOrSetupNBT(stack, false)).getName());
		NebulaModelLoader.registerItemMetaGenerator(new ResourceLocation(FarCore.ID, "ore/amount"), stack -> ItemOre.getAmount(ItemStacks.getOrSetupNBT(stack, false)).name());
		NebulaModelLoader.registerTextureSet(new ResourceLocation(FarCore.ID, "ore/ore"), () -> {
			ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
			for (Mat material : Mat.filt(SubTags.ORE))
			{
				for (EnumOreAmount amount : EnumOreAmount.values())
				{
					builder.put(material.name + "_" + amount.name(), new ResourceLocation(material.modid, "blocks/ore/" + amount.name() + "/" + material.name));
				}
			}
			return builder.build();
		});
		NebulaModelLoader.registerTextureSet(new ResourceLocation(FarCore.ID, "ore/rock"), () -> {
			ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
			for (Mat material : Mat.filt(SubTags.ROCK))
			{
				for (EnumRockType type : EnumRockType.values())
				{
					builder.put(material.name + "_" + type.name(), MaterialTextureLoader.getResource(material, type.condition, type.variant));
				}
			}
			return builder.build();
		});
		ModelResourceLocation location2 = new ModelResourceLocation(location, "inventory");
		ModelLoader.setCustomMeshDefinition(this.item, stack -> location2);
		NebulaModelLoader.registerModel(location, new ResourceLocation(FarCore.ID, "models/block1/ore"), NebulaModelDeserializer.BLOCK);
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if (FarCore.worldGenerationFlag && ORE_ELEMENT_THREAD.get() != null)
		{
			Object[] elements = ORE_ELEMENT_THREAD.get();
			worldIn.setTileEntity(pos, new TEOre((Mat) elements[0], (EnumOreAmount) elements[1], (Mat) elements[2], (EnumRockType) elements[3]));
		}
		else
		{
			super.onBlockAdded(worldIn, pos, state);
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TEOre();
	}
	
	private void registerLocalized()
	{
		LanguageManager.registerLocal(getTranslateNameForItemStack(OreDictionary.WILDCARD_VALUE), "Ore");
		for (Mat ore : Mat.filt(SubTags.ORE))
		{
			for (EnumOreAmount amount : EnumOreAmount.values())
			{
				NBTTagCompound nbt = ItemOre.setRock(ItemOre.setAmount(new NBTTagCompound(), amount), M.stone, EnumRockType.resource);
				ItemStack stack = new ItemStack(this, 1, ore.id);
				stack.setTagCompound(nbt);
				LanguageManager.registerLocal(getTranslateNameForItemStack(stack), String.format("%s %s Ore", nebula.common.util.Strings.upcaseFirst(amount.name()), ore.localName));
			}
		}
	}
	
	@Override
	protected Item createItemBlock()
	{
		return new ItemOre(this);
	}
	
	@Override
	public String getTranslateNameForItemStack(ItemStack stack)
	{
		if (stack.hasTagCompound())
		{
			NBTTagCompound nbt = stack.getTagCompound();
			return String.format("%s@%s.%s", getUnlocalizedName(), Mat.material(stack.getItemDamage()), ItemOre.getAmount(nbt).name());
		}
		else
			return getTranslateNameForItemStack(OreDictionary.WILDCARD_VALUE);
	}
	
	@Override
	public String getLocalizedName()
	{
		return LanguageManager.translateToLocal(getTranslateNameForItemStack(OreDictionary.WILDCARD_VALUE));
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TEOre) return new OreStateWrapper(state, (TEOre) tile);
		return state;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		// for(EnumOreAmount amount : EnumOreAmount.values())
		{
			EnumOreAmount amount = EnumOreAmount.normal;// Only provide normal
			// amount ore in
			// creative tab or it is
			// too many ore to
			// display.
			for (Mat ore : Mat.filt(SubTags.ORE))
			{
				// for(Mat rock : Mat.filt(SubTag.ROCK))
				{
					list.add(((ItemOre) item).createItemStack(1, ore, amount, M.stone, EnumRockType.resource));
				}
			}
		}
	}
	
	@Override
	public String getHarvestTool(IBlockState state)
	{
		return EnumToolType.PICKAXE.name;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	protected void addUnlocalizedInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList tooltip, boolean advanced)
	{
		super.addUnlocalizedInfomation(stack, player, tooltip, advanced);
		tooltip.addNotNull("info.material.chemical.formula." + Mat.material(stack.getItemDamage()).name);
	}
}
