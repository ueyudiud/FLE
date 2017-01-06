function apply(stack) {
	return stack.nbt != null && stack.nbt.tank != null && stack.nbt.tank.FluidName != null ?
		"fluid:" + stack.nbt.tank.FluidName : "empty";
}