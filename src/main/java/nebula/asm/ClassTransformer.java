/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.asm;

/**
 * The class transformer.<p>
 * Deprecated now, it is already extract as JSON file.
 * @author ueyudiud
 *
 */
@Deprecated
public class ClassTransformer extends ClassTransformerBase
{
	@Override
	protected void initObfModifies()
	{
		//		create("net.minecraft.world.chunk.storage.AnvilChunkLoader")
		//		.lName("checkedReadChunkFromNBT__Async|(Laid;IILdr;)[Ljava/lang/Object;")
		//		.remove(131, 2)
		//		.replace(131, 5,
		//				new MethodInsnNode(INVOKESTATIC, "nebula/Nebula", "loadChunkData", "(Laid;Ldr;)Lasv;", false))
		//		.remove(152, 2)
		//		.replace(152, 5,
		//				new MethodInsnNode(INVOKESTATIC, "nebula/Nebula", "loadChunkData", "(Laid;Ldr;)Lasv;", false))
		//		.lPut()
		//		.lName("a|(Laid;Lasv;)V")
		//		.remove(182, 1)
		//		.replace(182, 5,
		//				new MethodInsnNode(INVOKESTATIC, "nebula/Nebula", "saveChunkData", "(Lasv;Laid;Ldr;)V", false))
		//		.lPut()
		//		.put();
		//		create("net.minecraft.world.chunk.storage.ExtendedBlockStorage")
		//		.lName("<init>|(IZ)V")
		//		.lPosition(41, 2)
		//		.lNode(new TypeInsnNode(NEW, "nebula/common/world/chunk/BlockStateContainerExt"))
		//		.lLabel(OpType.REPLACE)
		//		.lPosition(41, 4)
		//		.lNode(new MethodInsnNode(INVOKESPECIAL, "nebula/common/world/chunk/BlockStateContainerExt", "<init>", "()V", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPut()
		//		.put();
		//		create("net.minecraft.util.text.TextComponentTranslation")
		//		.lName("g|()V")
		//		.lPosition(51, 5)
		//		.lNode(new MethodInsnNode(INVOKESTATIC, "nebula/common/LanguageManager", "translateToLocalOfText", "(Ljava/lang/String;)Ljava/lang/String;", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPut()
		//		.put();
		//		create("net.minecraft.entity.player.EntityPlayer")
		//		.lName("<init>|(Laid;Lcom/mojang/authlib/GameProfile;)V")
		//		.lPosition(115, 2)
		//		.lNode(new TypeInsnNode(NEW, "nebula/common/foodstat/FoodStatExt"))
		//		.lLabel(OpType.REPLACE)
		//		.lPosition(115, 4)
		//		.lNode(new MethodInsnNode(INVOKESPECIAL, "nebula/common/foodstat/FoodStatExt", "<init>", "()V", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPut()
		//		.put();
		//		create("net.minecraftforge.client.model.ModelLoader")
		//		.lName("onRegisterAllBlocks|(Lbou;)V")
		//		.lPosition(1079, 0)
		//		.lNode(new VarInsnNode(ALOAD, 0),
		//				new MethodInsnNode(INVOKESTATIC, "nebula/client/ClientProxy", "onRegisterAllBlocks", "(Lbou;)V", false))
		//		.lLabel(OpType.INSERT)
		//		.lPut()
		//		.put();
		//		create("net.minecraft.world.chunk.Chunk")
		//		.lName("a|(Lcm;Laiu;)Laiq;")
		//		.lPosition(1202, 3)
		//		.lNode(new VarInsnNode(ALOAD, 1),
		//				new VarInsnNode(ALOAD, 2),
		//				new MethodInsnNode(INVOKESTATIC, "nebula/common/CommonOverride", "regetBiome", "(ILcm;Laiu;)Laiq;", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPut()
		//		.put();
		//		create("net.minecraft.client.renderer.EntityRenderer")
		//		.lName("e|()V")
		//		.lPosition(416, 2)
		//		.lNode(new FieldInsnNode(GETFIELD, "bnz", "j", "Ljava/util/Random;"),
		//				new VarInsnNode(ALOAD, 0),
		//				new FieldInsnNode(GETFIELD, "bnz", "N", "I"),
		//				new MethodInsnNode(INVOKESTATIC, "nebula/client/ClientOverride", "renderDropOnGround", "(Ljava/util/Random;I)V", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPut()
		//		.put();
		//		create("net.minecraft.client.renderer.RenderItem")
		//		.lName("a|(Lbyl;ILadz;)V")
		//		.lPosition(160, 4)
		//		.lLength(2)
		//		.lLabel(OpType.REMOVE)
		//		.lPosition(160, 8)
		//		.lNode(new VarInsnNode(ALOAD, 3),
		//				new MethodInsnNode(INVOKESTATIC, "nebula/client/ClientOverride", "renderItemModel", "(Lbyl;Lct;JLadz;)Ljava/util/List;", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPosition(163, 5)
		//		.lLength(2)
		//		.lLabel(OpType.REMOVE)
		//		.lPosition(163, 10)
		//		.lNode(new VarInsnNode(ALOAD, 3),
		//				new MethodInsnNode(INVOKESTATIC, "nebula/client/ClientOverride", "renderItemModel", "(Lbyl;Lct;JLadz;)Ljava/util/List;", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPut()
		//		.put();
		//		create("net.minecraft.client.gui.inventory.GuiContainer")
		//		.lName("a|(Ladz;IILjava/lang/String;)V")
		//		.lPosition(206, 19)
		//		.lNode(new MethodInsnNode(INVOKESTATIC, "nebula/client/ClientOverride", "renderCustomItemOverlayIntoGUI", "(Lbsu;Lbdl;Ladz;IILjava/lang/String;)V", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPut()
		//		.lName("a|(Lacc;)V")
		//		.lPosition(299, 9)
		//		.lNode(new MethodInsnNode(INVOKESTATIC, "nebula/client/ClientOverride", "renderCustomItemOverlayIntoGUI", "(Lbsu;Lbdl;Ladz;IILjava/lang/String;)V", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPut()
		//		.put();
		//		create("net.minecraft.world.World")
		//		.lName("B|(Lcm;)Z")
		//		.lPosition(3593, 18)
		//		.lNode(new VarInsnNode(ALOAD, 0),
		//				new VarInsnNode(ALOAD, 1),
		//				new MethodInsnNode(INVOKESTATIC, "nebula/common/CommonOverride", "isRainingAtBiome", "(Laiq;Laid;Lcm;)Z", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPut()
		//		.lName("w|(Lcm;)Z")
		//		.lPosition(2777, 5)
		//		.lNode(new MethodInsnNode(INVOKESTATIC, "nebula/client/light/LightFix", "checkLightFor", "(Laid;Laij;Lcm;)Z", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPosition(2780, 6)
		//		.lNode(new MethodInsnNode(INVOKESTATIC, "nebula/client/light/LightFix", "checkLightFor", "(Laid;Laij;Lcm;)Z", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPut()
		//		.lName("a|(IIII)V")
		//		.lPosition(438, 9)
		//		.lNode(new MethodInsnNode(INVOKESTATIC, "nebula/client/light/LightFix", "checkLightFor", "(Laid;Laij;Lcm;)Z", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPut()
		//		.lName("d|()V")
		//		.lPosition(2546, 2)
		//		.lNode(new VarInsnNode(ALOAD, 0))
		//		.lNode(new MethodInsnNode(INVOKESTATIC, "nebula/client/light/LightFix", "tickLightUpdate", "(Laid;)V", false))
		//		.lLabel(OpType.INSERT)
		//		.put();
	}
	
