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

public class TrollBotTeleOp extends LinearOpMode {
	DcMotor motorBL;
	DcMotor motorBR;
	DcMotor motorFL;
	DcMotor motorFR;
	DcMotor center;

	public TrollBotTeleOp() {}
	
	@Override
	public void init()
	{
		
	}

	@Override
	public void runOpMode()
	{
		center = hardwareMap.dcMotor.get("matt");
		motorFL = hardwareMap.dcMotor.get("morgan");
		motorFR = hardwareMap.dcMotor.get("tim");
		motorBR = hardwareMap.dcMotor.get("1");
		motorBL = hardwareMap.dcMotor.get("2");
		waitForStart();
		//Move Motors
		while(opModeIsActive())
		{
			int number = 1;
			if(Math.abs(gamepad1.left_stick_y) > 0.1) //If x button is pressed
			{
				center.setPower(-gamepad.left_stick_y);
			}
			else
			{
				center.setPower(0.0);
			}
		}
	}
}
