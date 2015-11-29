package com.qualcomm.ftcrobotcontroller.opmodes;

public class BlueTeleOp extends TeleOp
{
	@Override
	public void runOpMode()
	{
		isRedSide = false;
		isBlueSide = true;
		super.runOpMode();
	}
}