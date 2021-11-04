package sweng_plus.framework.userinterface.gui;

import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.ColoredQuad;
import sweng_plus.framework.userinterface.gui.widget.Dimensions;

import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class DebugScreen extends Screen
{
    private float r = 1F;
    private boolean rUp = false;
    private float g = 0F;
    private boolean gUp = true;
    private float b = 0.7F;
    private boolean bUp = true;
    private final float colorChange = 0.02F;
    
    private double fps;
    private long nanos = 1;
    
    public DebugScreen()
    {
        super();
        //AnchorPoint anchor = AnchorPoint.M;
        for(AnchorPoint anchor : AnchorPoint.values())
            this.widgets.add(new ColoredQuad(this, new Dimensions(100, 100, anchor),
                    new Color4f(1F, 0F, 0F), new Color4f(1F, 1F, 1F)));
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        /*
        if(r >= 1F && rUp)
        {
            r = 1F;
            rUp = false;
        }
        else if(r <= 0F && !rUp)
        {
            r = 0F;
            rUp = true;
        }
        
        if(g >= 1F && gUp)
        {
            g = 1F;
            gUp = false;
        }
        else if(g <= 0F && !gUp)
        {
            g = 0F;
            gUp = true;
        }
        
        if(b >= 1F && bUp)
        {
            b = 1F;
            bUp = false;
        }
        else if(b <= 0F && !bUp)
        {
            b = 0F;
            bUp = true;
        }
        
        if(Engine.instance().getInputHandler().isKeyDown(GLFW_KEY_R))
        {
            r += rUp ? colorChange : -colorChange;
        }
        
        if(Engine.instance().getInputHandler().isKeyDown(GLFW_KEY_G))
        {
            g += gUp ? colorChange : -colorChange;
        }
        
        if(Engine.instance().getInputHandler().isKeyDown(GLFW_KEY_B))
        {
            b += bUp ? colorChange : -colorChange;
        }
        
        calculateFPS();
        
        if(Engine.instance().getInputHandler().isKeyDown(GLFW_KEY_SPACE))
            System.out.println(fps);
        */
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
        super.init(screenW, screenH);
        
        glViewport(0, 0, screenW, screenH);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, screenW, screenH, 0, 1, -1);
        glClearColor(0, 0.7f, 1, 0);
        
        System.out.println("init " + screenW + " " + screenH);
    }
    
    @Override
    public void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
        System.out.println("mouseButtonPressed " + mouseX + " " + mouseY + " " + mouseButton + " " + Integer.toBinaryString(mods));
    }
    
    @Override
    public void mouseButtonReleased(int mouseX, int mouseY, int mouseButton, int mods)
    {
        System.out.println("mouseButtonReleased " + mouseX + " " + mouseY + " " + mouseButton + " " + Integer.toBinaryString(mods));
    }
    
    @Override
    public void keyPressed(int key, int mods)
    {
        System.out.println("keyPressed " + key + " " + Integer.toBinaryString(mods));
    }
    
    @Override
    public void keyReleased(int key, int mods)
    {
        System.out.println("keyReleased " + key + " " + Integer.toBinaryString(mods));
    }
    
    @Override
    public void charTyped(char character)
    {
        System.out.println("charTyped '" + character + "'");
    }
}
