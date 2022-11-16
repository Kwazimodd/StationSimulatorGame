package ua.pz33.rendering.animation.interpolation;

class CubicInterpolator implements Interpolator {
    @Override
    public double getAbsoluteAnimationState(double absoluteTime) {
        return absoluteTime * absoluteTime * absoluteTime;
    }
}
