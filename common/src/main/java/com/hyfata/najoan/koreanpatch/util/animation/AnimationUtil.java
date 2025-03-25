package com.hyfata.najoan.koreanpatch.util.animation;

import org.lwjgl.glfw.GLFW;

public class AnimationUtil {
    private static final float animationDuration = 0.7f;

    private final float[] savedTargetPos = new float[2];
    private final float[] startPos = new float[2];
    private final float[] savedTime = new float[2];
    private final float[] resultPos = new float[2];

    private boolean init = false;

    public void init(float currentX, float currentY) {
        if (!init) {
            init = true;

            final float[] current = {currentX, currentY};
            for (int i = 0; i < 2; i++) {
                savedTargetPos[i] = current[i];
                startPos[i] = current[i];
                savedTime[i] = (float) GLFW.glfwGetTime();
                resultPos[i] = current[i];
            }
        }
    }

    public void calculateAnimation(float targetX, float targetY) {
        final float[] target = {targetX, targetY};

        for (int i=0; i<2; i++) {
            if (target[i] != savedTargetPos[i]) { // detect target changed
                savedTargetPos[i] = target[i];
                startPos[i] = resultPos[i]; // set start position to last result position
                savedTime[i] = (float) GLFW.glfwGetTime();
            }

            float elapsedTime = (float) (GLFW.glfwGetTime() - savedTime[i]);
            if (elapsedTime > animationDuration) { // animate end
                resultPos[i] = target[i];
            } else { // calculate for animate
                float targetDistance = target[i] - startPos[i];
                float x = elapsedTime / animationDuration; // 0~1

                resultPos[i] = startPos[i] + targetDistance * (float) EasingFunctions.easeOutQuint.calculate(x);
            }
        }
    }

    public float getResultX() {
        return resultPos[0];
    }

    public float getResultY() {
        return resultPos[1];
    }
}
