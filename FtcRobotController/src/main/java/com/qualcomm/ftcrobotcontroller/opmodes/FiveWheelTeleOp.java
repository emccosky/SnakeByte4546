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
public class FiveWheelTeleOp extends OpMode {
    DcMotor motorBL;
    DcMotor motorBR;
    DcMotor motorFL;
    DcMotor motorFR;
	DcMotor center;

    public FiveWheelTeleOp() {}
    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
        motorBL = hardwareMap.dcMotor.get("motorbl");
        motorBR = hardwareMap.dcMotor.get("motorbr");
        motorFR = hardwareMap.dcMotor.get("motorfr");
        motorFL = hardwareMap.dcMotor.get("motorfl");
		center = hardwareMap.dcMotor.get("motorcenter");
    }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void loop() 
    {
        float y1 = gamepad1.left_stick_y;
		//float x1 = gamepad1.left_stick_x;
		//float x2 = gamepad1.right_stick_x;
		float y2 = gamepad1.right_stick_y;
		//if same
		//move center wheel
		//else
		//dont move center wheel
		if((y1 > 0.1 && y2 > 0.1) || (y1 < -0.1 && y2 < -0.1)) //if moving same direction
		{ //move front wheel also
			center.setPower(y1);
			motorFR.setPower(y1);
			motorBR.setPower(y1);
			motorBL.setPower(-y2);
			motorFL.setPower(-y2);
		}
		else //else (sticks not moving same direction
		{
			center.setPower(0.0);
			if(y1 > 0.1 || y1 < -0.1)
			{
				motorFR.setPower(y1);
				motorBR.setPower(y1);
			}
			else
			{
				motorBR.setPower(0.0);
				motorFR.setPower(0.0);
			}
			if(y2 > 0.1 || y2 < -0.1)
			{
				motorFL.setPower(-y1);
				motorBL.setPower(-y1);
			}
			else
			{
				motorFL.setPower(0.0);
				motorBL.setPower(0.0);
			}
		}
    }

    public void stop() {

    }
}