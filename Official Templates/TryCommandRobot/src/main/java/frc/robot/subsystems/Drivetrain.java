/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.RobotMap;
import frc.robot.commands.DriveArcade;



/**
 * Add your docs here.
 */
public class Drivetrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  VictorSP leftSP = null;
  VictorSP rightSP = null;
  
  

  DifferentialDrive differentialDrive = null;

  public Drivetrain(){

    leftSP = new VictorSP(RobotMap.DRIVETRAIN_LEFT_VICTORSP);
    rightSP = new VictorSP(RobotMap.DRIVETRAIN_RIGHT_VICTORSP);
    
    SpeedControllerGroup leftMotors = new SpeedControllerGroup(leftSP);
    SpeedControllerGroup rightMotors = new SpeedControllerGroup(rightSP);
    

    differentialDrive = new DifferentialDrive(leftMotors, rightMotors);

  }

  public void arcadeDrive(double moveSpeed, double rotateSpeed){
    differentialDrive.arcadeDrive(moveSpeed, rotateSpeed);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
    setDefaultCommand(new DriveArcade());
  }
}
