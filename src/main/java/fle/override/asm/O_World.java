package fle.override.asm;

import static com.sun.org.apache.bcel.internal.Constants.*;

import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class O_World extends ClassTransformer
{
	public O_World()
	{
		super("net.minecraft.world.World", "ahb");

		List<OperationInfo> infos = Arrays.asList(
				new OperationInfo(OperationType.Remove, 1, 3242),
				new OperationInfo(OperationType.Remove, 2, 3242),
				new OperationInfo(OperationType.Remove, 3, 3242),
				new OperationInfo(OperationType.Remove, 4, 3242),
				new OperationInfo(OperationType.Replace, 5, 3242,
						new VarInsnNode(ALOAD, 0),
						new VarInsnNode(ILOAD, 1),
						new VarInsnNode(ILOAD, 2),
						new VarInsnNode(ILOAD, 3),
						new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "getBiomeBaseTemperature", "(Lnet/minecraft/world/World;III)F", false)));
		mcpMethods.put("canSnowAtBody|(IIIZ)Z", infos);
		infos = Arrays.asList(
				new OperationInfo(OperationType.Remove, 1, 2942),
				new OperationInfo(OperationType.Remove, 2, 2942),
				new OperationInfo(OperationType.Remove, 3, 2942),
				new OperationInfo(OperationType.Remove, 4, 2942),
				new OperationInfo(OperationType.Replace, 5, 2942,
						new VarInsnNode(ALOAD, 0),
						new VarInsnNode(ILOAD, 1),
						new VarInsnNode(ILOAD, 2),
						new VarInsnNode(ILOAD, 3),
						new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "getBiomeBaseTemperature", "(Lahb;III)F", false)));
		obfMethods.put("canSnowAtBody|(IIIZ)Z", infos);
	}
}