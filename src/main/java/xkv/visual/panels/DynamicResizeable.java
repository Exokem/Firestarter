package xkv.visual.panels;

import javafx.scene.layout.Region;

import javax.swing.text.html.ImageView;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class DynamicResizeable
{
    private static final Set<DynamicResizeable> ROOT_DYNAMICS = new HashSet<>();
    private static final Set<Runnable> EXTRA_LISTENERS = new HashSet<>();

    private final Region region;
    private final Consumer<Region> resizeFunction;

    private final Set<DynamicResizeable> dependents = new HashSet<>();

    public static void resizeAll()
    {
        ROOT_DYNAMICS.forEach(DynamicResizeable::deepResizeDependents);

        EXTRA_LISTENERS.forEach(Runnable::run);
    }

    public static void commonDependents(DynamicResizeable root, Collection<Region> regions, Consumer<Region> communFunction)
    {
        regions.forEach(region ->
        {
            DynamicResizeable resizeable = dependent(root, region, communFunction);
        });
    }

    public static DynamicResizeable dependent(DynamicResizeable root, Region region, Consumer<Region> resizeFunction)
    {
        DynamicResizeable resizeable = new DynamicResizeable(region, resizeFunction);

        root.dependents.add(resizeable);

        return resizeable;
    }

    public static DynamicResizeable root(Region region, Consumer<Region> resizeFunction)
    {
        DynamicResizeable resizeable = new DynamicResizeable(region, resizeFunction);

        ROOT_DYNAMICS.add(resizeable);

        return resizeable;
    }

    public static void addResizeListener(Runnable resizeFunction)
    {
        EXTRA_LISTENERS.add(resizeFunction);
    }

    private DynamicResizeable(Region region, Consumer<Region> resizeFunction)
    {
        this.region = region;
        this.resizeFunction = resizeFunction;
    }

    public void resize()
    {
        resizeFunction.accept(region);
    }

    public void resizeDependents()
    {
        dependents.forEach(DynamicResizeable::resize);
    }

    public void deepResizeDependents()
    {
        dependents.forEach(resizeable ->
        {
            resizeable.resize();
            resizeable.resizeDependents();
        });
    }
}
