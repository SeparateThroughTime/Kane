package kane.genericGame.userInteraction;

import static kane.renderer.Camera.CAMERA;
import static kane.renderer.ResolutionSpecification.RES_SPECS;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

import kane.math.Scalar;
import kane.math.Vec2i;

/**
 * The Mouse is managing all actions, when keys on the hw-mouse are pressed.
 */
public class Mouse{

    public static Mouse MOUSE;

    public final int NUM_BUTTONS = 16;
    public boolean[] click = new boolean[NUM_BUTTONS];

    public Vec2i mousePos = new Vec2i();
    public boolean[] mouseState = new boolean[NUM_BUTTONS];
    private final MouseInterface mouseInt;

    private Mouse(MouseInterface mouseInt, long window){
        this.mouseInt = mouseInt;
        generateChooseAction();
        glfwSetCursorPosCallback(window, mousePositionCallback);
        glfwSetMouseButtonCallback(window, mouseButtonCallback);
    }

    public static void initializeMouse(MouseInterface mouseInt, long window){
        if (MOUSE == null){
            MOUSE = new Mouse(mouseInt, window);
        }
    }

    public void update(){
        for (int i = 0; i < mouseState.length; i++){
            if (mouseState[i]){
                chooseActionPressed[i].choose();
                if (click[i]){
                    click[i] = false;
                    chooseActionClick[i].choose();
                }
            }
        }
    }

    protected GLFWCursorPosCallbackI mousePositionCallback = (window, xPos, yPos) -> {
        mousePos.set((int) xPos, Scalar.getY((int) yPos, RES_SPECS.height));
        if (CAMERA != null){
            mousePos.add(CAMERA.zeroPoint.toVec2i());
        }
    };

    protected GLFWMouseButtonCallbackI mouseButtonCallback = new GLFWMouseButtonCallbackI(){

        @Override
        public void invoke(long window, int button, int action, int mods){
            if (action == GLFW_PRESS){
                mouseState[button] = true;
                click[button] = true;
            } else if (action == GLFW_RELEASE){
                chooseActionReleased[button].choose();
                mouseState[button] = false;
            }
        }
    };

    private interface ChooseAction{
        /**
         * This is a dummy for deciding, which actual method is used in update. Though
         * java is not able to manage an array with methods, this will do the same.
         */
        void choose();
    }

    private final ChooseAction[] chooseActionClick = new ChooseAction[NUM_BUTTONS];
    private final ChooseAction[] chooseActionPressed = new ChooseAction[NUM_BUTTONS];
    private final ChooseAction[] chooseActionReleased = new ChooseAction[NUM_BUTTONS];


    private void generateChooseAction(){
        for (int i = 0; i < NUM_BUTTONS; i++){
            chooseActionClick[i] = () -> {
            };
            chooseActionPressed[i] = () -> {
            };
            chooseActionReleased[i] = () -> {
            };
        }

        chooseActionClick[GLFW_MOUSE_BUTTON_1] = mouseInt::leftMouseClick;
        chooseActionClick[GLFW_MOUSE_BUTTON_2] = mouseInt::rightMouseClick;

        chooseActionPressed[GLFW_MOUSE_BUTTON_1] = mouseInt::leftMousePressed;
        chooseActionPressed[GLFW_MOUSE_BUTTON_2] = mouseInt::rightMousePressed;

        chooseActionReleased[GLFW_MOUSE_BUTTON_1] = mouseInt::leftMouseReleased;
        chooseActionReleased[GLFW_MOUSE_BUTTON_2] = mouseInt::rightMouseReleased;
    }


}
