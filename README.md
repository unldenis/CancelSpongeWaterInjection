# CancelSpongeWaterInjection
Plugin that cancels the water absorption of the sponges in versions before 1.13.

## Wait... how?
As you know there is no event to cancel the absorption of the sponges in the water until 1.13 where spigot finally decides to implement it.

The simplest way is to find an alternative, such as not making the blocks in the water placeable, or to do a fork of spigot.

A minecraft server class, BlockSponge has a method 'e (World var1, BlockPosition var2, IBlockData var3)' which causes the absorption to be launched for two conditions.
The first that the sponge is not already wet and the second, calling another method 'e (World var1, BlockPosition var2)' which says if it is in the water.

```java
protected void e(World var1, BlockPosition var2, IBlockData var3) {
	if (!(Boolean)var3.get(WET) && this.e(var1, var2)) {
		var1.setTypeAndData(var2, var3.set(WET, true), 2);
		var1.triggerEffect(2001, var2, Block.getId(Blocks.WATER));
	}
}

private boolean e(World var1, BlockPosition var2) {
	//A lot of code...
}
```
The solution is to modify the method of the second condition, and to do so I use Byte Buddy, a code generation and manipulation library for creating and modifying Java classes during the runtime of a Java application and without the help of a compiler.

Without going into detail then, in the second I modify the body of the method and I always return false, this allows to never launch the water absorption.

Note that the plugin is tested for 1.8.8, in case of problems with later versions some adjustments may be needed.

## Preview

https://user-images.githubusercontent.com/80055679/187027830-ed8a003a-1aba-4c03-b3e1-21b3ede4fcc9.mp4

