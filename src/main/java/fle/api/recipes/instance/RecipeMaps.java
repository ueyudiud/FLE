/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes.instance;

import static nebula.common.data.NBTLSs.RW_INT;
import static nebula.common.data.NBTLSs.RW_ITEMSTACK;
import static nebula.common.data.NBTLSs.RW_UNORDERED_ITEMSTACKS;

import fle.api.recipes.TemplateRecipeMap;
import fle.api.recipes.instance.interfaces.ICraftingRecipeHandler;
import fle.api.recipes.instance.interfaces.IPolishRecipeHandler;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipeMaps
{
	//NAME        : minecraft.crafting
	public static final
	VanillaCraftingHandler CRAFTING_VANILLA =
	new VanillaCraftingHandler();
	//NAME        : fle.crafting
	//NBT ENTRY   : time(int), output(ItemStack)
	//DISPLAYMENTS: materialRequired(@Nullable Material)
	//              matrix(AbstractStack[][]), toolEntries(Entry<AbstractStack, AbstractStack>[]),
	//              outputDefault(ItemStack)
	public static final
	TemplateRecipeMap<ICraftingRecipeHandler> CRAFTING =
	TemplateRecipeMap.<ICraftingRecipeHandler>builder("fle.crafting")
	.addCacheEntry("time", RW_INT)
	.addCacheEntry("output", RW_ITEMSTACK).build();
	//NAME        : fle.washing.bar.grizzly
	//NBT ENTRY   : time(int), outputs(ItemStack[])
	//DISPLAYMENTS: input(AbstractStack), duration(int), output(ItemStack[]), outputChances(int[][])
	public static final
	TemplateRecipeMap<ItemStack> WASHING_BARGRIZZLY =
	TemplateRecipeMap.<ItemStack>builder("fle.washing.bar.grizzly")
	.addCacheEntry("time", RW_INT)
	.addCacheEntry("outputs", RW_UNORDERED_ITEMSTACKS).build();
	//NAME        : fle.polishing
	//NBT ENTRY   : output(ItemStack)
	//DISPLAYMENTS: input(AbstractStack), map(char[]), output(ItemStack)
	public static final
	TemplateRecipeMap<IPolishRecipeHandler> POLISHING =
	TemplateRecipeMap.<IPolishRecipeHandler>builder("fle.polishing").addCacheEntry("output", RW_ITEMSTACK).build();
}