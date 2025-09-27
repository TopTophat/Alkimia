package net.toptophat.alkimia.block.entity.renderer;

import net.toptophat.alkimia.block.ModBlocks;
import net.toptophat.alkimia.block.custom.ItemPedestal;
import net.toptophat.alkimia.block.entity.custom.ItemPedestalBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class ItemPedestalBlockEntityRenderer implements BlockEntityRenderer<ItemPedestalBlockEntity> {
    public ItemPedestalBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(ItemPedestalBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        ItemStack stack = entity.getStack(0);
        double offset = Math.sin((entity.getWorld().getTime() + tickDelta) / 16.0) / 6.0;

        if (entity.getWorld().getBlockState(entity.getPos()).getBlock() == ModBlocks.ITEM_PEDESTAL_BASE && entity.getWorld().getBlockState(entity.getPos()).get(ItemPedestal.IS_FAST))
        {
            matrices.push();
            matrices.translate(0.5f, 1.2f, 0.5f);
            matrices.scale(0.5f, 0.5f, 0.5f);
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((entity.getWorld().getTime() + tickDelta) * 32));

            itemRenderer.renderItem(stack, ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();

            matrices.push();
            matrices.translate(0.5f, 1.2f, 0.5f);
            float angle = (entity.getWorld().getTime() + tickDelta) * 24f;

            // Order matters: write spin FIRST, tilts AFTER
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle));   // spin around global Y
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(35.264f)); // tilt forward
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45f));     // tilt sideways

            itemRenderer.renderItem(ModBlocks.ITEM_PEDESTAL_GLOBE.asItem().getDefaultStack(), ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();
        }
        else
        {
            matrices.push();
            matrices.translate(0.5f, 1.4f + offset, 0.5f);
            matrices.scale(0.5f, 0.5f, 0.5f);
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((entity.getWorld().getTime() + tickDelta) * 3));

            itemRenderer.renderItem(stack, ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();

            matrices.push();
            matrices.translate(0.5f, 1.4f + offset, 0.5f);

            itemRenderer.renderItem(ModBlocks.ITEM_PEDESTAL_GLOBE.asItem().getDefaultStack(), ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();
        }
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}
