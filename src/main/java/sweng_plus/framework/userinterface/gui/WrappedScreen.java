package sweng_plus.framework.userinterface.gui;

import sweng_plus.framework.userinterface.gui.util.Color4f;

import static org.lwjgl.opengl.GL11.*;

public class WrappedScreen extends StackedScreen
{
    // Screen ist auf anderem Screen aufgebaut
    // (alter Screen wird nicht weggeworfen und man kann zu diesem zurückkehren)
    // Unterschied zu StackedScreen: SubScreen wird im Hintergrund verdunkelt gerendert
    
    public WrappedScreen(Screen subScreen)
    {
        super(subScreen);
    }
    
    @Override
    public void renderWidget(float deltaTick, int mouseX, int mouseY)
    {
        renderBackground(deltaTick);
        super.renderWidget(deltaTick, mouseX, mouseY);
    }
    
    public void renderBackground(float deltaTick)
    {
        subScreen.renderWidget(deltaTick, -1, -1);
        
        Color4f.BACKGROUND.glColor4f();
        glBegin(GL_QUADS);
        glVertex3f(0, 0, 0); // Oben Links
        glVertex3f(0, screenH, 0); // Unten Links
        glVertex3f(screenW, screenH, 0); // Unten Rechts
        glVertex3f(screenW, 0, 0); // Oben Rechts
        glEnd();
    }
}
