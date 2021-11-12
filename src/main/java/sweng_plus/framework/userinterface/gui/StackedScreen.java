package sweng_plus.framework.userinterface.gui;

public class StackedScreen extends Screen
{
    // Screen ist auf anderem Screen aufgebaut
    // (alter Screen wird nicht weggeworfen und man kann zu diesem zurückkehren)
    
    public Screen subScreen;
    
    public StackedScreen(Screen subScreen)
    {
        super(subScreen.screenHolder);
        this.subScreen = subScreen;
    }
    
    public void returnToSubScreen()
    {
        screenHolder.setScreen(subScreen);
    }
    
    @Override
    public void init(int screenW, int screenH)
    {
        subScreen.init(screenW, screenH);
        super.init(screenW, screenH);
    }
    
    @Override
    public void update(int mouseX, int mouseY)
    {
        subScreen.update(mouseX, mouseY);
        super.update(mouseX, mouseY);
    }
}
