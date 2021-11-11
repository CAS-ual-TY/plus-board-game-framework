package sweng_plus.framework_test.userinterface;

import org.junit.jupiter.api.Test;
import sweng_plus.framework.userinterface.WindowScale;

public class WindowTests
{
    @Test
    public void testWindowScaling()
    {
        WindowScale s = WindowScale.newWindowScale(1920, 1080, 16, 9)
                .add(640, 360)
                .add(1280, 720)
                .add(2560, 1440)
                .add(3200, 1800)
                .add(3840, 2160)
                .build();
        
        for(WindowScale.SingleScale scale : s.smallerScales)
        {
            System.out.println(scale.w + " " + scale.h + " " + scale.scaleFactor);
        }
        
        System.out.println(s.defaultScale.w + " " + s.defaultScale.h + " " + s.defaultScale.scaleFactor);
        
        for(WindowScale.SingleScale scale : s.biggerScales)
        {
            System.out.println(scale.w + " " + scale.h + " " + scale.scaleFactor);
        }
    }
}
