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
public class FiveWheelTeleOp extends OpMode {
    DcMotor motorBL;
    DcMotor motorBR;
    DcMotor motorFL;
    DcMotor motorFR;
	DcMotor center;
	DcMotor motorManip;
	//DcMotor rand;
	//DcMotor motorLeftH;
	//DcMotor motorRightH;

    public FiveWheelTeleOp() {}
    /*
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init()
	{
        center = hardwareMap.dcMotor.get("center");
        motorFL = hardwareMap.dcMotor.get("motorfl");
        motorFR = hardwareMap.dcMotor.get("motorfr");
        motorBR = hardwareMap.dcMotor.get("motorbr");
		motorBL = hardwareMap.dcMotor.get("motorbl");
		motorManip = hardware.dcMotor.get("manip");
		//rand = hardwareMap.dcMotor.get("rand");
		//motorLeftH = hardwareMap.dcMotor.get("motorLeftH");
		//motorRightH = hardwareMap.dcMotor.get("motorRightH");
		//center = hardwareMap.dcMotor.get("motorcenter");
    }

	public void start()
	{
		motorFL.setPower(0.0);
		motorBL.setPower(0.0);
		motorBR.setPower(0.0);
		motorFR.setPower(0.0);
	}

	private double scaleInputHang(double pwr)
	{
		double ret = 0.0;
		if(pwr > 0.0)
		{
			if(pwr < 0.05) //0 PWR on chart
				ret = 0.0;
			else if(pwr >= 0.05 && pwr < 0.10) //0.05 on chart
				ret = 0.01;
			else if(pwr >= 0.10 && pwr < 0.15) //0.10 on chart
				ret = 0.02;
			else if(pwr >= 0.15 && pwr < 0.20) //0.15 on chart
				ret = 0.03;
			else if(pwr >= 0.20 && pwr < 0.25) //0.20 on chart
				ret = 0.04;
			else if(pwr >= 0.25 && pwr < 0.30) //0.25 on chart
				ret = 0.05;
			else if(pwr >= 0.30 && pwr < 0.35) //0.30 on chart
				ret = 0.06;
			else if(pwr >= 0.35 && pwr < 0.40) //0.35 on chart
				ret = 0.07;
			else if(pwr >= 0.40 && pwr < 0.45) //0.40 on chart
				ret = 0.075;
			else if(pwr >= 0.45 && pwr < 0.50) //0.45 on chart
				ret = 0.08;
			else if(pwr >= 0.50 && pwr < 0.55) //0.50 on chart
				ret = 0.09;
			else if(pwr >= 0.55 && pwr < 0.60) //0.55 on chart
				ret = 0.10;
			else if(pwr >= 0.60 && pwr < 0.65) //0.60 on chart
				ret = 0.113;
			else if(pwr >= 0.65 && pwr < 0.70) //0.65 on chart
				ret = 0.126;
			else if(pwr >= 0.70 && pwr < 0.75) //0.70 on chart
				ret = 0.14;
			else if(pwr >= 0.75 && pwr < 0.80) //0.75 on chart
				ret = 0.15;
			else if(pwr >= 0.80 && pwr < 0.85) //0.80 on chart
				ret = 0.19;
			else if(pwr >= 0.85 && pwr < 0.90) //0.85 on chart
				ret = 0.225;
			else
				ret = 1.0;
		}
		else
		{
			if(pwr > -0.05) //0 PWR on chart
				ret = 0.0;
			else if(pwr <= -0.05 && pwr > -0.10) //0.05 on chart
				ret = -0.01;
			else if(pwr <= -0.10 && pwr > -0.15) //0.10 on chart
				ret = -0.02;
			else if(pwr <= -0.15 && pwr > -0.20) //0.15 on chart
				ret = -0.03;
			else if(pwr <= -0.20 && pwr > -0.25) //0.20 on chart
				ret = -0.04;
			else if(pwr <= -0.25 && pwr > -0.30) //0.25 on chart
				ret = -0.05;
			else if(pwr <= -0.30 && pwr > -0.35) //0.30 on chart
				ret = -0.06;
			else if(pwr <= -0.35 && pwr > -0.40) //0.35 on chart
				ret = -0.07;
			else if(pwr <= -0.40 && pwr > -0.45) //0.40 on chart
				ret = -0.075;
			else if(pwr <= -0.45 && pwr > -0.50) //0.45 on chart
				ret = -0.08;
			else if(pwr <= -0.50 && pwr > -0.55) //0.50 on chart
				ret = -0.09;
			else if(pwr <= -0.55 && pwr > -0.60) //0.55 on chart
				ret = -0.10;
			else if(pwr <= -0.60 && pwr > -0.65) //0.60 on chart
				ret = -0.113;
			else if(pwr <= -0.65 && pwr > -0.70) //0.65 on chart
				ret = -0.126;
			else if(pwr <= -0.70 && pwr > -0.75) //0.70 on chart
				ret = -0.14;
			else if(pwr <= -0.75 && pwr > -0.80) //0.75 on chart
				ret = -0.15;
			else if(pwr <= -0.80 && pwr > -0.85) //0.80 on chart
				ret = -0.19;
			else if(pwr <= -0.85 && pwr > -0.90) //0.85 on chart
				ret = -0.225;
			else
				ret = -1.0;
		}
		return ret;
	}

	private double scaleInput(double y)
	{
		double ret = 0.0;
		double pwr = 0.0;
		if(y < 0.0)
			pwr = -1 * 13620 * y;
		else
			pwr = 13620 * y;
		/*if(pwr > 0.0)
		{
			if(pwr < 0.05) //0 PWR on chart
				ret = 0.0;
			else if(pwr >= 0.05 && pwr < 0.10) //0.05 on chart
				ret = 0.01;
			else if(pwr >= 0.10 && pwr < 0.15) //0.10 on chart
				ret = 0.02;
			else if(pwr >= 0.15 && pwr < 0.20) //0.15 on chart
				ret = 0.03;
			else if(pwr >= 0.20 && pwr < 0.25) //0.20 on chart
				ret = 0.04;
			else if(pwr >= 0.25 && pwr < 0.30) //0.25 on chart
				ret = 0.05;
			else if(pwr >= 0.30 && pwr < 0.35) //0.30 on chart
				ret = 0.06;
			else if(pwr >= 0.35 && pwr < 0.40) //0.35 on chart
				 ret = 0.07;
			else if(pwr >= 0.40 && pwr < 0.45) //0.40 on chart
				ret = 0.075;
			else if(pwr >= 0.45 && pwr < 0.50) //0.45 on chart
				ret = 0.08;
			else if(pwr >= 0.50 && pwr < 0.55) //0.50 on chart
				ret = 0.09;
			else if(pwr >= 0.55 && pwr < 0.60) //0.55 on chart
				ret = 0.10;
			else if(pwr >= 0.60 && pwr < 0.65) //0.60 on chart
				ret = 0.113;
			else if(pwr >= 0.65 && pwr < 0.70) //0.65 on chart
				ret = 0.126;
			else if(pwr >= 0.70 && pwr < 0.75) //0.70 on chart
				ret = 0.14;
			else if(pwr >= 0.75 && pwr < 0.80) //0.75 on chart
				ret = 0.15;
			else if(pwr >= 0.80 && pwr < 0.85) //0.80 on chart
				ret = 0.19;
			else if(pwr >= 0.85 && pwr < 0.90) //0.85 on chart
				ret = 0.225;
			else if(pwr >= 0.90 && pwr < 0.95) //0.90 on chart
				ret = 0.30;
			else if(pwr >= 0.95 && pwr < 0.98) //0.95 on chart
				ret = 0.40;
			else
				ret = 1.0;
		}
		else
		{
			if(pwr > -0.05) //0 PWR on chart
				ret = 0.0;
			else if(pwr <= -0.05 && pwr > -0.10) //0.05 on chart
				ret = 0.01;
			else if(pwr <= -0.10 && pwr > -0.15) //0.10 on chart
				ret = 0.02;
			else if(pwr <= -0.15 && pwr > -0.20) //0.15 on chart
				ret = 0.03;
			else if(pwr <= -0.20 && pwr > -0.25) //0.20 on chart
				ret = 0.04;
			else if(pwr <= -0.25 && pwr > -0.30) //0.25 on chart
				ret = 0.05;
			else if(pwr <= -0.30 && pwr > -0.35) //0.30 on chart
				ret = 0.06;
			else if(pwr <= -0.35 && pwr > -0.40) //0.35 on chart
				ret = 0.07;
			else if(pwr <= -0.40 && pwr > -0.45) //0.40 on chart
				ret = 0.075;
			else if(pwr <= -0.45 && pwr > -0.50) //0.45 on chart
				ret = 0.08;
			else if(pwr <= -0.50 && pwr > -0.55) //0.50 on chart
				ret = 0.09;
			else if(pwr <= -0.55 && pwr > -0.60) //0.55 on chart
				ret = 0.10;
			else if(pwr <= -0.60 && pwr > -0.65) //0.60 on chart
				ret = 0.113;
			else if(pwr <= -0.65 && pwr > -0.70) //0.65 on chart
				ret = 0.126;
			else if(pwr <= -0.70 && pwr > -0.75) //0.70 on chart
				ret = 0.14;
			else if(pwr <= -0.75 && pwr > -0.80) //0.75 on chart
				ret = 0.15;
			else if(pwr <= -0.80 && pwr > -0.85) //0.80 on chart
				ret = 0.19;
			else if(pwr <= -0.85 && pwr > -0.90) //0.85 on chart
				ret = 0.225;
			else if(pwr <= -0.90 && pwr > -0.95) //0.90 on chart
				ret = 0.30;
			else if(pwr <= -0.95 && pwr > -0.98) //0.95 on chart
				ret = 0.40;
			else
				ret = 1.0;
		}*/
		String stringPwr = "" + pwr;
		BigDecimal pwrF = new BigDecimal(stringPwr);
		BigDecimal pwrE = pwrF.multiply(pwrF);
		BigDecimal pwrD = pwrE.multiply(pwrF);
		BigDecimal pwrC = pwrD.multiply(pwrF);
		BigDecimal pwrB = pwrC.multiply(pwrF);
		BigDecimal pwrA = pwrB.multiply(pwrF);

