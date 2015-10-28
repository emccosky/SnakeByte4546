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

public class FiveWheelTeleOpV2 extends OpMode
{
    DcMotor motorFL;
    DcMotor motorFR;
    DcMotor motorBL;
    DcMotor motorBR;
    DcMotor center;
    DcMotor debrisLift;
    DcMotor hangLiftL;
    DcMotor hangLiftR;
    int curMode; //Current mode
    boolean hasControllerBeenUsed;
    int g1y1;
    int g1y2;
    int g1x1;
    int g1x2;
    int g2y1;
    int g2y2;
    int g2x1;
    int g2x2;
    int scaleg1y1;
    int scaleg1y2;
    int scaleg1x1;
    int scaleg1x2;
    int scaleg2y1;
    int scaleg2y2;
    int scaleg2x1;
    int scaleg2x2;
    boolean g1Lbump;
    boolean g1Rbump;
    boolean g2Lbump;
    boolean g2Rbump;
    double g1Ltrig;
    double g1Rtrig;
    double g2Ltrig;
    double g2Rtrig;
    boolean g1XPressed;
    boolean g2XPressed;
    boolean g1YPressed;
    boolean g2YPressed;
    boolean g1APressed;
    boolean g2APressed;
    boolean g1BPressed;
    boolean g2BPressed;
    boolean startPhaseRunning;
    boolean startPhaseOver;
    boolean autoHangRunning;

    public void FiveWheelTeleOpV2() {}

    private double scaleInputSimple(double pwr)
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

    public void updateVals() //Updates all variable values
    {

    }

    public void autoHang()
    {

    }

    public void startPhaseOffRamp()
    {
        startPhaseRunning = true;
        //Code
        startPhaseRunning = false;
        startPhaseOver = true;
    }
    public void init()
    {
        curMode = 0;
        startPhaseRunning = false;
        startPhaseOver = false;
    }

    public void loop()
    {
        if(!startPhaseOver) //Robot has done nothing since start of match
        {
            if(g1y1 > 0.3 || g1y1 < -0.3 || g1y2 > 0.3 || g1y2 < -0.3)
            {
                startPhaseOver = true;
                curMode = 2;
            }
            else if(g1XPressed)
            {
                startPhaseOffRamp();
            }
        }
        else if(curMode == 1) //Debris collection Mode
        {
            if(!)
        }
        else if(curMode == 2) //Ramp Climbing Mode
        {
            //Stop manipulator


        }
        else if(curMode == 3) //Hanging Mode
        {

        }
        else if(curMode == 4) //Manual Override Mode
        {
            //
        }
        else if(curMode == 5) //Individual Wheel control
        {
            //
        }

    }
}