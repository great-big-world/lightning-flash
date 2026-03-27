package dev.creoii.lightningflash.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.creoii.lightningflash.util.ExtendedLivingEntity;
import dev.creoii.lightningflash.util.ExtendedLivingEntityRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @WrapOperation(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getWhiteOverlayProgress(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)F"))
    private <T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> float gbw$wrapOverlayForLightningOverlay(LivingEntityRenderer<T, S, M> instance, S state, Operation<Float> original) {
        if (state instanceof ExtendedLivingEntityRenderState extended && extended.gbw$isStruckByLightning()) {
            return 1f;
        }
        return original.call(instance, state);
    }

    @WrapOperation(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"))
    private <S extends LivingEntityRenderState> void gbw$wrapRenderForLightningOverlay(EntityModel<? super S> instance, PoseStack matrixStack, VertexConsumer vertexConsumer, int i, int j, int l, Operation<Void> original, @Local(argsOnly = true) S livingEntityRenderState) {
        if (livingEntityRenderState instanceof ExtendedLivingEntityRenderState extended && extended.gbw$isStruckByLightning()) {
            original.call(instance, matrixStack, vertexConsumer, i, j, CommonColors.WHITE);
        } else original.call(instance, matrixStack, vertexConsumer, i, j, l);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"))
    private <T extends LivingEntity, S extends LivingEntityRenderState> void gbw$updateExtendedLivingRenderState(T livingEntity, S livingEntityRenderState, float f, CallbackInfo ci) {
        if (livingEntityRenderState instanceof ExtendedLivingEntityRenderState extended && livingEntity instanceof ExtendedLivingEntity extendedLivingEntity) {
            extended.gbw$setStruckByLightning(extendedLivingEntity.gbw$getStruckByLightningTime() > 0);
        }
    }

    @Inject(method = "getOverlayCoords", at = @At("HEAD"), cancellable = true)
    private static void gbw$applyLightningOverlay(LivingEntityRenderState state, float f, CallbackInfoReturnable<Integer> cir) {
        if (state instanceof ExtendedLivingEntityRenderState extended && extended.gbw$isStruckByLightning()) {
            cir.setReturnValue(OverlayTexture.pack(OverlayTexture.u(1f), OverlayTexture.v(false)));
        }
    }
}
