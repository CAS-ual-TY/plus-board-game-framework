package sweng_plus.boardgames.ludo.gui;

import org.lwjgl.opengl.GL11;
import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.framework.userinterface.gui.AnchorPoint;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.ColoredQuad;
import sweng_plus.framework.userinterface.gui.widget.Dimensions;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.opengl.GL11.*;

public class DebugScreen extends Screen
{
    private double fps;
    private long millisFPS = System.currentTimeMillis();
    private LinkedList<Double> fpsAverage = new LinkedList<>();
    
    private double tps;
    private long millisTPS = System.currentTimeMillis();
    private LinkedList<Double> tpsAverage = new LinkedList<>();
    
    public DebugScreen()
    {
        super();
        
        for(AnchorPoint anchor : AnchorPoint.values())
            widgets.add(new ColoredQuad(this, new Dimensions(100, 100, anchor),
                    new Color4f(0F, 0F, 0F), new Color4f(1F, 0F, 0F)));
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        calculateTPS();
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        super.render(deltaTick, mouseX, mouseY);
        
        calculateFPS();
        
        if(fpsAverage.size() < 50)
        {
            GL11.glColor4f(1F, 0F, 0F, 1F);
        }
        else
        {
            GL11.glColor4f(1F, 1F, 1F, 1F);
        }
        Ludo.instance().fontRenderer32.render(110, 0, "FPS: " + String.valueOf(Math.round(fpsAverage.stream().mapToDouble(d -> d).average().orElse(0) * 10) / 10D));
    
        if(tpsAverage.size() < 50)
        {
            GL11.glColor4f(1F, 0F, 0F, 1F);
        }
        else
        {
            GL11.glColor4f(1F, 1F, 1F, 1F);
        }
        Ludo.instance().fontRenderer32.render(110, Ludo.instance().fontRenderer32.getHeight(), "TPS: " + String.valueOf(Math.round(tpsAverage.stream().mapToDouble(d -> d).average().orElse(0) * 10) / 10D));
    
        GL11.glColor4f(1F, 1F, 1F, 1F);
        final String abc = "abcdefghijklmnopqrstuvpxyz";
        final String ABC = abc.toUpperCase();
        int x = 110;
        int y = 110;
        Ludo.instance().fontRenderer64.render(x, y, "64: " + ABC);
        Ludo.instance().fontRenderer64.render(x, y += Ludo.instance().fontRenderer64.getHeight(), "64: " + abc);
        Ludo.instance().fontRenderer48.render(x, y += Ludo.instance().fontRenderer64.getHeight(), "48: " + ABC);
        Ludo.instance().fontRenderer48.render(x, y += Ludo.instance().fontRenderer48.getHeight(), "48: " + abc);
        Ludo.instance().fontRenderer32.render(x, y += Ludo.instance().fontRenderer48.getHeight(), "32: " + ABC);
        Ludo.instance().fontRenderer32.render(x, y += Ludo.instance().fontRenderer32.getHeight(), "32: " + abc);
        Ludo.instance().fontRenderer24.render(x, y += Ludo.instance().fontRenderer32.getHeight(), "24: " + ABC);
        Ludo.instance().fontRenderer24.render(x, y += Ludo.instance().fontRenderer24.getHeight(), "24: " + abc);
        Ludo.instance().fontRenderer16.render(x, y += Ludo.instance().fontRenderer24.getHeight(), "16: " + ABC);
        Ludo.instance().fontRenderer16.render(x, y += Ludo.instance().fontRenderer16.getHeight(), "16: " + abc);
    }
    
    private void calculateFPS()
    {
        fps = TimeUnit.SECONDS.toMillis(1) / (double) (System.currentTimeMillis() - millisFPS);
        millisFPS = System.currentTimeMillis();
        fpsAverage.add(fps);
        
        while(fpsAverage.size() > 50)
        {
            fpsAverage.removeFirst();
        }
    }
    
    private void calculateTPS()
    {
        tps = TimeUnit.SECONDS.toMillis(1) / (double) (System.currentTimeMillis() - millisTPS);
        millisTPS = System.currentTimeMillis();
        tpsAverage.add(tps);
    
        while(tpsAverage.size() > 50)
        {
            tpsAverage.removeFirst();
        }
    }
    
    @Override
    public void init(int screenW, int screenH)
    {
        System.out.println("init " + screenW + " " + screenH);
        
        super.init(screenW, screenH);
        
        glViewport(0, 0, screenW, screenH);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, screenW, screenH, 0, 1, -1);
        glClearColor(0, 0.7f, 1, 0);
    }
    
    @Override
    public void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
        System.out.println("mouseButtonPressed " + mouseX + " " + mouseY + " " + mouseButton + " " + Integer.toBinaryString(mods));
        
        super.mouseButtonPressed(mouseX, mouseY, mouseButton, mods);
    }
    
    @Override
    public void mouseButtonReleased(int mouseX, int mouseY, int mouseButton, int mods)
    {
        System.out.println("mouseButtonReleased " + mouseX + " " + mouseY + " " + mouseButton + " " + Integer.toBinaryString(mods));
        
        super.mouseButtonReleased(mouseX, mouseY, mouseButton, mods);
    }
    
    @Override
    public void keyPressed(int key, int mods)
    {
        System.out.println("keyPressed " + key + " " + Integer.toBinaryString(mods));
        
        super.keyPressed(key, mods);
    }
    
    @Override
    public void keyReleased(int key, int mods)
    {
        System.out.println("keyReleased " + key + " " + Integer.toBinaryString(mods));
        
        super.keyReleased(key, mods);
    }
    
    @Override
    public void charTyped(char character)
    {
        System.out.println("charTyped '" + character + "'");
    }
}
