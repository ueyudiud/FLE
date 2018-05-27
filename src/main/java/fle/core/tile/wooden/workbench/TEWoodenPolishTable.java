/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.tile.wooden.workbench;

import static fle.api.recipes.instance.RecipeMaps.POLISHING;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import farcore.data.MP;
import farcore.data.SubTags;
import farcore.lib.container.ItemSlotTool;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.tile.IHasToolSlotTile;
import fle.api.FLEAPI;
import fle.api.client.PolishingStateIconLoader;
import fle.api.recipes.TemplateRecipeMap.TemplateRecipeCache;
import fle.api.recipes.instance.PolishRecipe;
import fle.api.recipes.instance.interfaces.IPolishRecipeHandler;
import fle.api.recipes.instance.interfaces.IPolishableItem;
import nebula.client.gui.GuiBackground;
import nebula.client.gui.GuiContainer02TE;
import nebula.client.gui.GuiIconButton;
import nebula.client.gui.GuiIconButton.ButtonSize;
import nebula.common.gui.Container03TileEntity;
import nebula.common.gui.ISlotInitalizer;
import nebula.common.gui.ItemSlot;
import nebula.common.gui.ItemSlotOutput;
import nebula.common.stack.IS;
import nebula.common.tile.ITilePropertiesAndBehavior.ITB_BlockActived;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_BlockHardness;
import nebula.common.tile.ITilePropertiesAndBehavior.ITP_ExplosionResistance;
import nebula.common.tile.TE03StaticHasGui;
import nebula.common.util.Direction;
import nebula.common.util.NBTs;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@GuiBackground("fle:textures/gui/polish.png")
public class TEWoodenPolishTable extends TE03StaticHasGui<Container03TileEntity>
implements IPolishRecipeHandler, ITP_BlockHardness, ITP_ExplosionResistance, ITB_BlockActived, IHasToolSlotTile
{
	ItemStack	input;
	char[]		map	= "         ".toCharArray();
	
	public TEWoodenPolishTable()
	{
		super(2);
	}
	
	@Override
	public float getBlockHardness(IBlockState state)
	{
		return 1.5F;
	}
	
	@Override
	public float getExplosionResistance(Entity exploder, Explosion explosion)
	{
		return 2.0F;
	}
	
	@Override
	public EnumActionResult onBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, Direction side, float hitX, float hitY, float hitZ)
	{
		if (isServer())
		{
			openGUI(player, 0);
		}
		return EnumActionResult.SUCCESS;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		NBTs.setCharArray(compound, "map", this.map);
		NBTs.setItemStack(compound, "input", this.input, false);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.map = NBTs.getCharArrayOrDefault(compound, "map", this.map);
		this.input = NBTs.getItemStackOrDefault(compound, "input", null);
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		NBTs.setCharArray(nbt, "m", this.map);
		NBTs.setItemStack(nbt, "i", this.input, true);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		this.map = NBTs.getCharArrayOrDefault(nbt, "m", this.map);
		this.input = NBTs.getItemStackOrDefault(nbt, "i", this.input);
	}
	
	@Override
	public ActionResult<ItemStack> clickSlot(int id, ItemStack stack, EntityPlayer player)
	{
		if (stack == null || !(stack.getItem() instanceof IPolishableItem))
		{
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}
		int level = ((IPolishableItem) stack.getItem()).getPolishLevel(stack);
		if (level < 0)
		{
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		}
		if (this.input == null)
		{
			if (!PolishRecipe.isPolishable(this.stacks[0].getStackInContainer()))
			{
				return new ActionResult<>(EnumActionResult.FAIL, stack);
			}
			this.input = decrStackSize(0, 1);
		}
		if (PolishRecipe.getPolishLevel(this.input) <= level)
		{
			this.map[id] = ((IPolishableItem) stack.getItem()).getPolishResult(stack, this.map[id]);
			TemplateRecipeCache<IPolishRecipeHandler> cache = POLISHING.findRecipe(this);
			if (cache != null)
			{
				this.stacks[1].setStackInContainer(cache.<ItemStack> get(0));
			}
			else
			{
				this.stacks[1].setStackInContainer(null);
			}
			markDirty();
		}
		((IPolishableItem) stack.getItem()).onPolished(player, stack);
		syncToNearby();
		return new ActionResult<>(EnumActionResult.SUCCESS, IS.validate(stack));
	}
	
	public void clearPolishMap()
	{
		Arrays.fill(this.map, ' ');
		this.input = null;
		syncToNearby();
	}
	
	@Override
	public String getName()
	{
		return "inventory.polishing.table";
	}
	
	@Override
	public ItemStack getPolishingInput()
	{
		return this.input;
	}
	
	@Override
	public char[] getPolishingMatrix()
	{
		return this.map;
	}
	
	@Override
	public void initalizeContainer(Container03TileEntity container, ISlotInitalizer initalizer)
	{
		initalizer.addSlot("input", new ItemSlot(this.stacks[0], this, 0, 23, 35).setPredicate(PolishRecipe::isPolishable))
		.addLocation("player", false);
		initalizer.addSlot("output", new ItemSlotOutput(this.stacks[1], this, 1, 132, 35) {
			@Override
			public void onSlotChanged()
			{
				clearPolishMap();
				markDirty();
			}
		})
		.addLocation("player", false);
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 3; ++j)
			{
				initalizer.addSlot(new ItemSlotTool(this, container.player, i * 3 + j, 44 + j * 17, 18 + i * 17));
			}
		}
		initalizer.straegyPlayerBag().addLocation("input", false).addLocation("hand", false);
		initalizer.straegyPlayerHand().addLocation("input", false).addLocation("bag", false);
		syncToPlayer(container.player);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initalizeGui(GuiContainer02TE<?> gui, int x, int y, int w, int h, List<GuiButton> buttons)
	{
		buttons.add(new GuiIconButton(0, x + 102, y + 18, ButtonSize.Standard, FLEAPI.BUTTON_LOCATION, 48, 0, Minecraft.getMinecraft().getRenderItem())
		{
			@Override
			public void playPressSound(SoundHandler soundHandlerIn)
			{
				ItemStack stack = getPolishingInput();
				Mat material = ItemMulti.getMaterial(stack);
				if (material != Mat.VOID && SubTags.ROCK.test(material))
				{
					Block block = material.getProperty(MP.property_rock).block;
					soundHandlerIn.playSound(PositionedSoundRecord.getMusicRecord(block.getSoundType().getBreakSound()));
				}
				else
				{
					soundHandlerIn.playSound(PositionedSoundRecord.getMusicRecord(SoundEvents.BLOCK_STONE_BREAK));
				}
			}
		});
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonClicked(GuiContainer02TE<?> gui, int id)
	{
		super.onButtonClicked(gui, id);
		gui.sendGuiData(0, id, false);
	}
	
	@Override
	public void onDataRecieve(Container03TileEntity container, byte type, long value)
	{
		if (type == 0)
		{
			switch ((int) value)
			{
			case 0:
				clearPolishMap();
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawBackgroundSecondLayer(GuiContainer02TE<?> gui, int x, int y, float partialTicks, int mouseX, int mouseY)
	{
		if (this.input != null)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(44F, 18F, 0F);
			GL11.glScalef(3.125F, 3.125F, 1.0F);
			gui.drawItemStack(this.input, 0, 0, false, null, 50.0F);
			GL11.glPopMatrix();
			gui.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			gui.setZLevel(300.0F);
			for (int i = 0; i < 3; ++i)
			{
				for (int j = 0; j < 3; ++j)
				{
					gui.drawTexturedModalRect(44 + 17 * i, 18 + 17 * j, PolishingStateIconLoader.getIconFromChr(this.map[j * 3 + i]), 16, 16);
				}
			}
			gui.bindDefaultTexture();
		}
		gui.drawTexturedModalRect(44, 18, 176, 0, 50, 50);
	}
}
