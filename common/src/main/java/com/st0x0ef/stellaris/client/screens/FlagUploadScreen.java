package com.st0x0ef.stellaris.client.screens;

import com.st0x0ef.stellaris.common.network.packets.SendImagePacket;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.st0x0ef.stellaris.Stellaris.LOG;

@Environment(EnvType.CLIENT)
public class FlagUploadScreen extends Screen {

    public FlagUploadScreen() {
        super(Component.literal("Flag PNG Upload Screen"));
    }

    @Override
    public void onFilesDrop(List<Path> packs) {
        if (packs.size() > 1) LOG.warn("Can only upload one image at a time");
        Path texture = packs.getFirst();
        File file = new File(texture.toUri());
        if (!file.getName().toLowerCase().endsWith(".png")) {
            LOG.error("Uploaded file is not a PNG");
            return;
        }

        try {
            ImageIO.read(file).toString();
        } catch (Exception e) {
            LOG.error("Uploaded file is not an image");
            return;
        }

        try {
            byte[] bytes = Files.readAllBytes(texture);
            if (bytes.length > 16 * 1024 * 1024) { //TODO make size(64) configurable server-side
                LOG.error("Uploaded file was too large");
                return;
            }

            NetworkManager.sendToServer(new SendImagePacket(bytes));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
