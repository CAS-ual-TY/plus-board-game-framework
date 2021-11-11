package sweng_plus.framework.userinterface;

import java.util.ArrayList;
import java.util.LinkedList;

public class WindowScale
{
    private final SingleScale aspectRatio;
    
    public final SingleScale defaultScale;
    public final ArrayList<SingleScale> smallerScales;
    public final ArrayList<SingleScale> biggerScales;
    
    public WindowScale(SingleScale aspectRatio, SingleScale defaultScale, ArrayList<SingleScale> smallerScales, ArrayList<SingleScale> biggerScales)
    {
        this.aspectRatio = aspectRatio;
        this.defaultScale = defaultScale;
        this.smallerScales = smallerScales;
        this.biggerScales = biggerScales;
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
        private final LinkedList<SingleScale> smallerScales;
        private final LinkedList<SingleScale> biggerScales;
        
        public Builder(SingleScale defaultScale, SingleScale aspectRatio)
        {
            this.defaultScale = defaultScale;
            this.aspectRatio = aspectRatio;
            smallerScales = new LinkedList<>();
            biggerScales = new LinkedList<>();
        }
        
        public Builder add(int w, int h)
        {
            SingleScale scale = new SingleScale(w, h, (float) defaultScale.w / (float) w);
            
            if(!checkAspectRatio(scale, aspectRatio))
            {
                throw new IllegalArgumentException("Wrong aspect ratio");
            }
            
            LinkedList<SingleScale> list;
            
            if(w < defaultScale.w)
            {
                list = smallerScales;
            }
            else if(w > defaultScale.w)
            {
                list = biggerScales;
            }
            else
            {
                throw new IllegalArgumentException("Default scale");
            }
            
            if(list.stream().anyMatch(s -> s.w == defaultScale.w))
            {
                throw new IllegalArgumentException("Duplicate");
            }
            
            addToListSorted(list, scale);
            
            return this;
        }
        
        private void addToListSorted(LinkedList<SingleScale> list, SingleScale scale)
        {
            if(list.isEmpty() || scale.w < list.get(0).w)
            {
                list.add(scale);
            }
            else
            {
                int i = 1;
                for(SingleScale s : list)
                {
                    if(scale.w > s.w)
                    {
                        break;
                    }
                    
                    ++i;
                }
                
                list.add(i, scale);
            }
        }
        
        private ArrayList<SingleScale> buildList(LinkedList<SingleScale> list, boolean smaller)
        {
            int size = list.size();
            
            if(smaller)
            {
                smallerScales.addLast(defaultScale);
            }
            else
            {
                biggerScales.addFirst(defaultScale);
            }
            
            ArrayList<SingleScale> thresholds = new ArrayList<>(size);
            
            for(int i = 0; i < size; ++i)
            {
                SingleScale s1 = list.get(i);
                SingleScale s2 = list.get(i + 1);
                thresholds.add(new SingleScale((s1.w + s2.w) / 2, (s1.h + s2.h) / 2, smaller ? s1.scaleFactor : s2.scaleFactor));
            }
            
            return thresholds;
        }
        
        public WindowScale build()
        {
            return new WindowScale(aspectRatio, defaultScale, buildList(smallerScales, true), buildList(biggerScales, false));
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
