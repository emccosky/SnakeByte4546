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

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TeleOp Mode
 * <p>
 *Enables control of the robot via the gamepad
 */
public class RookieCode extends OpMode {
    DcMotor motorBL;
    DcMotor motorBR;
    DcMotor motorFL;
    DcMotor motorFR;
	DcMotor motorLiftL;
	DcMotor motorLiftR;
	Servo servo1;
	Servo servo2;
	double servoPos;

    public RookieCode() {}
    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
        motorBR = hardwareMap.dcMotor.get("motorbl");
        motorBL = hardwareMap.dcMotor.get("motorbr");
        motorFR = hardwareMap.dcMotor.get("motorfl");
        motorFL = hardwareMap.dcMotor.get("motorfr");
		motorLiftL = hardwareMap.dcMotor.get("motorLift");
		servo1 = hardwareMap.servo.get("right");
		servo2 = hardwareMap.servo.get("left");
		servoPos = 0.0;
	}

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
	public void start()
	{
		servo1.setPosition(0.0);
		servo2.setPosition(1.0);
	}

    @Override
    public void loop()
    {
		//Lift
		servo1.setPosition(servoPos);
		servo2.setPosition(1.0 - servoPos);
		if(Math.abs(gamepad2.left_trigger) > 0.1)
		{
			//if(servoPos <= 0.05)
				servoPos = 0.0;
			//else
				//servoPos = servoPos - 0.05;
		}
		else if(Math.abs(gamepad2.right_trigger) > 0.1)
		{
			//if(servoPos >= 0.95)
				servoPos = 0.3;
			//else
				//servoPos = servoPos + 0.05;
		}

		if(gamepad2.left_bumper) //Left Back Bumper controls lift up
		{
			motorLiftL.setPower(1.0);
			//motorLiftR.setPower(-1.0);
		}
		else if(gamepad2.right_bumper) //Right Back Bumper controls lift down
		{
			motorLiftL.setPower(-1.0);
			//motorLiftR.setPower(1.0);
		}
		else //Lift doesn't move if you press nothing
		{
			motorLiftL.setPower(0.0);
			//motorLiftR.setPower(0.0);
		}

		//Tank Drive
        if(Math.abs(gamepad1.left_stick_y) > 0.1) //Left stick moves left side of robot forwards/backwards
		{
			motorFL.setPower(-gamepad1.left_stick_y);
			motorBL.setPower(-gamepad1.left_stick_y);
		}
		else //Left side of robot doesn't move if you don't touch it
		{
			motorFL.setPower(0.0);
			motorBL.setPower(0.0);
		}

		if(Math.abs(gamepad1.right_stick_y) > 0.1) //Right stick controls right side of robot
		{
			motorBR.setPower(gamepad1.right_stick_y);
			motorFR.setPower(gamepad1.right_stick_y);
		}
		else //Right side of robot doesn't move if right stick isn't being moved
		{
			motorFR.setPower(0.0);
			motorBR.setPower(0.0);
		}
    }

    public void stop() {

    }
}