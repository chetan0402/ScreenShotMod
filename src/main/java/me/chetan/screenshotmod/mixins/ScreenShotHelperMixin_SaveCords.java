package me.chetan.screenshotmod.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ScreenShotHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Mixin(ScreenShotHelper.class)
public class ScreenShotHelperMixin_SaveCords {
    private static String filename;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

    @Inject(method="saveScreenshot(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;",at=@At(value="INVOKE_ASSIGN",target = "Lnet/minecraft/util/ChatStyle;setUnderlined(Ljava/lang/Boolean;)Lnet/minecraft/util/ChatStyle;"))
    private static void saveCords(File p_saveScreenshot_0_, String p_saveScreenshot_1_, int p_saveScreenshot_2_, int p_saveScreenshot_3_, Framebuffer p_saveScreenshot_4_, CallbackInfoReturnable<IChatComponent> cir){
        String gameDirectory = Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/screenshots";


        String s = dateFormat.format(new Date()).toString();
        int i = 1;

        while (true)
        {
            File file1 = new File(gameDirectory, s + (i == 1 ? "" : "_" + i) + ".png");

            if (!file1.exists())
            {
                filename= String.valueOf(file1);
                break;
            }

            ++i;
        }

        String csv_file = gameDirectory + "/screenshotmod.csv";

        // Check if the file exists
        File file = new File(csv_file);
        if (!file.exists()) {
            try {
                // Create the file if it doesn't exist
                file.createNewFile();
            } catch (IOException ignored) {}
        }

        String toAppend=filename+","+Minecraft.getMinecraft().thePlayer.posX+","+Minecraft.getMinecraft().thePlayer.posY+","+Minecraft.getMinecraft().thePlayer.posZ;
        appendToFile(csv_file,toAppend);
    }

    private static void appendToFile(String filePath, String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(text);
            writer.newLine();
        } catch (IOException ignored) {}
    }
}
