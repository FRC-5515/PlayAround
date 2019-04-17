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
    NetworkTableEntry lightStatus;
    NetworkTableEntry thetaEntry;
    NetworkTableEntry fDEntry;
    NetworkTableEntry dEntry;
    NetworkTableEntry indexEntry;
    //NetworkTableEntry lEntry;
    //NetworkTableEntry rEntry;
    double index = 0;
    double thetaMin = 0.785;
    double thetaGood = 0.11;
    double dMax = 30;

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
        lightStatus = table.getEntry("light");
        indexEntry = table.getEntry("imgproc_index");
        thetaEntry = table.getEntry("imgproc_theta");
        dEntry = table.getEntry("imgproc_delta");
        //lEntry = table.getEntry("L");
        //rEntry = table.getEntry("R");
        //light
        lightStatus.setDouble(1);
        traceSEntry.setDouble(1);

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double tempIndex = indexEntry.getDouble(-1);
        double theta = thetaEntry.getDouble(0);
        double d = dEntry.getDouble(0);

        double leftSpeed = 0;
        double rightSpeed = 0;
        thetaEntry.setDouble(theta);
        dEntry.setDouble(d);
        indexEntry.setDouble(index);

        if(index < tempIndex || tempIndex == 0) {
            index = tempIndex;
            if(d > dMax) {
                if(Math.abs(theta) < thetaMin) {
                    if(theta > 0) {
                        Robot.m_drivetrain.tankDrive(0.2, 0.2);
                    } else {
                        Robot.m_drivetrain.tankDrive(-0.2, -0.2);
                    }
                } else {
                    Robot.m_drivetrain.tankDrive(0.2, -0.2);
                }
            } else if(Math.abs(theta) > thetaGood) {
                if(theta > 0) {
                    Robot.m_drivetrain.tankDrive(0.2, 0.2);
                } else {
                    Robot.m_drivetrain.tankDrive(-0.2, -0.2);
                }
            }
            /*fD = Math.abs(d/Math.sin(theta));
            if(dDone == false) {
                if(d > 30) {
                    leftSpeed = 0.15;
                    rightSpeed = -0.15;
                } else {
                    dDone = true;
                }
            }
            if(thetaDone == false && dDone == true) {
                
                    if(theta < -0.11) {
                        leftSpeed = -0.15;
                        rightSpeed = -0.15;
                    }
                    else if (theta > 0.11) {
                        leftSpeed = 0.15;
                        rightSpeed = 0.15;
                    }
                    else thetaDone = true;
            }
            */

        }
        //lEntry.setDouble(leftSpeed);
        //rEntry.setDouble(-rightSpeed);
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
        traceSEntry.setDouble(0);
        lightStatus.setDouble(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
