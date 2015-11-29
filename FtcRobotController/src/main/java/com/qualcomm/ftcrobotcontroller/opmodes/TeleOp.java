package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

public class TeleOp extends LinearOp
{
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
	double haveSticksBeenPushedUpSinceModeChanged;

    public TeleOp()
    {
    
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
        if(
	}

	public void updateControllerValsLoop()
	{
		while(opModeIsActive())
		{
			updateControllerVals();
			Thread.sleep(50);
		}
	}

	public void updateMode()
	{
		prevMode = curMode; //Changes the previous mode to the current mode
		 if(curMode != 0) //Updates current mode
        {
        	if(g2XPressed) //If Controller 2 X is pressed, switch to mode 1
            	curMode = 1;
        	else if(g2BPressed) //If Controller 2 B is pressed, switch to mode 2
            	curMode = 2;
            //Else keep current mode
        }
	}

	public void updateModeLoop()
	{
		while(opModeIsActive())
		{
			updateMode();
			Thread.sleep(100);
		}
	}
	public void displayCurMode()
	{
		if(curMode == 1)
			telemetry.addData("Current mode"," Debris Collection");
		else if(curMode == 2)
			telemetry.addData("Current mode"," Climbing");
		else
			telemetry.addData("Current mode", " UNKNOWN");
	}

	public boolean controller1BeingUsed()
	{
		if(g1y1 > 0.1 || g1y1 < 0.1)
			return true;
		if(g1y2 > 0.1 || g1y2 < 0.1)
			return true;
		if(g1Lbump)
			return true;
		if(g1Rbump)
			return true;
		if(g1Ltrig > 0.1)
			return true;
		if(g1Rtrig > 0.1)
			return true;
		if(g1XPressed)
			return true;
		if(g1YPressed)
			return true;
		if(g1APressed)
			return true;
		if(g1BPressed)
			return true;
		return false;
	}

	public boolean controller2BeingUsed()
	{
		if(g2y1 > 0.1 || g2y1 < 0.1)
			return true;
		if(g2y2 > 0.1 || g2y2 < 0.1)
			return true;
		if(g2Lbump)
			return true;
		if(g2Rbump)
			return true;
		if(g2Ltrig > 0.1)
			return true;
		if(g2Rtrig > 0.1)
			return true;
		if(g2XPressed)
			return true;
		if(g2YPressed)
			return true;
		if(g2APressed)
			return true;
		if(g2BPressed)
			return true;
		return false;
	}

	public boolean controllersBeingUsed()
	{
		if(controller2BeingUsed() || controller1BeingUsed())
			return true;
		return false;
	}

	public void mode1()
	{
		//Controller 1
		//Left Stick
		runOddSide(scaleInputSimple(-g1y1)); //Moves left side of robot (Manipulator is front of robot)
		//Right Stick
		runEvenSide(scaleInputSimple(g1y2)); //Moves right side of robot (Manipulator is front of robot)

		//Controller 2
		//Left stick
		moveDebrisLift(scaleInputSimple(-g2y1)); //Moves the lift

		//Right stick
		if (g2y2 > 0.3 || g2y2 < 0.3) //Moves manipulator
		{
			runManip(-g2y2);
		}
		else //Stops manipulator
		{
			runManip(0.0);
		}

		//Moves basket
		//Right Bumper
		if (g2Rbump)
			basketNeutral(); //Moves basket back to neutral position
		else if(g2Rtrig > 0.3) //Right Trigger
			basketLeft(); //Dumps debris to the left
		else if(g2Lbump) //Left Bumper
			basketNeutral(); //Moves basket back to neutral position
		else if(g2Ltrig > 0.3) //Left Trigger
			basketRight(); //Dumps debris to the right

		center.setPower(0.0); //Stops center wheel
	}

	public void mode2()
	{
		
	}

	@Override
	public void runOpMode()
	{
		super.runOpMode();
		//POSITION SETUP IS NOT NEEDED, AS #s ARE SAVED FROM AUTO
		waitForStart();
		updateControllerValsLoop();
		updateModeLoop();
		while(opModeIsActive)
		{
			if(curMode == 1)
			{
				
			}
			else if(curMode == 2)
			{
				
			}
			else
				curMode = 2;
		}
	}
}