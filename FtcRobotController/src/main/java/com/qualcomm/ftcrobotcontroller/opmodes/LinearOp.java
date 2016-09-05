package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.math.*;
import android.util.Log;
import com.qualcomm.robotcore.exception.RobotCoreException;

public class LinearOp extends LinearOpMode
{
	//IMU name
	AdafruitIMU imu;

	//All Motors
    DcMotor motorQ1; //FL if manip considered the front, BR if center wheel is front
    DcMotor motorQ2; //FR if manip considered the front, BL if center wheel is front
    DcMotor motorQ3; //BL if manip considered the front, FR if center wheel is front
    DcMotor motorQ4; //BR if manip considered the front, FL if center wheel is front
    DcMotor center;
    DcMotor debrisLift;
    DcMotor motorManip;

    //Motor encoder positions
    double motorQ1Pos;
    double motorQ2Pos;
    double motorQ3Pos;
    double motorQ4Pos;
    double debrisLiftPos;
	//double servoDumpClimbersPos;

	//Position calculation variables
	double curRightSidePosition;
	double curLeftSidePosition;
	double prevRightSidePosition;
	double prevLeftSidePosition;

	//All Servos
    Servo tiltServo;
    Servo servoHitClimberL;
    Servo servoHitClimberR;
    Servo servoLFlap;
    Servo servoRFlap;
	//Servo servoDumpClimbers;

	//Servo Positions
    double servoHitClimberLPos;
    double servoHitClimberRPos;
    double tiltServoPos;
    double servoLFlapPos;
    double servoRFlapPos;

    //Basket Tilt Position Variables
    boolean isTiltRight;
    boolean isTiltLeft;

	//Position Information
	double curXPos;
	double curYPos;
	double curZPos;
	boolean isOnRamp;

	//Gyro Information
	double curYaw;
	double curPitch;
	double curRoll;

	//Side information
	boolean isRedSide;
	boolean isBlueSide;

	//Climbers being hit down or not
	boolean isLowClimberHit;
	boolean isMidClimberHit;

    public LinearOp()
    {

    }

	public void sleep(int time)
	{
		try{Thread.sleep(time);}catch(Exception e){}
	}

	//Setup Methods
	public void initIMU()
	{
		try
        {
            imu = new AdafruitIMU(hardwareMap, "bno055"
                        , (byte)(AdafruitIMU.BNO055_ADDRESS_A * 2)//By convention the FTC SDK always does 8-bit I2C bus
                        , (byte)AdafruitIMU.OPERATION_MODE_IMU);
            } catch (RobotCoreException e){
                Log.i("FtcRobotController", "Exception: " + e.getMessage());
            }
	}

	public void startIMU()
	{
		imu.startIMU();
	}

	public void initSetup()
	{
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
		//servoDumpClimbers = hardwareMap.servo.get("servoDumpClimbers");
		//initIMU();
	}

	public void initPosition(double x, double y, double z, double yaw, double pitch, double roll)
	{
		curXPos = x;
		curYPos = y;
		curZPos = z;
		curYaw = yaw;
		curPitch = pitch;
		curRoll = roll;
	}

	public void updatePosition()
	{
		//Update position variables
		motorQ1Pos = motorQ1.getCurrentPosition();
		motorQ2Pos = motorQ2.getCurrentPosition();
		motorQ3Pos = motorQ3.getCurrentPosition();
		motorQ4Pos = motorQ4.getCurrentPosition();
		debrisLiftPos = debrisLift.getCurrentPosition();
		//debrisLiftPos = debrisLift.getCurrentPosition();
		/*double[] yaws = new double[2];
		double[] rolls = new double[2];
		double[] pitches = new double[2];*/
		/*imu.getIMUGyroAngles(yaws,rolls,pitches);
		curYaw = yaws[0];
		curRoll = rolls[0];
		curPitch = pitches[0];*/
	}

	public void updatePositionLoop() //Doesn't work
	{
		while(opModeIsActive())
		{
			updatePosition();
			sleep(20);
		}
	}

