/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes.instance;

import static nebula.common.data.NBTLSs.RW_FLOAT;
import static nebula.common.data.NBTLSs.RW_FLUIDSTACK;
import static nebula.common.data.NBTLSs.RW_INT;
import static nebula.common.data.NBTLSs.RW_ITEMSTACK;
import static nebula.common.data.NBTLSs.RW_UNORDERED_ITEMSTACKS;

import java.util.Map.Entry;

import farcore.lib.solid.SolidStack;
import fle.api.recipes.TemplateRecipeMap;
import fle.api.recipes.instance.interfaces.ICraftingRecipeHandler;
import fle.api.recipes.instance.interfaces.IPolishRecipeHandler;
import fle.api.recipes.instance.interfaces.IRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * The all recipe maps used by FLE.
 * <p>
 * 
 * For uses of recipe map see {@link fle.api.recipes.IRecipeMap}, to register
 * FLE recipes, you can register it here but it is not suggested unless you want
 * to achieve some special function. <b>For most of normal recipe, you can
 * register them in {@link fle.api.recipes.instance.RecipeAdder}.</b>
 * <p>
 * 
 * Here give some data format of each recipe. Here are explanation of how to use
 * those data:
 * <li>The <tt>NAME</tt> is registry name of recipe map, you can get recipe map
 * name by {@link fle.api.recipes.IRecipeMap#getRegisteredName()}
 * <li>The <tt>NBT</tt> or <tt>NBT ENTRY</tt> is the key-value entry of NBT tag
 * to save recipe cache. The type in bracket is the
 * {@link nebula.common.nbt.INBTReaderAndWritter} (RAW here after) of nbt tag
 * used, here are some instances :
 * <p>
 * <b>int</b> : saved with NBTTagInt, the RAW is
 * {@link nebula.common.data.NBTLSs#RW_INT}
 * <p>
 * <b>float</b> : saved with NBTTagFloat, the RAW is
 * {@link nebula.common.data.NBTLSs#RW_FLOAT}
 * <p>
 * <b>ItemStack</b> : saved with NBTTagCompound, the RAW is
 * {@link nebula.common.data.NBTLSs#RW_ITEMSTACK}
 * <p>
 * <b>FluidStack</b> : saved with NBTTagCompound, the RAW is
 * {@link nebula.common.data.NBTLSs#RW_FLUIDSTACK}
 * <p>
 * <b>SolidStack</b> : saved with NBTTagCompound, the RAW is
 * {@link farcore.lib.solid.SolidStack#RW}
 * <p>
 * <li>The <tt>DISPLAYMENTS</tt> is arguments store in recipe, this property
 * only exist on recipe map extended {@link fle.api.recipes.TemplateRecipeMap}
 * and the arguments can get by
 * {@link fle.api.recipes.TemplateRecipeMap.TemplateRecipe#get(int)} method.
 * <p>
 * The all displayments will <i>not</i> save in NBT, for the source recipe will
 * not save in recipe map, but you can get source recipe when you get recipe
 * cache from recipe map. So please handle recipe data as soon as your recipe
 * handler get recipe cache.
 * <p>
 * The type in bracket is type of that argument. For modder to get what argument
 * of recipe they needed.</li>
 * 
 * @author ueyudiud
 * @see fle.api.recipes.IRecipeMap
 * @see fle.api.recipes.instance.RecipeAdder
 */
public class RecipeMaps
{
	// NAME : minecraft.crafting
	public static final VanillaCraftingHandler CRAFTING_VANILLA = new VanillaCraftingHandler();
	// NAME : minecraft.smelting
	// NBT : (ItemStack)
	public static final VanillaSmeltingHandler SMELTING_VANILLA = new VanillaSmeltingHandler();
	// NAME : fle.crafting
	// NBT ENTRY : time(int), output(ItemStack)
	// DISPLAYMENTS: materialRequired(@Nullable Material)
	// matrix(AbstractStack[][]), toolEntries(Entry<AbstractStack,
	// AbstractStack>[]),
	// outputDefault(ItemStack)
	public static final TemplateRecipeMap<ICraftingRecipeHandler> CRAFTING = TemplateRecipeMap.<ICraftingRecipeHandler> builder("fle.crafting").addCacheEntry("time", RW_INT).addCacheEntry("output", RW_ITEMSTACK).build();
	// NAME : fle.washing.bar.grizzly
	// NBT ENTRY : time(int), outputs(ItemStack[])
	// DISPLAYMENTS: input(AbstractStack), duration(int), output(ItemStack[]),
	// outputChances(int[][])
	public static final TemplateRecipeMap<ItemStack> WASHING_BARGRIZZLY = TemplateRecipeMap.<ItemStack> builder("fle.washing.bar.grizzly").addCacheEntry("time", RW_INT).addCacheEntry("outputs", RW_UNORDERED_ITEMSTACKS).build();
	// NAME : fle.oil.mill.lever
	// NBT ENTRY : time(int), output1(ItemStack), output2(FluidStack)
	// DISPLAYMENTS: input(AbstractStack), duration(int), output1(@Nullable
	// ItemStack), output1Chances(int[]), output2(@Nullable FluidStack),
	// output2Range(long)
	public static final TemplateRecipeMap<ItemStack> LEVER_OIL_MILL = TemplateRecipeMap.<ItemStack> builder("fle.oil.mill.lever").addCacheEntry("time", RW_INT).addCacheEntry("output1", RW_ITEMSTACK).addCacheEntry("output2", RW_FLUIDSTACK).build();
	// NAME : fle.portable.woodwork
	public static final PortableWoodworkRecipeMap PORTABLE_WOODWORK = new PortableWoodworkRecipeMap();
	// NAME : fle.polishing
	// NBT ENTRY : output(ItemStack)
	// DISPLAYMENTS: input(AbstractStack), map(char[]), output(ItemStack)
	public static final TemplateRecipeMap<IPolishRecipeHandler> POLISHING = TemplateRecipeMap.<IPolishRecipeHandler> builder("fle.polishing").addCacheEntry("output", RW_ITEMSTACK).build();
	// NAME : fle.dring
	// NBT ENTRY : duration(int), multiplier(float), output(ItemStack)
	// DISPLAYMENTS: input(AbstractStack), duration(int), multiplier(float),
	// output(ItemStack)
	public static final TemplateRecipeMap<ItemStack> DRYING = TemplateRecipeMap.<ItemStack> builder("fle.dring").addCacheEntry("duration", RW_INT).addCacheEntry("multiplier", RW_FLOAT).addCacheEntry("output", RW_ITEMSTACK).build();
	// NAME : fle.stone.mill
	// NBT ENTRY : duration(int), output1(SolidStack), output2(FluidStack)
	// DISPLAYMENTS: input(AbstractStack), duration(int), output1(@Nullable
	// SolidStack), output2(@Nullable FluidStack)
	public static final TemplateRecipeMap<ItemStack> STONE_MILL = TemplateRecipeMap.<ItemStack> builder("fle.stone.mill").addCacheEntry("duration", RW_INT).addCacheEntry("output1", SolidStack.RW).addCacheEntry("output2", RW_FLUIDSTACK).build();
	// NAME : fle.sifter.simple
	// NBT ENTRY : duration(int), output1(SolidStack), output2(ItemStack)
	// DISPLAYMENTS: input(SolidStack), duration(int), output1(@Nullable
	// SolidStack), output2(ItemStack), chances(int[])
	public static final TemplateRecipeMap<SolidStack> SIMPLE_SIFTER = TemplateRecipeMap.<SolidStack> builder("fle.sifter.simple").addCacheEntry("duration", RW_INT).addCacheEntry("output1", SolidStack.RW).addCacheEntry("output2", RW_ITEMSTACK).build();
	// NAME : fle.dirt.mixture.input
	// DISPLAYMENTS: input(AbstractStack), output(List<MWCStack>)
	public static final MaterialPoolInputRecipeHandler DIRT_MIXTURE_INPUT = new MaterialPoolInputRecipeHandler("fle.dirt.mixture.input");
	// NAME : fle.dirt.mixture.output
	// DISPLAYMENTS: input(AbstractStack), output(List<MWCStack>)
	public static final MaterialPoolOutputRecipeHandler DIRT_MIXTURE_OUTPUT = new MaterialPoolOutputRecipeHandler("fle.dirt.mixture.output");
	// NAME : fle.ceramic.pot.add.to.mix
	// NBT ENTRY : used(int), output(FluidStack)
	// DISPLAYMENTS: input1(SolidStack), input2(FluidStack), output(FluidStack)
	public static final TemplateRecipeMap<Entry<SolidStack, FluidStack>> CERAMICPOT_ADD_TO_MIX = TemplateRecipeMap.<Entry<SolidStack, FluidStack>> builder("fle.ceramic.pot.add.to.mix").addCacheEntry("used", RW_INT).addCacheEntry("output", RW_FLUIDSTACK).build();
	// NAME : fle.ceramic.pot.base
	// INPUT TAGS : input1(ItemStack), input2(FluidStack), input3(ItemStack)
	// NBT ENTRY : temp(int), duration(int), output1(ItemStack),
	// output2(ItemStack)
	// DISPLAYMENTS: input1(AbstractStack), input2(FluidStack),
	// tool(SingleInputMatch), minTemperature(int), duration(int),
	// output(ItemStack)
	public static final String							TAG_CERAMICPOT_BASE_INPUT1	= "fle.ceramic.pot.base.input1", TAG_CERAMICPOT_BASE_INPUT2 = "fle.ceramic.pot.base.input2", TAG_CERAMICPOT_BASE_INPUT3 = "fle.ceramic.pot.base.input3";
	public static final TemplateRecipeMap<IRecipeInput>	CERAMICPOT_BASE				= TemplateRecipeMap.<IRecipeInput> builder("fle.ceramic.pot.base").addCacheEntry("temp", RW_INT).addCacheEntry("duration", RW_INT).addCacheEntry("output1", RW_ITEMSTACK).addCacheEntry("output2", RW_ITEMSTACK)
			.build();
}
