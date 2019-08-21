package fr.badblock.api.worlds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;


public class WorldRegeneration {

    public static void replaceWorld(Boolean activate) {
        if(activate) {
            WorldRegeneration.deleteWorld("world");
            File from = new File("copiedworld");
            File to = new File("world");
            try {
                WorldRegeneration.copyFolder(from, to);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyFolder(File src, File dest) throws IOException{
        if (src.isDirectory()) {
            if(!dest.exists()) {
                dest.mkdir();
            }
            String[] files = src.list();
            String[] arrayOfString1;
            int j = (arrayOfString1 = files).length;
            for(int i = 0; i < j; i++) {
                String file = arrayOfString1[i];
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                copyFolder(srcFile, destFile);
            }
        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte['?'];
            int length;
            while((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }

    public static void deleteWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        File file = new File(worldName);
        if ((file == null) || (world == null)) {
            return;
        }
        Bukkit.unloadWorld(world, false);
        try {
            FileUtils.deleteDirectory(file);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private static boolean deleteWorld(File path) {
        if(path.exists()) {
            File[] files = path.listFiles();
            for(int x = 0; x < files.length; x++) {
                if (files[x].isDirectory()) {
                    deleteWorld(files[x]);
                } else {
                    files[x].delete();
                }
            }
        }
        return path.delete();
    }

}

