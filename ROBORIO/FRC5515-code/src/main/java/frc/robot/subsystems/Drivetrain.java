/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.ArcadeDrive;

/**
 * Add your docs here.
 */
public class Drivetrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public TalonSRX leftTalonSRX = null;
    public TalonSRX rightTalonSRX = null;

    NetworkTableEntry lEntry;
    NetworkTableEntry rEntry;

    public Drivetrain() {
        leftTalonSRX = new TalonSRX(RobotMap.DRIVETRAIN_LEFT_TALONSRX);
        rightTalonSRX = new TalonSRX(RobotMap.DRIVETRAIN_RIGHT_TALONSRX);
    }

    public void arcadeDrive(double moveSpeed, double rotateSpeed) {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("datatable");
        lEntry = table.getEntry("Left Speed");
        rEntry = table.getEntry("Right Speed");

        //differentialDrive.arcadeDrive(moveSpeed, rotateSpeed);
        double leftSpeed;
        double rightSpeed;

        double maxInput = Math.copySign(Math.max(Math.abs(moveSpeed), Math.abs(rotateSpeed)), moveSpeed);

        if (moveSpeed >= 0.0) {
            // First quadrant, else second quadrant
            if (rotateSpeed >= 0.0) {
                leftSpeed = maxInput;
                rightSpeed = moveSpeed - rotateSpeed;
            } else {
                leftSpeed = moveSpeed + rotateSpeed;
                rightSpeed = maxInput;
            }
        } else {
            // Third quadrant, else fourth quadrant
            if (rotateSpeed >= 0.0) {
                leftSpeed = moveSpeed + rotateSpeed;
                rightSpeed = maxInput;
            } else {
                leftSpeed = maxInput;
                rightSpeed = moveSpeed - rotateSpeed;
            }
        }
        // lEntry.setDouble(leftSpeed);
        rEntry.setDouble(rightSpeed);
        leftTalonSRX.set(ControlMode.PercentOutput, 0.5*leftSpeed);
        rightTalonSRX.set(ControlMode.PercentOutput, 0.5*rightSpeed);
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        // lEntry.setDouble(leftSpeed);
        rEntry.setDouble(rightSpeed);
        leftTalonSRX.set(ControlMode.PercentOutput, leftSpeed);
        rightTalonSRX.set(ControlMode.PercentOutput, rightSpeed);
    }



    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new ArcadeDrive());
    }
}
