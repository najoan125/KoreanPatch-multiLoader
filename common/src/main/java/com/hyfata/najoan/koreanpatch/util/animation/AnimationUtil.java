package com.hyfata.najoan.koreanpatch.util.animation;

import org.lwjgl.glfw.GLFW;

public class AnimationUtil {
    private static final float animationDuration = 0.7f;

    float[] savedIndicator = new float[2];
    float[] savedAnimatedIndicator = new float[2];
    float[] animatedIndicator = new float[2];
    float[] animationTickTime = new float[2];

    float resultX;
    float resultY;

    boolean init = false;

    public void init(float x, float y) {
        if (!init) {
            init = true;
            float[] crd = new float[]{x, y};
            for (int i = 0; i < 2; i++) {
                savedIndicator[i] = crd[i];
                savedAnimatedIndicator[i] = crd[i];
                animatedIndicator[i] = crd[i];
            }
        }
    }

    public void calculateAnimation(float targetX, float targetY) {
        float[] indicator = new float[]{targetX, targetY};
        for (int i=0; i<2; i++) {
            if (indicator[i] != savedIndicator[i]) {
                savedIndicator[i] = indicator[i];
                animationTickTime[i] = (float) GLFW.glfwGetTime();
                savedAnimatedIndicator[i] = animatedIndicator[i];
            }
            if (GLFW.glfwGetTime() - animationTickTime[i] > animationDuration) {
                savedAnimatedIndicator[i] = indicator[i];
            } else {
                animatedIndicator[i] = savedAnimatedIndicator[i] + (indicator[i] - savedAnimatedIndicator[i]) * EasingFunctions.easeOutQuint((float)(GLFW.glfwGetTime() - animationTickTime[i]) / animationDuration);
                indicator[i] = animatedIndicator[i];
            }
        }
        resultX = indicator[0];
        resultY = indicator[1];
    }

    public float getResultX() {
        return resultX;
    }

    public float getResultY() {
        return resultY;
    }
}
