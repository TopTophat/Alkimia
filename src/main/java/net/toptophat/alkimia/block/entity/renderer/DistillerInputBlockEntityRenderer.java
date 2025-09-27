package net.toptophat.alkimia.block.entity.renderer;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.entity.custom.DistillerInputBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class DistillerInputBlockEntityRenderer implements BlockEntityRenderer<DistillerInputBlockEntity> {
    public DistillerInputBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(DistillerInputBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        boolean firstToRender = true;
        float y1add = 0;
        if (entity.amount1 > 0)
        {
            FluidVariant fluidVariant = entity.storedFluid1;
            int amount = entity.amount1;
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

            float y1 = 1f / 16f + y1add;
            float y2 = ((fillPercentage * 9f) / 16f) + y1;
            y1add += (fillPercentage * 9f) / 16f;

            float minU = sprite.getFrameU(3f / 16f);
            float maxU = sprite.getFrameU(13f / 16f);
            float minV = sprite.getFrameV(y1);
            float maxV = sprite.getFrameV(y2);

            MatrixStack.Entry entry = matrices.peek();

            // front face
            drawQuad(vertexConsumer, entry, 3f / 16f, y1, 3f / 16f, 13f / 16f, y2, 3f / 16f, minU, minV, maxU, maxV, color, light, overlay);

            // back face
            drawQuad(vertexConsumer, entry, 13f / 16f, y1, 13f / 16f, 3f / 16f, y2, 13f / 16f, minU, minV, maxU, maxV, color, light, overlay);

            // left face
            drawQuad(vertexConsumer, entry, 3f / 16f, y1, 13f / 16f, 3f / 16f, y2, 3f / 16f, minU, minV, maxU, maxV, color, light, overlay);

            // right face
            drawQuad(vertexConsumer, entry, 13f / 16f, y1, 3f / 16f, 13f / 16f, y2, 13f / 16f, minU, minV, maxU, maxV, color, light, overlay);

            if(fillPercentage < 1.1f) {
                minU = sprite.getFrameU(3f / 16f);
                maxU = sprite.getFrameU(13f / 16f);
                minV = sprite.getFrameV(3f / 16f);
                maxV = sprite.getFrameV(13f / 16f);

                vertexConsumer.vertex(entry, 3f / 16f, y2, 3f / 16f)
                        .color(color)
                        .texture(minU, maxV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 3f / 16f, y2, 13f / 16f)
                        .color(color)
                        .texture(minU, minV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 13f / 16f, y2, 13f / 16f)
                        .color(color)
                        .texture(maxU, minV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 13f / 16f, y2, 3f / 16f)
                        .color(color)
                        .texture(maxU, maxV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);
            }

            if(fillPercentage < 1.1f && firstToRender) {
                minU = sprite.getFrameU(3f / 16f);
                maxU = sprite.getFrameU(13f / 16f);
                minV = sprite.getFrameV(3f / 16f);
                maxV = sprite.getFrameV(13f / 16f);

                vertexConsumer.vertex(entry, 3f / 16f, y1, 3f / 16f)
                        .color(color)
                        .texture(minU, maxV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 3f / 16f, y1, 13f / 16f)
                        .color(color)
                        .texture(minU, minV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 13f / 16f, y1, 13f / 16f)
                        .color(color)
                        .texture(maxU, minV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 13f / 16f, y1, 3f / 16f)
                        .color(color)
                        .texture(maxU, maxV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);
                firstToRender = false;
            }
        }
        if (entity.amount2 > 0)
        {
            FluidVariant fluidVariant = entity.storedFluid2;
            int amount = entity.amount2;
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

            float y1 = 1f / 16f + y1add;
            float y2 = ((fillPercentage * 9f) / 16f) + y1;
            y1add += (fillPercentage * 9f) / 16f;

            float minU = sprite.getFrameU(3f / 16f);
            float maxU = sprite.getFrameU(13f / 16f);
            float minV = sprite.getFrameV(y1);
            float maxV = sprite.getFrameV(y2);

            MatrixStack.Entry entry = matrices.peek();

            // front face
            drawQuad(vertexConsumer, entry, 3f / 16f, y1, 3f / 16f, 13f / 16f, y2, 3f / 16f, minU, minV, maxU, maxV, color, light, overlay);

            // back face
            drawQuad(vertexConsumer, entry, 13f / 16f, y1, 13f / 16f, 3f / 16f, y2, 13f / 16f, minU, minV, maxU, maxV, color, light, overlay);

            // left face
            drawQuad(vertexConsumer, entry, 3f / 16f, y1, 13f / 16f, 3f / 16f, y2, 3f / 16f, minU, minV, maxU, maxV, color, light, overlay);

            // right face
            drawQuad(vertexConsumer, entry, 13f / 16f, y1, 3f / 16f, 13f / 16f, y2, 13f / 16f, minU, minV, maxU, maxV, color, light, overlay);

            if(fillPercentage < 1.1f) {
                minU = sprite.getFrameU(3f / 16f);
                maxU = sprite.getFrameU(13f / 16f);
                minV = sprite.getFrameV(3f / 16f);
                maxV = sprite.getFrameV(13f / 16f);

                vertexConsumer.vertex(entry, 3f / 16f, y2, 3f / 16f)
                        .color(color)
                        .texture(minU, maxV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 3f / 16f, y2, 13f / 16f)
                        .color(color)
                        .texture(minU, minV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 13f / 16f, y2, 13f / 16f)
                        .color(color)
                        .texture(maxU, minV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 13f / 16f, y2, 3f / 16f)
                        .color(color)
                        .texture(maxU, maxV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);
            }

            if(fillPercentage < 1.1f && firstToRender) {
                minU = sprite.getFrameU(3f / 16f);
                maxU = sprite.getFrameU(13f / 16f);
                minV = sprite.getFrameV(3f / 16f);
                maxV = sprite.getFrameV(13f / 16f);

                vertexConsumer.vertex(entry, 3f / 16f, y1, 3f / 16f)
                        .color(color)
                        .texture(minU, maxV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 3f / 16f, y1, 13f / 16f)
                        .color(color)
                        .texture(minU, minV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 13f / 16f, y1, 13f / 16f)
                        .color(color)
                        .texture(maxU, minV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 13f / 16f, y1, 3f / 16f)
                        .color(color)
                        .texture(maxU, maxV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);
                firstToRender = false;
            }
        }
        if (entity.amount3 > 0)
        {
            FluidVariant fluidVariant = entity.storedFluid3;
            int amount = entity.amount3;
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

            float y1 = 1f / 16f + y1add;
            float y2 = ((fillPercentage * 9f) / 16f) + y1;
            y1add += (fillPercentage * 9f) / 16f;

            float minU = sprite.getFrameU(3f / 16f);
            float maxU = sprite.getFrameU(14f / 16f);
            float minV = sprite.getFrameV(y1);
            float maxV = sprite.getFrameV(y2);

            MatrixStack.Entry entry = matrices.peek();

            // front face
            drawQuad(vertexConsumer, entry, 3f / 16f, y1, 3f / 16f, 13f / 16f, y2, 3f / 16f, minU, minV, maxU, maxV, color, light, overlay);

            // back face
            drawQuad(vertexConsumer, entry, 13f / 16f, y1, 13f / 16f, 3f / 16f, y2, 13f / 16f, minU, minV, maxU, maxV, color, light, overlay);

            // left face
            drawQuad(vertexConsumer, entry, 3f / 16f, y1, 13f / 16f, 3f / 16f, y2, 3f / 16f, minU, minV, maxU, maxV, color, light, overlay);

            // right face
            drawQuad(vertexConsumer, entry, 13f / 16f, y1, 3f / 16f, 13f / 16f, y2, 13f / 16f, minU, minV, maxU, maxV, color, light, overlay);

            if(fillPercentage < 1.1f) {
                minU = sprite.getFrameU(3f / 16f);
                maxU = sprite.getFrameU(13f / 16f);
                minV = sprite.getFrameV(3f / 16f);
                maxV = sprite.getFrameV(13f / 16f);

                vertexConsumer.vertex(entry, 3f / 16f, y2, 3f / 16f)
                        .color(color)
                        .texture(minU, maxV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 3f / 16f, y2, 13f / 16f)
                        .color(color)
                        .texture(minU, minV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 13f / 16f, y2, 13f / 16f)
                        .color(color)
                        .texture(maxU, minV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 13f / 16f, y2, 3f / 16f)
                        .color(color)
                        .texture(maxU, maxV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);
            }

            if(fillPercentage < 1.1f && firstToRender) {
                minU = sprite.getFrameU(3f / 16f);
                maxU = sprite.getFrameU(13f / 16f);
                minV = sprite.getFrameV(3f / 16f);
                maxV = sprite.getFrameV(13f / 16f);

                vertexConsumer.vertex(entry, 3f / 16f, y1, 3f / 16f)
                        .color(color)
                        .texture(minU, maxV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 3f / 16f, y1, 13f / 16f)
                        .color(color)
                        .texture(minU, minV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 13f / 16f, y1, 13f / 16f)
                        .color(color)
                        .texture(maxU, minV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 13f / 16f, y1, 3f / 16f)
                        .color(color)
                        .texture(maxU, maxV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);
                firstToRender = false;
            }
        }
        if (entity.amount4 > 0)
        {
            FluidVariant fluidVariant = entity.storedFluid4;
            int amount = entity.amount4;
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

            float y1 = 1f / 16f + y1add;
            float y2 = ((fillPercentage * 9f) / 16f) + y1;
            y1add += (fillPercentage * 9f) / 16f;

            float minU = sprite.getFrameU(3f / 16f);
            float maxU = sprite.getFrameU(13f / 16f);
            float minV = sprite.getFrameV(y1);
            float maxV = sprite.getFrameV(y2);

            MatrixStack.Entry entry = matrices.peek();

            // front face
            drawQuad(vertexConsumer, entry, 3f / 16f, y1, 3f / 16f, 13f / 16f, y2, 3f / 16f, minU, minV, maxU, maxV, color, light, overlay);

            // back face
            drawQuad(vertexConsumer, entry, 13f / 16f, y1, 13f / 16f, 3f / 16f, y2, 13f / 16f, minU, minV, maxU, maxV, color, light, overlay);

            // left face
            drawQuad(vertexConsumer, entry, 3f / 16f, y1, 13f / 16f, 3f / 16f, y2, 3f / 16f, minU, minV, maxU, maxV, color, light, overlay);

            // right face
            drawQuad(vertexConsumer, entry, 13f / 16f, y1, 3f / 16f, 13f / 16f, y2, 13f / 16f, minU, minV, maxU, maxV, color, light, overlay);

            if(fillPercentage < 1.1f) {
                minU = sprite.getFrameU(3f / 16f);
                maxU = sprite.getFrameU(13f / 16f);
                minV = sprite.getFrameV(3f / 16f);
                maxV = sprite.getFrameV(13f / 16f);

                vertexConsumer.vertex(entry, 3f / 16f, y2, 3f / 16f)
                        .color(color)
                        .texture(minU, maxV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 3f / 16f, y2, 13f / 16f)
                        .color(color)
                        .texture(minU, minV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 13f / 16f, y2, 13f / 16f)
                        .color(color)
                        .texture(maxU, minV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 13f / 16f, y2, 3f / 16f)
                        .color(color)
                        .texture(maxU, maxV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);
            }

            if(fillPercentage < 1.1f && firstToRender) {
                minU = sprite.getFrameU(3f / 16f);
                maxU = sprite.getFrameU(13f / 16f);
                minV = sprite.getFrameV(3f / 16f);
                maxV = sprite.getFrameV(13f / 16f);

                vertexConsumer.vertex(entry, 3f / 16f, y1, 3f / 16f)
                        .color(color)
                        .texture(minU, maxV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 3f / 16f, y1, 13f / 16f)
                        .color(color)
                        .texture(minU, minV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 13f / 16f, y1, 13f / 16f)
                        .color(color)
                        .texture(maxU, minV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);

                vertexConsumer.vertex(entry, 13f / 16f, y1, 3f / 16f)
                        .color(color)
                        .texture(maxU, maxV)
                        .light(light)
                        .overlay(overlay)
                        .normal(0.0F, 1.0F, 0.0F);
                firstToRender = false;
            }
        }
        if (!entity.isEmpty())
        {
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();


            ItemStack stack0 = entity.getStack(0);
            matrices.push();
            matrices.translate(0.33f, 0.4f, 0.33f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            itemRenderer.renderItem(stack0, ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();


            ItemStack stack1 = entity.getStack(1);
            matrices.push();
            matrices.translate(0.33f, 0.4f, 0.66f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            itemRenderer.renderItem(stack1, ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();


            ItemStack stack2 = entity.getStack(2);
            matrices.push();
            matrices.translate(0.66f, 0.4f, 0.33f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            itemRenderer.renderItem(stack2, ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();

            ItemStack stack3 = entity.getStack(3);
            matrices.push();
            matrices.translate(0.66f, 0.4f, 0.66f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            itemRenderer.renderItem(stack3, ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();

            ItemStack stack4 = entity.getStack(4);
            matrices.push();
            matrices.translate(0.5f, 0.6f, 0.5f);
            matrices.scale(0.35f, 0.35f, 0.35f);
            itemRenderer.renderItem(stack4, ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();
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

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
