package dev.microcontrollers.mountopacity.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MountOpacityConfig {
    public static final ConfigClassHandler<MountOpacityConfig> CONFIG = ConfigClassHandler.createBuilder(MountOpacityConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("mountopacity.json"))
                    .build())
            .build();

    @SerialEntry public float horseOpacity = 100F;
    @SerialEntry public float pigOpacity = 100F;
    @SerialEntry public float striderOpacity = 100F;
    @SerialEntry public float camelOpacity = 100F;

    @SuppressWarnings("deprecation")
    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.literal("Mount Opacity"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Mount Opacity"))
                        .option(Option.createBuilder(float.class)
                                .name(Text.literal("Ridden Horse Opacity"))
                                .description(OptionDescription.of(Text.of("Changes the opacity of the horse you are currently riding.")))
                                .binding(100F, () -> config.horseOpacity, newVal -> config.horseOpacity = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                        .range(0F, 100F)
                                        .step(1F))
                                .build())
                        .option(Option.createBuilder(float.class)
                                .name(Text.literal("Ridden Pig Opacity"))
                                .description(OptionDescription.of(Text.of("Changes the opacity of the pig you are currently riding.")))
                                .binding(100F, () -> config.pigOpacity, newVal -> config.pigOpacity = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                        .range(0F, 100F)
                                        .step(1F))
                                .build())
                        .option(Option.createBuilder(float.class)
                                .name(Text.literal("Ridden Strider Opacity"))
                                .description(OptionDescription.of(Text.of("Changes the opacity of the strider you are currently riding.")))
                                .binding(100F, () -> config.striderOpacity, newVal -> config.striderOpacity = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                        .range(0F, 100F)
                                        .step(1F))
                                .build())
                        .option(Option.createBuilder(float.class)
                                .name(Text.literal("Ridden Camel Opacity"))
                                .description(OptionDescription.of(Text.of("Changes the opacity of the camel you are currently riding.")))
                                .binding(100F, () -> config.camelOpacity, newVal -> config.camelOpacity = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                        .range(0F, 100F)
                                        .step(1F))
                                .build())
                        .build())
        )).generateScreen(parent);
    }
}
