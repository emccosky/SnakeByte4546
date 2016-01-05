package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

public class TeleOp extends LinearOp {
	//Controller values
	double g1y1;
	double g1y2;
	double g1x1;
	double g1x2;
	double g2y1;
	double g2y2;
	double g2x1;
	double g2x2;
	boolean g1Lbump;
	boolean g1Rbump;
	boolean g2Lbump;
	boolean g2Rbump;
	double g1Ltrig;
	double g1Rtrig;
	double g2Ltrig;
	double g2Rtrig;
	boolean g1XPressed;
	boolean g2XPressed;
	boolean g1YPressed;
	boolean g2YPressed;
	boolean g1APressed;
	boolean g2APressed;
	boolean g1BPressed;
	boolean g2BPressed;

	//Information about dumps completed
	double numHighDumps;
	double numMediumDumps;
	double numLowDumps;

	//Mode variables
	int curMode;
	int prevMode;

	//Special driving vars
	boolean haveSticksBeenPushedUpSinceModeChanged;

	public TeleOp() {

	}

	public void updateControllerVals()
	{
		g1y1 = gamepad1.left_stick_y;
		g1y2 = gamepad1.right_stick_y;
		g1x1 = gamepad1.left_stick_x;
		g1x2 = gamepad1.right_stick_x;
		g2y1 = gamepad2.left_stick_y;
		g2y2 = gamepad2.right_stick_y;
		g2x1 = gamepad2.left_stick_x;
		g2x2 = gamepad2.right_stick_x;
		g1Lbump = gamepad1.left_bumper;
		g1Rbump = gamepad1.right_bumper;
		g2Lbump = gamepad2.left_bumper;
		g2Rbump = gamepad2.right_bumper;
		g1Ltrig = gamepad1.left_trigger;
		g1Rtrig = gamepad1.right_trigger;
		g2Ltrig = gamepad2.left_trigger;
		g2Rtrig = gamepad2.right_trigger;
		g1XPressed = gamepad1.x;
		g1APressed = gamepad1.a;
		g1YPressed = gamepad1.y;
		g1BPressed = gamepad1.b;
		g2XPressed = gamepad2.x;
		g2APressed = gamepad2.a;
		g2YPressed = gamepad2.y;
		g2BPressed = gamepad2.b;
	}

	public void updateControllerValsLoop() {
		while (opModeIsActive()) {
			updateControllerVals();
			sleep(200);
		}
	}

	public void updateMode() {
		prevMode = curMode; //Changes the previous mode to the current mode
		if (curMode != 0) //Updates current mode
		{
			if (g2XPressed && curMode != 1) //If Controller 2 X is pressed, switch to mode 1
			{
				curMode = 1;
				if (g1y1 < -0.1 && g1y2 < -0.1) //Are sticks pushed down
				else
					haveSticksBeenPushedUpSinceModeChanged = false; //The sticks have not been pushed up
			} else if ((g2BPressed && curMode != 2)) //|| (curPitch > 10 || curPitch < -10)) //If Controller 2 B is pressed, switch to mode 2
			{
				curMode = 2;
				if (g1y1 < -0.1 && g1y2 < -0.1) //Are sticks pushed up
					haveSticksBeenPushedUpSinceModeChanged = true; //The sticks have been pushed up
				else
					haveSticksBeenPushedUpSinceModeChanged = false; //The sticks have not been pushed up
			}
			//Else keep current mode
		}
		if (g1y1 < 0 || g1y2 < 0) //Are sticks pushed up
			haveSticksBeenPushedUpSinceModeChanged = true; //The sticks have been pushed up
	}

	@Override
	public void wiggleTilt(String side)
	{
		if(side.equals("right"))
		{
			tiltServo.setPosition(0.77);
			sleep(6);
			tiltServo.setPosition(0.9);
			sleep(2);
			tiltServoPos = 0.87;
		}
		else
		{
			tiltServo.setPosition(0.73);
			sleep(6);
			tiltServo.setPosition(0.52);
			sleep(2);
			tiltServoPos = 0.58;
			/*tiltServoPos = moveServoVel(tiltServo, tiltServoPos, 0.62, 0.06);
			tiltServoPos = moveServoVel(tiltServo, tiltServoPos, 0.58, 0.06);*/
		}
	}

