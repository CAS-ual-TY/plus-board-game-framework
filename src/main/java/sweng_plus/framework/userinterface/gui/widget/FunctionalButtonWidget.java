package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class FunctionalButtonWidget extends ButtonWidget
{
    protected ButtonAction buttonAction;
    
    public FunctionalButtonWidget(IScreenHolder screenHolder, Dimensions dimensions, ButtonAction buttonAction)
    {
        super(screenHolder, dimensions);
        this.buttonAction = buttonAction;
    }
    
    @Override
    protected void clicked(int mouseX, int mouseY, int mods)
    {
        buttonAction.clicked(this, mouseX, mouseY, mods);
    }
    
    public interface ButtonAction
    {
        void clicked(ButtonWidget buttonWidget, int mouseX, int mouseY, int mods);
    }
}
