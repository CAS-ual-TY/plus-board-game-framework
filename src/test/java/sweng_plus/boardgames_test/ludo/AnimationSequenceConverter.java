package sweng_plus.boardgames_test.ludo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AnimationSequenceConverter
{
    private static int maxW = 0;
    private static int maxH = 0;
    
    public static void main(String[] args)
    {
        for(int target = 1; target <= 6; ++target)
        {
            System.out.println("Dice Animation: " + target);
            
            File sourceFolder = new File("C:/Studium/BSc Informatik/5. Semester/Software Praktikum/" +
                    "dice/anim_dice_4_d" + target);
            File targetFolder = new File("src/main/resources/textures/dice/anim_" + target);
            targetFolder.mkdirs();
            
            String prefix = "anim";
            int digits = 4;
            
            convert(sourceFolder, targetFolder, prefix, digits, 0, 180);
        }
    }
    
    public static void convert(File sourceFolder, File targetFolder, String prefix, int digits,
                               int startNum, int endNum)
    {
        if(!sourceFolder.isDirectory() || sourceFolder.listFiles() == null)
        {
            throw new IllegalArgumentException();
        }
        
        File coordsFile = new File(targetFolder.getAbsolutePath() + "/" + "coordinates.txt");
        File spritesFile = new File(targetFolder.getAbsolutePath() + "/" + "sprites.png");
        int spritesPerWidth = 16;
        int spritesSize = 64;
        
        BufferedImage sprites = new BufferedImage(spritesPerWidth * spritesSize, spritesPerWidth * spritesSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = sprites.createGraphics();
        
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(coordsFile)))
        {
            for(int number = startNum; number <= endNum; ++number)
            {
                System.out.println("    Sprite: " + (number + 1) + " / " + (endNum + 1));
                
                String suffix = String.format("%0" + digits + "d", number);
                File sourceFile = new File(sourceFolder.getAbsolutePath() + "/" + prefix + suffix + ".png");
                
                File targetFile = new File(targetFolder.getAbsolutePath() + "/" + prefix + suffix + ".png");
                
                try
                {
                    BufferedImage source = ImageIO.read(sourceFile);
                    
                    final int w = source.getWidth();
                    final int h = source.getHeight();
                    
                    int[] pixels = new int[w * h];
                    source.getRGB(0, 0, w, h, pixels, 0, w);
                    
                    int left = w;
                    int top = h;
                    int right = 0;
                    int bottom = 0;
                    
                    for(int y = 0; y < h; ++y)
                    {
                        for(int x = 0; x < w; ++x)
                        {
                            int rgba = pixels[x + y * w];
                            int alpha = (rgba >> 0x18) & 0xFF;
                            
                            if(alpha > 0)
                            {
                                left = Math.min(left, x);
                                top = Math.min(top, y);
                                right = Math.max(right, x);
                                bottom = Math.max(bottom, y);
                            }
                        }
                    }
                    
                    writer.write(suffix + ": " + left + " " + top + " " + right + " " + bottom);
                    writer.newLine();
                    
                    BufferedImage target = source.getSubimage(left, top, right - left, bottom - top);
                    //ImageIO.write(target, "png", targetFile);
                    
                    maxW = Math.max(maxW, right - left);
                    maxH = Math.max(maxH, bottom - top);
                    
                    int ws = right - left;
                    int hs = bottom - top;
                    int xs = (number % spritesPerWidth) * spritesSize + (spritesSize - ws) / 2;
                    int ys = (number / spritesPerWidth) * spritesSize + (spritesSize - hs) / 2;
                    
                    g.drawImage(target, xs, ys, ws, hs, (Image img, int infoflags,
                                                         int xu, int yu, int width, int height) -> true);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
            
            ImageIO.write(sprites, "png", spritesFile);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        System.out.println(maxW + " / " + maxH);
    }
}
