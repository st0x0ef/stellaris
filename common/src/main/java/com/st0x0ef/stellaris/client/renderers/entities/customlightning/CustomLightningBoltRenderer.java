package com.st0x0ef.stellaris.client.renderers.entities.customlightning;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.st0x0ef.stellaris.common.entities.CustomLightningBolt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.RandomSource;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class CustomLightningBoltRenderer extends EntityRenderer<CustomLightningBolt, CustomLightningBoltRenderState> {

    public CustomLightningBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public CustomLightningBoltRenderState createRenderState() {
        return new CustomLightningBoltRenderState(0.45f, 0.45f, 0.50f);
    }


    @Override
    public void render(CustomLightningBoltRenderState entityRenderState, PoseStack poseStack, MultiBufferSource buffer, int a) {
        float[] fs = new float[8];
        float[] gs = new float[8];
        float f = 0.0F;
        float g = 0.0F;
        RandomSource randomSource = RandomSource.create(entityRenderState.seed);

        for(int i = 7; i >= 0; --i) {
            fs[i] = f;
            gs[i] = g;
            f += (float)(randomSource.nextInt(11) - 5);
            g += (float)(randomSource.nextInt(11) - 5);
        }

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.lightning());
        Matrix4f matrix4f = poseStack.last().pose();

        for(int j = 0; j < 4; ++j) {
            RandomSource randomSource2 = RandomSource.create(entityRenderState.seed);

            for(int k = 0; k < 3; ++k) {
                int l = 7;
                int m = 0;
                if (k > 0) {
                    l = 7 - k;
                }

                if (k > 0) {
                    m = l - 2;
                }

                float h = fs[l] - f;
                float n = gs[l] - g;

                for(int o = l; o >= m; --o) {
                    float p = h;
                    float q = n;
                    if (k == 0) {
                        h += (float)(randomSource2.nextInt(11) - 5);
                        n += (float)(randomSource2.nextInt(11) - 5);
                    } else {
                        h += (float)(randomSource2.nextInt(31) - 15);
                        n += (float)(randomSource2.nextInt(31) - 15);
                    }

                    float v = 0.1F + (float)j * 0.2F;
                    if (k == 0) {
                        v *= (float)o * 0.1F + 1.0F;
                    }

                    float w = 0.1F + (float)j * 0.2F;
                    if (k == 0) {
                        w *= ((float)o - 1.0F) * 0.1F + 1.0F;
                    }

                    quad(matrix4f, vertexConsumer, h, n, o, p, q, entityRenderState.red, entityRenderState.green, entityRenderState.blue, v, w, false, false, true, false);
                    quad(matrix4f, vertexConsumer, h, n, o, p, q, entityRenderState.red, entityRenderState.green, entityRenderState.blue, v, w, true, false, true, true);
                    quad(matrix4f, vertexConsumer, h, n, o, p, q, entityRenderState.red, entityRenderState.green, entityRenderState.blue, v, w, true, true, false, true);
                    quad(matrix4f, vertexConsumer, h, n, o, p, q, entityRenderState.red, entityRenderState.green, entityRenderState.blue, v, w, false, true, false, false);
                }
            }
        }
    }

    private static void quad(Matrix4f matrix, VertexConsumer consumer, float x1, float z1, int index, float x2, float z2, float red, float green, float blue, float f, float g, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        consumer.addVertex(matrix, x1 + (bl ? g : -g), (float)(index * 16), z1 + (bl2 ? g : -g)).setColor(red, green, blue, 0.3F);
        consumer.addVertex(matrix, x2 + (bl ? f : -f), (float)((index + 1) * 16), z2 + (bl2 ? f : -f)).setColor(red, green, blue, 0.3F);
        consumer.addVertex(matrix, x2 + (bl3 ? f : -f), (float)((index + 1) * 16), z2 + (bl4 ? f : -f)).setColor(red, green, blue, 0.3F);
        consumer.addVertex(matrix, x1 + (bl3 ? g : -g), (float)(index * 16), z1 + (bl4 ? g : -g)).setColor(red, green, blue, 0.3F);
    }
}
