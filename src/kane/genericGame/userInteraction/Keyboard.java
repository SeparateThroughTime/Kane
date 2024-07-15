package kane.genericGame.userInteraction;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F10;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F11;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F12;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F5;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F6;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F7;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F8;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_I;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UNKNOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.Arrays;

public class Keyboard{

    public static Keyboard KEYBOARD;

    public final int NUM_BUTTONS = 348;
    public boolean[] keyState = new boolean[NUM_BUTTONS];
    public boolean[] click = new boolean[NUM_BUTTONS];
    public boolean[] clickAgain = new boolean[NUM_BUTTONS];

    private KeyboardInterface keyInt;

    private Keyboard(KeyboardInterface keyInt, long window){
        this.keyInt = keyInt;
        generateChooseAction();

        Arrays.fill(clickAgain, true);

        glfwSetKeyCallback(window, keyCallback);
    }

    public static void initializeKeyboard(KeyboardInterface keyInt, long window){
        if (KEYBOARD == null){
            KEYBOARD = new Keyboard(keyInt, window);
        }
    }


    public boolean isKeyPressed(Keys key){
        return switch (key){
            case LEFT -> keyState[GLFW_KEY_LEFT];
            case UP -> keyState[GLFW_KEY_UP];
            case RIGHT -> keyState[GLFW_KEY_RIGHT];
            case DOWN -> keyState[GLFW_KEY_DOWN];
            case F1 -> keyState[GLFW_KEY_F1];
            case F2 -> keyState[GLFW_KEY_F2];
            case F3 -> keyState[GLFW_KEY_F3];
            case F4 -> keyState[GLFW_KEY_F4];
            case F5 -> keyState[GLFW_KEY_F5];
            case F6 -> keyState[GLFW_KEY_F6];
            case F7 -> keyState[GLFW_KEY_F7];
            case F8 -> keyState[GLFW_KEY_F8];
            case F9 -> keyState[GLFW_KEY_F9];
            case F10 -> keyState[GLFW_KEY_F10];
            case F11 -> keyState[GLFW_KEY_F11];
            case F12 -> keyState[GLFW_KEY_F12];
            case SHIFT -> keyState[GLFW_KEY_LEFT_SHIFT];
            case C -> keyState[GLFW_KEY_C];
            case SPACE -> keyState[GLFW_KEY_SPACE];
            case ESC -> keyState[GLFW_KEY_ESCAPE];
            case I -> keyState[GLFW_KEY_I];
        };
    }

    protected GLFWKeyCallbackI keyCallback = new GLFWKeyCallbackI(){

        @Override
        public void invoke(long window, int key, int scancode, int action, int mods){
            if (key == GLFW_KEY_UNKNOWN){
                throw new IllegalArgumentException("Keyboard Input is Unknown for key: " + key);
            } else if (action == GLFW_PRESS){
                keyState[key] = true;
                click[key] = true;
            } else if (action == GLFW_RELEASE){
                keyState[key] = false;
                chooseActionReleased[key].choose();
                clickAgain[key] = true;
            }
        }
    };

    public void update(){
        for (int i = 0; i < keyState.length; i++){
            if (keyState[i]){
                chooseActionPressed[i].choose();
                if (click[i] && clickAgain[i]){
                    click[i] = false;
                    clickAgain[i] = false;
                    chooseActionClick[i].choose();
                }
            }
        }
    }

    public void setKeyInt(KeyboardInterface keyInt){
        this.keyInt = keyInt;
    }

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
        chooseActionClick[GLFW_KEY_LEFT] = keyInt::leftArrowClick;
        chooseActionPressed[GLFW_KEY_LEFT] = keyInt::leftArrowPressed;
        chooseActionReleased[GLFW_KEY_LEFT] = keyInt::leftArrowReleased;

        chooseActionClick[GLFW_KEY_UP] = keyInt::upArrowClick;
        chooseActionPressed[GLFW_KEY_UP] = keyInt::upArrowPressed;
        chooseActionReleased[GLFW_KEY_UP] = keyInt::upArrowReleased;

        chooseActionClick[GLFW_KEY_RIGHT] = keyInt::rightArrowClick;
        chooseActionPressed[GLFW_KEY_RIGHT] = keyInt::rightArrowPressed;
        chooseActionReleased[GLFW_KEY_RIGHT] = keyInt::rightArrowReleased;

        chooseActionClick[GLFW_KEY_DOWN] = keyInt::downArrowClick;
        chooseActionPressed[GLFW_KEY_DOWN] = keyInt::downArrowPressed;
        chooseActionReleased[GLFW_KEY_DOWN] = keyInt::downArrowReleased;

        chooseActionClick[GLFW_KEY_F1] = keyInt::f1Click;
        chooseActionClick[GLFW_KEY_F2] = keyInt::f2Click;
        chooseActionClick[GLFW_KEY_F3] = keyInt::f3Click;
        chooseActionClick[GLFW_KEY_F4] = keyInt::f4Click;
        chooseActionClick[GLFW_KEY_F5] = keyInt::f5Click;
        chooseActionClick[GLFW_KEY_F6] = keyInt::f6Click;
        chooseActionClick[GLFW_KEY_F7] = keyInt::f7Click;
        chooseActionClick[GLFW_KEY_F8] = keyInt::f8Click;
        chooseActionClick[GLFW_KEY_F9] = keyInt::f9Click;
        chooseActionClick[GLFW_KEY_F10] = keyInt::f10Click;
        chooseActionClick[GLFW_KEY_F11] = keyInt::f11Click;
        chooseActionClick[GLFW_KEY_F12] = keyInt::f12Click;

        chooseActionPressed[GLFW_KEY_LEFT_SHIFT] = keyInt::shiftPressed;
        chooseActionReleased[GLFW_KEY_LEFT_SHIFT] = keyInt::shiftReleased;
        chooseActionClick[GLFW_KEY_LEFT_SHIFT] = keyInt::shiftClick;

        chooseActionPressed[GLFW_KEY_C] = keyInt::cPressed;
        chooseActionReleased[GLFW_KEY_C] = keyInt::cReleased;
        chooseActionClick[GLFW_KEY_C] = keyInt::cClick;

        chooseActionPressed[GLFW_KEY_SPACE] = keyInt::spacePressed;
        chooseActionReleased[GLFW_KEY_SPACE] = keyInt::spaceReleased;
        chooseActionClick[GLFW_KEY_SPACE] = keyInt::spaceClick;

        chooseActionClick[GLFW_KEY_ESCAPE] = keyInt::escClick;

        chooseActionPressed[GLFW_KEY_I] = keyInt::iPressed;
        chooseActionReleased[GLFW_KEY_I] = keyInt::iReleased;
        chooseActionClick[GLFW_KEY_I] = keyInt::iClick;
    }
}
