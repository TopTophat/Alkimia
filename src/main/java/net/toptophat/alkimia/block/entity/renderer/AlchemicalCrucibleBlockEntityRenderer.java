package net.toptophat.alkimia.block.entity.renderer;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.toptophat.alkimia.block.entity.custom.AlchemicalCrucibleBlockEntity;
import net.toptophat.alkimia.item.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class AlchemicalCrucibleBlockEntityRenderer implements BlockEntityRenderer<AlchemicalCrucibleBlockEntity> {
    public AlchemicalCrucibleBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(AlchemicalCrucibleBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {

        if (entity.getFillLevel() < 1) return;

        // Determine color
        int color;
        boolean isEmpty = entity.getContents().stream().allMatch(i -> i == 0);

//        System.out.println("Done " + entity.isDone());
//        System.out.println("Fill level " + entity.getFillLevel());
//        System.out.println("Contents " + entity.getContents());
//        System.out.println("Has Water " + entity.getHasWater());
//        System.out.println("Inventory " + entity.getStack(0));
        if (entity.isDone()) {
            color = 0xFFBD05F0; // violet
        } else if (!isEmpty) {
            color = 0xFF1CF005; // green
        } else {
            color = 0xFF05F0E8; // blue
        }

        // Split ARGB into floats
        float a = ((color >> 24) & 0xFF) / 255f;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        // Fluid sprite for UVs only
        FluidVariant variant = FluidVariant.of(Fluids.WATER);
        Sprite sprite = FluidVariantRendering.getSprites(variant)[0];

        float minU = sprite.getFrameU(1f / 16f);
        float maxU = sprite.getFrameU(15f / 16f);
        float minV = sprite.getFrameV(1f / 16f);
        float maxV = sprite.getFrameV(15f / 16f);

        // Y position
        float y = 3f / 16f + (11f / 16f) * (entity.getFillLevel() / 5f);

        MatrixStack.Entry entry = matrices.peek();

        // Use translucent render layer to ignore vanilla water color
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getTranslucent());

        // Render quad
        vertexConsumer.vertex(entry, 1f / 16f, y, 1f / 16f)
                .color(r, g, b, a)
                .texture(minU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0f, 1f, 0f);

        vertexConsumer.vertex(entry, 1f / 16f, y, 15f / 16f)
                .color(r, g, b, a)
                .texture(minU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0f, 1f, 0f);

        vertexConsumer.vertex(entry, 15f / 16f, y, 15f / 16f)
                .color(r, g, b, a)
                .texture(maxU, minV)
                .light(light)
                .overlay(overlay)
                .normal(0f, 1f, 0f);

        vertexConsumer.vertex(entry, 15f / 16f, y, 1f / 16f)
                .color(r, g, b, a)
                .texture(maxU, maxV)
                .light(light)
                .overlay(overlay)
                .normal(0f, 1f, 0f);

        //weird aspect ui stuff starts right here
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        float minX = 0f;
        float maxX = 1f;
        float minY = 1.4f;
        float offset = 0.2f;
        float scale = 0.25f;
        Vec3d currentPos = new Vec3d(minX, minY, 0.5);
        List<Item> aspectRender = List.of(ModItems.GLOW_FULL, ModItems.SENSES_FULL, ModItems.TRANSPARENCY_FULL, ModItems.LIQUID_FULL, ModItems.DEATH_FULL, ModItems.METAL_FULL, ModItems.GLOW_HALF, ModItems.SENSES_HALF, ModItems.TRANSPARENCY_HALF, ModItems.LIQUID_HALF, ModItems.DEATH_HALF, ModItems.METAL_HALF);
        List<Integer> checked = new ArrayList<>(entity.getContents());
        for (int i = 0; i < checked.size(); i++) {
            while (checked.get(i) > 0) {
                if (checked.get(i) > 1) {
                    matrices.push();
                    matrices.translate(currentPos.x, currentPos.y, currentPos.z);
                    matrices.scale(scale, scale, scale);

                    itemRenderer.renderItem(aspectRender.get(i).getDefaultStack(), ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                            entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
                    matrices.pop();
                    checked.set(i, checked.get(i) - 2);
                    if (currentPos.x < maxX) {
                        currentPos = new Vec3d(currentPos.x + offset, currentPos.y, currentPos.z);
                    } else {
                        currentPos = new Vec3d(minX, currentPos.y + offset, currentPos.z);
                    }
                }
                else
                {
                    matrices.push();
                    matrices.translate(currentPos.x, currentPos.y, currentPos.z);
                    matrices.scale(scale, scale, scale);

                    itemRenderer.renderItem(aspectRender.get(i + aspectRender.size() / 2).getDefaultStack(), ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                            entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
                    matrices.pop();
                    checked.set(i, checked.get(i) - 1);
                    if (currentPos.x < maxX) {
                        currentPos = new Vec3d(currentPos.x + offset, currentPos.y, currentPos.z);
                    } else {
                        currentPos = new Vec3d(minX, currentPos.y + offset, currentPos.z);
                    }
                }
            }
        }
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }


}
