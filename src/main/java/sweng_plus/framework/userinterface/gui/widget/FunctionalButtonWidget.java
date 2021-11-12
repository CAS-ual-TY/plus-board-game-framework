package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.util.Texture;

public class FunctionalButtonWidget extends ButtonWidget
{
    protected ButtonAction buttonAction;
    
    public FunctionalButtonWidget(IWidgetParent parent, Dimensions dimensions, Texture active, Texture inactive, ButtonAction buttonAction)
    {
        super(parent, dimensions, active, inactive);
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
