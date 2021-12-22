package sweng_plus.boardgames.mill.gui.util;

import sweng_plus.boardgames.mill.Mill;
import sweng_plus.framework.userinterface.gui.style.*;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.InputWidget;

import java.util.function.Supplier;

public class MillStyles
{
    public static BaseStyle makeActiveButtonBackground()
    {
        return new CorneredTextureStyle(MillTextures.activeButton);
    }
    
    public static BaseStyle makeInactiveButtonBackground()
    {
        return new CorneredTextureStyle(MillTextures.inactiveButton);
    }
    
    public static BaseStyle makeActiveButtonStyle(String text)
    {
        return makeActiveButtonBackground().stack(new TextStyle(Mill.instance().fontRenderer32, text, Color4f.BLACK));
    }
    
    public static BaseStyle makeInactiveButtonStyle(String text)
    {
        return makeInactiveButtonBackground().stack(new TextStyle(Mill.instance().fontRenderer32, text, Color4f.BLACK));
    }
    
    public static BaseStyle makeButtonStyle(String text)
    {
        return new HoverStyle(makeInactiveButtonStyle(text), makeActiveButtonStyle(text));
    }
    
    public static BaseStyle makeActiveInputStyle(Supplier<InputWidget> inputWidget, AnchorPoint anchor)
    {
        return makeActiveButtonBackground().stack(new FunctionalTextStyle(Mill.instance().fontRenderer32, () -> inputWidget.get().getTextAsList(), anchor, Color4f.BLACK));
    }
    
    public static BaseStyle makeInactiveInputStyle(Supplier<InputWidget> inputWidget, AnchorPoint anchor)
    {
        return makeInactiveButtonBackground().stack(new FunctionalTextStyle(Mill.instance().fontRenderer32, () -> inputWidget.get().getTextAsList(), anchor, Color4f.BLACK));
    }
    
    public static BaseStyle makeActiveInputStyle(Supplier<InputWidget> inputWidget)
    {
        return makeActiveInputStyle(inputWidget, AnchorPoint.M);
    }
    
    public static BaseStyle makeInactiveInputStyle(Supplier<InputWidget> inputWidget)
    {
        return makeInactiveInputStyle(inputWidget, AnchorPoint.M);
    }
}