	public void displayDistance()
	{
		double multiplier = (3.775 * Math.PI) / 1440;
		double q1cm = motorQ1Pos * multiplier;
		double q2cm = motorQ2Pos * multiplier;
		double q3cm = motorQ3Pos * multiplier;
		double q4cm = motorQ4Pos * multiplier;
		if(q1cm < 0)
			q1cm *= -1;
		if(q2cm < 0)
			q2cm *= -1;
		if(q3cm < 0)
			q3cm *= -1;
		if(q4cm < 0)
			q4cm *= -1;
		telemetry.addData("motorQ1Pos",motorQ1Pos);
		telemetry.addData("motorQ2Pos",motorQ2Pos);
		telemetry.addData("motorQ3Pos",motorQ3Pos);
		telemetry.addData("motorQ4Pos",motorQ4Pos);

		double estDist = (q1cm + q2cm + q3cm + q4cm)/4;
		telemetry.addData("estdist", estDist);
		//telemetry.addData("debrisLift", debrisLiftPos);
	}

	//Simple Movement methods
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

    public void stopWheels()
    {
        motorQ1.setPower(0.0);
        motorQ2.setPower(0.0);
        motorQ3.setPower(0.0);
        motorQ4.setPower(0.0);
    }

	//Other Simple Motor and Servo Movement Methods
	public void runManip(double speed) //Moves manipulator
	{
		motorManip.setPower(speed);
	}

	public void moveDebrisLift(double speed) //Moves lift
	{
		debrisLift.setPower(speed);
	}

	public void extendRightClimberServo()
	{
		servoHitClimberRPos = 0.75;
	}

	public void retractRightClimberServo()
	{
		servoHitClimberRPos = 0.25;
	}

	public void extendLeftClimberServo()
	{
		servoHitClimberLPos = 0.75;
	}

	public void retractLeftClimberServo()
	{
		servoHitClimberLPos = 0.25;
	}

	//Lift movement methods (Complex)
	public void raiseLift(double dist)
	{
		while(debrisLiftPos < dist)
		{
			debrisLift.setPower(0.6);
			updatePosition();
		}
		debrisLift.setPower(0.0);
	}

	public void raiseLiftMedium()
	{
		raiseLift(1000);
	}

	public void raiseLiftHigh()
	{
		raiseLift(2000);
	}

	public void retractLift()
	{
		while(debrisLiftPos > 0)
		{
			debrisLift.setPower(-0.6);
			updatePosition();
		}
	}

	public void macroLiftMedium(String side) //Bad method
	{
		if(side.equals("right"))
		{
			raiseLiftMedium();
			sleep(500);
			dumpRight();
			sleep(500);
			retractLift();
		}
		else
		{
			raiseLiftMedium();
			sleep(500);
			dumpLeft();
			sleep(500);
			retractLift();
		}
	}

	public void macroLiftHigh(String side)
	{
		if(side.equals("right"))
		{
			raiseLiftHigh();
			sleep(500);
			dumpRight();
			sleep(500);
			retractLift();
		}
		else
		{
			raiseLiftHigh();
			sleep(500);
			dumpLeft();
			sleep(500);
			retractLift();
		}
	}

	public void updateServoPos()
	{
		servoRFlap.setPosition(servoRFlapPos);
		servoLFlap.setPosition(servoLFlapPos);
		servoHitClimberL.setPosition(servoHitClimberLPos);
		servoHitClimberR.setPosition(servoHitClimberRPos);
		tiltServo.setPosition(tiltServoPos);
	}

	//Basket movement methods (Simple)
	public double moveServoVel(Servo servo, double curPos, double newPos, double seconds)
	{
		long start = System.nanoTime();
		long end = System.nanoTime();
		long timeElapsed = (end - start)/1000000000;
		double distToMove = newPos - curPos;
		while(timeElapsed < seconds)
		{
			servo.setPosition(curPos + (distToMove*(timeElapsed/seconds)));
			//servoHitClimberLPos = curPos + (distToMove*(timeElapsed/seconds));
			end = System.nanoTime();
			timeElapsed = (end - start)/1000000000;
			sleep(50);
		}
		servo.setPosition(newPos);
		return newPos;
	}

