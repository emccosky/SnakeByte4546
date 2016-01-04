package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

public class RedAutonomous extends Autonomous
{
    public RedAutonomous()
    {

    }

    @Override
    public void runOpMode()
    {
    	isRedSide = true;
		isBlueSide = false;
    	/*double startX = 0.0;
    	double startY = 0.0;
    	double startZ = 0.0;
    	double startYaw = 0.0;
    	double startPitch = 0.0;
    	double startRoll = 0.0;
		initPosition(startX, startY, startZ, startYaw, startPitch, startRoll);*/
		super.runOpMode();
		double oddSideAvg = (Math.abs(motorQ1Pos) + Math.abs(motorQ3Pos)) / 2;
		double evenSideAvg = (Math.abs(motorQ2Pos) + Math.abs(motorQ4Pos)) / 2;
		while(((evenSideAvg < (oddSideAvg + 1000)) || (oddSideAvg < (evenSideAvg + 1000))) && opModeIsActive())
		{
			runOddSide(0.7);
			runEvenSide(-0.7);
			oddSideAvg = (Math.abs(motorQ1Pos) + Math.abs(motorQ3Pos)) / 2;
			evenSideAvg = (Math.abs(motorQ2Pos) + Math.abs(motorQ4Pos)) / 2;
			sleep(15);
		}
		if(opModeIsActive()) {
			sleep(500);
	//		extendDumpClimberArm();
			sleep(1000);
	//		retractDumpClimberArm();
		}
		/*ACTUAL AUTONOMOUS MOVEMENT HERE, EX:
		moveTo(100,100);
		raiseLift(1000);
		dumpRight();
		*/
    }
}