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

public class TraceTarget extends Command {
    NetworkTableEntry traceSEntry;
    NetworkTableEntry thetaEntry;
    NetworkTableEntry dEntry;
    NetworkTableEntry indexEntry;
    NetworkTableEntry lEntry;
    NetworkTableEntry rEntry;
    double index = 0;

    public TraceTarget() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.m_drivetrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("datatable");
        traceSEntry = table.getEntry("Trace Status");
        indexEntry = table.getEntry("imgproc_index");
        thetaEntry = table.getEntry("imgproc_theta");
        dEntry = table.getEntry("imgproc_dist");
        lEntry = table.getEntry("L");
        rEntry = table.getEntry("R");
        //light
        Robot.light.set(true);

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double tempIndex = indexEntry.getDouble(-1);
        double theta = thetaEntry.getDouble(0);
        double d = dEntry.getDouble(0);
        double leftSpeed = 0;
        double rightSpeed = 0;
        traceSEntry.setDouble(1);
        thetaEntry.setDouble(theta);
        dEntry.setDouble(d);
        indexEntry.setDouble(index);
        if(index < tempIndex || tempIndex == 0) {
            index = tempIndex;
            if(d > 180) {
                leftSpeed = 0.15;
                rightSpeed = -0.15;
            } else {
                if(theta < -0.11) {
                    leftSpeed = -0.15;
                    rightSpeed = -0.15;
                }
                else if (theta > 0.11) {
                    leftSpeed = 0.15;
                    rightSpeed = 0.15;
                }
                else end();
            }

        }
        lEntry.setDouble(leftSpeed);
        rEntry.setDouble(-rightSpeed);
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
        traceSEntry.setDouble(0);
        Robot.m_drivetrain.tankDrive(0, 0);
        Robot.light.set(false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
