package net.toptophat.alkimia.block.entity.renderer;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.toptophat.alkimia.block.ModBlocks;
import net.toptophat.alkimia.block.custom.TransmutingPedestal;
import net.toptophat.alkimia.block.entity.custom.FluidPedestalBlockEntity;
import net.toptophat.alkimia.block.entity.custom.ItemPedestalBlockEntity;
import net.toptophat.alkimia.block.entity.custom.TransmutingPedestalBlockEntity;
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
import net.toptophat.alkimia.util.ModTags;

public class TransmutingPedestalBlockEntityRenderer implements BlockEntityRenderer<TransmutingPedestalBlockEntity> {
    public TransmutingPedestalBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(TransmutingPedestalBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        ItemStack stack = entity.getStack(0);

        if (entity.getWorld().getBlockState(entity.getPos()).getBlock() == ModBlocks.TRANSMUTING_PEDESTAL)
        {
            if (entity.recipeIndex != -1)
            {
                DoParticles(entity.getPos(), entity.getWorld());
            }

            matrices.push();
            matrices.translate(0.5f, 0.5f, 0.5f);
            matrices.scale(0.5f, 0.5f, 0.5f);
            matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees((entity.getWorld().getTime() + tickDelta) * 32));

            itemRenderer.renderItem(stack, ModelTransformationMode.GUI, getLightLevel(entity.getWorld(),
                    entity.getPos()), OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 1);
            matrices.pop();

            matrices.push();
            matrices.translate(0.5f, 0.5f, 0.5f);

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.rotation));
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

    public void DoParticles(BlockPos pos, World world)
    {
        System.out.println("Doing particles");
        subDoParticlesItems(getItemPedestal(Direction.NORTH, world, pos).getPos().toCenterPos(), pos.toCenterPos(), getItemPedestal(Direction.NORTH, world, pos).inventory.get(0), 1, world);
        subDoParticlesItems(getItemPedestal(Direction.SOUTH, world, pos).getPos().toCenterPos(), pos.toCenterPos(), getItemPedestal(Direction.SOUTH, world, pos).inventory.get(0), 1, world);
        subDoParticlesItems(getItemPedestal(Direction.WEST, world, pos).getPos().toCenterPos(), pos.toCenterPos(), getItemPedestal(Direction.WEST, world, pos).inventory.get(0), 1, world);
        subDoParticlesItems(getItemPedestal(Direction.EAST, world, pos).getPos().toCenterPos(), pos.toCenterPos(), getItemPedestal(Direction.EAST, world, pos).inventory.get(0), 1, world);
    }

    public void subDoParticlesItems(Vec3d startPoint, Vec3d endPoint, ItemStack item, double velocity, World world)
    {
        if (item.isEmpty())
        {
            return;
        }
        System.out.println("Doing sub particles");
        Vec3d toCentre = calculateDirectionToCenter(startPoint, endPoint);
        Vec3d random1 = new Vec3d(world.random.nextGaussian() * 0.03, world.random.nextGaussian() * 0.03, world.random.nextGaussian() * 0.03);
        Vec3d random2 = new Vec3d(world.random.nextGaussian() * 0.03, world.random.nextGaussian() * 0.03, world.random.nextGaussian() * 0.03);
        world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, item), startPoint.x + random1.x, startPoint.y + random1.y, startPoint.z + random1.z, toCentre.x + random2.x * velocity, toCentre.y + random2.y * velocity, toCentre.z  + random2.z * velocity);
    }

    public Vec3d calculateDirectionToCenter(Vec3d startPoint, Vec3d endPoint)
    {
        return new Vec3d(endPoint.x - startPoint.x, endPoint.y - startPoint.y, endPoint.z - startPoint.z).normalize();
    }

    public ItemPedestalBlockEntity getItemPedestal(Direction dir, World world, BlockPos pos)
    {
        return switch (dir)
        {
            case NORTH -> (ItemPedestalBlockEntity) world.getBlockEntity(pos.down(2).north(4));
            case SOUTH -> (ItemPedestalBlockEntity) world.getBlockEntity(pos.down(2).south(4));
            case WEST -> (ItemPedestalBlockEntity) world.getBlockEntity(pos.down(2).west(4));
            case EAST -> (ItemPedestalBlockEntity) world.getBlockEntity(pos.down(2).east(4));
            case DOWN, UP -> null;
        };
    }

    public FluidPedestalBlockEntity getFluidPedestal(Direction dir, World world, BlockPos pos)
    {
        return switch (dir)
        {
            case NORTH -> (FluidPedestalBlockEntity) world.getBlockEntity(pos.down(2).north(3).east(3));
            case SOUTH -> (FluidPedestalBlockEntity) world.getBlockEntity(pos.down(2).south(3).west(3));
            case WEST -> (FluidPedestalBlockEntity) world.getBlockEntity(pos.down(2).west(3).north(3));
            case EAST -> (FluidPedestalBlockEntity) world.getBlockEntity(pos.down(2).east(3).south(3));
            default -> null;
        };
    }
}
