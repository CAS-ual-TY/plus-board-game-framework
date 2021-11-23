package sweng_plus.framework.userinterface.gui;

import java.util.List;

public interface INestedRenderable extends IRenderable
{
    /**
     * @return All sub-{@link IRenderable}s of this {@link INestedRenderable}.
     */
    List<? extends IRenderable> getRenderables();
    
    /**
     * Calls {@link IRenderable#update(int, int)} for all {@link IRenderable}s
     * returned by {@link #getRenderables()}.
     */
    @Override
    default void update(int mouseX, int mouseY)
    {
        for(IRenderable renderable : getRenderables())
        {
            renderable.update(mouseX, mouseY);
        }
    }
    
    /**
     * Calls {@link IRenderable#render(float, int, int)} for all {@link IRenderable}s
     * returned by {@link #getRenderables()}.
     */
    @Override
    default void render(float deltaTick, int mouseX, int mouseY)
    {
        for(IRenderable renderable : getRenderables())
        {
            renderable.render(deltaTick, mouseX, mouseY);
        }
    }
}
