package sword.android.vectorial_renderer;

public class Color {

    public static final int COMPONENT_ALPHA = 0;
    public static final int COMPONENT_RED = 1;
    public static final int COMPONENT_GREEN = 2;
    public static final int COMPONENT_BLUE = 3;

    private static final int NUMBER_OF_COMPONENTS = 4;

    private float[] mComponents = new float[NUMBER_OF_COMPONENTS];

    /**
     * @brief Creates a color. By default it is transparent.
     */
    public Color() { }

    public Color(float alpha, float red, float green, float blue) {
        mComponents[COMPONENT_ALPHA] = alpha;
        mComponents[COMPONENT_RED] = red;
        mComponents[COMPONENT_GREEN] = green;
        mComponents[COMPONENT_BLUE] = blue;
    }

    public int getComponentCount() {
        return NUMBER_OF_COMPONENTS;
    }

    public float getComponentAt(int index) {
        return mComponents[index];
    }

    public void setComponentWithoutBoundsAt(int index, float value) {
        mComponents[index] = value;
    }

    public void saturateColorComponents() {
        for(int component=0; component<NUMBER_OF_COMPONENTS; component++) {

            if (mComponents[component] < 0.0f) {
                mComponents[component] = 0.0f;
            }

            if (mComponents[component] > 1.0f) {
                mComponents[component] = 1.0f;
            }
        }
    }
}
