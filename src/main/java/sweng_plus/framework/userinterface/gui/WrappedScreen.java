package sweng_plus.framework.userinterface.gui;

import sweng_plus.framework.userinterface.gui.util.Color4f;

import static org.lwjgl.opengl.GL11.*;

public class WrappedScreen extends Screen
{
    public Screen subScreen;
    
    public WrappedScreen(Screen subScreen)
    {
        super(subScreen.screenHolder);
        this.subScreen = subScreen;
    }
    
    public void returnToSubScreen()
    {
        screenHolder.setScreen(subScreen);
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        renderBackground(deltaTick);
        super.render(deltaTick, mouseX, mouseY);
    }
    
    public void renderBackground(float deltaTick)
    {
        subScreen.render(deltaTick, -1, -1);
        
        Color4f.BACKGROUND.glColor4f();
        glBegin(GL_QUADS);
        glVertex3f(0, 0, 0); // Oben Links
        glVertex3f(0, screenH, 0); // Unten Links
        glVertex3f(screenW, screenH, 0); // Unten Rechts
        glVertex3f(screenW, 0, 0); // Oben Rechts
        glEnd();
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        subScreen.update(mouseX, mouseY);
        super.update(mouseX, mouseY);
    }
}
