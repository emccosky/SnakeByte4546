package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


/**
 * TeleOp Mode
 * <p>
 *Enables control of the robot via the gamepad
 */
public class MotorTester extends OpMode
{
    DcMotor motorQ1;
    DcMotor motorQ2;
    DcMotor motorQ3;
    DcMotor motorQ4;
    DcMotor center;
    DcMotor debrisLift;
	DcMotor motorManip;
    Servo tiltServo;
    Servo servoRFlap;
    Servo servoLFlap;
    Servo servoHitClimberL;
	Servo servoHitClimberR;

    double tiltServoPos;
    double servoRFlapPos;
    double servoLFlapPos;
    double servoHitClimberLPos;
    double servoHitClimberRPos;

    boolean X;
    boolean A;
    boolean B;
    boolean Y;
    boolean rightTrig;
    boolean leftTrig;
    boolean rightBump;
    boolean leftBump;

	boolean switched;

    public MotorTester() {
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

    @Override
    public void init()
    {
		X = false;
		Y = false;
		A = false;
		B = false;
		leftTrig = false;
		rightTrig = false;
		rightBump = false;
		rightTrig = false;
		center = hardwareMap.dcMotor.get("center");
        motorQ1 = hardwareMap.dcMotor.get("motorq1");
        motorQ2 = hardwareMap.dcMotor.get("motorq2");
        motorQ3 = hardwareMap.dcMotor.get("motorq3");
        motorQ4 = hardwareMap.dcMotor.get("motorq4");
        motorManip = hardwareMap.dcMotor.get("manip");
        debrisLift = hardwareMap.dcMotor.get("debrisLift");
        tiltServo = hardwareMap.servo.get("tiltServo");
        servoRFlap = hardwareMap.servo.get("servoRFlap");
        servoLFlap = hardwareMap.servo.get("servoLFlap");
		servoHitClimberL = hardwareMap.servo.get("servoHitClimberL");
		servoHitClimberR = hardwareMap.servo.get("servoHitClimberR");
    }
	
	@Override
	public void start()
	{
		tiltServo.setPosition(0.75);
		servoRFlap.setPosition(0.46);
		servoLFlap.setPosition(0.62);
		servoHitClimberL.setPosition(0.5);
		servoHitClimberR.setPosition(0.5);
		
		tiltServoPos = 0.75;
		servoRFlapPos = 0.46;
		servoLFlapPos = 0.62;
		servoHitClimberLPos = 0.5;
		servoHitClimberRPos = 0.5;
	}

	public void updateVals()
	{
		if(gamepad1.x)
		{
			X = true;
			Y = false;
			A = false;
			B = false;
			rightTrig = false;
			rightBump = false;
			leftTrig = false;
			leftBump = false;
		}
		else if(gamepad1.y)
		{
			X = false;
			Y = true;
			A = false;
			B = false;
			rightTrig = false;
			rightBump = false;
			leftTrig = false;
			leftBump = false;
		}
		else if(gamepad1.a)
		{
			X = false;
			Y = false;
			A = true;
			B = false;
			rightTrig = false;
			rightBump = false;
			leftTrig = false;
			leftBump = false;
		}
		else if(gamepad1.b)
		{
			X = false;
			Y = false;
			A = false;
			B = true;
			rightTrig = false;
			rightBump = false;
			leftTrig = false;
			leftBump = false;
		}
		else if(gamepad1.right_trigger > 0.1)
		{
			X = false;
			Y = false;
			A = false;
			B = false;
			rightTrig = true;
			rightBump = false;
			leftTrig = false;
			leftBump = false;
		}
		else if(gamepad1.right_bumper)
		{
			X = false;
			Y = false;
			A = false;
			B = false;
			rightTrig = false;
			rightBump = true;
			leftTrig = false;
			leftBump = false;
		}
		else if(gamepad1.left_trigger > 0.1)
		{
			X = false;
			Y = false;
			A = false;
			B = false;
			rightTrig = false;
			rightBump = false;
			leftTrig = true;
			leftBump = false;
		}
		else if(gamepad1.left_bumper)
		{
			X = false;
			Y = false;
			A = false;
			B = false;
			rightTrig = false;
			rightBump = false;
			leftTrig = false;
			leftBump = true;
		}
	}
	
	public void stopOthers(String inp)
	{
		if(inp.equals("X"))
		{
			motorQ2.setPower(0.0);
			motorQ4.setPower(0.0);
			motorManip.setPower(0.0);
			debrisLift.setPower(0.0);
			center.setPower(0.0);
		}
		else if(inp.equals("Y"))
		{
			motorQ1.setPower(0.0);
			motorQ3.setPower(0.0);
			motorManip.setPower(0.0);
			debrisLift.setPower(0.0);
			center.setPower(0.0);
		}
		else if(inp.equals("A"))
		{
			motorQ1.setPower(0.0);
			motorQ2.setPower(0.0);
			motorQ3.setPower(0.0);
			motorQ4.setPower(0.0);
			center.setPower(0.0);
		}
		else if(inp.equals("B"))
		{
			motorQ1.setPower(0.0);
			motorQ2.setPower(0.0);
			motorQ3.setPower(0.0);
			motorQ4.setPower(0.0);
			motorManip.setPower(0.0);
			debrisLift.setPower(0.0);
		}
		else
		{
			motorQ1.setPower(0.0);
			motorQ2.setPower(0.0);
			motorQ3.setPower(0.0);
			motorQ4.setPower(0.0);
			motorManip.setPower(0.0);
			debrisLift.setPower(0.0);
			center.setPower(0.0);
		}
	}

	@Override
	public void loop()
	{
		updateVals();
		if(X)
		{
			stopOthers("X");
			double spdL = scaleInputSimple(gamepad1.left_stick_y);
			double spdR = scaleInputSimple(gamepad1.right_stick_y);
			telemetry.addData("Left stick controls motorQ1", String.format("%.2f",gamepad1.left_stick_y)); //FL if manip is front
			telemetry.addData("Right stick controls motorQ3", String.format("%.2f",gamepad1.right_stick_y)); //BL if manip is front
			motorQ1.setPower(spdL);
			motorQ3.setPower(spdR);
			switched = true;
		}
		else if(Y)
		{
			stopOthers("Y");
			double spdL = scaleInputSimple(gamepad1.left_stick_y);
			double spdR = scaleInputSimple(gamepad1.right_stick_y);
			telemetry.addData("Left stick controls motorQ2", String.format("%.2f",gamepad1.left_stick_y)); //FR if manip is front
			telemetry.addData("Right stick controls motorQ4", String.format("%.2f",gamepad1.right_stick_y)); //BR if manip is front
			motorQ2.setPower(spdL);
			motorQ4.setPower(spdR);
		}
		else if(A)
		{
			stopOthers("A");
			double spdL = scaleInputSimple(gamepad1.left_stick_y);
			double spdR = scaleInputSimple(gamepad1.right_stick_y);
			telemetry.addData("Left stick controls manip", String.format("%.2f",gamepad1.left_stick_y));
			telemetry.addData("Right stick controls lift", String.format("%.2f",gamepad1.right_stick_y));
			motorManip.setPower(spdL);
			debrisLift.setPower(spdR);
		}
		else if(B)
		{
			stopOthers("B");
			double spd = scaleInputSimple(gamepad1.left_stick_y);
			telemetry.addData("Left stick controls center wheel", String.format("%.2f",gamepad1.left_stick_y));
			center.setPower(spd);
		}
		else if(rightTrig)
		{
			stopOthers("RT");
			double spdL = gamepad1.left_stick_y;
			double spdR = gamepad1.right_stick_y;

			if(Math.abs(spdL) > 0.1)
				servoHitClimberLPos += (spdL / 400);
			if(servoHitClimberLPos < 0)
				servoHitClimberLPos = 0.0;
			else if(servoHitClimberLPos > 1.0)
				servoHitClimberLPos = 1.0;

			if(Math.abs(spdR) > 0.1)
				servoHitClimberRPos += (spdR / 400);
			if(servoHitClimberRPos < 0)
				servoHitClimberRPos = 0.0;
			else if(servoHitClimberRPos > 1.0)
				servoHitClimberRPos = 1.0;

			servoHitClimberL.setPosition(servoHitClimberLPos);
			servoHitClimberR.setPosition(servoHitClimberRPos);
			telemetry.addData("Left stick controls servoHitClimberL", String.format("%.2f",servoHitClimberLPos));
			telemetry.addData("Right stick controls servoHitClimberR", String.format("%.2f",servoHitClimberRPos));
		}
		else if(leftTrig)
		{
			stopOthers("LT");
			double spdL = gamepad1.left_stick_y;
			double spdR = gamepad1.right_stick_y;

			if(Math.abs(spdL) > 0.1)
				servoLFlapPos += (spdL / 400);
			if(servoLFlapPos < 0)
				servoLFlapPos = 0.0;
			else if(servoLFlapPos > 1.0)
				servoLFlapPos = 1.0;

			if(Math.abs(spdR) > 0.1)
				servoRFlapPos += (spdR / 400);
			if(servoRFlapPos < 0)
				servoRFlapPos = 0.0;
			else if(servoRFlapPos > 1.0)
				servoRFlapPos = 1.0;

			servoLFlap.setPosition(servoLFlapPos);
			servoRFlap.setPosition(servoRFlapPos);
			telemetry.addData("Left stick controls servoLFlap", String.format("%.2f",servoLFlapPos));
			telemetry.addData("Right stick controls servoRFlap", String.format("%.2f",servoRFlapPos));
		}
		else if(rightBump)
		{
			stopOthers("RB");
			double spd = gamepad1.left_stick_y;

			if(Math.abs(spd) > 0.1)
				tiltServoPos += (spd / 400);
			if(tiltServoPos < 0)
				tiltServoPos = 0.0;
			else if(tiltServoPos > 1.0)
				tiltServoPos = 1.0;

			tiltServo.setPosition(tiltServoPos);
			telemetry.addData("Left stick controls tiltServo", String.format("%.2f",tiltServoPos));
		}
		else if(leftBump)
		{
			stopOthers("LB");
			telemetry.addData("This mode does not do anything","");
		}
	}
}
