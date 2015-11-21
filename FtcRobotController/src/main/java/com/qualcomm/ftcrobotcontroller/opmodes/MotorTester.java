/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc
All rights reserved.
Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:
Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.
Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.
NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

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
		tiltServo.setPosition(0.5);
		servoRFlap.setPosition(0.5);
		servoLFlap.setPosition(0.5);
		servoHitClimberL.setPosition(0.5);
		servoHitClimberR.setPosition(0.5);
		
		tiltServoPos = 0.5;
		servoRFlap = 0.5;
		servoLFlap = 0.5;
		servoHitClimberL = 0.5;
		servoHitClimberR = 0.5;
	}
	
	public static void updateVals()
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
	
	@Override
	public void loop()
	{
		updateVals();
		if(X)
		{
			double spdL = scaleInputSimple(gamepad1.left_stick_y);
			double spdR = scaleInputSimple(gamepad1.right_stick_y);
			telemetry.addData("Left stick controls motorQ1" + String.format("%.2f",gamepad1.left_stick_y)); //FL if manip is front
			telemetry.addData("Right stick controls motorQ3" + String.format("%.2f",gamepad1.right_stick_y)); //BL if manip is front
			motorQ1.setPower(spdL);
			motorQ3.setPower(spdR);
		}
		else if(Y)
		{
			double spdL = scaleInputSimple(gamepad1.left_stick_y);
			double spdR = scaleInputSimple(gamepad1.right_stick_y);
			telemetry.addData("Left stick controls motorQ2" + String.format("%.2f",gamepad1.left_stick_y)); //FR if manip is front
			telemetry.addData("Right stick controls motorQ4" + String.format("%.2f",gamepad1.right_stick_y)); //BR if manip is front
			motorQ2.setPower(spdL);
			motorQ4.setPower(spdR);
		}
		else if(A)
		{
			double spdL = scaleInputSimple(gamepad1.left_stick_y);
			double spdR = scaleInputSimple(gamepad1.right_stick_y);
			telemetry.addData("Left stick controls manip" + String.format("%.2f",gamepad1.left_stick_y));
			telemetry.addData("Right stick controls lift" + String.format("%.2f",gamepad1.right_stick_y));
			manip.setPower(spdL);
			debrisLift.setPower(spdR);
		}
		else if(B)
		{
			double spd = scaleInputSimple(gamepad1.left_stick_y);
			telemetry.addData("Left stick controls center wheel " + String.format("%.2f",gamepad1.left_stick_y);
			center.setPower(spd);
		}
		else if(rightTrig)
		{
			double spdL = gamepad1.left_stick_y;
			double spdR = gamepad1.right_stick_y;
			
			servoHitClimberLPos += (spdL / 50);
			if(servoHitClimberLPos < 0)
				servoHitClimberLPos = 0.0;
			else if(servoHitClimberLPos > 1.0)
				servoHitClimberLPos = 1.0;

			servoHitClimberRPos += (spdR / 50);
			if(servoHitClimberRPos < 0)
				servoHitClimberRPos = 0.0;
			else if(servoHitClimberRPos > 1.0)
				servoHitClimberRPos = 1.0;
			
			
			servoHitClimberL.setPosition(servoHitClimberLPos);
			servoHitClimberR.setPosition(servoHitClimberRPos);
			telemetry.addData("Left stick controls servoHitClimberL" + String.format("%.2f",servoHitClimberLPos));
			telemetry.addData("Right stick controls servoHitClimberR" + String.format("%.2f",servoHitClimberRPos));
		}
		else if(leftTrig)
		{
			double spdL = gamepad1.left_stick_y;
			double spdR = gamepad1.right_stick_y;
			
			servoLFlapPos += (spdL / 50);
			if(servoLFlapPos < 0)
				servoLFlapPos = 0.0;
			else if(servoLFlapPos > 1.0)
				servoLFlapPos = 1.0;

			servoRFlapPos += (spdR / 50);
			if(servoRFlapPos < 0)
				servoRFlapPos = 0.0;
			else if(servoRFlapPos > 1.0)
				servoRFlapPos = 1.0;
			
			servoLFlap.setPosition(servoLFlapPos);
			servoRFlap.setPosition(servoRFlapPos);
			telemetry.addData("Left stick controls servoLFlap" + String.format("%.2f",servoLFlapPos));
			telemetry.addData("Right stick controls servoRFlap" + String.format("%.2f",servoRFlapPos));
		}
		else if(rightBump)
		{
			double spd = gamepad1.left_stick_y;
			
			tiltServoPos += (spd / 50);
			if(tiltServoPos < 0)
				tiltServoPos = 0.0;
			else if(tiltServoPos > 1.0)
				tiltServoPos = 1.0;
			
			tiltServo.setPosition(tiltServoPos);
			telemetry.addData("Left stick controls tiltServo" + String.format("%.2f",tiltServoPos));
		}
		else if(leftBump)
		{
			telemetry.addData("This mode does not do anything");		
		}
	}
}
