package com.github.unldenis.cswj.cswj;

import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;

public final class CancelSpongeWaterInjection extends JavaPlugin {

  @Override
  public void onEnable() {
    // Plugin startup logic
    try {
      if (CancelSpongeWaterInjector.inject(this)) {
        getLogger().info("The injection has just been performed.");
      } else {
        getLogger().info("The injection has already been made.");
      }
    } catch (Exception e) {
      getLogger().log(Level.SEVERE, "There was a mistake in the injection. Contact the developer.",
          e);
    }
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
