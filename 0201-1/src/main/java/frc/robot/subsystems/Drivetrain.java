/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.TankDrive;
import frc.robot.commands.TraceTarget;

/**
 * Add your docs here.
 */
public class Drivetrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    TalonSRX leftTalonSRX = null;
    TalonSRX rightTalonSRX = null;

    public Drivetrain() {
        leftTalonSRX = new TalonSRX(RobotMap.DRIVETRAIN_LEFT_TALONSRX);
        rightTalonSRX = new TalonSRX(RobotMap.DRIVETRAIN_RIGHT_TALONSRX);
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        leftTalonSRX.set(ControlMode.PercentOutput, leftSpeed);
        rightTalonSRX.set(ControlMode.PercentOutput, rightSpeed);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());

        setDefaultCommand(new TraceTarget());
    }
}
