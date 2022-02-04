package sweng_plus.boardgames_test.ludo;

import org.lwjgl.opengl.GL11;
import sweng_plus.boardgames.ludo.gui.widget.DimensionsDebugWidget;
import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.style.ColoredQuadStyle;
import sweng_plus.framework.userinterface.gui.style.IStyle;
import sweng_plus.framework.userinterface.gui.style.MarginStyle;
import sweng_plus.framework.userinterface.gui.style.MatrixStyle;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.util.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;

public class WidgetTestScreen extends Screen
{
    public WidgetTestScreen(IScreenHolder screenHolder)
    {
        super(screenHolder);
    
        GL11.glClearColor(Color4f.DARK_GREY.r, Color4f.DARK_GREY.g, Color4f.DARK_GREY.b, 1.0F);
        
        /*for(AnchorPoint ap : AnchorPoint.values())
        {
            widgets.add(new SimpleWidget(screenHolder, new Dimensions(100, 100, ap, AnchorPoint.M), new MatrixStyle(new IStyle[][]
                    {
                            {
                                    new ColoredQuadStyle(Color4f.RED),
                                    new ColoredQuadStyle(Color4f.YELLOW)
                            },
                            {
                                    new ColoredQuadStyle(Color4f.GREEN),
                                    new ColoredQuadStyle(Color4f.BLUE)
                            }
                    }, new float[] {0.5F, 0.5F}, new float[] {0.5F, 0.5F})
                    .stack(new MarginStyle(new ColoredQuadStyle(Color4f.BLACK), 25))));
        }/**/
        
        final int off = 200;
    
        widgets.add(new SimpleWidget(screenHolder, new Dimensions(100, 200, AnchorPoint.M, AnchorPoint.BR, -off, -off), new ColoredQuadStyle(Color4f.RED)));
        widgets.add(new SimpleWidget(screenHolder, new Dimensions(200, 100, AnchorPoint.M, AnchorPoint.M, off, -off), new ColoredQuadStyle(Color4f.YELLOW)));
        widgets.add(new SimpleWidget(screenHolder, new Dimensions(200, 100, AnchorPoint.M, AnchorPoint.BL, -off, off), new ColoredQuadStyle(Color4f.GREEN)));
        widgets.add(new SimpleWidget(screenHolder, new Dimensions(200, 100, AnchorPoint.M, AnchorPoint.T, off, off), new ColoredQuadStyle(Color4f.BLUE)));
    
        widgets.add(new SimpleWidget(screenHolder, new Dimensions(100, 200, AnchorPoint.BR, AnchorPoint.BR, -off, -off), new ColoredQuadStyle(Color4f.RED)));
        widgets.add(new SimpleWidget(screenHolder, new Dimensions(100, 200, AnchorPoint.R, AnchorPoint.BR, -off, -off), new ColoredQuadStyle(Color4f.RED)));
    
    
        widgets.add(new DimensionsDebugWidget(screenHolder, this, Color4f.INVISIBLE, Color4f.INVISIBLE, Color4f.BLACK));
    }
}
