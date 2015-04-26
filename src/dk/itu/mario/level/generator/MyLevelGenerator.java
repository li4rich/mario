package dk.itu.mario.level.generator;

import java.util.Random;

import dk.itu.mario.MarioInterface.Constraints;
import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelGenerator;
import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.level.CustomizedLevel;
import dk.itu.mario.level.MyLevel;

public class MyLevelGenerator extends CustomizedLevelGenerator implements LevelGenerator{

    public static int GENERATION_LIMIT = 100;

	public LevelInterface generateLevel(GamePlay playerMetrics) {
    
        // Initialization
        System.out.println("hello");
        System.out.println("\n*Creating Adam, Eve, and Steve*\n");
        MyLevel[] levelPool = new MyLevel[20];
        float[] fit = new float[20];
        for (int i = 0; i < levelPool.length; i++) {
            levelPool[i] = new MyLevel(320,15,new Random().nextLong(),1,LevelInterface.TYPE_OVERGROUND,playerMetrics);
        }
        
        // Generational loop
        System.out.println("\n*Fast Forwading Millenia*\n");
        for (int i = 0; i < GENERATION_LIMIT; i++) {
            // TODO: change this to use a heap for run time/clarity (?)
            for (int l = 0; l < levelPool.length; l++) {
                fit[l] = FitnessTest(playerMetrics, levelPool[l]);
            }
            int[] top10index = new int[10];
            for (int j = 0; j < top10index.length; j++) {
                float max = fit[0];
                int index = 0;
                for (int k = 0; k < fit.length; k++) {
                    if (fit[k] > max) {
                        max = fit[k];
                        index = k;
                    }
                }
                fit[index] = 0;
                top10index[j] = index;
            }
            
            for (int j = 0; j < top10index.length; j++) {
                levelPool[j] = levelPool[top10index[j]];
            }
            
            // Reproduce
            for (int j = 0; j < 5; j++) {
                levelPool[10+j*2] = this.generateBoy(levelPool[2*j], levelPool[2*j+1]);
                levelPool[11+j*2] = this.generateGirl(levelPool[2*j], levelPool[2*j+1]);
            }
            
            
            // Mutations
            for (int j = 0; j < 10; j++) {
                levelPool[j] = this.mutate(levelPool[j]);
            }
        }
        
        // Selecting best level
        System.out.println("*Selecting Apex*");
        for (int l = 0; l < levelPool.length; l++) {
                fit[l] = FitnessTest(playerMetrics, levelPool[l]);
        }
        float max = fit[0];
        MyLevel result = levelPool[0];
        for (int k = 0; k < fit.length; k++) {
            if (fit[k] > max) {
                max = fit[k];
                result = levelPool[k];
            }
        }
        
		//LevelInterface level = new MyLevel(320,15,new Random().nextLong(),1,LevelInterface.TYPE_OVERGROUND,playerMetrics);
		//System.out.println(FitnessTest(playerMetrics,(MyLevel)level));
		return result;
	}
    
    public MyLevel generateBoy(MyLevel level1, MyLevel level2) {
        return level1.breedWith(level2);
    }
    
    public MyLevel generateGirl(MyLevel level1, MyLevel level2) {
        return level2.breedWith(level1);
    }
    
    public MyLevel mutate(MyLevel level) {
        return level.mutate();
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
        int jumps = metrics.jumpsNumber;

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
        value += jumps*level.gaps;

        return value;
    }
}
