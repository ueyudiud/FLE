package fle.api.recipe;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface AbstractRecipe
{
	public String recipeName();
	
	public boolean requireCraftingTime() default true;
	
	@Target(value = {ElementType.METHOD})
	@Retention(value = RetentionPolicy.RUNTIME)
	@Documented
	public static @interface GetRecipeName
	{
		
	}
	
	@Target(value = {ElementType.METHOD})
	@Retention(value = RetentionPolicy.RUNTIME)
	@Documented
	public static @interface RecipeMatch
	{
		
	}
	

	@Target(value = {ElementType.METHOD})
	@Retention(value = RetentionPolicy.RUNTIME)
	@Documented
	public static @interface OnInput
	{
		
	}
	
	@Target(value = {ElementType.METHOD})
	@Retention(value = RetentionPolicy.RUNTIME)
	@Documented
	public static @interface OnOutput
	{
		
	}
	
	@Target(value = {ElementType.METHOD})
	@Retention(value = RetentionPolicy.RUNTIME)
	@Documented
	public static @interface GetRecipeMap
	{
		
	}
}