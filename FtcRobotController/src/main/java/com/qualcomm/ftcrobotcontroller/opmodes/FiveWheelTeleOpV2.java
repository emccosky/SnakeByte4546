package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.ftccommon.FtcEventLoopHandler;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FiveWheelTeleOpV2 extends OpMode
{
    DcMotor motorQ1; //FL if manip considered the front, BR if center wheel is front
    DcMotor motorQ2; //FR if manip considered the front, BL if center wheel is front
    DcMotor motorQ3; //BL if manip considered the front, FR if center wheel is front
    DcMotor motorQ4; //BR if manip considered the front, FL if center wheel is front
    DcMotor center;
    DcMotor debrisLift;
    DcMotor motorManip;

    Servo debrisLiftServo1;
    Servo debrisLiftServo2;
    Servo debrisLiftServo3;

    double debrisLiftServoPos1;
    double debrisLiftServoPos2;
    double debrisLiftServoPos3;

    int curMode; //Current mode
    int prevMode;
    boolean hasControllerBeenUsed;

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
    boolean startPhaseRunning;
    boolean startPhaseOver;
    boolean autoHangRunning;

    public void FiveWheelTeleOpV2() {}

    public void init()
    {
        curMode = 1;
        startPhaseRunning = false;
        startPhaseOver = false;
        center = hardwareMap.dcMotor.get("center");
        motorQ1 = hardwareMap.dcMotor.get("motorq1");
        motorQ2 = hardwareMap.dcMotor.get("motorq2");
        motorQ3 = hardwareMap.dcMotor.get("motorq3");
        motorQ4 = hardwareMap.dcMotor.get("motorq4");
        motorManip = hardwareMap.dcMotor.get("manip");
        //debrisLift = hardwareMap.dcMotor.get("debrisLift");
        debrisLiftServo1 = hardwareMap.servo.get("servo1");
        debrisLiftServo2 = hardwareMap.servo.get("servo2");
        debrisLiftServo3 = hardwareMap.servo.get("servo3");
    }

    private double scaleInputSimple(double pwr)
    {
        if(pwr > 0.0)
        {
            if(pwr < 0.05) //0 PWR on chart
                return 0.0;
            else if(pwr >= 0.05 && pwr < 0.10) //0.05 on chart
                return 0.01;
            else if(pwr >= 0.10 && pwr < 0.15) //0.10 on chart
                return 0.02;
            else if(pwr >= 0.15 && pwr < 0.20) //0.15 on chart
                return 0.03;
            else if(pwr >= 0.20 && pwr < 0.25) //0.20 on chart
                return 0.04;
            else if(pwr >= 0.25 && pwr < 0.30) //0.25 on chart
                return 0.05;
            else if(pwr >= 0.30 && pwr < 0.35) //0.30 on chart
                return 0.06;
            else if(pwr >= 0.35 && pwr < 0.40) //0.35 on chart
                return 0.07;
            else if(pwr >= 0.40 && pwr < 0.45) //0.40 on chart
                return 0.075;
            else if(pwr >= 0.45 && pwr < 0.50) //0.45 on chart
                return 0.08;
            else if(pwr >= 0.50 && pwr < 0.55) //0.50 on chart
                return 0.09;
            else if(pwr >= 0.55 && pwr < 0.60) //0.55 on chart
                return 0.10;
            else if(pwr >= 0.60 && pwr < 0.65) //0.60 on chart
                return 0.113;
            else if(pwr >= 0.65 && pwr < 0.70) //0.65 on chart
                return 0.126;
            else if(pwr >= 0.70 && pwr < 0.75) //0.70 on chart
                return 0.14;
            else if(pwr >= 0.75 && pwr < 0.80) //0.75 on chart
                return 0.15;
            else if(pwr >= 0.80 && pwr < 0.85) //0.80 on chart
                return 0.19;
            else if(pwr >= 0.85 && pwr < 0.90) //0.85 on chart
                return 0.225;
            else
                return 1.0;
        }
        else
        {
            if(pwr > -0.05) //0 PWR on chart
                return 0.0;
            else if(pwr <= -0.05 && pwr > -0.10) //0.05 on chart
                return -0.01;
            else if(pwr <= -0.10 && pwr > -0.15) //0.10 on chart
                return -0.02;
            else if(pwr <= -0.15 && pwr > -0.20) //0.15 on chart
                return -0.03;
            else if(pwr <= -0.20 && pwr > -0.25) //0.20 on chart
                return -0.04;
            else if(pwr <= -0.25 && pwr > -0.30) //0.25 on chart
                return -0.05;
            else if(pwr <= -0.30 && pwr > -0.35) //0.30 on chart
                return -0.06;
            else if(pwr <= -0.35 && pwr > -0.40) //0.35 on chart
                return -0.07;
            else if(pwr <= -0.40 && pwr > -0.45) //0.40 on chart
                return -0.075;
            else if(pwr <= -0.45 && pwr > -0.50) //0.45 on chart
                return -0.08;
            else if(pwr <= -0.50 && pwr > -0.55) //0.50 on chart
                return -0.09;
            else if(pwr <= -0.55 && pwr > -0.60) //0.55 on chart
                return -0.10;
            else if(pwr <= -0.60 && pwr > -0.65) //0.60 on chart
                return -0.113;
            else if(pwr <= -0.65 && pwr > -0.70) //0.65 on chart
                return -0.126;
            else if(pwr <= -0.70 && pwr > -0.75) //0.70 on chart
                return -0.14;
            else if(pwr <= -0.75 && pwr > -0.80) //0.75 on chart
                return -0.15;
            else if(pwr <= -0.80 && pwr > -0.85) //0.80 on chart
                return -0.19;
            else if(pwr <= -0.85 && pwr > -0.90) //0.85 on chart
                return -0.225;
            else
                return -1.0;
        }
    }

    public void updateVals() //Updates all variable values
    {
        g1y1 = gamepad1.left_stick_y;
        g1y2 = gamepad1.right_stick_y;
        g1x1 = gamepad1.left_stick_x;
        g1x2 = gamepad1.right_stick_x;
        g2y1 = gamepad1.left_stick_y;
        g2y2 = gamepad1.right_stick_y;
        g2x1 = gamepad1.left_stick_x;
        g2x2 = gamepad1.right_stick_x;
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
        prevMode = curMode;
        if(curMode != 0)
        {
        	if(g2XPressed)
            	curMode = 1;
        	else if(g2BPressed)
            	curMode = 2;
        	//else if(g2YPressed)
        	//    curMode = 3;
        }
    }

    public void autoDumpMedium(int side)
    {
        //Side 0 = Right
        //Side 1 = Left

    }

    public void autoDumpHigh(int side)
    {
        //Side 0 = Right
        //Side 1 = Left
    }

    public void autoHang()
    {
        autoHangRunning = true;
        //Code
        autoHangRunning = false;
    }

    public void startPhaseOffRamp()
    {
        startPhaseRunning = true;
        //Code
        //Drive down the ramp, turn to prepare to pick up things
        startPhaseRunning = false;
        startPhaseOver = true;
    }



	public void runManip(double speed)
	{
		motorManip.setPower(speed);
	}

	public void moveDebrisLift(double speed)
	{
		debrisLift.setPower(speed);
	}

	public void runOddSide(double speed)
	{
		motorQ1.setPower(speed);
		motorQ3.setPower(speed);
	}
	
	public void runEvenSide(double speed)
	{
		motorQ2.setPower(speed);
		motorQ4.setPower(speed);
	}
	
	/*public void setDebrisLiftServos(double pos)
	{
		debrisLiftL.setPosition(pos);
		debrisLiftR.setPosition(1.0 - pos);
	}*/
	
	/*public void moveDebrisLiftServos(double speed)
	{
		debrisLiftPos += speed / 10.0;
		if(debrisLiftPos > 1.0)
			debrisLiftPos = 1.0;
		else if(debrisLiftPos < 1.0)
            debrisLiftPos = 0.0;
		setDebrisLiftServos(debrisLiftPos);
	}*/

	/*public void resetDebrisLiftServos() {
        debrisLiftPos = 0.0;
		setDebrisLiftServos(debrisLiftPos);
	}*/

	public void moveDebrisLiftBasket(double change)
	{
		double ch = change / 10.0;
		debrisLiftServoPos1 += ch;
		debrisLiftServoPos2 += ch;
		debrisLiftServo1.setPosition(debrisLiftServoPos1);
        debrisLiftServo2.setPosition(debrisLiftServoPos2);
	}
	
    public void loop()
    {
        //curMode = 1;
        //updateVals();
        /*if(!startPhaseOver) //Robot has done nothing since start of match
        {
            if(g1y1 > 0.3 || g1y1 < -0.3 || g1y2 > 0.3 || g1y2 < -0.3)
            {
                startPhaseOver = true;
                curMode = 2;
            }
            else if(g1XPressed)
            {
                startPhaseOffRamp();
            }
        }*/
        /*if(curMode == 0) //Stop robot Mode
        {
        	runOddSide(0.0);
        	runEvenSide(0.0);
        	center.setPower(0.0);
        	moveDebrisLift(0.0);
        	moveDebrisLiftServos(0.0);
        	if(g1YPressed)
        		curMode = 2;
        }*/
        if(Math.abs(gamepad1.left_stick_y) > 0.1)
        {
        	debrisLiftServo1.setPosition(0.5 + (gamepad1.left_stick_y / 2.0));
        	debrisLiftServo2.setPosition(0.5 - (gamepad1.left_stick_y / 2.0));
            //telemetry.addData("g1y1", g1y1);
        }

        if(Math.abs(gamepad1.right_stick_y) > 0.1)
		{
			debrisLiftServo3.setPosition(0.5 + (gamepad1.right_stick_y / 2.0));
            //telemetry.addData("g1y2",gamepad1.right_stick_y);
		}        	
        /*if(curMode == 1) //Debris collection Mode
        {
        	if(g2Lbump)
        		runManip(1.0);
        	else if(g2Ltrig > 0.3)
        		runManip(-1.0);
        	else
        		runManip(0.0);
			moveDebrisLiftServo3(g2y2);
			if(g2Lbump)
				moveDebrisLiftBasket(0.7);
			else if(g2Ltrig > 0.3)
				moveDebrisLiftBasket(-0.7);
			moveDebrisLift(scaleInputSimple(g2y1));
			runOddSide(scaleInputSimple(g1y1));
			runEvenSide(scaleInputSimple(g1y2));
			center.setPower(0.0);
        }
        else if(curMode == 2) //Ramp Climbing Mode
        {
            //Stop manipulator
            runManip(0.0);
            moveDebrisLift(scaleInputSimple(g2y1));
            moveDebrisLiftServos(g2y2);
            runOddSide(scaleInputSimple(g1y2));
            runEvenSide(scaleInputSimple(-g1y1));
			if((g1y2 > 0.1 && g1y2 > 0.1) || (g1y2 < -0.1 && g1y2 < -0.1))
				center.setPower(scaleInputSimple(-g1y2));
        }*/
        /*else if(curMode == 3) //Hanging Mode
        {
        	runManip(0.0);
        	//retractDebrisLift();
        	resetDebrisLiftServos();
			runOddSide(scaleInputSimple(g1y2));
            runEvenSide(scaleInputSimple(-g1y1));
            if((g1y1 > 0.1 && g1y2 > 0.1) || (g1y1 < -0.1 && g1y2 < -0.1))
				center.setPower(scaleInputSimple(-g1y2));
        }
        else if(curMode == 4) //Manual Override Mode
        {
            //After Scrimmage
        }
        else if(curMode == 5) //Individual Wheel control
        {
            //After Scrimmage
        }*/
    }

    public void stop()
    {
    
    }
}