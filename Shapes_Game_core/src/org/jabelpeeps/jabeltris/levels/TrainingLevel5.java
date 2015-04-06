package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.MainMenu;
import org.jabelpeeps.jabeltris.Shape;

public class TrainingLevel5 extends TrainingLevelAbstract {

	public TrainingLevel5(boolean b, boolean c) {
	}

	@Override
	protected void stage6Tasks(int score) {
	}

	@Override
	protected void nextLearningLevel() {
		core.setScreen( Core.levelCompleted("TrainingLevel6")? new TrainingLevel1(true, true) 
															 : new MainMenu(core, 3) );
	}

	@Override
	protected void nextLevel() {
	}

	@Override
	protected Shape getNewShape() {
		return null;
	}

}
