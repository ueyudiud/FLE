function apply(stack) {
	return stack.nbt == null || stack.nbt.tank == null || stack.nbt.tank.FluidName == null ?
		"empty" : stack.nbt.tank.FluidName;
}