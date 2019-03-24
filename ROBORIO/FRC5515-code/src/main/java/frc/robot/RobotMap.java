/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    //TalonSRX
    public static final int DRIVETRAIN_LEFT_TALONSRX = 1;
    public static final int DRIVETRAIN_RIGHT_TALONSRX = 2;

    //Victor SP
    public static final int SHOOTER = 2;

    //Solenoid
    public static final int SOLENOIDS_FINGER_SOLENOID = 7;
    /*public static final int SOLENOIDS_LIFT_SOLENOID = 7;
    public static final int SOLENOIDS_PANELTAKING_SOLENOID = 7;
    public static final int SOLENOIDS_SPEED_SOLENOID = 7;
    public static final int SOLENOIDS_STRETCH_SOLENOID = 7;
    public static final int SOLENOIDS_SWITCH0_SOLENOID = 7;*/

    //Joystick
    public static final int OI_DRIVER_CONTROLLER1 = 0;
    public static final int OI_DRIVER_CONTROLLER2 = 1;
    //Axis
    public static final int DRIVE_CONTROLLER_MOVE_AXIS = 0;
    public static final int DRIVE_CONTROLLER_ROTATE_AXIS = 1;
    public static final int DRIVE_CONTROLLER_SHOOT_AXIS = 5;
    public static final int DRIVE_CONTROLLER_LEFT_TRIGGER_AXIS = 2;
    public static final int DRIVE_CONTROLLER_RIGHT_TRIGGER_AXIS = 3;
    //Buttons
    public static final int DRIVE_CONTROLLER1_SPEED_BUTTON = 9;
    public static final int DRIVE_CONTROLLER1_SWITCH_BUTTON = 2;
    public static final int DRIVE_CONTROLLER1_TRACE_BUTTON = 3;

    public static final int DRIVE_CONTROLLER2_PENNELTAKING_BUTTON = 9;
    public static final int DRIVE_CONTROLLER2_LIFT_BUTTON = 9;
    public static final int DRIVE_CONTROLLER2_FINGER_BUTTON = 2;
    public static final int DRIVE_CONTROLLER2_STRETCH_BUTTON = 9;

}