		BigDecimal a = new BigDecimal("0.000000000000000000000007626656");
		BigDecimal b = new BigDecimal("0.000000000000000000272080701657");
		BigDecimal c = new BigDecimal("0.000000000000003715117053405310");
		BigDecimal d = new BigDecimal("0.000000000024215489818429200000");
		BigDecimal e = new BigDecimal("0.000000077305839163423100000000");
		BigDecimal f = new BigDecimal("0.000102195266110368000000000000");
		BigDecimal g = new BigDecimal("0.079417125827859500000000000000");
		a = a.multiply(pwrA);
		b = b.multiply(pwrB);
		c = c.multiply(pwrC);
		d = d.multiply(pwrD);
		e = e.multiply(pwrE);
		f = f.multiply(pwrF);

		BigDecimal h = a.subtract(b);
		h = h.add(c);
		h = h.subtract(d);
		h = h.add(e);
		h = h.subtract(f);
		h = h.add(g);

		ret = h.doubleValue();
		if(ret < 0.0)
			ret = 0.0;
		if(ret > 1.0)
			ret = 1.0;
		if(y < 0.0)
			ret *= -1;
		return ret;
	}
    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void loop()
    {
        float g1y1 = gamepad1.left_stick_y;
		float g1y2 = gamepad1.right_stick_y;
		float g2y1 = gamepad2.right_stick_y;
		float g2y2 = gamepad2.left_stick_y;
		double sendg1y1 = scaleInput(g1y1);
		double sendg1y2 = scaleInput(g1y2);

		if(gamepad1.left_bumper)
			motorManip = 1.0;
		else if(gamepad1.right_bumper)
			motorManip = -1.0;
		else
			motorManip = 0.0;
		/*motorFL.setPower(1.0);
		motorBL.setPower(1.0);
		motorFR.setPower(1.0);
		center.setPower(1.0);
		motorBR.setPower(1.0);*/
		/*if(Math.abs(g1y1) > 0.1)
			rand.setPower(sendg1y1);
		else
			rand.setPower(0.0);*/
		/*if(Math.abs(g1y1) > 0.1)
			motorFR.setPower(sendg1y1);
		else
			motorFR.setPower(0.0);

		if(Math.abs(g1y2) > 0.1)
			motorBR.setPower(sendg1y2);
		else
			motorBR.setPower(0.0);*/
		/*if(y1 > 0.3)
			motorBR.setPower(0.01);
		else if(y1 < -0.3)
			motorBR.setPower(-0.01);
		else
			motorBR.setPower(0.0);*/
		/*if(Math.abs(gamepad2.right_trigger) > 0.5)
		{
			motorLeftH.setPower(scaleInputHang(gamepad2.right_trigger));
			motorRightH.setPower(scaleInputHang(-gamepad2.right_trigger));
		}
		else if(Math.abs(gamepad1.left_trigger) > 0.5)
		{
			motorLeftH.setPower(scaleInputHang(-gamepad2.left_trigger));
			motorRightH.setPower(scaleInputHang(gamepad2.left_trigger));
		}*/
		/*double servoDif = 0.0;
		if(gamepad1.left_trigger > 0.2 || gamepad1.left_trigger < -0.2)
			servoDif -= 0.05;
		else if(gamepad1.right_trigger > 0.2 || gamepad1.right_trigger < -0.2)
			servoDif += 0.05;
		if(servoDif < 0.0)
			servoDif = 0.0;
		if(servoDif > 1.0)
			servoDif = 1.0;
		l.setPosition(1.0 - servoDif);
		r.setPosition(servoDif);*/

		/*if(gamepad1.left_bumper)
		{
			motorLeftH.setPower(1.0);
			motorRightH.setPower(-1.0);
		}
		else if(gamepad1.right_bumper)
		{
			motorLeftH.setPower(-1.0);
			motorRightH.setPower(1.0);
		}
		else
		{
			motorLeftH.setPower(0.0);
			motorRightH.setPower(0.0);
		}*/
		if(((g1y1 > 0.1 && g1y2 > 0.1) || (g1y1 < -0.1 && g1y2 < -0.1))) //if moving same direction
		{ //move front wheel also
			center.setPower(sendg1y2);
			motorFR.setPower(-sendg1y2);
			motorBR.setPower(-sendg1y2);
			motorBL.setPower(sendg1y1);
			motorFL.setPower(sendg1y1);
		}
		else//else (sticks not moving same direction
		{
			center.setPower(0.0);
			if(g1y2 > 0.1 || g1y2 < -0.1)
			{
				motorFR.setPower(sendg1y2);
				motorBR.setPower(sendg1y2);
			}
			else
			{
				motorBR.setPower(0.0);
				motorFR.setPower(0.0);
			}
			if(g1y1 > 0.1 || g1y1 < -0.1)
			{
				motorFL.setPower(-sendg1y1);
				motorBL.setPower(-sendg1y1);
			}
			else
			{
				motorFL.setPower(0.0);
				motorBL.setPower(0.0);
			}
		}
    }

    public void stop()
	{

    }
}
