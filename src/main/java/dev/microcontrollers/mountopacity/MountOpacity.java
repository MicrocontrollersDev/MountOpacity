package dev.microcontrollers.mountopacity;

import dev.microcontrollers.mountopacity.config.MountOpacityConfig;
import net.fabricmc.api.ModInitializer;

public class MountOpacity implements ModInitializer {
	@Override
	public void onInitialize() {
		MountOpacityConfig.CONFIG.load();
	}
}
