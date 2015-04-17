package dk.itu.mario.level.generator;

import java.util.Random;

import dk.itu.mario.MarioInterface.Constraints;
import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelGenerator;
import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.level.CustomizedLevel;
import dk.itu.mario.level.MyLevel;

public class MyLevelGenerator extends CustomizedLevelGenerator implements LevelGenerator{

	public LevelInterface generateLevel(GamePlay playerMetrics) {
		System.out.println("----GENERATING----");
		LevelInterface level = new MyLevel(320,15,new Random().nextLong(),1,LevelInterface.TYPE_OVERGROUND,playerMetrics);
		System.out.println(FitnessTest(playerMetrics,(MyLevel)level));
		return level;
	}
	public LevelInterface generateLevel(GamePlay playerMetrics,GamePlay playerMetrics1,GamePlay playerMetrics2,GamePlay playerMetrics3){
		LevelInterface level = generateLevel(playerMetrics);		
		System.out.println(FitnessTest(playerMetrics1,(MyLevel)level));
		System.out.println(FitnessTest(playerMetrics2,(MyLevel)level));
		System.out.println(FitnessTest(playerMetrics3,(MyLevel)level));
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//System.exit(0);
		return level;
	}

	@Override
	public LevelInterface generateLevel(String detailedInfo) {
		// TODO Auto-generated method stub
		System.out.println("PLEASE DONT PRINT THIS");
		return null;
	}
    
    public float FitnessTest(GamePlay metrics, MyLevel level) {
        //TODO: implement fitness algorithm to evaluate levels fitness based on playerMetrics

        float value=0;

        double pBD = metrics.percentageBlocksDestroyed; //percentage of all blocks destroyed
		double pCBD = metrics.percentageCoinBlocksDestroyed; //percentage of coin blocks destroyed
		double pEBD = metrics.percentageEmptyBlockesDestroyed; //percentage of empty blocks destroyed
		double pPBD = metrics.percentagePowerBlockDestroyed; //percentage of power blocks destroyed

		double pCoins = ((double)metrics.coinsCollected)/metrics.totalCoins;

		double pKilled = ((metrics.RedTurtlesKilled+metrics.GreenTurtlesKilled+metrics.ArmoredTurtlesKilled+metrics.GoombasKilled+
							metrics.CannonBallKilled+metrics.JumpFlowersKilled+metrics.ChompFlowersKilled)/(double)metrics.totalEnemies);

		//difficulty
		int lTime = metrics.totalTimeLittleMode; //total time spent in little mode
		int bTime = metrics.totalTimeLargeMode; //total time spent in large mode
		int fTime = metrics.totalTimeFireMode; //total time spent in fire mode

		int blocks = level.BLOCKS_EMPTY + level.BLOCKS_COINS+ level.BLOCKS_POWER;

		value += pBD*blocks;
		value += pCBD*level.BLOCKS_COINS;
		value += pPBD*level.BLOCKS_POWER;
		value += pCoins*level.COINS;
		value += pKilled*level.ENEMIES;

        return value;
    }
}
