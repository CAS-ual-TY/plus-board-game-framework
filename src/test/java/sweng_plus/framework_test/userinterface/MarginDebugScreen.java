package sweng_plus.framework_test.userinterface;

import sweng_plus.framework.userinterface.gui.IScreenHolder;
import sweng_plus.framework.userinterface.gui.Screen;
import sweng_plus.framework.userinterface.gui.style.*;
import sweng_plus.framework.userinterface.gui.util.AnchorPoint;
import sweng_plus.framework.userinterface.gui.util.Color4f;
import sweng_plus.framework.userinterface.gui.util.Dimensions;
import sweng_plus.framework.userinterface.gui.widget.SimpleWidget;

public class MarginDebugScreen extends Screen
{
    public MarginDebugScreen(IScreenHolder screenHolder)
    {
        super(screenHolder);
        
        int size = 200;
        int margin = 50;
        
        AnchorPoint[] as = AnchorPoint.values();
        
        IStyle[][] styles = new IStyle[3][3];
        
        for(int y = 0; y < 3; ++y)
        {
            for(int x = 0; x < 3; ++x)
            {
                int idx = y * 3 + x;
                styles[y][x] = new ColoredQuadStyle(idx % 2 == 0 ? Color4f.BLACK : Color4f.WHITE)
                        .stack(new MarginStyle(new MarginStyle(new ColoredQuadStyle(Color4f.RED), margin, as[idx]), margin / 2, AnchorPoint.M));
            }
        }
        
        IStyle[][] styles2 = new IStyle[4][2];
        
        for(int y = 0; y < 4; ++y)
        {
            int idx = y * 2;
            
            styles2[y][0] = new ColoredQuadStyle((idx + y) % 2 == 0 ? Color4f.BLACK : Color4f.WHITE)
                    .stack(new MarginStyle(new MarginStyle(new ColoredQuadStyle(Color4f.RED), margin, AnchorPoint.L), margin / 2, AnchorPoint.M));
            
            idx++;
            
            styles2[y][1] = new ColoredQuadStyle((idx + y) % 2 == 0 ? Color4f.BLACK : Color4f.WHITE)
                    .stack(new MarginStyle(new MarginStyle(new ColoredQuadStyle(Color4f.RED), margin, AnchorPoint.R), margin / 2, AnchorPoint.M));
        }
        
        widgets.add(new SimpleWidget(screenHolder, new Dimensions(3 * size, 3 * size, AnchorPoint.M),
                new ColoredQuadStyle(Color4f.HALF_VISIBLE)));
        
        widgets.add(new SimpleWidget(screenHolder, new Dimensions(3 * size, 3 * size, AnchorPoint.M),
                new ColoredBorderStyle(Color4f.RED, 10, false)));
        
        widgets.add(new SimpleWidget(screenHolder, new Dimensions(3 * size, 3 * size, AnchorPoint.M),
                new MatrixStyle(styles, new float[] {1F, 1F, 1F}, new float[] {1F, 1F, 1F})));
        
        widgets.add(new SimpleWidget(screenHolder, new Dimensions(2 * size, 4 * size, AnchorPoint.L, margin, 0),
                new MatrixStyle(styles2, new float[] {1F, 1F}, new float[] {1F, 1F, 1F, 1F})));
    }
}
