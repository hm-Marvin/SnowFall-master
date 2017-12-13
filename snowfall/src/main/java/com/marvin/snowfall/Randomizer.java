package com.marvin.snowfall;

import java.util.Random;

/**
 * Created by hmw on 2017/11/30.
 */

public class Randomizer {


    private Random random;
    public Randomizer(){
        random = new Random();
    }

    public Double randomDouble(int max){
        return random.nextDouble()*(max +1);
    }

    public int randomInt(int min,int max,boolean gaussian){
        return randomInt(max-min,gaussian)+min;
    }

    public int randomInt(int max,boolean gaussian){
        return gaussian?(int) (Math.abs(randomGaussian())*(max+1)):random.nextInt(max+1);
    }

    public double randomGaussian (){
        double gaussian = random.nextGaussian() / 3;
        return (gaussian>-1&&gaussian<1)?gaussian:randomGaussian();
    }

    public int randomSignum(){
        return random.nextBoolean()?1:-1;
    }
}
