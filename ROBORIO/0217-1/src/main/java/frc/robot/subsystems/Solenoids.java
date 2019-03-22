/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Solenoids extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Solenoid cargo = null;
    public Solenoid speed = null;
    public DoubleSolenoid panelTaking = null;
    public DoubleSolenoid stretch = null;
    

    public Solenoids() {
        cargo = new Solenoid(RobotMap.SOLENOIDS_CARGO_SOLENOID);
        speed  = new Solenoid(RobotMap.SOLENOIDS_SPEED_SOLENOID);
        panelTaking = new DoubleSolenoid(RobotMap.SOLENOIDS_PANELTAKING_SOLENOID_DEPLOY, RobotMap.SOLENOIDS_PANELTAKING_SOLENOID_RETRACT);
        stretch = new DoubleSolenoid(RobotMap.SOLENOIDS_STRETCH_SOLENOID_DEPLOY, RobotMap.SOLENOIDS_STRETCH_SOLENOID_RETRACT);
    }

    public void cargoUp() {
        cargo.set(true);
    }

    public void cargoDown() {
        cargo.set(false);
    }

    public void panelTakingOn() {
        panelTaking.set(Value.kReverse);
    }

    public void panelTakingOff() {
        panelTaking.set(Value.kForward);
    }

    public void speedUp() {
        speed.set(true);
    }

    public void speedDown() {
        speed.set(false);
    }

    public void stretchOut() {
        stretch.set(Value.kReverse);
    }

    public void stretchIn() {
        stretch.set(Value.kForward);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
