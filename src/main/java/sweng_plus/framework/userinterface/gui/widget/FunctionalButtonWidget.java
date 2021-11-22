package sweng_plus.framework.userinterface.gui.widget;

public class FunctionalButtonWidget extends ButtonWidget
{
    protected ButtonAction buttonAction;
    
    public FunctionalButtonWidget(IWidgetParent parent, Dimensions dimensions, ButtonAction buttonAction)
    {
        super(parent, dimensions);
        this.buttonAction = buttonAction;
    }
    
    @Override
    protected void clicked(int mouseX, int mouseY, int mods)
    {
        buttonAction.clicked(mouseX, mouseY, mods);
    }
    
    public interface ButtonAction
    {
        void clicked(int mouseX, int mouseY, int mods);
    }
}
