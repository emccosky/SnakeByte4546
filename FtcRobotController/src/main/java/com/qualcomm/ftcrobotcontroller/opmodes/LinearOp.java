package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;

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

	//All Servos
    Servo tiltServo;
    Servo servoHitClimberL;
    Servo servoHitClimberR;
    Servo servoLFlap;
    Servo servoRFlap;

	//Servo Positions
    double getServoHitClimberLPos;
    double getServoHitClimberRPos;
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

	//Setup Methods
	public void initIMU()
	{
		try
        {
            gyro = new AdafruitIMU(hardwareMap, "bno055"
                        , (byte)(AdafruitIMU.BNO055_ADDRESS_A * 2)//By convention the FTC SDK always does 8-bit I2C bus
                        , (byte)AdafruitIMU.OPERATION_MODE_IMU);
            } catch (RobotCoreException e){
                Log.i("FtcRobotController", "Exception: " + e.getMessage());
            }
	}

	public void startIMU()
	{
		gyro.startIMU();
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
		initIMU();
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
	}

	public void updatePositionLoop()
	{
		while(opModeIsActive())
		{
			updatePosition();
			Thead.sleep(20);
		}
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
		servoHitClimberR.setPosition(0.8);
	}

	public void retractRightClimberServo()
	{
		servoHitClimberR.setPosition(0.2);
	}

	public void extendLeftClimberServo()
	{
		servoHitClimberL.setPosition(0.8);
	}

	public void retractLeftClimberServo()
	{
		servoHitClimberL.setPosition(0.2);
	}

	//Lift movement methods (Complex)
	public void raiseLift(double dist)
	{
		
	}

	public void raiseLiftMedium()
	{
		
	}

	public void raiseLiftHigh()
	{
		
	}

	//Basket movement methods (Simple)
	public void lockRightFlap()
	{
		servoRFlap.setPosition(0.68);
	}

	public void unlockRightFlap()
	{
		servoRFlap.setPosition(0.46);
	}

	public void openRightFlap()
	{
		servoRFlap.setPosition(0.05);
	}

	public void lockLeftFlap()
	{
		servoLFlap.setPosition(0.39);
	}

	public void unlockLeftFlap()
	{
		servoLFlap.setPosition(0.62);
	}

	public void openLeftFlap()
	{
		servoLFlap.setPosition(0.97);
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
		tiltServo.setPosition(0.91);
	}

	public void tiltLeft()
	{
		tiltServo.setPosition(0.6);
	}

	public void tiltFlat()
	{
		tiltServo.setPosition(0.75);
	}

	//Basket movement methods (Complex)
	public void basketInitBlue()
	{
		unlockFlaps();
		Thread.sleep(200);
		tiltRight();
		Thread.sleep(200);
		lockFlaps();
	}

	public void basketInitRed()
	{
		unlockFlaps();
		Thread.sleep(200);
		tiltRight();
		Thread.sleep(200);
		lockFlaps();
	}

	public void dumpLeft()
	{
		f(isTiltLeft)
		{
			openLeftFlap();
			Thread.sleep(2000);
			lockLeftFlap();
		}
		else if(isTiltRight)
		{
			unlockRightFlap();
			unlockLeftFlap();
			Thread.sleep(200);
			tiltLeft();
			Thread.sleep(500);
			openLeftFlap();
			Thread.sleep(2000);
			lockLeftFlap();
		}
		else
		{
			unlockLeftFlap();
			Thread.sleep(200);
			tiltLeft();
			Thread.sleep(500);
			openLeftFLap();
			Thread.sleep(2000);
			lockLeftFlap();
		}
		isTiltRight = false;
		isTiltLeft = true;
	}

	public void dumpRight()
	{
		if(isTiltRight)
		{
			openRightFlap();
			Thread.sleep(2000);
			lockRightFlap();
		}
		else if(isTiltLeft)
		{
			unlockLeftFlap();
			unlockRightFlap();
			Thread.sleep(200);
			tiltRight();
			Thread.sleep(500);
			openRightFlap();
			Thread.sleep(2000);
			lockRightFlap();
		}
		else
		{
			unlockRightFlap();
			Thread.sleep(200);
			tiltRight();
			Thread.sleep(500);
			openRightFLap();
			Thread.sleep(2000);
			lockRightFlap();
		}
		isTiltRight = true;
		isTiltLeft = false;
	}

	//Motor Power Scaling
	private double scaleInput(double y)
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

	private double scaleInputSimple(double pwr) //Scales input power
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

        if(changeX < 0)
            changeX *= -1;
        if(changeY < 0)
            changeY *= -1;
        dist = (changeX * changeX) + (changeY * changeY);
        dist = Math.sqrt(dist);
        return dist;
    }

	public void turnToHeading(double desiredHeading)
    {
		if(curHeading > desiredHeading)
		{
			//Turn left until robot reaches the desiredHeading
			while(curYaw > desiredHeading)
			{
				runOddSide(0.8);
				runEvenSide(-0.8);
				Thread.sleep(25);
			}
			stopWheels();
		}
		else
		{
			//Turn right until robot reaches the desiredHeading
			while(curHeading < desiredHeading)
			{
				runOddSide(-0.8);
				runEvenSide(0.8);
				Thread.sleep(25);
			}
			stopWheels();
		}
	}

	public void moveTo(double desiredX, double desiredY)
	{
		desiredHeading = calcDesiredHeading(curXPos, curYPos, x, y);
    	turnToHeading(desiredHeading);
    	double desiredDist = calcDesiredDistance(curXPos, curYPos, x, y);
    	double curDist = 0.0;
    	while(curDist < desiredDist)
    	{
    		runOddSide(0.8);
    		runEvenSide(0.8);
    		curDist += calcDesiredDistance(prevXPos, prevYPos, curXPos, curYPos);
    		Thread.sleep(25);
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