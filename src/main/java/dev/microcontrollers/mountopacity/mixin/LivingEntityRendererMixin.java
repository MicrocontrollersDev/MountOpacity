package dev.microcontrollers.mountopacity.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.microcontrollers.mountopacity.config.MountOpacityConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.CamelEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.util.Identifier;
//#if MC >= 1.21
import net.minecraft.util.math.ColorHelper;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
    @SuppressWarnings("rawtypes")
    @WrapOperation(method = "getRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;getLayer(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer transparentEntityRenderLayer(EntityModel model, Identifier texture, Operation<RenderLayer> original) {
        // let's not set this unless we absolutely have to
        // TODO: fix pig saddles
        if (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.hasVehicle() &&
                ((MountOpacityConfig.CONFIG.instance().horseOpacity != 100 && texture.toString().contains("horse")) ||
                        (MountOpacityConfig.CONFIG.instance().pigOpacity != 100 && texture.toString().contains("pig")) ||
                        (MountOpacityConfig.CONFIG.instance().striderOpacity != 100 && texture.toString().contains("strider")) ||
                        (MountOpacityConfig.CONFIG.instance().camelOpacity != 100 && texture.toString().contains("camel")))) {
            return RenderLayer.getEntityTranslucent(texture);
        }
        return original.call(model, texture);
    }

    //#if MC >= 1.21
    @ModifyArgs(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"))
    //#else
    //$$ @ModifyArgs(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    //#endif
    private void transparentRiddenEntity(Args args, T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (livingEntity instanceof AbstractHorseEntity && livingEntity.hasPassenger(MinecraftClient.getInstance().player) && MountOpacityConfig.CONFIG.instance().horseOpacity != 0) {
            //#if MC >= 1.21
            args.set(4, ColorHelper.Argb.fromFloats(MountOpacityConfig.CONFIG.instance().horseOpacity / 100F, 1.0F, 1.0F, 1.0F));
            //#else
            //$$ args.set(7, MountOpacityConfig.CONFIG.instance().horseOpacity / 100F);
            //#endif
        } else if (livingEntity instanceof PigEntity && livingEntity.hasPassenger(MinecraftClient.getInstance().player) && MountOpacityConfig.CONFIG.instance().pigOpacity != 0) {
            //#if MC >= 1.21
            args.set(4, ColorHelper.Argb.fromFloats(MountOpacityConfig.CONFIG.instance().pigOpacity / 100F, 1.0F, 1.0F, 1.0F));
            //#else
            //$$ args.set(7, MountOpacityConfig.CONFIG.instance().pigOpacity / 100F);
            //#endif
        } else if (livingEntity instanceof StriderEntity && livingEntity.hasPassenger(MinecraftClient.getInstance().player) && MountOpacityConfig.CONFIG.instance().striderOpacity != 0) {
            //#if MC >= 1.21
            args.set(4, ColorHelper.Argb.fromFloats(MountOpacityConfig.CONFIG.instance().striderOpacity / 100F, 1.0F, 1.0F, 1.0F));
            //#else
            //$$ args.set(7, MountOpacityConfig.CONFIG.instance().striderOpacity / 100F);
            //#endif
        } else if (livingEntity instanceof CamelEntity && livingEntity.hasPassenger(MinecraftClient.getInstance().player) && MountOpacityConfig.CONFIG.instance().camelOpacity != 0) {
            //#if MC >= 1.21
            args.set(4, ColorHelper.Argb.fromFloats(MountOpacityConfig.CONFIG.instance().camelOpacity / 100F, 1.0F, 1.0F, 1.0F));
            //#else
            //$$ args.set(7, MountOpacityConfig.CONFIG.instance().camelOpacity / 100F);
            //#endif
        }
    }

    // if it's 0, let's just cancel the rendering. this will also help prevent translucency sorting issues
    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void cancelRiddenEntity(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if ((livingEntity instanceof AbstractHorseEntity && MountOpacityConfig.CONFIG.instance().horseOpacity == 0) ||
                (livingEntity instanceof PigEntity && MountOpacityConfig.CONFIG.instance().pigOpacity == 0) ||
                (livingEntity instanceof StriderEntity && MountOpacityConfig.CONFIG.instance().striderOpacity == 0) ||
                (livingEntity instanceof CamelEntity && MountOpacityConfig.CONFIG.instance().camelOpacity == 0)) {
            ci.cancel();
        }

    }
}
