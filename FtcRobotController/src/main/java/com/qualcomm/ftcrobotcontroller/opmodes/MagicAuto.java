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
import com.qualcomm.robotcore.eventloop.*;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.ftccommon.*;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.*;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.ftccommon.FtcEventLoopHandler;
//import com.custom.;


/**
 * TeleOp Mode
 * <p>
 *Enables control of the robot via the gamepad
 */
public class MagicAuto extends LinearOpMode {
    DcMotor motorBL;
    DcMotor motorBR;
    DcMotor motorFL;
    DcMotor motorFR;
    DcMotor center;

    public MagicAuto() {
    }

    @Override
    public void runOpMode()
    {
        //Intialize all of the motors
        center = hardwareMap.dcMotor.get("motorfr");
        motorFL = hardwareMap.dcMotor.get("motorfl");
        motorFR = hardwareMap.dcMotor.get("center");
        motorBR = hardwareMap.dcMotor.get("motorbl");
        motorBL = hardwareMap.dcMotor.get("motorbr");
        //Setting channel mode
        motorFL.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFR.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBR.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBL.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        center.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        //Gyro set up

        //center = hardwareMap.dcMotor.get("motorcenter");
        //Intialize all motor encoder positions
        double encbl = motorBL.getCurrentPosition();
        double encbr = motorBR.getCurrentPosition();
        double encfr = motorFR.getCurrentPosition();
        double encfl = center.getCurrentPosition(); //switched motorFL and center
        //Create the variable that control the motor's speed
        double BL = 0.066;
        double BR = -0.066;
        double FR = -0.066;
        double FL = 0.066;

        /*Structure of code:
        1. Monitor Encoders on all motors
        2. Monitor gyro/accelerometer data
        3. Calculate position based on both of these things
        4. Update position into virtual MM grid of the field
        5. Devise best path to destination and sense obstructions
        6. If an obstruction is encountered (debris or another robot), understand the problem and
        go around the problem.

        In summation, this code should be able to get a robot from point A to point B accurately
        and quickly while avoiding/going around obstructions. Anything that throws the robot off
        course should be corrected.



        How to do these things:
        1. Multithreading, efficient use of CPU
        2. Compare Encoders to Gyro/Accelerometer to more accurately determine position



        Plan for building program:
        Loop 1
        1. Build self-correcting drive using encoders.
        2. Test self-correcting drive using encoders.
        Loop 2
        3. Build grid of robot position.
        4. Test grid of robot position.

        5. Build product that can move from point to point using encoders and self correct if pushed off course.
        6. Improve this product's speed and efficiency enough to do real autonomous. AutoProductV1
        7. Build some real autonomous programs that are competition ready and test them.

        8. Encode grid of arena.
        9. Test gyro/accelerometer to record to data file.
        10. Run trial runs and collect data for all sensors (encoders, gyro, accelerometer) (More info on this step needed)
        11. Update AutoProductV1 to AutoProductV2, includes gyro and accel to increase accuracy.
        AutoProductV2 also includes name of points. "Start", "Mountain1", "Mountain2", "Dropper"
        Loop 3
        12. Test for moving over debris
        13. Fix code based on debris results

        14. Build basic AI for avoiding arena walls and what to do if running into unexpected
        obstacle i.e a robot
        Loop 4
        15. Test basic AI
        16. Fix basic AI

        17. Update AutoProductV2 to AutoProductV3, including AI
        18. Improve speed, efficiency and accuracy.
        */

        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        while(opModeIsActive())
        {
            telemetry.addData("01","encbr " + encbr);
            telemetry.addData("02","encfl " + encfl);
            telemetry.addData("03","encbl " + encbl);
            telemetry.addData("04", "encfr" + encfr);
            //buildAndSendTelemetry("","");
            encbl = motorBL.getCurrentPosition();
            encbr = motorBR.getCurrentPosition();
            encfr = motorFR.getCurrentPosition();
            encfl = center.getCurrentPosition(); //switched motorFL and center

            if((Math.abs(encbl) + Math.abs(encfl) - 800) > (Math.abs(encfr) + Math.abs(encbr)))
            {
                    BR = 0.066;

                    FR = 0.066;

                    FL = 0.033;

                    BL = 0.033;
            }
            else if((encbl + encfl) < (encfr + encbr - 800))
            {
                BL = 0.066;

                FL = 0.066;

                FR = 0.033;

                BR = 0.033;
            }
            else
            {
                FL = 0.066;
                BL = 0.066;
                FR = 0.066;
                BR = 0.066;
            }
            motorFL.setPower(FL);
            motorBL.setPower(BL);
            motorBR.setPower(-BR);
            motorFR.setPower(-FR);
            //try{waitOneHardwareCycle();} catch(InterruptedException e){}
            timer.time();
            long a = 10;
            try{waitForNextHardwareCycle();}catch(InterruptedException b){}
        }
        motorFL.setPower(0.0);
        motorFR.setPower(0.0);
        motorBR.setPower(0.0);
        motorBL.setPower(0.0);
        center.setPower(0.0);
    }

    //public void stop()
    //{

    //}
}
