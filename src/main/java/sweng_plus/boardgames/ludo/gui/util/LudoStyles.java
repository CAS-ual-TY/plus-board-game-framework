package sweng_plus.boardgames.ludo.gui.util;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.framework.userinterface.gui.style.*;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.InputWidget;

import java.util.function.Supplier;

public class LudoStyles
{
    public static BaseStyle makeActiveButtonBackground()
    {
        return new CorneredTextureStyle(LudoTextures.activeButton);
    }
    
    public static BaseStyle makeInactiveButtonBackground()
    {
        return new CorneredTextureStyle(LudoTextures.inactiveButton);
    }
    
    public static BaseStyle makeActiveButtonStyle(String text)
    {
        return makeActiveButtonBackground().stack(new I18nStyle(Ludo.instance().fontRenderer32, text, Color4f.BLACK));
    }
    
    public static BaseStyle makeInactiveButtonStyle(String text)
    {
        return makeInactiveButtonBackground().stack(new I18nStyle(Ludo.instance().fontRenderer32, text, Color4f.BLACK));
    }
    
    public static BaseStyle makeButtonStyle(String text)
    {
        return new HoverStyle(makeInactiveButtonStyle(text), makeActiveButtonStyle(text));
    }
    
    public static BaseStyle makeActiveInputStyle(Supplier<InputWidget> inputWidget, AnchorPoint anchor)
    {
        return makeActiveButtonBackground().stack(new FunctionalTextStyle(Ludo.instance().fontRenderer32, () -> inputWidget.get().getTextAsList(), anchor, Color4f.BLACK));
    }
    
    public static BaseStyle makeInactiveInputStyle(Supplier<InputWidget> inputWidget, AnchorPoint anchor)
    {
        return makeInactiveButtonBackground().stack(new FunctionalTextStyle(Ludo.instance().fontRenderer32, () -> inputWidget.get().getTextAsList(), anchor, Color4f.BLACK));
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
