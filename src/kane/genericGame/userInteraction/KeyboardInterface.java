package kane.genericGame.userInteraction;

public interface KeyboardInterface {
	
	public boolean spaceClick = false;
	public boolean shiftClick = false;
	
	public void leftArrowPressed();
	public void leftArrowReleased();
	public void rightArrowPressed();
	public void rightArrowReleased();
	public void upArrowPressed();
	public void upArrowReleased();
	public void downArrowPressed();
	public void downArrowReleased();
	
	public void f1Click();
	public void f2Click();
	public void f3Click();
	public void f4Click();
	public void f5Click();
	public void f6Click();
	public void f7Click();
	public void f8Click();
	public void f9Click();
	public void f10Click();
	public void f11Click();
	public void f12Click();
	
	public void cPressed();
	public void cReleased();
	public void cClick();
	public void spacePressed();
	public void spaceReleased();
	public void spaceClick();
	public void shiftPressed();
	public void shiftReleased();
	public void shiftClick();
	public void escClick();
}