	@Override
	public void dumpLeft()
	{
		boolean hasBeenCancelled = false;
		//if(isTiltLeft)
		//{
		tiltLeft();

		openLeftFlap();
		do{
			if(!hasBeenCancelled) {
				updateControllerVals();
				if (g1y1 > 0.1 || g1y1 < -0.1 || g1y2 < -0.1 || g1y2 > 0.1) {
					runEvenSide(scaleInputSimple(-g1y1)); //Moves left side of robot (Center wheel is front of robot)
					//Right Stick
					runOddSide(scaleInputSimple(g1y2)); //Moves right side of robot (Center wheel is front of robot)
					motorQ1.setPower(1.0);
					hasBeenCancelled = true;
				}
				wiggleTilt("left");

				sleep(5);
			}
		}while(!controller2BumpersBeingUsed());
		if(hasBeenCancelled)
			wiggleTilt("left");
		//wiggleTilt("left");
		/*}
		else if(isTiltRight)
		{
			unlockRightFlap();
			unlockLeftFlap();
			sleep(300);
			tiltLeft();
			sleep(600);
			openLeftFlap();
		}
		else
		{
			unlockLeftFlap();
			sleep(300);
			tiltLeft();
			sleep(500);
			openLeftFlap();
		}
		sleep(500);
		wiggleTilt("left");
		sleep(700);
		wiggleTilt("left");
		sleep(700);
		lockLeftFlap();
		isTiltRight = false;
		isTiltLeft = true;*/
	}

