package sweng_plus.framework.userinterface.gui;

import sweng_plus.framework.userinterface.IInputListener;

import static org.lwjgl.opengl.GL11.*;

public class Screen implements IInputListener
{
    public static final Screen EMPTY_SCREEN = new Screen();
    
    public int screenW;
    public int screenH;
    
    public void update(int mouseX, int mouseY)
    {
    
    }
    
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        glBegin(GL_TRIANGLES);
        glColor3f(1, 0, 0.7f);
        glVertex3f(200, 0, 0); // Vertex one
        glColor3f(1, 0, 0.7f);
        glVertex3f(0, 400, 0); // Vertex two
        glColor3f(1, 0, 0.7f);
        glVertex3f(400, 400, 0); // Vertex three
        glEnd();
    }
    
    /** Wird immer gecallt, wenn das Window resized wird, oder der Screen zum ersten mal wieder aktiv ist */
    public void init(int screenW, int screenH)
    {
        glViewport(0, 0, screenW, screenH);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, screenW, screenH, 0, 1, -1);
        glClearColor(0, 0.7f, 1, 0);
        
        this.screenW = screenW;
        this.screenH = screenH;
    }
    
    public void mouseButtonPressed(int mouseX, int mouseY, int mouseButton, int mods)
    {
    
    }
    
    public void mouseButtonReleased(int mouseX, int mouseY, int mouseButton, int mods)
    {
    
    }
    
    public void keyPressed(int key, int mods)
    {
    
    }
    
    public void keyReleased(int key, int mods)
    {
    
    }
    
    public void charTyped(char character)
    {
    
    }
}
