package dev.creoii.lightningflash.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.creoii.lightningflash.LightningFlash;
import dev.creoii.lightningflash.util.ExtendedEntity;
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
        if (livingEntity instanceof ExtendedEntity extended && extended.gbw$getStruckByLightningTime() > 0) {
            cir.setReturnValue(1f);
        }
    }

    @WrapOperation(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"))
    private <T extends LivingEntity> void gbw$wrapRenderForLightningOverlay(EntityModel<T> instance, PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j, int l, Operation<Void> original, @Local(argsOnly = true) T livingEntity) {
        if (livingEntity instanceof ExtendedEntity extended && extended.gbw$getStruckByLightningTime() > 0) {
            original.call(instance, poseStack, vertexConsumer, i, j, LightningFlash.getLightningFlashColor());
        } else original.call(instance, poseStack, vertexConsumer, i, j, l);
    }

    @Inject(method = "getOverlayCoords", at = @At("HEAD"), cancellable = true)
    private static void gbw$applyLightningOverlay(LivingEntity livingEntity, float f, CallbackInfoReturnable<Integer> cir) {
        if (livingEntity instanceof ExtendedEntity extended && extended.gbw$getStruckByLightningTime() > 0) {
            cir.setReturnValue(OverlayTexture.pack(OverlayTexture.u(1f), OverlayTexture.v(false)));
        }
    }
}
