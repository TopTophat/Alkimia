package net.falling_pan.alkimia.block.entity.renderer;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.falling_pan.alkimia.block.entity.custom.CalcifyingCrucibleBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class CalcifyingCrucibleBlockEntityRenderer implements BlockEntityRenderer<CalcifyingCrucibleBlockEntity> {
    public CalcifyingCrucibleBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(CalcifyingCrucibleBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        boolean firstToRender = true;
        if (entity.storedFluid.isBlank() || entity.amount <= 0)
            return;

        FluidVariant fluidVariant = entity.storedFluid;
        int amount = entity.amount;
        int capacity = entity.capacity;
        float fillPercentage = (float) amount / capacity;
//        System.out.println("Amount " + entity.amount);
//        System.out.println("Capacity " + entity.capacity);
//        System.out.println("Fill " + fillPercentage);
        fillPercentage = MathHelper.clamp(fillPercentage, 0, 1);

        int color = FluidVariantRendering.getColor(fluidVariant, entity.getWorld(), entity.getPos());
        Sprite sprite = FluidVariantRendering.getSprites(fluidVariant)[0];
        RenderLayer renderLayer = RenderLayer.getTranslucent();
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);

        float y1 = 2f / 16f;
        float y2 = ((fillPercentage * 14f) / 16f) + y1;

        float minU = sprite.getFrameU(2f / 16f);
        float maxU = sprite.getFrameU(14f / 16f);
        float minV = sprite.getFrameV(y1);
        float maxV = sprite.getFrameV(y2);

        MatrixStack.Entry entry = matrices.peek();

        // front face
        drawQuad(vertexConsumer, entry, 2f / 16f, y1, 2f / 16f, 14f / 16f, y2, 2f / 16f, minU, minV, maxU, maxV, color, light, overlay);

        // back face
        drawQuad(vertexConsumer, entry, 14f / 16f, y1, 14f / 16f, 2f / 16f, y2, 14f / 16f, minU, minV, maxU, maxV, color, light, overlay);

        // left face
        drawQuad(vertexConsumer, entry, 2f / 16f, y1, 14f / 16f, 2f / 16f, y2, 2f / 16f, minU, minV, maxU, maxV, color, light, overlay);

        // right face
        drawQuad(vertexConsumer, entry, 14f / 16f, y1, 2f / 16f, 14f / 16f, y2, 14f / 16f, minU, minV, maxU, maxV, color, light, overlay);

        if(fillPercentage < 1.1f) {
            minU = sprite.getFrameU(2f / 16f);
            maxU = sprite.getFrameU(14f / 16f);
            minV = sprite.getFrameV(2f / 16f);
            maxV = sprite.getFrameV(14f / 16f);

            vertexConsumer.vertex(entry, 2f / 16f, y2, 2f / 16f)
                    .color(color)
                    .texture(minU, maxV)
                    .light(light)
                    .overlay(overlay)
                    .normal(0.0F, 1.0F, 0.0F);

            vertexConsumer.vertex(entry, 2f / 16f, y2, 14f / 16f)
                    .color(color)
                    .texture(minU, minV)
                    .light(light)
                    .overlay(overlay)
                    .normal(0.0F, 1.0F, 0.0F);

            vertexConsumer.vertex(entry, 14f / 16f, y2, 14f / 16f)
                    .color(color)
                    .texture(maxU, minV)
                    .light(light)
                    .overlay(overlay)
                    .normal(0.0F, 1.0F, 0.0F);

            vertexConsumer.vertex(entry, 14f / 16f, y2, 2f / 16f)
                    .color(color)
                    .texture(maxU, maxV)
                    .light(light)
                    .overlay(overlay)
                    .normal(0.0F, 1.0F, 0.0F);
        }

        if(fillPercentage < 1.1f && firstToRender) {
            minU = sprite.getFrameU(2f / 16f);
            maxU = sprite.getFrameU(14f / 16f);
            minV = sprite.getFrameV(2f / 16f);
            maxV = sprite.getFrameV(14f / 16f);

            vertexConsumer.vertex(entry, 2f / 16f, y1, 2f / 16f)
                    .color(color)
                    .texture(minU, maxV)
                    .light(light)
                    .overlay(overlay)
                    .normal(0.0F, 1.0F, 0.0F);

            vertexConsumer.vertex(entry, 2f / 16f, y1, 14f / 16f)
                    .color(color)
                    .texture(minU, minV)
                    .light(light)
                    .overlay(overlay)
                    .normal(0.0F, 1.0F, 0.0F);

            vertexConsumer.vertex(entry, 14f / 16f, y1, 14f / 16f)
                    .color(color)
                    .texture(maxU, minV)
                    .light(light)
                    .overlay(overlay)
                    .normal(0.0F, 1.0F, 0.0F);

            vertexConsumer.vertex(entry, 14f / 16f, y1, 2f / 16f)
                    .color(color)
                    .texture(maxU, maxV)
                    .light(light)
                    .overlay(overlay)
                    .normal(0.0F, 1.0F, 0.0F);
            firstToRender = false;
        }
    }

    private static void drawQuad(VertexConsumer vertexConsumer,
                                 MatrixStack.Entry entry,
                                 float x1, float y1, float z1,
                                 float x2, float y2, float z2,
                                 float minU, float minV,
                                 float maxU, float maxV,
                                 int color,
                                 int light, int overlay) {
        vertexConsumer.vertex(entry, x1, y1, z1)
                .color(color)
                .texture(minU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, x1, y2, z1)
                .color(color)
                .texture(minU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, x2, y2, z2)
                .color(color)
                .texture(maxU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);

        vertexConsumer.vertex(entry, x2, y1, z2)
                .color(color)
                .texture(maxU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0.0F, 1.0F, 0.0F);
    }
}
