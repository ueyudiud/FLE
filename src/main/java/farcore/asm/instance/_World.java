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
	}
}