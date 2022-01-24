package sweng_plus.framework.userinterface.gui.style;

public class EmptyStyle extends BaseStyle
{
    public static final EmptyStyle EMPTY_STYLE = new EmptyStyle();
    
    private EmptyStyle() {}
    
    @Override
    public void renderStyle(float deltaTick, int mouseX, int mouseY)
    {
    
    }
}
