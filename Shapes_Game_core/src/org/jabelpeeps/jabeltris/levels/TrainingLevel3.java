package org.jabelpeeps.jabeltris.levels;

import org.jabelpeeps.jabeltris.BorderButtonsInput;
import org.jabelpeeps.jabeltris.Core;
import org.jabelpeeps.jabeltris.DemoGameLogic;
import org.jabelpeeps.jabeltris.LevelMaster;
import org.jabelpeeps.jabeltris.Shape;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class TrainingLevel3 extends LevelMaster {

	private Shape newShape;
	private int demoStage = 1;
	private Vector3 touch = new Vector3();
	private float alpha = 0.3f;
	private boolean playOn = true;
	private boolean playingLearningLevels = false;
	private boolean levelIsFinished = false;
	
// ---------------------------------------------Constructors--------	
	public TrainingLevel3(Core c) {
		this(c, true, false);
	}
	public TrainingLevel3(Core c, boolean playNext) {	
		this(c, playNext, false);
	}
	public TrainingLevel3(Core c, boolean playNext, boolean learningLevels) {
		super(c);
		playOn = playNext;
		playingLearningLevels = learningLevels;
		baseColor = new Color(1f, 1f, 1f, 1f);
		x = 6;
		y = 6;
		initPlayArea();
		logic = new DemoGameLogic(game);
		setupInput(new BorderButtonsInput(game, logic));
		logic.start();
	}

// ---------------------------------------------Methods----------
	@Override
	public void render(float delta) {
	}

	@Override
	protected Shape makeNewShape(int i, int j) {
		return null;
	}

}