	@Override
	protected void initMcpModifies()
	{
		//		create("net.minecraft.world.chunk.storage.AnvilChunkLoader")
		//		.lName("checkedReadChunkFromNBT__Async|(Lnet/minecraft/world/World;IILnet/minecraft/nbt/NBTTagCompound;)[Ljava/lang/Object;")
		//		.remove(138, 2)
		//		.replace(138, 5,
		//				new MethodInsnNode(INVOKESTATIC, "nebula/Nebula", "loadChunkData", "(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/world/chunk/Chunk;", false))
		//		.remove(159, 2)
		//		.replace(159, 5,
		//				new MethodInsnNode(INVOKESTATIC, "nebula/Nebula", "loadChunkData", "(Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)Lnet/minecraft/world/chunk/Chunk;", false))
		//		.lPut()
		//		.lName("saveChunk|(Lnet/minecraft/world/World;Lnet/minecraft/world/chunk/Chunk;)V")
		//		.remove(182, 1)
		//		.replace(182, 5,
		//				new MethodInsnNode(INVOKESTATIC, "nebula/Nebula", "saveChunkData", "(Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/World;Lnet/minecraft/nbt/NBTTagCompound;)V", false))
		//		.lPut()
		//		.put();
		//		create("net.minecraft.world.chunk.storage.ExtendedBlockStorage")
		//		.lName("<init>|(IZ)V")
		//		.replace(29, 2,
		//				new TypeInsnNode(NEW, "nebula/common/world/chunk/BlockStateContainerExt"))
		//		.replace(29, 4,
		//				new MethodInsnNode(INVOKESPECIAL, "nebula/common/world/chunk/BlockStateContainerExt", "<init>", "()V", false))
		//		.lPut()
		//		.put();
		//		create("net.minecraft.util.text.TextComponentTranslation")
		//		.lName("ensureInitialized|()V")
		//		.replace(60, 5,
		//				new MethodInsnNode(INVOKESTATIC, "nebula/common/LanguageManager", "translateToLocalOfText", "(Ljava/lang/String;)Ljava/lang/String;", false))
		//		.lPut()
		//		.put();
		//		create("net.minecraft.entity.player.EntityPlayer")
		//		.lName("<init>|(Lnet/minecraft/world/World;Lcom/mojang/authlib/GameProfile;)V")
		//		.replace(119, 2,
		//				new TypeInsnNode(NEW, "nebula/common/foodstat/FoodStatExt"))
		//		.replace(119, 4,
		//				new MethodInsnNode(INVOKESPECIAL, "nebula/common/foodstat/FoodStatExt", "<init>", "()V", false))
		//		.lPut()
		//		.put();
		//		create("net.minecraftforge.client.model.ModelLoader")
		//		.lName("onRegisterAllBlocks|(Lnet/minecraft/client/renderer/BlockModelShapes;)V")
		//		.insert(1079, 0, false,
		//				new VarInsnNode(ALOAD, 0),
		//				new MethodInsnNode(INVOKESTATIC, "nebula/client/ClientProxy", "onRegisterAllBlocks", "(Lnet/minecraft/client/renderer/BlockModelShapes;)V", false))
		//		.lPut()
		//		.put();
		//		create("net.minecraft.world.chunk.Chunk")
		//		.lName("getBiome|(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/BiomeProvider;)Lnet/minecraft/world/biome/Biome;")
		//		.lPosition(1278, 3)
		//		.lNode(new VarInsnNode(ALOAD, 1),
		//				new VarInsnNode(ALOAD, 2),
		//				new MethodInsnNode(INVOKESTATIC, "nebula/common/CommonOverride", "regetBiome", "(ILnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/BiomeProvider;)Lnet/minecraft/world/biome/Biome;", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPut()
		//		.put();
		//		create("net.minecraft.client.renderer.EntityRenderer")
		//		.lName("updateRenderer|()V")
		//		.lPosition(325, 2)
		//		.lNode(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/EntityRenderer", "random", "Ljava/util/Random;"),
		//				new VarInsnNode(ALOAD, 0),
		//				new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/EntityRenderer", "rainSoundCounter", "I"),
		//				new MethodInsnNode(INVOKESTATIC, "nebula/client/ClientOverride", "renderDropOnGround", "(Ljava/util/Random;I)V", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPut()
		//		.put();
		//		create("net.minecraft.client.renderer.RenderItem")
		//		.lName("renderModel|(Lnet/minecraft/client/renderer/block/model/IBakedModel;ILnet/minecraft/item/ItemStack;)V")
		//		.lPosition(133, 4)
		//		.lLength(2)
		//		.lLabel(OpType.REMOVE)
		//		.lPosition(133, 8)
		//		.lNode(new VarInsnNode(ALOAD, 3),
		//				new MethodInsnNode(INVOKESTATIC, "nebula/client/ClientOverride", "renderItemModel", "(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/util/EnumFacing;JLnet/minecraft/item/ItemStack;)Ljava/util/List;", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPosition(136, 5)
		//		.lLength(2)
		//		.lLabel(OpType.REMOVE)
		//		.lPosition(136, 10)
		//		.lNode(new VarInsnNode(ALOAD, 3),
		//				new MethodInsnNode(INVOKESTATIC, "nebula/client/ClientOverride", "renderItemModel", "(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/util/EnumFacing;JLnet/minecraft/item/ItemStack;)Ljava/util/List;", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPut()
		//		.put();
		//		create("net.minecraft.client.gui.inventory.GuiContainer")
		//		.lName("drawItemStack|(Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
		//		.lPosition(206, 19)
		//		.lNode(new MethodInsnNode(INVOKESTATIC, "nebula/client/ClientOverride", "renderCustomItemOverlayIntoGUI", "(Lnet/minecraft/client/renderer/RenderItem;Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPut()
		//		.lName("drawSlot|(Lnet/minecraft/inventory/Slot;)V")
		//		.lPosition(299, 9)
		//		.lNode(new MethodInsnNode(INVOKESTATIC, "nebula/client/ClientOverride", "renderCustomItemOverlayIntoGUI", "(Lnet/minecraft/client/renderer/RenderItem;Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", false))
		//		.lLabel(OpType.REPLACE)
		//		.lPut()
		//		.put();
		//		create("net.minecraft.world.World")
		//		.lName("isRainingAt|(Lnet/minecraft/util/math/BlockPos;)Z")
		//		.replace(3887, 18,
		//				new VarInsnNode(ALOAD, 0),
		//				new VarInsnNode(ALOAD, 1),
		//				new MethodInsnNode(INVOKESTATIC, "nebula/common/CommonOverride", "isRainingAtBiome", "(Lnet/minecraft/world/biome/Biome;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", false))
		//		.lPut()
		//		.lName("checkLight|(Lnet/minecraft/util/math/BlockPos;)Z")
		//		.replace(2989, 5,
		//				new MethodInsnNode(INVOKESTATIC, "nebula/client/light/LightFix", "checkLightFor", "(Lnet/minecraft/world/World;Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/util/math/BlockPos;)Z", false))
		//		.replace(2992, 6,
		//				new MethodInsnNode(INVOKESTATIC, "nebula/client/light/LightFix", "checkLightFor", "(Lnet/minecraft/world/World;Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/util/math/BlockPos;)Z", false))
		//		.lPut()
		//		.lName("markBlocksDirtyVertical|(IIII)V")
		//		.replace(502, 9,
		//				new MethodInsnNode(INVOKESTATIC, "nebula/client/light/LightFix", "checkLightFor", "(Lnet/minecraft/world/World;Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/util/math/BlockPos;)Z", false))
		//		.lPut()
		//		.lName("tick|()V")
		//		.insert(2746, 2, false,
		//				new VarInsnNode(ALOAD, 0),
		//				new MethodInsnNode(INVOKESTATIC, "nebula/client/light/LightFix", "tickLightUpdate", "(Lnet/minecraft/world/World;)V", false))
		//		.lPut()
		//		.put();
	}
}