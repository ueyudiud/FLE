/**
 * This package provide solid this kind of object in world.<br>
 * Different from item, you can't take the solid with out tool
 * (Ex: sack), and also, they has some properties like fluid.<br>
 * The basic class is Solid. You need add<br>
 * <code>
 * Solid solid = new Solid("YourSolidName");
 * </code><br>
 * and solid will register to SolidRegistry.
 * Get the container can current solid, and register solid container
 * in SolidRegistry.<br>
 * SolidStack is similar to ItemStack.
 * @see flapi.solid.Solid
 * @see flapi.solid.SolidRegistry
 * @see flapi.solid.SolidContainerItem
 */
package flapi.solid;