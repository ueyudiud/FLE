package farcore.asm.instance;

import static com.sun.org.apache.bcel.internal.Constants.ALOAD;
import static com.sun.org.apache.bcel.internal.Constants.INVOKESTATIC;

import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import farcore.asm.ClassTransformer;

public class _RenderItem extends ClassTransformer
{
	public _RenderItem()
	{
		super("net.minecraft.client.renderer.RenderItem", "?");
		List<OperationInfo> list = Arrays.asList(
				new OperationInfo(OperationType.Remove, 4, 133),
				new OperationInfo(OperationType.Remove, 5, 133),
				new OperationInfo(OperationType.Replace, 8, 133,
						new VarInsnNode(ALOAD, 3),
						new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderItemModel", "(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/util/EnumFacing;JLnet/minecraft/item/ItemStack;)Ljava/util/List;", false)),
				new OperationInfo(OperationType.Remove, 5, 136),
				new OperationInfo(OperationType.Remove, 6, 136),
				new OperationInfo(OperationType.Replace, 10, 136,
						new VarInsnNode(ALOAD, 3),
						new MethodInsnNode(INVOKESTATIC, "farcore/asm/ClientOverride", "renderItemModel", "(Lnet/minecraft/client/renderer/block/model/IBakedModel;Lnet/minecraft/util/EnumFacing;JLnet/minecraft/item/ItemStack;)Ljava/util/List;", false)));
		mcpMethods.put("renderModel|(Lnet/minecraft/client/renderer/block/model/IBakedModel;ILnet/minecraft/item/ItemStack;)V", list);
	}
}