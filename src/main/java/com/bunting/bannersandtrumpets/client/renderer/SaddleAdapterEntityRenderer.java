package com.bunting.bannersandtrumpets.client.renderer;

import com.bunting.bannersandtrumpets.entities.SaddleAdapterEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SaddleAdapterEntityRenderer extends EntityRenderer<SaddleAdapterEntity> {
    public SaddleAdapterEntityRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public ResourceLocation getTextureLocation(SaddleAdapterEntity p_114482_) {
        return null;
    }
}
