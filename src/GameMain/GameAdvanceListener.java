package GameMain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class: GameAdvanceListener
 * @author F23-A-403
 * <br>Purpose: Advances one tick and updates state of component and redraws it
 */

public class GameAdvanceListener implements ActionListener {

	private LevelGameComponent gameComponent;
	
	public GameAdvanceListener(LevelGameComponent gameComponent) {
		this.gameComponent = gameComponent;
	}
	
	public void actionPerformed(ActionEvent e) {
		advanceOneTick();
	}
	
	public void advanceOneTick() {
		gameComponent.updateState();
		gameComponent.drawScreen();
	}
}
