package sweng_plus.framework.userinterface;

import java.util.ArrayList;
import java.util.LinkedList;

public class WindowScale
{
    private final SingleScale aspectRatio;
    
    public final SingleScale defaultScale;
    public final int defaultScaleIndex;
    
    public final ArrayList<SingleScale> baseScales;
    public final ArrayList<SingleScale> thresholds;
    
    public WindowScale(SingleScale aspectRatio, SingleScale defaultScale, ArrayList<SingleScale> baseScales, ArrayList<SingleScale> thresholds)
    {
        this.aspectRatio = aspectRatio;
        
        this.defaultScale = defaultScale;
        defaultScaleIndex = baseScales.indexOf(defaultScale);
        
        this.baseScales = baseScales;
        this.thresholds = thresholds;
    }
    
    public SingleScale getAspectRatio()
    {
        return aspectRatio;
    }
    
    public static boolean checkAspectRatio(SingleScale scale, SingleScale aspectRatio)
    {
        return scale.w % aspectRatio.w == 0 && scale.h % aspectRatio.h == 0
                && scale.w / aspectRatio.w == scale.h / aspectRatio.h;
    }
    
    public static Builder newWindowScale(int defaultW, int defaultH, int aspectRatioX, int aspectRatioY)
    {
        return new Builder(new SingleScale(defaultW, defaultH), new SingleScale(aspectRatioX, aspectRatioY));
    }
    
    public static class Builder
    {
        private final SingleScale defaultScale;
        private final SingleScale aspectRatio;
        
        private final LinkedList<SingleScale> baseScales;
        
        public Builder(SingleScale defaultScale, SingleScale aspectRatio)
        {
            this.defaultScale = defaultScale;
            this.aspectRatio = aspectRatio;
            baseScales = new LinkedList<>();
            baseScales.add(defaultScale);
        }
        
        public Builder add(int w, int h)
        {
            SingleScale scale = new SingleScale(w, h, (float) defaultScale.w / (float) w);
            
            if(!checkAspectRatio(scale, aspectRatio))
            {
                throw new IllegalArgumentException("Wrong aspect ratio");
            }
            
            if(defaultScale.w == scale.w)
            {
                throw new IllegalArgumentException("Default scale");
            }
            
            if(baseScales.stream().anyMatch(s -> s.w == scale.w))
            {
                throw new IllegalArgumentException("Duplicate");
            }
            
            baseScales.add(scale);
            
            return this;
        }
        
        private ArrayList<SingleScale> buildThresholdList(ArrayList<SingleScale> list)
        {
            boolean smaller = true;
            
            ArrayList<SingleScale> thresholds = new ArrayList<>(list.size());
            
            for(int i = 0; i < list.size() - 1; ++i)
            {
                SingleScale s1 = list.get(i);
                SingleScale s2 = list.get(i + 1);
                
                if(s1 == defaultScale)
                {
                    smaller = false;
                }
                
                thresholds.add(new SingleScale((s1.w + s2.w) / 2, (s1.h + s2.h) / 2, smaller ? s1.scaleFactor : s2.scaleFactor));
            }
            
            return thresholds;
        }
        
        public WindowScale build()
        {
            ArrayList<SingleScale> baseScales = new ArrayList<>(this.baseScales.size());
            baseScales.addAll(this.baseScales);
            baseScales.sort((s1, s2) -> Integer.compare(s1.w, s2.w));
            
            return new WindowScale(aspectRatio, defaultScale, baseScales, buildThresholdList(baseScales));
        }
    }
    
    public static class SingleScale
    {
        public final int w;
        public final int h;
        public final float scaleFactor;
        
        public SingleScale(int w, int h)
        {
            this.w = w;
            this.h = h;
            scaleFactor = 1.0F;
        }
        
        public SingleScale(int w, int h, float scaleFactor)
        {
            this.w = w;
            this.h = h;
            this.scaleFactor = scaleFactor;
        }
    }
}
