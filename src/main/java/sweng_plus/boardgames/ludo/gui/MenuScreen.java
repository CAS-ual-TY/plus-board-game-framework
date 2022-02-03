package sweng_plus.boardgames.ludo.gui;

import sweng_plus.boardgames.ludo.Ludo;
import sweng_plus.boardgames.ludo.gui.util.LudoStyles;
import sweng_plus.boardgames.ludo.gui.util.LudoTextures;
import sweng_plus.framework.boardgame.Engine;
import sweng_plus.framework.boardgame.I18N;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.widget.ButtonWidget;
import sweng_plus.framework.userinterface.gui.widget.FunctionalButtonWidget;
import sweng_plus.framework.userinterface.gui.util.Dimensions;

import java.io.IOException;

public class MenuScreen extends Screen
{
    protected ButtonWidget germanButton;
    protected ButtonWidget englishButton;
    
    public MenuScreen(IScreenHolder screenHolder)
    {
        super(screenHolder);
        
        Dimensions hostDims = new Dimensions(700, 80, AnchorPoint.BR, -120, -415);
        widgets.add(new FunctionalButtonWidget(screenHolder, hostDims, LudoStyles.makeButtonStyle("menu.main.new_game"), this::host));
        
        Dimensions connectDims = new Dimensions(700, 80, AnchorPoint.BR, -120, -310);
        widgets.add(new FunctionalButtonWidget(screenHolder, connectDims, LudoStyles.makeButtonStyle("menu.main.join_game"), this::connect));
        
        Dimensions optionsDims = new Dimensions(700, 80, AnchorPoint.BR, -120, -205);
        widgets.add(new FunctionalButtonWidget(screenHolder, optionsDims, LudoStyles.makeButtonStyle("menu.main.settings"), this::options));
        
        Dimensions exitDims = new Dimensions(700, 80, AnchorPoint.BR, -120, -100);
        widgets.add(new FunctionalButtonWidget(screenHolder, exitDims, LudoStyles.makeButtonStyle("menu.main.quit"), this::exit));
        
        Dimensions langButtonDimensions = new Dimensions(80, 80, AnchorPoint.TR, -25, 25);
        germanButton = new FunctionalButtonWidget(screenHolder, langButtonDimensions, LudoStyles.makeButtonStyle("DE"), this::switchToGerman);
        englishButton = new FunctionalButtonWidget(screenHolder, langButtonDimensions, LudoStyles.makeButtonStyle("EN"), this::switchToEnglish);
        widgets.add(englishButton);
    }
    
    private void host()
    {
        screenHolder.setScreen(new MenuHostScreen(this));
    }
    
    private void connect()
    {
        screenHolder.setScreen(new MenuConnectScreen(this));
    }
    
    private void options()
    {
    
    }
    
    private void exit()
    {
        Engine.instance().close();
    }
    
    @Override
    public void render(float deltaTick, int mouseX, int mouseY)
    {
        Color4f.BLUE.glColor4f();
        LudoTextures.figureHuge.render(325, (screenH - 700 - 150) / 2, 700, 700, 0, 0, LudoTextures.figureHuge.getWidth(), LudoTextures.figureHuge.getHeight());
        
        Color4f.YELLOW.glColor4f();
        LudoTextures.figureHuge.render(-25, (screenH - 700 - 150) / 2, 700, 700, 0, 0, LudoTextures.figureHuge.getWidth(), LudoTextures.figureHuge.getHeight());
        
        Color4f.RED.glColor4f();
        LudoTextures.figureHuge.render(450, (screenH - 700 - 150) / 2 + 150, 700, 700, 0, 0, LudoTextures.figureHuge.getWidth(), LudoTextures.figureHuge.getHeight());
        
        Color4f.GREEN.glColor4f();
        LudoTextures.figureHuge.render(100, (screenH - 700 - 150) / 2 + 150, 700, 700, 0, 0, LudoTextures.figureHuge.getWidth(), LudoTextures.figureHuge.getHeight());
        
        Color4f.BLACK.glColor4f();
        LudoTextures.logo.render(900, 50, LudoTextures.logo.getWidth(), LudoTextures.logo.getHeight(), 0, 0, LudoTextures.logo.getWidth(), LudoTextures.logo.getHeight());
        
        super.render(deltaTick, mouseX, mouseY);
    }
    
    public void switchToGerman()
    {
        try
        {
            I18N.initializeI18N(Ludo.LOCALE_DE_DE);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        widgets.remove(germanButton);
        widgets.add(englishButton);
        englishButton.initWidget(this);
    }
    
    public void switchToEnglish()
    {
        try
        {
            I18N.initializeI18N(Ludo.LOCALE_EN_US);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        widgets.remove(englishButton);
        widgets.add(germanButton);
        germanButton.initWidget(this);
    }
}
