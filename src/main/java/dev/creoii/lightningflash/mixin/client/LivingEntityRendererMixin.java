package dev.creoii.lightningflash.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.creoii.lightningflash.LightningFlash;
import dev.creoii.lightningflash.util.ExtendedLivingEntity;
import me.shedaniel.math.Color;
import net.fabricmc.fabric.impl.client.indigo.renderer.helper.ColorHelper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(method = "getWhiteOverlayProgress", at = @At("HEAD"), cancellable = true)
    private void gbw$fixWhiteOverlayProgress(LivingEntity livingEntity, float f, CallbackInfoReturnable<Float> cir) {
        if (livingEntity instanceof ExtendedLivingEntity extended && extended.gbw$getStruckByLightningTime() > 0) {
            cir.setReturnValue(1f);
        }
    }

    @WrapOperation(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"))
    private <T extends LivingEntity> void gbw$wrapRenderForLightningOverlay(EntityModel<T> instance, PoseStack poseStack, VertexConsumer vertexConsumer, int i, int p, float r, float g, float b, float a, Operation<Void> original, @Local(argsOnly = true) T livingEntity) {
        if (livingEntity instanceof ExtendedLivingEntity extended && extended.gbw$getStruckByLightningTime() > 0) {
            int color = ColorHelper.toVanillaColor(LightningFlash.getLightningFlashColor());

            float alpha = (color >> 24) & 0xff;
            float red = (color >> 16) & 0xff;
            float green = (color >> 8) & 0xff;
            float blue = color & 0xff;

            original.call(instance, poseStack, vertexConsumer, i, p, red, green, blue, alpha);
        } else original.call(instance, poseStack, vertexConsumer, i, p, r, g, b, a);
    }

    @Inject(method = "getOverlayCoords", at = @At("HEAD"), cancellable = true)
    private static void gbw$applyLightningOverlay(LivingEntity livingEntity, float f, CallbackInfoReturnable<Integer> cir) {
        if (livingEntity instanceof ExtendedLivingEntity extended && extended.gbw$getStruckByLightningTime() > 0) {
            cir.setReturnValue(OverlayTexture.pack(OverlayTexture.u(1f), OverlayTexture.v(false)));
        }
    }
}
