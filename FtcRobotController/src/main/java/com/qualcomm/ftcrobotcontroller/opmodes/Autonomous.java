package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Autonomous extends LinearOp
{
	public Autonomous()
	{
		
	}

	@Override
	public void runOpMode()
	{
		super.runOpMode();
		waitForStart();
	}

	public void stop()
	{
		
	}
}