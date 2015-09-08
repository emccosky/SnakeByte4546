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

    public RookieCode() {}
    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
        motorBL = hardwareMap.dcMotor.get("motor_1");
        motorBR = hardwareMap.dcMotor.get("motor_2");
        motorFR = hardwareMap.dcMotor.get("motor_3");
        motorFL = hardwareMap.dcMotor.get("motor_4");
		motorLiftL = hardwareMap.dcMotor.get("motor_5");
		motorLiftR = hardwareMap.dcMotor.get("motor_5");
    }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void loop()
    {
		//Lift

		if(gamepad1.left_bumper)
		{
			motorLiftL.setPower(1.0);
			motorLiftR.setPower(-1.0);
		}
		else if(gamepad1.right_bumper)
		{
			motorLiftL.setPower(1.0);
			motorLiftR.setPower(-1.0);
		}
		else
		{
			motorLiftL.setPower(0.0);
			motorLiftR.setPower(0.0);
		}

		//Tank Drive
        if(Math.abs(gamepad1.left_stick_y) > 0.1)
		{
			motorFL.setPower(-gamepad1.left_stick_y);
			motorBL.setPower(-gamepad1.left_stick_y);
		}
		else
		{
			motorFL.setPower(0.0);
			motorBL.setPower(0.0);
		}
		if(Math.abs(gamepad1.right_stick_y) > 0.1)
		{
			motorBR.setPower(gamepad1.left_stick_y);
			motorFR.setPower(gamepad1.left_stick_y);
		}
		else
		{
			motorFR.setPower(0.0);
			motorBR.setPower(0.0);
		}
    }

    public void stop() {

    }
}