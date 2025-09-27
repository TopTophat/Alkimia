package net.falling_pan.alkimia.block.entity.renderer;

import net.falling_pan.alkimia.block.ModBlocks;
import net.falling_pan.alkimia.block.custom.ItemPedestal;
import net.falling_pan.alkimia.block.custom.TransmutingPedestal;
import net.falling_pan.alkimia.block.entity.custom.ItemPedestalBlockEntity;
import net.falling_pan.alkimia.block.entity.custom.TransmutingPedestalBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
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

public class TransmutingPedestalBlockEntityRenderer implements BlockEntityRenderer<TransmutingPedestalBlockEntity> {
    public TransmutingPedestalBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(TransmutingPedestalBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        ItemStack stack = entity.getStack(0);

        if (entity.getWorld().getBlockState(entity.getPos()).getBlock() == ModBlocks.TRANSMUTING_PEDESTAL && entity.getWorld().getBlockState(entity.getPos()).get(TransmutingPedestal.IS_FAST))
        {
            matrices.push();
            matrices.translate(0.5f, 0.5f, 0.5f);
            matrices.scale(0.5f, 0.5f, 0.5f);
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((entity.getWorld().getTime() + tickDelta) * 32));

            itemRenderer.renderItem(stack, ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();

            matrices.push();
            matrices.translate(0.5f, 0.5f, 0.5f);
            float angle = (entity.getWorld().getTime() + tickDelta) * 64f;

            // Order matters: write spin FIRST, tilts AFTER
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle));   // spin around global Y
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(35.264f)); // tilt forward
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45f));     // tilt sideways

            itemRenderer.renderItem(ModBlocks.TRANSMUTING_PEDESTAL.asItem().getDefaultStack(), ModelTransformationMode.HEAD, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();
        }
        else
        {
            matrices.push();
            matrices.translate(0.5f, 0.5f, 0.5f);
            matrices.scale(0.5f, 0.5f, 0.5f);
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((entity.getWorld().getTime() + tickDelta) * 3));

            itemRenderer.renderItem(stack, ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();

            matrices.push();
            matrices.translate(0.5f, 0.5f, 0.5f);

            itemRenderer.renderItem(ModBlocks.TRANSMUTING_PEDESTAL.asItem().getDefaultStack(), ModelTransformationMode.HEAD, getLightLevel(entity.getWorld(),
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
