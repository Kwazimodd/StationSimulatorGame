package ua.pz33.rendering.animation.interpolation;

public class LinearInterpolator implements Interpolator {
    @Override
    public double getAbsoluteAnimationState(double absoluteTime) {
        return absoluteTime;
    }
}