	@Override
	public void dumpRight()
	{
		//if(isTiltRight)
		//{
		tiltRight();
		openRightFlap();
		do{
			wiggleTilt("right");
			sleep(2);
		}while(!controller2BumpersBeingUsed());
		//wiggleTilt("right");
		//wiggleTilt("right");
		/*}
		else if(isTiltLeft)
		{
			unlockLeftFlap();
			unlockRightFlap();
			sleep(200);
			tiltRight();
			sleep(200);
			openRightFlap();
		}
		else
		{
			unlockRightFlap();
			sleep(200);
			tiltRight();
			sleep(200);
			openRightFlap();
		}
		sleep(200);
		wiggleTilt("right");
		sleep(200);
		wiggleTilt("right");
		sleep(200);
		lockRightFlap();
		isTiltRight = true;
		isTiltLeft = false;*/
	}
	public void updateModeLoop() {
		while (opModeIsActive()) {
			updateMode();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	public void displayCurMode()
	{
		if (curMode == 1)
			telemetry.addData("Current mode", " Debris Collection");
		else if (curMode == 2)
			telemetry.addData("Current mode", " Climbing");
		else
			telemetry.addData("Current mode", " UNKNOWN");
	}

	public boolean controller1BeingUsed() {
		updateControllerVals();
		if (g1y1 > 0.1 || g1y1 < 0.1)
			return true;
		if (g1y2 > 0.1 || g1y2 < 0.1)
			return true;
		if (g1Lbump)
			return true;
		if (g1Rbump)
			return true;
		if (g1Ltrig > 0.1)
			return true;
		if (g1Rtrig > 0.1)
			return true;
		if (g1XPressed)
			return true;
		if (g1YPressed)
			return true;
		if (g1APressed)
			return true;
		if (g1BPressed)
			return true;
		return false;
	}

	public boolean controller2BeingUsed() {
		updateControllerVals();
		if (g2y1 > 0.1 || g2y1 < 0.1)
			return true;
		if (g2y2 > 0.1 || g2y2 < 0.1)
			return true;
		if (g2Lbump)
			return true;
		if (g2Rbump)
			return true;
		if (g2Ltrig > 0.1)
			return true;
		if (g2Rtrig > 0.1)
			return true;
		if (g2XPressed)
			return true;
		if (g2YPressed)
			return true;
		if (g2APressed)
			return true;
		if (g2BPressed)
			return true;
		return false;
	}

	public boolean controller2BumpersBeingUsed()
	{
		updateControllerVals();
		if(g2Lbump)
			return true;
		if(g2Rbump)
			return true;
		return false;
	}

	public boolean controllersBeingUsed()
	{
		if (controller2BeingUsed() || controller1BeingUsed())
			return true;
		return false;
	}

	public void mode1()
	{
		//Controller 1
		//Left Stick
		runOddSide(scaleInputSimple(-g1y1)); //Moves left side of robot (Manipulator is front of robot) 1
		//Right Stick
		runEvenSide(scaleInputSimple(g1y2)); //Moves right side of robot (Manipulator is front of robot) -1

		//Controller 2
		//Left stick
		moveDebrisLift(scaleInputSimple(-g2y1)); //Moves the lift

		//Right stick
		if (g2y2 > 0.3 || g2y2 < 0.3) //Moves manipulator
		{
			runManip(-g2y2);
		} else //Stops manipulator
		{
			runManip(0.0);
		}

		//Moves basket
		//Right Bumper
		if (g2Lbump)
			basketInitBlue(); //Moves basket back to neutral position
		else if (g2Ltrig > 0.3) //Right Trigger
			dumpLeft(); //Dumps debris to the left
		else if (g2Rbump) //Left Bumper
			basketInitRed(); //Moves basket back to neutral position
		else if (g2Rtrig > 0.3) //Left Trigger
			dumpRight(); //Dumps debris to the right

		center.setPower(0.0); //Stops center wheel
	}

	public void mode2() {
		//Controller 1
		if(!haveSticksBeenPushedUpSinceModeChanged) //Sticks have not been pushed up
		{
			//Controller 1
			//Left Stick
			runOddSide(scaleInputSimple(-g1y1)); //Moves left side of robot (Manipulator is front of robot)
			//Right Stick
			runEvenSide(scaleInputSimple(g1y2)); //Moves right side of robot (Manipulator is front of robot)
			center.setPower(0.0);
		}
		else
		{
			//runOddSide(0.9);
			//Left Stick
			runEvenSide(scaleInputSimple(-g1y1)); //Moves left side of robot (Center wheel is front of robot)
			//Right Stick
			runOddSide(scaleInputSimple(g1y2)); //Moves right side of robot (Center wheel is front of robot)

			//Both sticks
			
			if ((g1y1 > 0.1 && g1y1 > 0.1) || (g1y2 < -0.1 && g1y2 < -0.1)) //If both sticks being moved
				center.setPower(scaleInputSimple(g1y2)); //Move center wheel along with other wheels
			else
				center.setPower(0.0);
		}

		//Controller 2
		//Left stick
		moveDebrisLift(scaleInputSimple(-g2y1)); //Moves the lift

		if (g2Lbump) //Right Bumper
		{
			basketInitBlue(); //Goes to neutral
		} else if (g2Ltrig > 0.3) //Right trigger
		{
			dumpLeft(); //Dumps on right side
		} else if (g2Rbump) //Left Bumper
		{
			basketInitRed(); //Goes to neutral
		} else if (g2Rtrig > 0.3) //Left Trigger
		{
			dumpRight(); //Dumps on left side
		}
		motorManip.setPower(0.0); //Turns off manipulato
	}

	public void displayDistanceLoop()
	{
		while(opModeIsActive())
		{
			displayDistance();
			sleep(200);
		}
	}

	@Override
	public void runOpMode()
	{
		curMode = 1;
		isBlueSide = true;
		isRedSide = false;
		super.runOpMode();
		updateServoPos();
		//POSITION SETUP IS NOT NEEDED, AS #s ARE SAVED FROM AUTO
		try{waitForStart();}catch(InterruptedException e){}
		//startIMU();
		//updateControllerValsLoop();
		/*updateModeLoop();
		displayDistanceLoop();
		updatePositionLoop();*/
		//basketInitBlue();
		while(opModeIsActive()) {
			if (g1Rtrig > 0.1)
				extendRightClimberServo();
			if (g1Rbump)
				retractRightClimberServo();
			if (g1Lbump)
				extendLeftClimberServo();
			if (g1Ltrig > 0.1)
				retractLeftClimberServo();
			//updatePosition();
			updateControllerVals();
			updateServoPos();
			updateMode();
			//displayDistance();
			/*if (g1YPressed)
			{
				extendDumpClimberArm();
			}
			else if(g2APressed)
			{
				retractDumpClimberArm();
			}
			else
			{
				setDumpClimberArm(0.5);
			}*/
			if(curMode == 1)
			{
				mode1();
			}
			else if(curMode == 2)
			{
				mode2();
			}
			else
				curMode = 2;
			//mode2();
			sleep(20);
		}
	}
}