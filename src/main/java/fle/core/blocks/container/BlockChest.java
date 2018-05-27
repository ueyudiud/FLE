/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.blocks.container;

import static farcore.FarCoreRegistry.registerBuiltInModelBlock;
import static farcore.FarCoreRegistry.registerTESR;
import static nebula.V.CF.GRAY;
import static nebula.V.CF.GREEN;
import static nebula.V.CF.WHITE;

import java.util.List;

import farcore.data.Materials;
import fle.core.FLE;
import fle.core.client.render.TESRChest1;
import fle.core.tile.chest.TEChest1;
import nebula.base.A;
import nebula.base.register.IRegister;
import nebula.client.util.Client;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.block.BlockTE;
import nebula.common.data.NBTLSs;
import nebula.common.util.ItemStacks;
import nebula.common.util.NBTs;
import nebula.common.util.Strings;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockChest extends BlockTE
{
	public BlockChest()
	{
		super("chest", Material.WOOD);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		for (TEChest1.ChestType type : TEChest1.ChestType.values())
		{
			LanguageManager.registerLocal(getTranslateNameForItemStack(0) + "." + type.name, "Small " + Strings.upcaseFirst(type.name) + " Chest");
		}
		LanguageManager.registerLocal("info.chest.content.title", "Content: ");
		LanguageManager.registerLocal("info.chest.content.extra", WHITE + "...");
		LanguageManager.registerLocal("inventory.fle.chest", "Chest");
	}
	
	@Override
	public String getTranslateNameForItemStack(ItemStack stack)
	{
		switch (stack.getItemDamage())
		{
		case 0:
		{
			TEChest1.ChestType type = TEChest1.getChestType(stack);
			return super.getTranslateNameForItemStack(stack.getItemDamage()) + "." + type.name;
		}
		default:
			return super.getTranslateNameForItemStack(stack);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		registerBuiltInModelBlock(this);
		registerTESR(TESRChest1.class);
		ModelLoader.registerItemVariants(this.item, A.transform(TEChest1.ChestType.values(), ResourceLocation.class, t -> new ResourceLocation(FLE.MODID, "chest/small_" + t.name)));
		ModelLoader.setCustomMeshDefinition(this.item, stack -> {
			switch (stack.getItemDamage())
			{
			case 0:
				return new ModelResourceLocation(FLE.MODID + ":chest/small_" + TEChest1.getChestType(stack).name, "inventory");
			default:
				return Client.MODEL_MISSING;
			}
		});
	}
	
	@Override
	public Material getMaterial(IBlockState state)
	{
		switch (this.property_TE.getMetaFromState(state))
		{
		case 0:
			return Materials.WOOD;
		default:
			return super.getMaterial(state);
		}
	}
	
	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register(0, "chest1", TEChest1.class);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	protected void addSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
	{
		for (TEChest1.ChestType type : TEChest1.ChestType.values())
		{
			ItemStack stack = new ItemStack(item, 1, 0);
			NBTs.setEnum(ItemStacks.getSubOrSetupNBT(stack, "chest", true), "material", type);
			list.add(stack);
		}
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return Container.calcRedstoneFromInventory((IInventory) worldIn.getTileEntity(pos));
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	protected void addUnlocalizedInfomation(ItemStack stack, EntityPlayer player, UnlocalizedList tooltip, boolean advanced)
	{
		super.addUnlocalizedInfomation(stack, player, tooltip, advanced);
		NBTTagCompound compound = ItemStacks.getSubOrSetupNBT(stack, "chest", false);
		List<ItemStack> stacks = NBTs.getUnorderedListFromOrdered(compound, "items", NBTLSs.ITEMSTACK_READER);
		int size = Math.min(stacks.size(), 4);
		if (size > 0)
		{
			tooltip.add("info.chest.content.title");
			for (int i = 0; i < size; ++i)
			{
				ItemStack stack1 = stacks.get(i);
				tooltip.addLocal(WHITE + stack1.getDisplayName() + GRAY + "x" + GREEN + stack1.stackSize);
			}
			if (stacks.size() > 4)
			{
				tooltip.add("info.chest.content.extra");
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(IBlockState state, World world, RayTraceResult target, ParticleManager manager)
	{
		switch (this.property_TE.getMetaFromState(state))
		{
		case 0:
			Client.addBlockHitEffect(world, world.rand, state, target.sideHit, target.getBlockPos(), manager, ((TEChest1) world.getTileEntity(target.getBlockPos())).getChestType().icon);
			break;
		default:
			break;
		}
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
	{
		IBlockState state = world.getBlockState(pos);
		switch (this.property_TE.getMetaFromState(state))
		{
		case 0:
			Client.addBlockDestroyEffects(world, pos, state, manager, ((TEChest1) world.getTileEntity(pos)).getChestType().icon);
			break;
		default:
			break;
		}
		return true;
	}
}
