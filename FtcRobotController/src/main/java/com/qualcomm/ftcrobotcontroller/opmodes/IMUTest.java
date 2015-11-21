package com.qualcomm.ftcrobotcontroller.opmodes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;

/**
 * Created by Owner on 8/31/2015.
 */
public class IMUTest extends LinearOpMode
{
    AdafruitIMU gyro;

    //The following arrays contain both the Euler angles reported by the IMU (indices = 0) AND the
    // Tait-Bryan angles calculated from the 4 components of the quaternion vector (indices = 1)
    volatile double[] rollAngle = new double[2], pitchAngle = new double[2], yawAngle = new double[2], accs = new double[3];
    double curXAcc; //In m/s^2
    double curYAcc; //In m/s^2
    double curZAcc; //In m/s^2
    double prevXAcc; //In m/s^2
    double prevYAcc; //In m/s^2
    double prevZAcc; //In m/s^2

    double curXVel; //In m/s
    double curYVel; //In m/s
    double curZVel; //In m/s
    double prevXVel; //In m/s
    double prevYVel; //In m/s
    double prevZVel; //In m/s
    
    double curXPos; //In cm
    double curYPos; //In cm
    double curZPos; //In cm
    double prevXPos; //In cm
    double prevYPos; //In cm
    double prevZVel; //In cm
    
    boolean hasStarted;

    long systemTime;//Relevant values of System.nanoTime
    long elapsedTime;
    long prevTime;

    double curHeading;
    double desiredHeading;

    /************************************************************************************************
     * The following method was introduced in the 3 August 2015 FTC SDK beta release and it runs
     * before "start" runs.
     */
     
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
	
	public void stopMotors()
	{
		motorQ1.setPower(0.0);
		motorQ2.setPower(0.0);
		motorQ3.setPower(0.0);
		motorQ4.setPower(0.0);
	}
	
    @Override
    public void init() {
    curHeading = 0; //CHANGE BASED ON PROGRAM
    hasStarted = false;
    prevHeading = curHeading;
    curXAcc = 0;
    curYAcc = 0;
    curZAcc = 0;
    prevXAcc = 0;
    prevYAcc = 0;
    prevZAcc = 0;
    
    curXVel = 0;
    curYVel = 0;
    curZVel = 0;
    prevXVel = 0;
    prevYVel = 0;
    prevZVel = 0;
    
    curXPos = 0; //CHANGE BASED ON PROGRAM
    curYPos = 0; //CHANGE BASED ON PROGRAM
    curZPos = 0;
    prevXPos = curXPos;
    prevYPos = 0;
    prevZVel = 0;
    

        systemTime = System.nanoTime();
        prevTime = systemTime;
        try {
            gyro = new AdafruitIMU(hardwareMap, "bno055"
                    , (byte)(AdafruitIMU.BNO055_ADDRESS_A * 2)//By convention the FTC SDK always does 8-bit I2C bus
                    , (byte)AdafruitIMU.OPERATION_MODE_IMU);
        } catch (RobotCoreException e){
            Log.i("FtcRobotController", "Exception: " + e.getMessage());
        }
    }