	public void lockRightFlap()
	{
		//moveServoVel(servoRFlap, servoRFlapPos, 0.68, 0.5);
		servoRFlapPos = 0.68;
		servoRFlap.setPosition(servoRFlapPos);
	}

	public void unlockRightFlap()
	{
		//moveServoVel(servoRFlap, servoRFlapPos, 0.20, 0.5);
		servoRFlapPos = 0.20;
		servoRFlap.setPosition(servoRFlapPos);
	}

	public void openRightFlap()
	{
		//moveServoVel(servoRFlap, servoRFlapPos, 0.05, 0.5);
		servoRFlapPos = 0.05;
		servoRFlap.setPosition(servoRFlapPos);

	}

	public void lockLeftFlap()
	{
		//moveServoVel(servoLFlap, servoLFlapPos, 0.42, 0.5);
		servoLFlapPos = 0.42;
		servoLFlap.setPosition(servoLFlapPos);
	}

	public void unlockLeftFlap()
	{
		//moveServoVel(servoLFlap, servoLFlapPos, 0.8, 0.5);
		servoLFlapPos = 0.8;
		servoLFlap.setPosition(servoLFlapPos);
	}

	public void openLeftFlap()
	{
		//moveServoVel(servoLFlap, servoLFlapPos, 0.97, 0.5);
		servoLFlapPos = 0.97;
		servoLFlap.setPosition(servoLFlapPos);
	}

	public void lockFlaps()
	{
		lockLeftFlap();
		lockRightFlap();
	}

	public void unlockFlaps()
	{
		unlockLeftFlap();
		unlockRightFlap();
	}

	public void openFlaps()
	{
		openLeftFlap();
		openRightFlap();
	}

	public void tiltRight()
	{
		//tiltServoPos = moveServoVel(tiltServo, tiltServoPos, 0.87, 0.5);
		tiltServoPos = 0.87;
		tiltServo.setPosition(tiltServoPos);
	}

	public void tiltLeft()
	{
		//tiltServoPos = moveServoVel(tiltServo, tiltServoPos, 0.58, 0.5);
		tiltServoPos = 0.58;
		tiltServo.setPosition(tiltServoPos);
	}

	public void tiltFlat()
	{
		//tiltServoPos = moveServoVel(tiltServo, tiltServoPos, 0.75, 0.25);
		tiltServoPos = 0.75;
		tiltServo.setPosition(tiltServoPos);
	}

	//Basket movement methods (Complex)
	public void basketInitBlue()
	{
		lockFlaps();
		tiltLeft();
	}

	public void basketInitRed()
	{
		lockFlaps();
		tiltRight();
	}

	public void wiggleTilt(String side)
	{
		/*if(side.equals("right"))
		{
			tiltServoPos = moveServoVel(tiltServo, tiltServoPos, 0.82, 0.2);
			tiltServoPos = moveServoVel(tiltServo, tiltServoPos, 0.87, 0.2);
		}
		else
		{
			tiltServoPos = moveServoVel(tiltServo, tiltServoPos, 0.54, 0.2);
			tiltServoPos = moveServoVel(tiltServo, tiltServoPos, 0.58, 0.2);
		}
			moveServoVel(tiltServo, tiltServoPos, 0.87, 0.2);
			tiltServoPos = 0.89;
			sleep(200);
			moveServoVel(tiltServo, tiltServoPos, 0.91, 0.2);
			tiltServoPos = 0.91;
		else
		{
			moveServoVel(tiltServo, tiltServoPos, 0.58, 0.2);
			tiltServoPos = 0.58;
			sleep(200);
			moveServoVel(tiltServo, tiltServoPos, 0.6, 0.2);
			tiltServoPos = 0.6;
		}*/
	}

