/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class ArcadeDrive extends Command {
    double moveSpeed;
    double rotateSpeed;

    NetworkTableEntry mEntry;
    NetworkTableEntry rEntry;

    public ArcadeDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);

        requires(Robot.m_drivetrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("datatable");
        mEntry = table.getEntry("Move Speed");
        rEntry = table.getEntry("Rotate Speed");

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        moveSpeed = Robot.m_oi.driveController1.getRawAxis(RobotMap.DRIVE_CONTROLLER_MOVE_AXIS);
        rotateSpeed = -Robot.m_oi.driveController1.getRawAxis(RobotMap.DRIVE_CONTROLLER_ROTATE_AXIS);
        mEntry.setDouble(moveSpeed);
        rEntry.setDouble(rotateSpeed);
        Robot.m_drivetrain.arcadeDrive(moveSpeed, rotateSpeed);

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.m_drivetrain.arcadeDrive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
