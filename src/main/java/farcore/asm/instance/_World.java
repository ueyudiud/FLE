package farcore.asm.instance;

import static com.sun.org.apache.bcel.internal.Constants.ALOAD;
import static com.sun.org.apache.bcel.internal.Constants.INVOKESTATIC;

import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import farcore.asm.ClassTransformer;

public class _World extends ClassTransformer
{
	public _World()
	{
		super("net.minecraft.world.World", "ahb");
		List<OperationInfo> infos = Arrays.asList(
				new OperationInfo(OperationType.Remove, 17, 3882),
				new OperationInfo(OperationType.Replace, 18, 3882,
						new VarInsnNode(ALOAD, 2),
						new VarInsnNode(ALOAD, 0),
						new VarInsnNode(ALOAD, 1),
						new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "isRainingAtBiome", "(Lnet/minecraft/world/biome/Biome;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", false)));
		mcpMethods.put("isRainingAt|(Lnet/minecraft/util/math/BlockPos;)Z", infos);
		infos = Arrays.asList(
				new OperationInfo(OperationType.Replace, 5, 2984,
						new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Lnet/minecraft/world/World;Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/util/math/BlockPos;)Z", false)),
				new OperationInfo(OperationType.Replace, 6, 2987,
						new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Lnet/minecraft/world/World;Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/util/math/BlockPos;)Z", false)));
		mcpMethods.put("checkLight|(Lnet/minecraft/util/math/BlockPos;)Z", infos);
		infos = Arrays.asList(
				new OperationInfo(OperationType.Replace, 9, 500,
						new MethodInsnNode(INVOKESTATIC, "farcore/asm/LightFix", "checkLightFor", "(Lnet/minecraft/world/World;Lnet/minecraft/world/EnumSkyBlock;Lnet/minecraft/util/math/BlockPos;)Z", false)));
		mcpMethods.put("markBlocksDirtyVertical|(IIII)V", infos);
	}
}