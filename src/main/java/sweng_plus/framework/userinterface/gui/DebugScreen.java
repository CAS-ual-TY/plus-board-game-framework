package sweng_plus.framework.userinterface.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.util.TextureHelper;
import sweng_plus.framework.userinterface.gui.widget.ColoredQuad;
import sweng_plus.framework.userinterface.gui.widget.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.TextureWidget;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class DebugScreen extends Screen
{
    private double fps;
    private long nanos = 1;
    
    public DebugScreen()
    {
        super();
        
        for(AnchorPoint anchor : AnchorPoint.values())
            this.widgets.add(new ColoredQuad(this, new Dimensions(100, 100, anchor),
                    new Color4f(1F, 0F, 0F), new Color4f(1F, 1F, 1F)));
    
        try
        {
            this.widgets.add(new TextureWidget(this, new Dimensions(128, 128, AnchorPoint.TL, 100, 100),
                    TextureHelper.createTexture("src/main/resources/textures/test_texture.png")));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        calculateFPS();
        
        if(Engine.instance().getInputHandler().isKeyDown(GLFW_KEY_SPACE))
            System.out.println(fps);
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        super.render(deltaTick, mouseX, mouseY);
    }
    
    private void calculateFPS()
    {
        fps = TimeUnit.SECONDS.toNanos(1) / (double) (System.nanoTime() - nanos);
        nanos = System.nanoTime();
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
