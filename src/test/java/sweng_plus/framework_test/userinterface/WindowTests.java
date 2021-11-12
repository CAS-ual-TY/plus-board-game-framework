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
        
        for(WindowScale.SingleScale scale : s.baseScales)
        {
            System.out.println(scale.w + " " + scale.h + " " + scale.scaleFactor);
        }
        
        System.out.println("---");
        
        for(WindowScale.SingleScale scale : s.thresholds)
        {
            System.out.println(scale.w + " " + scale.h + " " + scale.scaleFactor);
        }
        
        System.out.println("---");
        
        System.out.println(s.defaultScale.w + " " + s.defaultScale.h + " " + s.defaultScale.scaleFactor);
        
        System.out.println("---");
        System.out.println("---");
        
        for(WindowScale.SingleScale baseScale : s.baseScales)
        {
            WindowScale.SingleScale scale = s.getScaleForWindowSize(baseScale.w, baseScale.h);
            System.out.println(scale.w + " " + scale.h + " " + scale.scaleFactor);
        }
        
        System.out.println("---");
        
        WindowScale.SingleScale scale = s.getScaleForWindowSize(s.getAspectRatio().w, s.getAspectRatio().h);
        System.out.println(scale.w + " " + scale.h + " " + scale.scaleFactor);
        
        for(WindowScale.SingleScale baseScale : s.thresholds)
        {
            scale = s.getScaleForWindowSize(baseScale.w, baseScale.h);
            System.out.println(scale.w + " " + scale.h + " " + scale.scaleFactor);
        }
        
        System.out.println("---");
        
        scale = s.getScaleForWindowSize(640, 2160);
        System.out.println(scale.w + " " + scale.h + " " + scale.scaleFactor);
        
        System.out.println("---");
        
        scale = s.getScaleForWindowSize(3840, 360);
        System.out.println(scale.w + " " + scale.h + " " + scale.scaleFactor);
    }
}
