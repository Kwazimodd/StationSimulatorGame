package ua.pz33.rendering.animation.interpolation;

class SinPiXHalfInterpolator implements Interpolator {
    @Override
    public double getAbsoluteAnimationState(double absoluteTime) {
        return Math.sin(absoluteTime * Math.PI / 2);
    }
}
