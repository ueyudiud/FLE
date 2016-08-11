package farcore.asm.instance;

import static com.sun.org.apache.bcel.internal.Constants.ALOAD;
import static com.sun.org.apache.bcel.internal.Constants.GETFIELD;
import static com.sun.org.apache.bcel.internal.Constants.INVOKESTATIC;

import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import farcore.asm.ClassTransformer;

public class _EntityRenderer extends ClassTransformer
{
	public _EntityRenderer()
	{
		super("net.minecraft.client.renderer.EntityRenderer", "blt");
		List<OperationInfo> infos = Arrays.asList(
				new OperationInfo(OperationType.Replace, 2, 325,
						new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/EntityRenderer", "random", "Ljava/util/Random;"),
						new VarInsnNode(ALOAD, 0),
						new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/EntityRenderer", "rendererUpdateCount", "I"),
						new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderDropOnGround", "(Ljava/util/Random;I)V", false)));
		mcpMethods.put("updateRenderer|()V", infos);
	}
}