	public void dumpLeft()
	{
		//if(isTiltLeft)
		//{
			tiltLeft();
			openLeftFlap();
			//wiggleTilt("left");
			//wiggleTilt("left");
		/*}
		else if(isTiltRight)
		{
			unlockRightFlap();
			unlockLeftFlap();
			sleep(300);
			tiltLeft();
			sleep(600);
			openLeftFlap();
		}
		else
		{
			unlockLeftFlap();
			sleep(300);
			tiltLeft();
			sleep(500);
			openLeftFlap();
		}
		sleep(500);
		wiggleTilt("left");
		sleep(700);
		wiggleTilt("left");
		sleep(700);
		lockLeftFlap();
		isTiltRight = false;
		isTiltLeft = true;*/
	}

	public void dumpRight()
	{
		//if(isTiltRight)
		//{
			tiltRight();
			openRightFlap();
			//wiggleTilt("right");
			//wiggleTilt("right");
		/*}
		else if(isTiltLeft)
		{
			unlockLeftFlap();
			unlockRightFlap();
			sleep(200);
			tiltRight();
			sleep(200);
			openRightFlap();
		}
		else
		{
			unlockRightFlap();
			sleep(200);
			tiltRight();
			sleep(200);
			openRightFlap();
		}
		sleep(200);
		wiggleTilt("right");
		sleep(200);
		wiggleTilt("right");
		sleep(200);
		lockRightFlap();
		isTiltRight = true;
		isTiltLeft = false;*/
	}

	//Motor Power Scaling
	protected double scaleInput(double y)
	{
		double ret = 0.0;
		double pwr = 0.0;
		if(y < 0.0)
			pwr = -1 * 13620 * y;
		else
			pwr = 13620 * y;
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

	protected double scaleInputSimple(double pwr) //Scales input power
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

	//Advanced Robot Movement methods
	public static double calcDesiredHeading(double startX, double startY, double endX, double endY)
	{
        double changeX = endX - startX;
        double changeY = endY - startY;

        double ratio = changeY / changeX;
        double disp = Math.toDegrees(Math.atan(ratio));
        if(changeY > 0 && changeX > 0)
            disp = 90 - disp;
        else if(changeY > 0 && changeX < 0)
            disp = -90 - disp;
        else if(changeY < 0 && changeX > 0)
            disp = 90 - disp;
        else if(changeY < 0 && changeX < 0)
            disp = -90 - disp;
        return disp;
	}

	public static double calcDesiredDistance(double startX, double startY, double endX, double endY)
    {
        double dist = 0.0;
        double changeX = startX - endX;
        double changeY = startY - endY;

        dist = (changeX * changeX) + (changeY * changeY);
        dist = Math.sqrt(dist);
        return dist;
    }

	public void turnToHeading(double desiredHeading)
    {
		if(curYaw > desiredHeading)
		{
			//Turn left until robot reaches the desiredHeading
			while(curYaw > desiredHeading)
			{
				runOddSide(0.8);
				runEvenSide(-0.8);
				sleep(25);
			}
			stopWheels();
		}
		else
		{
			//Turn right until robot reaches the desiredHeading
			while(curYaw < desiredHeading)
			{
				runOddSide(-0.8);
				runEvenSide(0.8);
				sleep(25);
			}
			stopWheels();
		}
	}

	public void moveTo(double desiredX, double desiredY)
	{
		double desiredHeading = calcDesiredHeading(curXPos, curYPos, desiredX, desiredY);
    	turnToHeading(desiredHeading);
    	double desiredDist = calcDesiredDistance(curXPos, curYPos, desiredX, desiredY);
    	double curDist = 0.0;
    	while(curDist < desiredDist)
    	{
    		runOddSide(0.8);
    		runEvenSide(0.8);
    		//curDist += calcDesiredDistance(prevXPos, prevYPos, curXPos, curYPos);
    		sleep(25);
    	}
    	stopWheels();
	}

	//Parent runOpMode
	@Override
	public void runOpMode()
	{
		initSetup(); //Sets up motors, servos, and gyros
		if(isRedSide)
			basketInitRed();
		else
			basketInitBlue();
	}
}