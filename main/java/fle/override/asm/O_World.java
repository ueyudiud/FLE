package fle.override.asm;

import static com.sun.org.apache.bcel.internal.Constants.*;

import java.util.Arrays;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class O_World extends ClassTransformer
{
	public O_World()
	{
		super("net.minecraft.world.World", "ahb");

		mcpMethods.put("canSnowAtBody|(IIIZ)Z", Arrays.asList(
				new OperationInfo(OperationType.Remove, 1, 3242),
				new OperationInfo(OperationType.Remove, 2, 3242),
				new OperationInfo(OperationType.Remove, 3, 3242),
				new OperationInfo(OperationType.Remove, 4, 3242),
				new OperationInfo(OperationType.Replace, 5, 3242,
						new VarInsnNode(ALOAD, 0),
						new VarInsnNode(ILOAD, 1),
						new VarInsnNode(ILOAD, 2),
						new VarInsnNode(ILOAD, 3),
						new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "getBiomeBaseTemperature", "(Lnet/minecraft/world/World;III)F", false))));
		obfMethods.put("canSnowAtBody|(IIIZ)Z", Arrays.asList(
				new OperationInfo(OperationType.Remove, 1, 3242),
				new OperationInfo(OperationType.Remove, 2, 3242),
				new OperationInfo(OperationType.Remove, 3, 3242),
				new OperationInfo(OperationType.Remove, 4, 3242),
				new OperationInfo(OperationType.Replace, 5, 3242,
						new VarInsnNode(ALOAD, 0),
						new VarInsnNode(ILOAD, 1),
						new VarInsnNode(ILOAD, 2),
						new VarInsnNode(ILOAD, 3),
						new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "getBiomeBaseTemperature", "(Lahb;III)F", false))));
	}
}