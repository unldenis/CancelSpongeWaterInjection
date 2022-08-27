package com.github.unldenis.cswj.cswj;

import static net.bytebuddy.matcher.ElementMatchers.named;

import com.cryptomorin.xseries.ReflectionUtils;
import com.github.unldenis.cswj.cswj.util.UnsafeUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import org.bukkit.plugin.java.JavaPlugin;

public class CancelSpongeWaterInjector {

  private static final Class<?> BlockPositionClass = ReflectionUtils.getNMSClass("BlockPosition");
  private static final Class<?> BlockSpongeClass = ReflectionUtils.getNMSClass("BlockSponge");
  private static final Class<?> WorldClass = ReflectionUtils.getNMSClass("World");

  public static boolean inject(JavaPlugin plugin) throws Exception {
    Field classLoaderField = getField(plugin, "classLoader");
    classLoaderField.setAccessible(true);

    ClassLoader pluginClassLoader = (ClassLoader) classLoaderField.get(plugin);

    // if already set to false return
    Object blockSponge = UnsafeUtils.allocateInstance(BlockSpongeClass);
    Method method = BlockSpongeClass.getDeclaredMethod("e", WorldClass, BlockPositionClass);
    method.setAccessible(true);
    try {
      method.invoke(blockSponge, null, null);
      return false;
    } catch (Exception ignored) {
    }

    ByteBuddyAgent.install();
    ClassReloadingStrategy classReloadingStrategy = ClassReloadingStrategy.fromInstalledAgent();

    new ByteBuddy()
        .redefine(BlockSpongeClass)
        .method(named("e").and(MethodDescription::isPrivate))
        .intercept(FixedValue.value(false))
        .make()
        .load(pluginClassLoader, classReloadingStrategy);
    return true;
  }

  private static <T> Field getField(T t, String name) {
    Class<?> clazz = t.getClass();
    while (clazz != Object.class) {
      Field f = Arrays.stream(clazz.getDeclaredFields())
          .filter(field -> field.getName().equals(name)).findFirst().orElse(null);
      if (f != null) {
        return f;
      }
      clazz = clazz.getSuperclass();
    }
    return null;
  }
}