    /************************************************************************************************
     * Code to run when the op mode is first enabled goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void start() {
        /*
      	* Use the hardwareMap to get the dc motors, servos and other sensors by name. Note
      	* that the names of the devices must match the names used when you
      	* configured your robot and created the configuration file. The hardware map
      	* for this OpMode is not initialized until the OpModeManager's "startActiveOpMode" method
      	* runs.
    		*/
        systemTime = System.nanoTime();
        gyro.startIMU();//Set up the IMU as needed for a continual stream of I2C reads.
        Log.i("FtcRobotController", "IMU Start method finished in: "
                + (-(systemTime - (systemTime = System.nanoTime()))) + " ns.");
    }

    /***********************************************************************************************
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     * NOTE: BECAUSE THIS "loop" METHOD IS PART OF THE OVERALL OpMode/EventLoop/ReadWriteRunnable
     * MECHANISM, ALL THAT THIS METHOD WILL BE USED FOR, IN AUTONOMOUS MODE, IS TO:
     * 1. READ SENSORS AND ENCODERS AND STORE THEIR VALUES IN SHARED VARIABLES
     * 2. WRITE MOTOR POWER AND CONTROL VALUES STORED IN SHARED VARIABLES BY "WORKER" THREADS, AND
     * 3. SEND TELELMETRY DATA TO THE DRIVER STATION
     * THIS "loop" METHOD IS THE ONLY ONE THAT "TOUCHES" ANY SENSOR OR MOTOR HARDWARE.
     */
 
    public void updatePosition()
    {
    	elapsedTime = systemTime - prevTime;
        prevTime = systemTime;
        double elapsedSeconds = elapsedTime / 1000000000;
        systemTime = System.nanoTime();
		
		//Update accelerations
        gyro.getAccel(accs);
        prevXAcc = curXAcc;
    	prevYAcc = curYAcc;
    	prevZAcc = curZAcc;
    	curXAcc = accs[0];
    	curYAcc = accs[1];
    	curZAcc = accs[2];
    	
    	//Update velocities
    	prevXVel = curXVel;
    	prevYVel = curYVel;
    	prevZVel = curZVel;
    	curXVel = prevXVel + (curXAcc / elapsedSeconds);
    	curYVel = prevYVel + (curYAcc / elapsedSeconds);
    	curZVel = prevZVel + (curZAcc / elapsedSeconds);
    	
    	//Update position
    	prevXPos = curXPos;
    	prevYPos = curYPos;
    	prevZPos = curZPos;
    	curXPos = prevXPos + ((curXVel / elapsedSeconds) * 100);
    	curYPos = prevYPos + ((curYVel / elapsedSeconds) * 100);
    	curZPos = prevZPos + ((curZPos / elapsedSeconds) * 100);
    	
    	//Update gyro values
    	gyro.getIMUGyroAngles(rollAngle, pitchAngle, yawAngle);
    	prevHeading = curHeading;
    	curHeading = yawAngle[0];
    	
    	//Display information on screen
        telemetry.addData("Headings(yaw): ",
                String.format("Euler= %4.5f", yawAngle[0]);
	}
 
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
    	updatePosition();
		if(curHeading > desiredHeading)
		{
			//Turn left until robot reaches the desiredHeading
			while(curHeading > desiredHeading)
			{
				updatePosition();
				runOddSide(0.8);
				runEvenSide(-0.8);
			}
			stopMotors();
		}
		else
		{
			//Turn right until robot reaches the desiredHeading
			while(curHeading < desiredHeading)
			{
				updatePosition();
				runOddSide(-0.8);
				runEvenSide(0.8);
			}
			stopMotors();
		}
		updatePosition();
	}

    public void moveTo(double x, double y)
    {
    	desiredHeading = calcDesiredHeading(curXPos, curYPos, x, y);
    	turnToHeading(desiredHeading);
    	desiredDist = calcDesiredDistance(curXPos, curYPos, x, y);
    	double curDist = 0.0;
    	while(curDist < desiredDist)
    	{
    		updatePosition();
    		runOddSide(0.8);
    		runEvenSide(0.8);
    		curDist += calcDesiredDistance(prevXPos, prevYPos, curXPos, curYPos);
    	}
    }

    @Override
    public void loop()
    {
		updatePosition();
		if(gamepad1.x && !hasStarted)
		{
			moveTo(0,0);
			hasStarted = true;	
		}
    }

    /*
    * Code to run when the op mode is first disabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
    */
    @Override
    public void stop() {
        //When the FTC Driver Station's "Start with Timer" button commands autonomous mode to start,
        //then stop after 30 seconds, stop the motors immediately!
        //Following this method, the underlying FTC system will call a "stop" routine of its own
        systemTime = System.nanoTime();
        Log.i("FtcRobotController", "IMU Stop method finished in: "
                + (-(systemTime - (systemTime = System.nanoTime()))) + " ns.");
    }
}
