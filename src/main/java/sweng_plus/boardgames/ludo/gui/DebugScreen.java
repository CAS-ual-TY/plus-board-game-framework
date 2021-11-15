package sweng_plus.boardgames.ludo.gui;

import org.lwjgl.glfw.GLFW;
import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.util.TextureHelper;
import sweng_plus.framework.userinterface.gui.widget.ColoredWidget;
import sweng_plus.framework.userinterface.gui.widget.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.InputWidget;
import sweng_plus.framework.userinterface.gui.widget.TextureWidget;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class DebugScreen extends Screen
{
    private long millisFPS = System.currentTimeMillis();
    private LinkedList<Double> fpsAverage = new LinkedList<>();
    
    private long millisTPS = System.currentTimeMillis();
    private LinkedList<Double> tpsAverage = new LinkedList<>();
    
    @SuppressWarnings("ThisEscapedInObjectConstruction")
    public DebugScreen(IScreenHolder screenHolder)
    {
        super(screenHolder);
        
        try
        {
            widgets.add(new TextureWidget(this, new Dimensions(2048, 2048, AnchorPoint.M),
                    TextureHelper.createTexture("src/test/resources/textures/test_raster.png"))
            {
                @Override
                public void render(float deltaTick, int mouseX, int mouseY)
                {
                    Color4f.HALF_VISIBLE.glColor4f();
                    texture.render(dimensions.x, dimensions.y, dimensions.w, dimensions.h);
                }
            });
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        for(AnchorPoint anchor : AnchorPoint.values())
        {
            widgets.add(new ColoredWidget(this, new Dimensions(100, 100, anchor),
                    Color4f.BLACK, Color4f.RED));
        }
        
        widgets.add(new InputWidget(this, new Dimensions(600, 80, AnchorPoint.BR), Ludo.instance().fontRenderer24));
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
            Color4f.RED.glColor4f();
        }
        else
        {
            Color4f.NEUTRAL.glColor4f();
        }
        Ludo.instance().fontRenderer32.render(110, 0, "FPS: " + String.valueOf(Math.round(fpsAverage.stream().mapToDouble(d -> d).average().orElse(0) * 10) / 10D));
        
        if(tpsAverage.size() < 50)
        {
            Color4f.RED.glColor4f();
        }
        else
        {
            Color4f.NEUTRAL.glColor4f();
        }
        Ludo.instance().fontRenderer32.render(110, Ludo.instance().fontRenderer32.getHeight(), "TPS: " + String.valueOf(Math.round(tpsAverage.stream().mapToDouble(d -> d).average().orElse(0) * 10) / 10D));
        
        Color4f.NEUTRAL.glColor4f();
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
        fpsAverage.add(TimeUnit.SECONDS.toMillis(1) / (double) (System.currentTimeMillis() - millisFPS));
        millisFPS = System.currentTimeMillis();
        
        while(fpsAverage.size() > 50)
        {
            fpsAverage.removeFirst();
        }
    }
    
    private void calculateTPS()
    {
        tpsAverage.add(TimeUnit.SECONDS.toMillis(1) / (double) (System.currentTimeMillis() - millisTPS));
        millisTPS = System.currentTimeMillis();
        
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
        
        if(key == GLFW.GLFW_KEY_ESCAPE)
        {
            screenHolder.setScreen(new OptionsScreen(this));
        }
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
        
        super.charTyped(character);
    }
}
