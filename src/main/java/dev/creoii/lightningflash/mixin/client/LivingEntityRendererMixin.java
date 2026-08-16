package dev.creoii.lightningflash.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.creoii.lightningflash.LightningFlash;
import dev.creoii.lightningflash.util.ExtendedEntity;
import dev.creoii.lightningflash.util.ExtendedLivingEntityRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @WrapOperation(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getWhiteOverlayProgress(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)F"))
    private <T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> float gbw$wrapOverlayForLightningOverlay(LivingEntityRenderer<T, S, M> instance, S state, Operation<Float> original, @Local(argsOnly = true) S livingEntityRenderState) {
        if (livingEntityRenderState instanceof ExtendedLivingEntityRenderState extended && extended.gbw$isStruckByLightning()) {
            return 1f;
        }
        return original.call(instance, state);
    }

    @WrapOperation(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"))
    private <S extends LivingEntityRenderState> void gbw$wrapRenderForLightningOverlay(SubmitNodeCollector instance, Model<S> model, Object o, PoseStack matrixStack, RenderType renderLayer, int l, int i, int k, TextureAtlasSprite sprite, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlayCommand, Operation<Void> original, @Local(argsOnly = true) S livingEntityRenderState) {
        if (livingEntityRenderState instanceof ExtendedLivingEntityRenderState extended && extended.gbw$isStruckByLightning()) {
            original.call(instance, model, o, matrixStack, renderLayer, l, LightningFlash.getLightningFlashColor(), LightningFlash.getLightningFlashColor(), sprite, LightningFlash.getLightningFlashColor(), crumblingOverlayCommand);
        } else original.call(instance, model, o, matrixStack, renderLayer, l, i, k, sprite, outlineColor, crumblingOverlayCommand);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"))
    private <T extends LivingEntity, S extends LivingEntityRenderState> void gbw$updateExtendedLivingRenderState(T livingEntity, S livingEntityRenderState, float f, CallbackInfo ci) {
        if (livingEntityRenderState instanceof ExtendedLivingEntityRenderState extended && livingEntity instanceof ExtendedEntity extendedEntity) {
            extended.gbw$setStruckByLightning(extendedEntity.gbw$getStruckByLightningTime() > 0);
        }
    }

    @Inject(method = "getOverlayCoords", at = @At("HEAD"), cancellable = true)
    private static void gbw$applyLightningOverlay(LivingEntityRenderState state, float whiteOverlayProgress, CallbackInfoReturnable<Integer> cir) {
        if (state instanceof ExtendedLivingEntityRenderState extended && extended.gbw$isStruckByLightning()) {
            cir.setReturnValue(OverlayTexture.pack(OverlayTexture.u(1f), OverlayTexture.v(false)));
        }
    }
}
