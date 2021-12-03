package sweng_plus.framework.userinterface.gui.widget;

import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.style.IStyle;
import sweng_plus.framework.userinterface.gui.widget.base.Dimensions;

public class FunctionalButtonWidget extends ButtonWidget
{
    protected ButtonAction buttonAction;
    
    public FunctionalButtonWidget(IScreenHolder screenHolder, Dimensions dimensions, IStyle style, ButtonAction buttonAction)
    {
        super(screenHolder, dimensions, style);
        this.buttonAction = buttonAction;
    }
    
    public FunctionalButtonWidget(IScreenHolder screenHolder, Dimensions dimensions, IStyle style, SimpleButtonAction buttonAction)
    {
        this(screenHolder, dimensions, style, (buttonWidget, mouseX, mouseY, mods) -> buttonAction.clicked(buttonWidget));
    }
    
    public FunctionalButtonWidget(IScreenHolder screenHolder, Dimensions dimensions, IStyle style, SimplerButtonAction buttonAction)
    {
        this(screenHolder, dimensions, style, (buttonWidget, mouseX, mouseY, mods) -> buttonAction.clicked());
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
    
    public interface SimpleButtonAction
    {
        void clicked(ButtonWidget buttonWidget);
    }
    
    public interface SimplerButtonAction
    {
        void clicked();
    }
}
