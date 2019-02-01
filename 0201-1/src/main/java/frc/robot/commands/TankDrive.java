/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class TankDrive extends Command {
    double leftSpeed;
    double rightSpeed;
        
    public TankDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);

        requires(Robot.m_drivetrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        leftSpeed = -0.5*Robot.m_oi.driveController.getRawAxis(RobotMap.DRIVE_CONTROLLER_LEFT_AXIS);
        rightSpeed = 0.5*Robot.m_oi.driveController.getRawAxis(RobotMap.DRIVE_CONTROLLER_RIGHT_AXIS);
        Robot.m_drivetrain.tankDrive(leftSpeed, rightSpeed);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.m_drivetrain.tankDrive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
