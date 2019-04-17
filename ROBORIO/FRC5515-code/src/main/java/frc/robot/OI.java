/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.RobotMap;
import frc.robot.commands.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    public Joystick driveController1 = new Joystick(RobotMap.OI_DRIVER_CONTROLLER1);
    public Joystick driveController2 = new Joystick(RobotMap.OI_DRIVER_CONTROLLER2);

    //driveController1
    public Button traceButton = new JoystickButton(driveController1, RobotMap.DRIVE_CONTROLLER1_TRACE_BUTTON);
    public Button switchButton = new JoystickButton(driveController1, RobotMap.DRIVE_CONTROLLER1_SWITCH_BUTTON);
    public Button speedButton = new JoystickButton(driveController1, RobotMap.DRIVE_CONTROLLER1_SPEED_BUTTON);
    
    //driverController2
    public Button fingerButton = new JoystickButton(driveController2, RobotMap.DRIVE_CONTROLLER2_FINGER_BUTTON);
    public Button stretchButton = new JoystickButton(driveController2, RobotMap.DRIVE_CONTROLLER2_STRETCH_BUTTON);
    public Button panelTakingButton = new JoystickButton(driveController2, RobotMap.DRIVE_CONTROLLER2_PENNELTAKING_BUTTON);
    public Button liftButton = new JoystickButton(driveController2, RobotMap.DRIVE_CONTROLLER2_LIFT_BUTTON);


    public OI() {
        traceButton.whileHeld(new TraceTarget());
        switchButton.whileHeld(new Spin());
    }
}
