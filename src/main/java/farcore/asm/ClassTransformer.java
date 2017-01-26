/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.NEW;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * The class transformer.
 * @author ueyudiud
 *
 */
public class ClassTransformer extends ClassTransformerBase
{
	@Override
	protected void initObfModifies()
	{
		create("net.minecraft.world.chunk.storage.AnvilChunkLoader")
		.lName("checkedReadChunkFromNBT__Async|(Lnet/minecraft/world/World;IILnet/minecraft/nbt/NBTTagCompound;)[Ljava/lang/Object;")
		.lPosition(138, 5)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/FarOverride", "loadChunkData", "(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/world/chunk/Chunk;", false))
		.lLabel(OpType.REPLACE)
		.lPosition(159, 4)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/FarOverride", "loadChunkData", "(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/world/chunk/Chunk;", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.lName("saveChunk|(Lnet/minecraft/world/World;Lnet/minecraft/world/chunk/Chunk;)V")
		.lPosition(182, 5)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/FarOverride", "saveChunkData", "(Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)V", false))
		.lPut()
		.put();
		create("net.minecraft.world.chunk.storage.ExtendedBlockStorage")
		.lName("<init>|(IZ)V")
		.lPosition(41, 2)
		.lNode(new TypeInsnNode(NEW, "farcore/lib/world/chunk/BlockStateContainerExt"))
		.lLabel(OpType.REPLACE)
		.lPosition(41, 4)
		.lNode(new MethodInsnNode(INVOKESPECIAL, "farcore/lib/world/chunk/BlockStateContainerExt", "<init>", "()V", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.put();
		create("net.minecraft.util.text.TextComponentTranslation")
		.lName("g|()V")
		.lPosition(51, 5)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/lib/util/LanguageManager", "translateToLocalOfText", "(Ljava/lang/String;)Ljava/lang/String;", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.put();
		create("net.minecraft.entity.player.EntityPlayer")
		.lName("<init>|(Laid;Lcom/mojang/authlib/GameProfile;)V")
		.lPosition(115, 2)
		.lNode(new TypeInsnNode(NEW, "farcore/lib/util/FoodStatExt"))
		.lLabel(OpType.REPLACE)
		.lPosition(115, 4)
		.lNode(new MethodInsnNode(INVOKESPECIAL, "farcore/lib/util/FoodStatExt", "<init>", "()V", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.put();
		create("net.minecraftforge.client.model.ModelLoader")
		.lName("onRegisterAllBlocks|(Lbou;)V")
		.lPosition(1079, 0)
		.lNode(new VarInsnNode(ALOAD, 0),
				new MethodInsnNode(INVOKESTATIC, "farcore/FarCoreSetup$ClientProxy", "onRegisterAllBlocks", "(Lbou;)V", false))
		.lLabel(OpType.INSERT)
		.lPut()
		.put();
		create("net.minecraft.world.chunk.Chunk")
		.lName("a|(Lcm;Laiu;)Laiq;")
		.lPosition(1202, 3)
		.lNode(new VarInsnNode(ALOAD, 1),
				new VarInsnNode(ALOAD, 2),
				new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "regetBiome", "(ILcm;Laiu;)Laiq;", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.put();
		create("net.minecraft.client.renderer.EntityRenderer")
		.lName("e|()V")
		.lPosition(416, 2)
		.lNode(new FieldInsnNode(GETFIELD, "bnz", "j", "Ljava/util/Random;"),
				new VarInsnNode(ALOAD, 0),
				new FieldInsnNode(GETFIELD, "bnz", "N", "I"),
				new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderDropOnGround", "(Ljava/util/Random;I)V", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.put();
		create("net.minecraft.client.renderer.RenderItem")
		.lName("a|(Lbyl;ILadz;)V")
		.lPosition(160, 4)
		.lLength(2)
		.lLabel(OpType.REMOVE)
		.lPosition(160, 8)
		.lNode(new VarInsnNode(ALOAD, 3),
				new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderItemModel", "(Lbyl;Lct;JLadz;)Ljava/util/List;", false))
		.lLabel(OpType.REPLACE)
		.lPosition(163, 5)
		.lLength(2)
		.lLabel(OpType.REMOVE)
		.lPosition(163, 10)
		.lNode(new VarInsnNode(ALOAD, 3),
				new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderItemModel", "(Lbyl;Lct;JLadz;)Ljava/util/List;", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.put();
		create("net.minecraft.client.gui.inventory.GuiContainer")
		.lName("a|(Ladz;IILjava/lang/String;)V")
		.lPosition(206, 19)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderCustomItemOverlayIntoGUI", "(Lbsu;Lbdl;Ladz;IILjava/lang/String;)V", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.lName("a|(Lacc;)V")
		.lPosition(299, 9)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderCustomItemOverlayIntoGUI", "(Lbsu;Lbdl;Ladz;IILjava/lang/String;)V", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.put();
		create("net.minecraft.world.World")
		.lName("B|(Lcm;)Z")
		.lPosition(3590, 18)
		.lNode(new VarInsnNode(ALOAD, 0),
				new VarInsnNode(ALOAD, 1),
				new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "isRainingAtBiome", "(Laiq;Laid;Lcm;)Z", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.lName("w|(Lcm;)Z")
		.lPosition(2774, 5)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Laid;Laij;Lcm;)Z", false))
		.lLabel(OpType.REPLACE)
		.lPosition(2777, 6)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Laid;Laij;Lcm;)Z", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.lName("a|(IIII)V")
		.lPosition(438, 9)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Laid;Laij;Lcm;)Z", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.lName("d|()V")
		.lPosition(2543, 2)
		.lNode(new VarInsnNode(ALOAD, 0))
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "tickLightUpdate", "(Laid;)V", false))
		.lLabel(OpType.INSERT)
		.put();
	}
	
	@Override
	protected void initMcpModifies()
	{
		create("net.minecraft.world.chunk.storage.AnvilChunkLoader")
		.lName("checkedReadChunkFromNBT__Async|(Lnet/minecraft/world/World;IILnet/minecraft/nbt/NBTTagCompound;)[Ljava/lang/Object;")
		.remove(138, 2)
		.replace(138, 5,
				new MethodInsnNode(INVOKESTATIC, "farcore/asm/FarOverride", "loadChunkData", "(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/world/chunk/Chunk;", false))
		.remove(159, 2)
		.replace(159, 5,
				new MethodInsnNode(INVOKESTATIC, "farcore/asm/FarOverride", "loadChunkData", "(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/world/chunk/Chunk;", false))
		.lPut()
		.lName("saveChunk|(Lnet/minecraft/world/World;Lnet/minecraft/world/chunk/Chunk;)V")
		.remove(182, 1)
		.replace(182, 5,
				new MethodInsnNode(INVOKESTATIC, "farcore/asm/FarOverride", "saveChunkData", "(Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)V", false))
		.lPut()
		.put();
		create("net.minecraft.world.chunk.storage.ExtendedBlockStorage")
		.lName("<init>|(IZ)V")
		.replace(29, 2,
				new TypeInsnNode(NEW, "farcore/lib/world/chunk/BlockStateContainerExt"))
		.replace(29, 4,
				new MethodInsnNode(INVOKESPECIAL, "farcore/lib/world/chunk/BlockStateContainerExt", "<init>", "()V", false))
		.lPut()
		.put();
		create("net.minecraft.util.text.TextComponentTranslation")
		.lName("ensureInitialized|()V")
		.replace(60, 5,
				new MethodInsnNode(INVOKESTATIC, "farcore/lib/util/LanguageManager", "translateToLocalOfText", "(Ljava/lang/String;)Ljava/lang/String;", false))
		.lPut()
		.put();
		create("net.minecraft.entity.player.EntityPlayer")
		.lName("<init>|(Lnet/minecraft/world/World;Lcom/mojang/authlib/GameProfile;)V")
		.replace(119, 2,
				new TypeInsnNode(NEW, "farcore/lib/util/FoodStatExt"))
		.replace(119, 4,
				new MethodInsnNode(INVOKESPECIAL, "farcore/lib/util/FoodStatExt", "<init>", "()V", false))
		.lPut()
		.put();
		create("net.minecraftforge.client.model.ModelLoader")
		.lName("onRegisterAllBlocks|(Lnet/minecraft/client/renderer/BlockModelShapes;)V")
		.insert(1079, 0, false,
				new VarInsnNode(ALOAD, 0),
				new MethodInsnNode(INVOKESTATIC, "farcore/FarCoreSetup$ClientProxy", "onRegisterAllBlocks", "(Lnet/minecraft/client/renderer/BlockModelShapes;)V", false))
		.lPut()
		.put();
		create("net.minecraft.world.chunk.Chunk")
		.lName("getBiome|(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/BiomeProvider;)Lnet/minecraft/world/biome/Biome;")
		.lPosition(1278, 3)
		.lNode(new VarInsnNode(ALOAD, 1),
				new VarInsnNode(ALOAD, 2),
				new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "regetBiome", "(ILnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/BiomeProvider;)Lnet/minecraft/world/biome/Biome;", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.put();
		create("net.minecraft.client.renderer.EntityRenderer")
		.lName("updateRenderer|()V")
		.lPosition(325, 2)
		.lNode(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/EntityRenderer", "random", "Ljava/util/Random;"),
				new VarInsnNode(ALOAD, 0),
				new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/EntityRenderer", "rainSoundCounter", "I"),
				new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderDropOnGround", "(Ljava/util/Random;I)V", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.put();
		create("net.minecraft.client.renderer.RenderItem")
		.lName("renderModel|(Lnet/minecraft/client/renderer/block/model/IBakedModel;ILnet/minecraft/item/ItemStack;)V")
		.lPosition(133, 4)
		.lLength(2)
		.lLabel(OpType.REMOVE)
		.lPosition(133, 8)
		.lNode(new VarInsnNode(ALOAD, 3),
				new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderItemModel", "(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/util/EnumFacing;JLnet/minecraft/item/ItemStack;)Ljava/util/List;", false))
		.lLabel(OpType.REPLACE)
		.lPosition(136, 5)
		.lLength(2)
		.lLabel(OpType.REMOVE)
		.lPosition(136, 10)
		.lNode(new VarInsnNode(ALOAD, 3),
				new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderItemModel", "(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/util/EnumFacing;JLnet/minecraft/item/ItemStack;)Ljava/util/List;", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.put();
		create("net.minecraft.client.gui.inventory.GuiContainer")
		.lName("drawItemStack|(Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
		.lPosition(206, 19)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderCustomItemOverlayIntoGUI", "(Lnet/minecraft/client/renderer/RenderItem;Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.lName("drawSlot|(Lnet/minecraft/inventory/Slot;)V")
		.lPosition(299, 9)
		.lNode(new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderCustomItemOverlayIntoGUI", "(Lnet/minecraft/client/renderer/RenderItem;Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", false))
		.lLabel(OpType.REPLACE)
		.lPut()
		.put();
		create("net.minecraft.world.World")
		.lName("isRainingAt|(Lnet/minecraft/util/math/BlockPos;)Z")
		.replace(3882, 18,
				new VarInsnNode(ALOAD, 0),
				new VarInsnNode(ALOAD, 1),
				new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "isRainingAtBiome", "(Lnet/minecraft/world/biome/Biome;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", false))
		.lPut()
		.lName("checkLight|(Lnet/minecraft/util/math/BlockPos;)Z")
		.replace(2984, 5,
				new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Lnet/minecraft/world/World;Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/util/math/BlockPos;)Z", false))
		.replace(2987, 6,
				new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Lnet/minecraft/world/World;Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/util/math/BlockPos;)Z", false))
		.lPut()
		.lName("markBlocksDirtyVertical|(IIII)V")
		.replace(500, 9,
				new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Lnet/minecraft/world/World;Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/util/math/BlockPos;)Z", false))
		.lPut()
		.lName("tick|()V")
		.insert(2741, 2, false,
				new VarInsnNode(ALOAD, 0),
				new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "tickLightUpdate", "(Lnet/minecraft/world/World;)V", false))
		.lPut()
		.put();
	}
}