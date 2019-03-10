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
    public static final int SOLENOIDS_SPEED_SOLENOID = 1;
    public static final int SOLENOIDS_CARGO_SOLENOID = 2;
    public static final int SOLENOIDS_PANELTAKING_SOLENOID_DEPLOY = 5;
    public static final int SOLENOIDS_PANELTAKING_SOLENOID_RETRACT = 4;
    public static final int SOLENOIDS_STRETCH_SOLENOID_DEPLOY = 6;
    public static final int SOLENOIDS_STRETCH_SOLENOID_RETRACT = 7;

    //Joystick
    public static final int OI_DRIVER_CONTROLLER = 0;
    public static final int DRIVE_CONTROLLER_MOVE_AXIS = 0;
    public static final int DRIVE_CONTROLLER_ROTATE_AXIS = 1;
    public static final int DRIVE_CONTROLLER_SHOOT_AXIS = 5;
}
